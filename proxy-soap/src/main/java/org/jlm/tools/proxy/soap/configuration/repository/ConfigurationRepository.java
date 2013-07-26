/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.configuration.repository;

import org.jlm.tools.proxy.soap.configuration.entity.Configuration;

/**
 *
 * @author julamand
 */
public interface ConfigurationRepository {

    Configuration findConfigurationByPath(String path);

    void save(Configuration configuration, String path);
}
