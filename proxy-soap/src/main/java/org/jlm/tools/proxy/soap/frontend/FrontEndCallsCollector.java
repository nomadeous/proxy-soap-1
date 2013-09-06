/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.frontend;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author julamand
 */
public class FrontEndCallsCollector {

    private static final int MAX_SIZE = 50;
    List<FrontEndCall> internalList = Collections.synchronizedList(new ArrayList<FrontEndCall>());

    public void addCall(String requestUri, boolean xsdValidation, String requestBody) {
        this.addCall(new FrontEndCall(Calendar.getInstance().getTime(), requestUri, requestBody, "",
                false, null, xsdValidation, 0));
    }

    public void addCall(String requestUri, boolean xsdValidation, String requestBody,
            String responseBody,
            boolean requestValid, boolean responseValid, long time) {
        this.addCall(new FrontEndCall(Calendar.getInstance().getTime(), requestUri, requestBody,
                responseBody, requestValid,
                responseValid, xsdValidation, time));
    }

    private void addCall(FrontEndCall call) {
        if (internalList.size() > MAX_SIZE) {
            internalList.remove(0);
        }
        internalList.add(call);
    }

    public List<FrontEndCall> getCalls() {
        List<FrontEndCall> calls = ImmutableList.copyOf(this.internalList);
        return calls;
    }
}
