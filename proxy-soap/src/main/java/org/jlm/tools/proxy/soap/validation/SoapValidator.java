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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author jlamande
 */
public class SoapValidator extends XmlValidator {

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
        System.out.println("REQUEST NODE");
        debugNode(requestNode);
        return requestNode;
    }
    
    private void debugNode(Node node) {
        System.err.println(" Node Name : " + node.getNodeName());
        System.err.println(" NS URI : " + node.getNamespaceURI());
        if(node.getNodeValue() != null) {
            System.err.println(" Node Value : " + node.getNodeValue());
        }
        System.err.println(" Children : " + node.getChildNodes().getLength());
        for(int i=0;i<node.getChildNodes().getLength();i++) {
            Node child = node.getChildNodes().item(i);
            debugNode(child);
        }
    }

    private void trySoap(String requestXMLBody) {
        try {
            SOAPMessage msg = MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(requestXMLBody.getBytes()));
            System.out.println("SOAP BODY");
            System.out.println(msg.getSOAPBody());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
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
            } else if (requestLine.matches(ENV_BEGIN_PATTERN) && !requestLine.matches(ENV_END_PATTERN)) {
                requestXMLBuf.append(requestLine);
                beginFound = true;
            } else if (!requestLine.matches(ENV_BEGIN_PATTERN) && requestLine.matches(ENV_END_PATTERN)) {
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