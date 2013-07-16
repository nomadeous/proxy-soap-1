/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.validator;

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
    public void testNotValidXML() {
        ValidationResult result = new XMLValidator().validate("");
        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
        ValidationResult result2 = new XMLValidator().validate("", "");
        assertFalse(result2.isValid());
        assertEquals(1, result2.getErrors().size());
    }

    /**
     * Test of validate method, of class XMLValidator.
     */
    @Test
    public void testValidXML() {
        ValidationResult result = new XMLValidator().validate("<?xml version=\"1.0\" ?><root></root>");
        assertTrue(result.isValid());
        assertEquals(0, result.getErrors().size());
        ValidationResult result2 = new XMLValidator().validate("<?xml version=\"1.0\" ?><root></root>", "");
        assertTrue(result2.isValid());
        assertEquals(0, result2.getErrors().size());
    }

    /**
     * Test of validate method, of class XMLValidator.
     */
    @Test
    public void testValidXMLValitedByValidXSD() {
        String xsdPath = this.getClass().getClassLoader().getResource("simple.xsd").getFile();
        ValidationResult result = new XMLValidator().validate("<?xml version=\"1.0\" ?><root><child>xxx</child></root>", xsdPath);
        assertTrue(result.isValid());
        assertEquals(0, result.getErrors().size());
    }

    /**
     * Test of validate method, of class XMLValidator.
     */
    @Test
    public void testInvalidXMLValitedByValidXSD() {
        String xsdPath = this.getClass().getClassLoader().getResource("simple.xsd").getFile();
        ValidationResult result = new XMLValidator().validate("<?xml version=\"1.0\" ?><root></root>", xsdPath);
        assertFalse("Should not be valid", result.isValid());
        assertEquals(1, result.getErrors().size());
        result = new XMLValidator().validate("<?xml version=\"1.0\" ?><root><child></child><child></child></root>", xsdPath);
        assertFalse("Should not be valid", result.isValid());
        assertEquals(1, result.getErrors().size());
    }

    /**
     * Test of validate method, of class XMLValidator.
     */
    @Test
    public void testValidXMLValitedByInvalidXSD() {
        String xsdPath = this.getClass().getClassLoader().getResource("simple_invalid.xsd").getFile();
        ValidationResult result = new XMLValidator().validate("<?xml version=\"1.0\" ?><root><child>xxx</child></root>", xsdPath);
        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
    }
}
