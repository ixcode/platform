package ixcode.platform.http.server.resource;

import ixcode.platform.di.InjectionContext;
import ixcode.platform.http.template.FileJadeTemplateLoader;
import ixcode.platform.http.template.JadeTemplateEngine;
import ixcode.platform.http.template.TemplateEngine;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;

public class TemplatedPageMap implements Iterable<TemplatedPage> {

    private static final Logger log = Logger.getLogger(TemplatedPageMap.class);

    public final TemplateEngine templateEngine;

    private final List<TemplatedPage> entries;

    private TemplatedPageMap(TemplateEngine templateEngine,
                             List<TemplatedPage> entries) {

        this.templateEngine = templateEngine;
        this.entries = entries;
    }

    public static TemplatedPageMap loadTemplatedPagesFrom(String rootPath, InjectionContext injectionContext) {
        log.info(format("Pages are served from [%s]", rootPath));

        List<TemplatedPage> entries = new TemplatedPageDirectory(rootPath, ".jade", injectionContext)
                .findPages();

        return new TemplatedPageMap(
                new JadeTemplateEngine(
                        new FileJadeTemplateLoader(rootPath)),
                entries);
    }




    @Override public Iterator<TemplatedPage> iterator() {
        return entries.iterator();
    }
}
