/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.proxy;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author julamand
 */
@Data
public class ProxyResult {

    private int responseCode;
    private String responseMessage;
    private byte[] responseBody = new byte[0];
    private boolean gzipped = false;
    private Map<String, List<String>> headers;
    private String contentType;
    private String contentEncoding;
}
