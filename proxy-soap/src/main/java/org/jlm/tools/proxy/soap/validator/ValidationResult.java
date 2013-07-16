/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.validator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jlamande
 */
public class ValidationResult {

    private boolean xmlWellFormed = false;
    private boolean xsdValidationActive = false;
    private boolean xsdWellFormed = false;
    private boolean xmlValidatedByXSD = false;
    private List<String> errors = new ArrayList<String>();

    public ValidationResult() {
    }

    public ValidationResult(boolean xmlWellFormed, boolean xsdValidationActive, boolean xsdWellFormed, boolean xmlValidatedByXSD, List<String> errors) {
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
        if (!(obj instanceof ValidationResult)) {
            return false;
        }
        ValidationResult vO = (ValidationResult) obj;
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
