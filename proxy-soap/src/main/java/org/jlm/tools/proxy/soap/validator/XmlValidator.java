/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.validator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author jlamande
 */
public class XmlValidator {

    public ValidatorResult validate(String xmlContent) {
        return validate(xmlContent, null);
    }

    public ValidatorResult validate(String xmlContent, String schemaPath) {
        boolean xmlWellFormed = false;
        boolean xsdValidationActive = (schemaPath != null && !schemaPath.equals(""));
        boolean xsdWellFormed = false;
        boolean xmlValidatedByXSD = false;

        List<String> errors = new ArrayList<String>();

        try {
            // Check XML
            Node root = parseXML(xmlContent);
            xmlWellFormed = true;

            // Check if schema validation is required
            if (xsdValidationActive) {
                // create validator with care if xsd is invalid
                Validator validator = createSchemaValidator(schemaPath);
                xsdWellFormed = true;
                // validate xml against xsd
                xmlValidatedByXSD = validateXSD(nodeToString(root), validator, errors);
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        }

        ValidatorResult vOut =
                new ValidatorResult(xmlWellFormed, xsdValidationActive, xsdWellFormed, xmlValidatedByXSD, errors);

        return vOut;
    }

    protected Node parseXML(String xmlContent) throws ParserConfigurationException, SAXException, IOException {
        ByteArrayInputStream bAIS = new ByteArrayInputStream(xmlContent.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        factory.setNamespaceAware(true);
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Failed to initialize XML document builder", e);
        }
        Document document = docBuilder.parse(bAIS);
        Element root = document.getDocumentElement();
        return root;
    }

    private Validator createSchemaValidator(String schemaPath) throws Exception {
        File schemaFile = new File(schemaPath);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaFile);
        Validator validator = schema.newValidator();
        return validator;

    }

    protected boolean validateXSD(String xmlContent, Validator validator, final List<String> errors) throws SAXException, IOException {
        Source xmlFile = new SAXSource(new InputSource(new StringReader(xmlContent)));
        validator.setErrorHandler(new org.xml.sax.ErrorHandler() {
            @Override
            public void warning(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
                errors.add(exception.getMessage());
            }

            @Override
            public void fatalError(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
                errors.add(exception.getMessage());
            }

            @Override
            public void error(org.xml.sax.SAXParseException exception) throws org.xml.sax.SAXException {
                errors.add(exception.getMessage());
            }
        });
        validator.validate(xmlFile);
        if (!errors.isEmpty()) {
            return false;
        }
        return true;
    }

    private String nodeToString(Node node) throws TransformerException {
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(node);
        trans.transform(source, result);
        String content = sw.toString();
        return content;
    }
}
