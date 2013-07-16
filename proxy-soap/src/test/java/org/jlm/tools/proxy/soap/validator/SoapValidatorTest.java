/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.validator;

import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author jlamande
 */
public class SoapValidatorTest {

    /**
     * Test of validate method, of class SoapValidator.
     */
    @Test
    public void testValidate() {
        String xmlContent = "<?xml version=\"1.0\" ?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://www.webserviceX.NET\">\n"
                + "   <soapenv:Header/>\n"
                + "   <soapenv:Body>\n"
                + "      <web:root>\n"
                + "         <!--Optional:-->\n"
                + "         <web:child>123</web:child>\n"
                + "      </web:root>\n"
                + "   </soapenv:Body>\n"
                + "</soapenv:Envelope>";
        String schemaPath = this.getClass().getClassLoader().getResource("simple.xsd").getFile();
        SoapValidator instance = new SoapValidator();
        ValidationResult expResult = new ValidationResult(true, true, true, true, new ArrayList<String>());
        ValidationResult result = instance.validate(xmlContent, schemaPath);
        System.err.println(result.getErrors());
        assertEquals(expResult.isValid(), result.isValid());
    }
}
