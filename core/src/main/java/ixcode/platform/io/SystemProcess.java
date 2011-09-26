package ixcode.platform.io;

import org.apache.log4j.*;

import java.io.*;
import java.lang.management.*;

import static java.lang.String.format;
import static ixcode.platform.io.StreamHandling.*;

public class SystemProcess {

    private static final Logger LOG = Logger.getLogger(SystemProcess.class);


    public SystemProcess() {
    }

    public void writeProcessIdToFile(String filename) {
           FileOutputStream out = null;
           try {
               File processIdFile = new File(System.getProperty("user.home"), filename);
               if (processIdFile.exists()) {
                   processIdFile.delete();
               }
               processIdFile.deleteOnExit();
               processIdFile.createNewFile();

               out = new FileOutputStream(processIdFile);

               String processName = ManagementFactory.getRuntimeMXBean().getName();
               if (!processName.contains("@")) {
                   throw new RuntimeException(format("Process name [%s] is not parsable for the processId!", processName));
               }

               String[] parts = processName.split("@");
               String processId = parts[0];
               String hostName = parts[1];

               PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
               writer.println(processId);
               writer.flush();
               LOG.info(format("Writing PID file for process with PID [%s] on [%s] (file is at [%s])", processId, hostName, processIdFile.getAbsolutePath()));

           } catch (Exception e) {
               throw new RuntimeException(format("Could not write process Id file [~/%s]", filename), e);
           } finally {
               closeQuietly(out);
           }
       }

}