package ixcode.platform.http.server.resource;

import ixcode.platform.http.template.FileJadeTemplateLoader;
import ixcode.platform.http.template.JadeTemplateEngine;
import ixcode.platform.http.template.TemplateEngine;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;

public class TemplatedPageMap implements Iterable<TemplatedPageEntry> {

    private static final Logger log = Logger.getLogger(TemplatedPageMap.class);

    public final TemplateEngine templateEngine;

    private final List<TemplatedPageEntry> entries;

    private TemplatedPageMap(TemplateEngine templateEngine,
                             List<TemplatedPageEntry> entries) {

        this.templateEngine = templateEngine;
        this.entries = entries;
    }

    public static TemplatedPageMap loadTemplatedPagesFrom(String rootPath) {
        log.info(format("Pages are served from [%s]", rootPath));

        List<TemplatedPageEntry> entries = new TemplatedPageDirectory(rootPath, ".jade")
                .findPages();

        return new TemplatedPageMap(
                new JadeTemplateEngine(
                        new FileJadeTemplateLoader(rootPath)),
                entries);
    }




    @Override public Iterator<TemplatedPageEntry> iterator() {
        return entries.iterator();
    }
}
