/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.frontend;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author julamand
 */
@Getter
@AllArgsConstructor
public class FrontEndCall {

    private Date date;
    private String target;
    private String request;
    private String response;
    private Boolean requestValid;
    private Boolean responseValid;
    private boolean xsdValidation;
    private long proxyTime;
}
