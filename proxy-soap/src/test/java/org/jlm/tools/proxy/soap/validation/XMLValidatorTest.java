/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.validation;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author jlamande
 */
public class XMLValidatorTest {

    /**
     * Test of validate method, of class XMLValidator.
     */
    @Test
    public void testNotValidXMLNoXsd() {
        ValidatorResult result = new XmlValidator().validate("");
        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
        ValidatorResult result2 = new XmlValidator().validate("", "");
        assertFalse(result2.isValid());
        assertEquals(1, result2.getErrors().size());
    }

    /**
     * Test of validate method, of class XMLValidator.
     */
    @Test
    public void testValidXMLNoXsd() {
        ValidatorResult result = new XmlValidator().validate("<?xml version=\"1.0\" ?><root></root>");
        assertTrue(result.isValid());
        assertEquals(0, result.getErrors().size());
        ValidatorResult result2 = new XmlValidator().validate(
                "<?xml version=\"1.0\" ?><root></root>", "");
        assertTrue(result2.isValid());
        assertEquals(0, result2.getErrors().size());
    }

    /**
     * Test of validate method, of class XMLValidator.
     */
    @Test
    public void testValidXmlValitedByValidXsd() {
        String xsdPath = this.getClass().getClassLoader().getResource("simple.xsd").getFile();
        ValidatorResult result = new XmlValidator().validate(
                "<?xml version=\"1.0\" ?><web:root xmlns:web=\"http://www.webserviceX.NET\"><child>xxx</child></web:root>",
                xsdPath);
        assertTrue(result.isValid());
        assertEquals(0, result.getErrors().size());
    }

    /**
     * Test of validate method, of class XMLValidator.
     */
    @Test
    public void testInvalidXmlValitedByValidXsd() {
        String xsdPath = this.getClass().getClassLoader().getResource("simple.xsd").getFile();
        ValidatorResult result = new XmlValidator().validate("<?xml version=\"1.0\" ?><root></root>",
                xsdPath);
        assertFalse("Should not be valid", result.isValid());
        assertEquals(1, result.getErrors().size());
        result = new XmlValidator().validate(
                "<?xml version=\"1.0\" ?><root><child></child><child></child></root>", xsdPath);
        assertFalse("Should not be valid", result.isValid());
        assertEquals(1, result.getErrors().size());
    }

    /**
     * Test of validate method, of class XMLValidator.
     */
    @Test
    public void testValidXmlValitedByInvalidXsd() {
        String xsdPath = this.getClass().getClassLoader().getResource("simple_invalid.xsd").getFile();
        ValidatorResult result = new XmlValidator().validate(
                "<?xml version=\"1.0\" ?><web:root xmlns:web=\"http://www.webserviceX.NET\"><child>xxx</child></web:root>",
                xsdPath);
        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
    }
}
