/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author julamand
 */
public class ResourceLoader {

    private static final Logger LOG =
            LoggerFactory.getLogger(ResourceLoader.class);

    public String load(String path) {
        String resource = "";
        URL url = null;
        if (path != null && path.startsWith("classpath://")) {
            String subPath = path.substring("classpath://".length());
            url = this.getClass().getClassLoader().getResource(subPath);
        } else if (path != null && path.startsWith("file:")) {
            try {
                url = new URL(path);
            } catch (MalformedURLException e) {
                LOG.error("Bad File URL");
            }
        }
        if (url != null) {
            LOG.info("URL for '" + path + "' is " + url.toString());
            resource = readFile(url);
        } else {
            LOG.warn("URL for '" + path + "' is null");
        }
        return resource;
    }

    private String readFile(URL url) {
        StringBuilder text = new StringBuilder("");
        String NL = System.getProperty("line.separator");
        String encoding = "UTF-8";
        String filePath = url.toString().substring("file:/".length());
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(filePath), encoding);
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine()).append(NL);
            }
        } catch (FileNotFoundException e) {
            LOG.error("Not found : " + filePath);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return text.toString();
    }
}
