package ixcode.platform.http.server.resource;

import ixcode.platform.http.template.FileJadeTemplateLoader;
import ixcode.platform.http.template.JadeTemplateEngine;
import ixcode.platform.http.template.TemplateEngine;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;

public class TemplatedPageMap implements Iterable<TemplatedPageEntry> {

    private static final Logger log = Logger.getLogger(TemplatedPageMap.class);

    public final TemplateEngine templateEngine;

    private final List<TemplatedPageEntry> entries = new ArrayList<TemplatedPageEntry>();

    private TemplatedPageMap(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;

        entries.add(new TemplatedPageEntry());

    }

    public static TemplatedPageMap loadTemplatedPagesFrom(String rootPath) {
        log.info(format("Pages are served from [%s]", rootPath));
        return new TemplatedPageMap(
                new JadeTemplateEngine(
                        new FileJadeTemplateLoader(rootPath)));
    }


    @Override public Iterator<TemplatedPageEntry> iterator() {
        return entries.iterator();
    }
}
