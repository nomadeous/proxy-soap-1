/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.configuration.entity;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author julamand
 */
@EqualsAndHashCode
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationUri implements Serializable {

    private static final transient Logger LOG =
            LoggerFactory.getLogger(ConfigurationUri.class);
    // Fields
    private String uriSuffix;
    @XmlElement(name = "validationActive")
    private Boolean validationActive = null;
    @XmlElement(name = "targetUrl")
    private URL targetUrl;

    public boolean isValidationActive() {
        return (validationActive != null) ? validationActive : false;
    }

    protected ConfigurationUri cloneConfigurationUri(String defaultTarget, Boolean defaultValidation) {
        Boolean validation = validationActive;
        if (validation == null) {
            validation = defaultValidation;
        }
        URL url = targetUrl;
        if (url == null) {
            // if targetURL is not or bad setted (non parseable URL)
            // alors la targetUrl par defaut =
            // globalProtocol + "://" + globalHost:globalPort + uriSuffix
            try {
                url = new URL(defaultTarget + uriSuffix);
            } catch (Exception e2) {
                LOG.warn("URL computing in error : " + e2.getMessage());
            }
        }
        return new ConfigurationUri(uriSuffix, validation, url);
    }

    @XmlElement(name = "uriSuffix")
    public void setUriSuffix(String uriSuffix) {
        if (uriSuffix == null || "".equals(uriSuffix.trim())) {
            throw new IllegalArgumentException("uriSuffix is not well formed");
        }
        // add a leading /
        if (!uriSuffix.startsWith("/")) {
            uriSuffix = "/" + uriSuffix;
        }
        if (isUriSuffixFormatValid(uriSuffix)) {
            LOG.debug("URI Suffix is OK");
        } else {
            LOG.debug("URI Suffix '" + uriSuffix + "'is KO");
            throw new IllegalArgumentException("uriSuffix is not well formed");
        }
        this.uriSuffix = uriSuffix;
    }

    public boolean isUriSuffixFormatValid(String uriSuffix) {
        boolean isValid = false;
        // make a try with a sample host
        try {
            if (uriSuffix.indexOf(" ") == -1) {
                URL url = new URL("http://localhost" + uriSuffix);
                LOG.debug(url.toString());
                isValid = true;
            }
        } catch (MalformedURLException e) {
            LOG.warn("URI Suffix is OK");
        }
        return isValid;
    }
}
