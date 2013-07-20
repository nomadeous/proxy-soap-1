/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.configuration.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author julamand
 */
public class ResourceLoaderTest {

    public ResourceLoaderTest() {
    }

    /**
     * Test of load method, of class ResourceLoader.
     */
    @Test
    public void testLoad() {
        ResourceLoader loader = new ResourceLoader();
        String res = "";
        // empty case
        res = loader.load("");
        assertNotNull(res);

        //classpath case
        String classpath = "classpath://simple.xsd";
        res = loader.load(classpath);
        assertNotNull(res);
        assertTrue(res.contains("complexType"));

        // filepath case
        String filepath = "file:" + this.getClass().getClassLoader().getResource("simple.xsd").getPath();
        res = loader.load(filepath);
        assertNotNull(res);
        assertTrue(res.contains("complexType"));
    }
}
