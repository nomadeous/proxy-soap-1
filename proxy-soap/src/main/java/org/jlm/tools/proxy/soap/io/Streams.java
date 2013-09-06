/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author julamand
 */
public class Streams {

    public static String getString(InputStream is, boolean zipped) {
        InputStream finalIS = is;
        if (zipped) {
            try {
                finalIS = new java.util.zip.GZIPInputStream(is);
            } catch (IOException e) {
            }
        }
        return getString(finalIS);
    }

    public static String getString(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int readed;
        try {

            while ((readed = is.read()) != -1) {
                baos.write(readed);
            }

        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

        return new String(baos.toByteArray());

    }
}
