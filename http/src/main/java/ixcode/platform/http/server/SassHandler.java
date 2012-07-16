package ixcode.platform.http.server;

import ixcode.platform.http.server.resource.TemplatedPage;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SassHandler extends AbstractHandler {

    private static final Logger log = Logger.getLogger(SassHandler.class);

    private final Map<String, File> sassFileMap;
    private final File sassRoot;

    public SassHandler(File sassRoot) {
        this.sassRoot = sassRoot;

        sassFileMap = loadFiles(sassRoot);
    }


    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {



    }

    private static Map<String, File> loadFiles(File sassRoot) {
        log.info("Serving sass From: " + sassRoot.getAbsolutePath());
        Map<String, File> fileMap = new HashMap<String, File>();

        loadDir(fileMap, sassRoot, sassRoot.getPath());

        return fileMap;
    }

    private static void loadDir(Map<String, File> fileMap, File dir, String root) {
        File[] children = dir.listFiles();

        for (File child : children) {
            if (child.isDirectory()) {
                loadDir(fileMap, child, root);
            } else if (child.getName().endsWith(".scss")) {
                String key = child.getPath().substring(root.length()+1);
                log.info("Loading sass called: " + key);
                fileMap.put(key, child);
            }
        }
    }




}