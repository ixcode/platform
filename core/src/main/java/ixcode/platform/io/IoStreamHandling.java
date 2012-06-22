package ixcode.platform.io;

import java.io.*;

public class IoStreamHandling {

    public static void closeQuietly(OutputStream out) {
        if (out == null) {
            return;
        }

        try {
            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close stream (See Cause)", e);
        }
    }

    public static void closeQuietly(Writer writer) {
        if (writer == null) {
            return;
        }

        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close writer (See Cause)", e);
        }
    }

    public static void closeQuietly(Reader reader) {
        if (reader == null) {
            return;
        }

        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close stream (See Cause)", e);
        }
    }

    public static void closeQuietly(InputStream in) {
        if (in == null) {
            return;
        }

        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not close stream (See Cause)", e);
        }
    }

    public static InputStream toInputStream(String input, String charset) {
        try {
            return new ByteArrayInputStream(input.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFileAsString(File file, String charsetName) {
        try {
            return readFully(new FileInputStream(file), charsetName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFully(InputStream inputStream, String charsetName) {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
            char[] charBuffer = new char[512];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(bufferedReader);
        }
        return stringBuilder.toString();
    }

    public static void copyStream(InputStream in, OutputStream out) {
        byte[] buffy = new byte[1024];
        try {
            int bytesRead = 0;
            while ((bytesRead = in.read(buffy)) != -1) {
                out.write(buffy, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}