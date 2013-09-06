/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author julamand
 */
public class Proxy {

    private static final Logger LOG =
            LoggerFactory.getLogger(Proxy.class);

    public ProxyResult forward(URL targetURL, String body, HttpServletRequest req) {
        ProxyResult proxyResult = new ProxyResult();
        try {
            HttpURLConnection httpConn = (HttpURLConnection) targetURL.openConnection();
            httpConn.setDoOutput(false);

            this.applyHeaders(req, httpConn);

            httpConn.setRequestMethod(req.getMethod());
            httpConn.setDoOutput(true);
            if (LOG.isDebugEnabled()) {
                LOG.debug("target request body : " + body);
            }
            httpConn.getOutputStream().write(body.getBytes());
            httpConn.getOutputStream().close();

            int responseCode = httpConn.getResponseCode();
            String responseMessage = httpConn.getResponseMessage();
            if (LOG.isDebugEnabled()) {
                LOG.debug("target response code: " + responseCode);
                LOG.debug("target response message: " + responseMessage);
            }
            proxyResult.setResponseCode(httpConn.getResponseCode());
            proxyResult.setResponseMessage(responseMessage);
            proxyResult.setHeaders(httpConn.getHeaderFields());
            proxyResult.setContentType(httpConn.getContentType());
            proxyResult.setContentEncoding(httpConn.getContentEncoding());
            if (httpConn.getContentEncoding().equals("gzip")) {
                proxyResult.setGzipped(true);
            }
            try {
                proxyResult.setResponseBody(this.readHttpConnResponseBody(httpConn));
                if (LOG.isDebugEnabled()) {
                    LOG.debug("target response body : " + proxyResult.getResponseBody());
                }
            } catch (IOException e) {
                LOG.debug("Failed to read target response body", e);
            }
        } catch (IOException e) {
        }
        return proxyResult;
    }

    private void applyHeaders(HttpServletRequest req, HttpURLConnection httpConn) {
        for (Enumeration<String> e = req.getHeaderNames(); e.hasMoreElements();) {
            String headerName = e.nextElement();
            String headerValue = req.getHeader(headerName);
            if (headerName != null && !headerName.toLowerCase().equals("transfer-encoding")) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("adding header (" + headerName + "=" + headerValue + ") to request");
                }
                httpConn.addRequestProperty(headerName, headerValue);
            }
        }
    }

    private byte[] readHttpConnResponseBody(HttpURLConnection resp) throws IOException {
        StringBuilder sB = new StringBuilder();
        InputStream respIS = resp.getInputStream();
        if (resp.getContentEncoding().equals("gzip")) {
            respIS = new java.util.zip.GZIPInputStream(respIS);

        }
        int bytesRead = -1;
        byte[] buffer = new byte[1024];
        while ((bytesRead = respIS.read(buffer)) >= 0) {
            // process the buffer, "bytesRead" have been read, no more, no less
            sB.append(new String(buffer));
        }
        byte[] responseBody = sB.toString().getBytes();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Response body : " + new String(responseBody, "UTF-8"));
        }
        return responseBody;
    }
}
