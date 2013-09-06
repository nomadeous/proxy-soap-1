/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.jlm.tools.proxy.soap.configuration.entity.Configuration;
import org.jlm.tools.proxy.soap.configuration.repository.ConfigurationRepository;
import org.jlm.tools.proxy.soap.configuration.repository.ConfigurationRepositoryJson;
import org.jlm.tools.proxy.soap.frontend.FrontEndCallsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author julamand
 */
public class ApplicationInitListener implements ServletContextListener {

    private static final Logger LOG =
            LoggerFactory.getLogger(ApplicationInitListener.class);
    private static final String DEFAULT_CONF_PATH = "classpath://proxyConfiguration.json";
    private ConfigurationRepository repository;

    public ApplicationInitListener() {
        repository = new ConfigurationRepositoryJson();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Application starting");
        Configuration config = repository.findConfigurationByPath(getConfigurationPath());
        sce.getServletContext().setAttribute("config", config);
        sce.getServletContext().setAttribute("collector", new FrontEndCallsCollector());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info("Application stopped");
        Configuration config = (Configuration) sce.getServletContext().getAttribute("config");
        //repository.save(config, getConfigurationPath() + ".bk");
    }

    private String getConfigurationPath() {
        return System.getProperty("proxyConfigurationPath", DEFAULT_CONF_PATH);
    }
}
