/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.configuration.entity;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author julamand
 *
 * TODO : change the List impl of ConfigUris by a Map. JSON marshalling can be unaffected.
 *
 */
@XmlRootElement
@ToString
@Getter
@EqualsAndHashCode
public class Configuration implements Serializable {

    private static final transient Logger LOG =
            LoggerFactory.getLogger(Configuration.class);
    private String globalHost = "localhost";
    @Setter
    private int globalPort = 0;
    private String globalProtocol = "http";
    @Setter
    private boolean globalValidationActive = false;
    private List<ConfigurationUri> uris = new ArrayList<ConfigurationUri>();

    public void setGlobalProtocol(String globalProtocol) {
        // only http supported yet
    }

    public void setGlobalHost(String host) {
        if (host != null && !"".equals(host)) {
            this.globalHost = host;
        }
    }

    private String getDefaultUriPrefix() {
        String defaultPrefix = globalProtocol + "://" + globalHost;
        if (globalPort > 0) {
            defaultPrefix += ":" + globalPort;
        }
        return defaultPrefix;
    }

    @XmlElement(name = "uris")
    protected void setUris(List<ConfigurationUri> uris) {
        LOG.debug("Setting Uris");
        LOG.debug(uris.toString());
        List<ConfigurationUri> clearedUris = clearUris(uris);
        this.uris = clearedUris;
    }

    private List<ConfigurationUri> clearUris(List<ConfigurationUri> uris) {
        List<ConfigurationUri> clearedUris = new ArrayList<ConfigurationUri>();
        List<String> registeredSuffixes = new ArrayList<String>();
        // default Uri
        ConfigurationUri defautlConfUri = new ConfigurationUri();
        for (ConfigurationUri uri : getUris()) {
            if (!uri.equals(defautlConfUri) && !registeredSuffixes.contains(uri.getUriSuffix())) {
                clearedUris.add(uri);
                registeredSuffixes.add(uri.getUriSuffix());
            }
        }
        return clearedUris;
    }

    public void addUri(String uriSuffix, boolean isValidationActive, URL targetUrl) {
        // checks if uriSuffix is not already used
        if (getConfigurationUri(uriSuffix) == null) {
            getUris().add(new ConfigurationUri(uriSuffix, isValidationActive, targetUrl));
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug(this.getUris().toString());
            }
            throw new IllegalArgumentException("uriSuffix already in use");
        }
    }

    public ConfigurationUri getConfigurationUri(String uriSuffix) {
        // searches for the Uri with the uriSuffix provided
        ConfigurationUri confUri = null;
        if (uriSuffix != null && !"".equals(uriSuffix.trim())) {
            for (ConfigurationUri uri : getUris()) {
                if (uriSuffix.equals(uri.getUriSuffix())) {
                    confUri = uri.cloneConfigurationUri(getDefaultUriPrefix(),
                            globalValidationActive);
                    break;
                }
            }
        }
        return confUri;
    }

    public ConfigurationUri getConfigurationUri(String uriSuffix, boolean forceCreation) {
        ConfigurationUri confUri = getConfigurationUri(uriSuffix);
        if (confUri == null) {
            confUri = new ConfigurationUri(uriSuffix, globalValidationActive, null).cloneConfigurationUri(
                    getDefaultUriPrefix(),
                    globalValidationActive);
        }
        return confUri;
    }
}
