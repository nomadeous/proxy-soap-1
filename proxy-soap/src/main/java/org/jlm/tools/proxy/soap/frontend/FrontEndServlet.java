/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.frontend;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jlm.tools.proxy.soap.configuration.entity.Configuration;
import org.jlm.tools.proxy.soap.configuration.entity.ConfigurationUri;
import org.jlm.tools.proxy.soap.io.Streams;
import org.jlm.tools.proxy.soap.proxy.Proxy;
import org.jlm.tools.proxy.soap.proxy.ProxyResult;
import org.jlm.tools.proxy.soap.validation.SoapValidator;
import org.jlm.tools.proxy.soap.validation.ValidatorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author julamand
 */
public class FrontEndServlet extends HttpServlet {

    private static final Logger LOG =
            LoggerFactory.getLogger(FrontEndServlet.class);
    Configuration config;
    FrontEndCallsCollector collector;

    @Override
    public void init(ServletConfig sconfig) throws ServletException {
        super.init(sconfig);
        config = (Configuration) this.getServletContext().getAttribute("config");
        collector = (FrontEndCallsCollector) this.getServletContext().getAttribute("collector");
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("POST - Path Info : " + request.getPathInfo());
        }
        String requestUri = request.getPathInfo();
        String requestBody = Streams.getString(request.getInputStream());
        ConfigurationUri configUri = config.getConfigurationUri(requestUri, true);

        SoapValidator validator = new SoapValidator();
        boolean xsdValidation = configUri.isValidationActive();
        if (xsdValidation) {
            // create SoapValidator and pass the wsdl path
            // validate the request
            // could validate the xml even without xsd validation
        } else {
            ValidatorResult res = validator.validate(requestBody);
            LOG.debug("Response already committed ? " + response.isCommitted());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Request Validation Result " + res);
            }
            if (!res.isValid()) {
                // 400 Bad Request
                // The request cannot be fulfilled due to bad syntax.
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid SOAP content received from client");
                collector.addCall(requestUri, xsdValidation, requestBody);
                return;
            }
        }

        URL targetUrl = configUri.getTargetUrl();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Target URL " + targetUrl);
        }
        Proxy proxy = new Proxy();
        long start = System.currentTimeMillis();
        ProxyResult result = proxy.forward(targetUrl, requestBody, request);
        long proxyTime = System.currentTimeMillis() - start;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Proxy time " + proxyTime + "ms");
        }
        int responseCode = result.getResponseCode();
        if (responseCode != 200) {
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY,
                    "Invalid response received from server");
            collector.addCall(requestUri, xsdValidation, requestBody, new String(
                    result.getResponseBody()),
                    true, false,
                    proxyTime);
            return;
        }

        if (xsdValidation) {
            // reuse SoapValidator and pass the wsdl path
            // validate the response
            // could validate the xml even without xsd validation
        } else {
            ValidatorResult forwardValidationRes = validator.validate(new String(
                    result.getResponseBody()));
            if (!forwardValidationRes.isValid()) {
                // 502 Bad Gateway
                // The server was acting as a gateway or proxy
                // and received an invalid response from the upstream server.
                response.sendError(HttpServletResponse.SC_BAD_GATEWAY,
                        "Invalid SOAP content received from server");
                collector.addCall(requestUri, xsdValidation, requestBody, new String(
                        result.getResponseBody()),
                        true, false,
                        proxyTime);

                return;
            }
        }
        collector.addCall(requestUri, xsdValidation, requestBody, new String(
                result.getResponseBody()), true,
                true, proxyTime);

        this.addResponseHeaders(response, result);
        if (result.getResponseBody() != null && result.getResponseBody().length > 0) {
            LOG.debug("Is committed : " + response.isCommitted());
            response.setContentLength(result.getResponseBody().length);
//            if (result.isGzipped()) {
//                LOG.info("Gzipping response...");
//                GZIPOutputStream gzOS = new java.util.zip.GZIPOutputStream(
//                        response.getOutputStream());
//                gzOS.write(
//                        result.getResponseBody());
//                response.getOutputStream().close();
//                gzOS.close();
//            } else {
            response.getOutputStream().write(result.getResponseBody());
            response.getOutputStream().close();
            //}
            LOG.debug("Is committed : " + response.isCommitted());
        }
    }

    private void addResponseHeaders(HttpServletResponse resp, ProxyResult responseHandler) {
        for (Map.Entry<String, List<String>> respHeader : responseHandler.getHeaders().entrySet()) {
            String headerName = respHeader.getKey();
            List<String> headerValues = respHeader.getValue();
            for (String headerValue : headerValues) {
                if (headerName != null && !headerName.toLowerCase().equals("transfer-encoding") && !headerName.toLowerCase().equals(
                        "content-encoding")) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(
                                "adding header (" + headerName + "=" + headerValue + ") to response");
                    }
                    resp.addHeader(headerName, headerValue);
                }
            }
        }
    }
}
