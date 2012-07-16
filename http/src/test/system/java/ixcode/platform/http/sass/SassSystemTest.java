package ixcode.platform.http.sass;

import ixcode.platform.test.SystemTest;
import net.quenchnetworks.sassybarista.sass.JavaStringInterpolator;
import net.quenchnetworks.sassybarista.sass.SassParser;
import net.quenchnetworks.sassybarista.sass.SassSheetSerializer;
import net.quenchnetworks.sassybarista.sass.eval.EvaluationException;
import net.quenchnetworks.sassybarista.sass.eval.IFunction;
import net.quenchnetworks.sassybarista.sass.eval.SassSheetEvaluator;
import net.quenchnetworks.sassybarista.sass.models.SassSheet;
import net.quenchnetworks.sassybarista.sass.value.DefaultPropertyValue;
import net.quenchnetworks.sassybarista.sass.value.IPropertyValue;
import net.quenchnetworks.sassybarista.sass.value.NumberPropertyValue;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static ixcode.platform.io.IoStreamHandling.closeQuietly;
import static ixcode.platform.io.IoStreamHandling.readFileAsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SystemTest
public class SassSystemTest {

    @Test
    public void can_parse_some_sass() {

        String resultingCss = parseSomeSassFrom(new File("./src/test/sample-pages/sass/application.scss"));

        System.out.println(resultingCss);

    }

    @Test
    public void can_pre_process_imports() {
        String source = "@import \"mixins\";\n" +
                "@import \"fonts\";\n" +
                "@import \"global\";\n" +
                "@import \"example\";\n" +
                "@import \"response\";";

        String resultingSource = preProcessImports(new StringReader(source), new File("./src/test/sample-pages/sass/application.scss"));

        System.out.println(resultingSource);

    }

    @Test
    public void can_work_out_the_filename_of_an_import() {

        assertThat(importFileNameFrom("global"), is("_global.scss"));
        assertThat(importFileNameFrom("compass/reset"), is("compass/_reset.scss"));
        assertThat(importFileNameFrom("some/real/long/path/reset"), is("some/real/long/path/_reset.scss"));

    }


    private String parseSomeSassFrom(File file) {
        if (!file.exists()) {
            throw new RuntimeException("Could not find my test sass - looking in " + file.getAbsolutePath());
        }

        Reader in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            in = new FileReader(file);
            String withImports = preProcessImports(in, file);

            SassParser parser = new SassParser(new StringReader(withImports));
            SassSheet sheet = parser.parse();

            SassSheetEvaluator evaluator = new SassSheetEvaluator(new JavaStringInterpolator());
            evaluator.addFunction("formatProperty", new IFunction() {
                public IPropertyValue evaluate(List<IPropertyValue> params)
                        throws EvaluationException {
                    if ("test".equals(((DefaultPropertyValue) params.get(0)).getValue())) {
                        return new NumberPropertyValue("3");
                    } else if ("title".equals(((DefaultPropertyValue) params.get(0)).getValue())) {
                        return new DefaultPropertyValue("2");
                    } else {
                        return new DefaultPropertyValue("1");
                    }
                }
            });

            evaluator.evaluate(sheet);

            SassSheetSerializer serializer = new SassSheetSerializer(new PrintStream(out));
            serializer.render(sheet);

            return new String(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    private String preProcessImports(Reader in, File sourceFile) {
        StringBuilder out = new StringBuilder();

        BufferedReader bin = new BufferedReader(in);
        try {

            while (true) {
                String line = bin.readLine();
                if (line == null) {
                    break;
                }

                if (line.trim().startsWith("@import")) {
                    out.append(importFileFrom(line, sourceFile));
                } else {
                    out.append(line);
                }
            }

            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String importFileFrom(String line, File sourceFile) {
        File parent = sourceFile.getParentFile();
        String[] splits = line.split("\"");

        String importFileName = importFileNameFrom(splits[1]);


        return readFileAsString(new File(parent, importFileName), "UTF8");
    }

    private String importFileNameFrom(String importKey) {
        int posToInsertUnderscore = importKey.lastIndexOf("/");


        if (posToInsertUnderscore == -1) {
            return new StringBuilder()
                    .append("_")
                    .append(importKey)
                    .append(".scss")
                    .toString();
        }

        String pre = importKey.substring(0, posToInsertUnderscore);
        String post = importKey.substring(posToInsertUnderscore + 1, importKey.length());


        StringBuilder sb = new StringBuilder();
        sb.append(pre).append("/_").append(post).append(".scss");

        return sb.toString();
    }
}