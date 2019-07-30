package org.riversun.dp.builder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * File reader/writer utilities
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 *
 */
public class FileUtil {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    /**
     * Returns true if string is not NULL and if length greater than 0.
     * 
     * @param str
     * @return
     */
    static boolean isNotBlank(String str) {

        if (str != null && !str.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Read whole text as list from inputStream line by line
     * 
     * @param is
     * @param charset
     * @return
     * @throws IOException
     */
    static List<String> readTextAsList(InputStream is, String charset) throws IOException {

        final List<String> lineList = new ArrayList<String>();

        InputStreamReader isr = null;
        BufferedReader br = null;

        try {

            if (isNotBlank(charset)) {
                isr = new InputStreamReader(is, charset);
            } else {
                isr = new InputStreamReader(is);
            }

            br = new BufferedReader(isr);

            String line;

            while ((line = br.readLine()) != null) {
                lineList.add(line);
            }

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return lineList;
    }

    public static String getTextFromResourceFile(String fileName) throws IOException {

        InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        return getTextFromStream(inputStream);
    }

    static String getTextFromStream(InputStream inputStream) throws IOException {

        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(UTF_8);
    }

    /**
     * Write text to TEXT file
     * 
     * @param file
     * @param text
     * @param charset
     * @param append
     */
    static boolean writeText(File file, String text, String charset, boolean append) {

        if (file == null) {
            return false;
        }

        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(file, append);
            writeTextToStream(fos, text, charset);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;

        } finally {

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }

        return true;

    }

    /**
     * Write text to Stream
     * 
     * @param os
     * @param text
     * @param charset
     * @return
     */
    static boolean writeTextToStream(OutputStream os, String text, String charset) {

        if (os == null) {
            return false;
        }

        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        try {

            osw = new OutputStreamWriter(os, charset);
            bw = new BufferedWriter(osw);

            bw.write(text);
            bw.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {

            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }

            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                }
            }

        }
        return true;
    }
}
