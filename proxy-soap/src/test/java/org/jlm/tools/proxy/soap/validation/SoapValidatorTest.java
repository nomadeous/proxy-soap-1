/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.validation;

import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author jlamande
 */
public class SoapValidatorTest {

    String xmlContent = "<?xml version=\"1.0\" ?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://www.webserviceX.NET\">\n"
            + "   <soapenv:Header/>\n"
            + "   <soapenv:Body>\n"
            + "    <web:root>\n"
            + "         <!--Optional:-->\n"
            + "         <child>123</child>\n"
            + "      </web:root>\n"
            + "   </soapenv:Body>\n"
            + "</soapenv:Envelope>";

    /**
     * Test of validate method, of class SoapValidator.
     */
    @Test
    public void testValidate() {
        String schemaPath = this.getClass().getClassLoader().getResource("simple.xsd").getFile();
        SoapValidator instance = new SoapValidator();
        ValidatorResult expResult = new ValidatorResult(true, true, true, true,
                new ArrayList<String>());
        ValidatorResult result = instance.validate(xmlContent, schemaPath);
        System.err.println(result);
        assertEquals(expResult.isValid(), result.isValid());
    }

    @Test
    public void testParseXML() throws Exception {
        SoapValidator instance = new SoapValidator();
        instance.parseXML(xmlContent);
    }
}
