/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.validation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jlamande
 */
public class ValidatorResult {

    private boolean xmlWellFormed = false;
    private boolean xsdValidationActive = false;
    private boolean xsdWellFormed = false;
    private boolean xmlValidatedByXSD = false;
    private List<String> errors = new ArrayList<String>();

    public ValidatorResult() {
    }

    public ValidatorResult(boolean xmlWellFormed, boolean xsdValidationActive, boolean xsdWellFormed, boolean xmlValidatedByXSD, List<String> errors) {
        this.xmlWellFormed = xmlWellFormed;
        this.xsdValidationActive = xsdValidationActive;
        this.xsdWellFormed = xsdWellFormed;
        this.xmlValidatedByXSD = xmlValidatedByXSD;
        this.errors = errors;
    }

    public boolean isValid() {
        if (xsdValidationActive) {
            return xmlValidatedByXSD;
        } else {
            return xmlWellFormed;
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    protected void addError(String error) {
        errors.add(error);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ValidatorResult)) {
            return false;
        }
        ValidatorResult vO = (ValidatorResult) obj;
        if (vO.isValid() != this.isValid()) {
            return false;
        }
        return errors.equals(vO.getErrors());
    }

    @Override
    public String toString() {
        return "ValidationResult{" + "xmlWellFormed=" + xmlWellFormed + ", xsdValidationActive=" + xsdValidationActive + ", xsdWellFormed=" + xsdWellFormed + ", xmlValidatedByXSD=" + xmlValidatedByXSD + ", errors=" + errors + '}';
    }
}
