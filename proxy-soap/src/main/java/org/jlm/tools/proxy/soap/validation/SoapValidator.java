/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.validation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author jlamande
 */
public class SoapValidator extends XmlValidator {

    private static final Logger LOG =
            LoggerFactory.getLogger(XmlValidator.class);
    private static final String ENV_BEGIN_PATTERN = "(^<[a-zA-Z]+:[e|E]nvelope([^>]*)>.*)|(^<[e|E]nvelope([^>]*)>.*)";
    private static final String ENV_END_PATTERN = "(^</[a-zA-Z]+:[e|E]nvelope([^>]*)>.*)|(^</[e|E]nvelope([^>]*)>.*)";

    public static String cleanXmlRequest(String request) {
        String res = request;
        res = res.trim();
        res = res.replaceAll(">(\\W)*<", "><");
        return res;
    }

    @Override
    protected Node parseXML(String xmlContent) throws ParserConfigurationException, SAXException, IOException {
        //trySoap(cleanXmlRequest(xmlContent));
        String requestXMLBody = cleanXmlRequest(xmlContent);//extractSoapEnvelope(xmlContent);
        Element root = (Element) super.parseXML(requestXMLBody);
        //debugNode(root);

        // SOAP Specifics
        String soapEnvNamespace = "http://schemas.xmlsoap.org/soap/envelope/";
        //Node bodyNode = root.getElementsByTagNameNS(soapEnvNamespace, "Body").item(0);
        Node bodyNode = root.getElementsByTagNameNS(soapEnvNamespace, "Body").item(0);
        System.out.println("BODY NODE : " + (bodyNode != null));
        //debugNode(bodyNode);
        Node requestNode = bodyNode.getFirstChild();
        System.out.println("BODY FIRST NODE");
        if (LOG.isDebugEnabled()) {
            debugNode(requestNode);
        }
        return requestNode;
    }

    private void debugNode(Node node) {
        LOG.debug(" Node Name : " + node.getNodeName());
        LOG.debug(" NS URI : " + node.getNamespaceURI());
        if (node.getNodeValue() != null) {
            LOG.debug(" Node Value : " + node.getNodeValue());
        }
        LOG.debug(" Children : " + node.getChildNodes().getLength());
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            debugNode(child);
        }
    }

    private void trySoap(String requestXMLBody) {
        try {
            SOAPMessage msg = MessageFactory.newInstance().createMessage(null,
                    new ByteArrayInputStream(requestXMLBody.getBytes()));
            LOG.debug("SOAP BODY");
            LOG.debug(msg.getSOAPBody().toString());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private String extractSoapEnvelope(String body) throws IOException {
        StringBuilder requestXMLBuf = new StringBuilder();
        BufferedReader xmlRequestReader = new BufferedReader(new StringReader(body));
        boolean beginFound = false;
        boolean endFound = false;
        String requestLine = xmlRequestReader.readLine();

        while ((!endFound) && (requestLine != null)) {
            //logger.debug("requestLine : " + requestLine);

            if (requestLine.matches(ENV_BEGIN_PATTERN) && requestLine.matches(ENV_END_PATTERN)) {
                beginFound = true;
                endFound = true;
            } else if (requestLine.matches(ENV_BEGIN_PATTERN) && !requestLine.matches(
                    ENV_END_PATTERN)) {
                requestXMLBuf.append(requestLine);
                beginFound = true;
            } else if (!requestLine.matches(ENV_BEGIN_PATTERN) && requestLine.matches(
                    ENV_END_PATTERN)) {
                requestXMLBuf.append(requestLine);
                endFound = true;
            }
            if (beginFound) {
                requestXMLBuf.append(requestLine);
            }
            requestLine = xmlRequestReader.readLine();
        }
        //logger.debug("beginFound : " + beginFound + " endFound=" + endFound);
        return requestXMLBuf.toString();
    }
}
