/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.configuration.repository;

import org.jlm.tools.proxy.soap.configuration.entity.Configuration;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author julamand
 */
public class ConfigurationRepositoryJsonTest {

    String jsonConf1 = "{\n"
            + "    \"globalHost\": \"\",\n"
            + "    \"globalPort\": 0,\n"
            + "    \"uris\": {\n"
            + "        \"/services/service1\": {\"validation\": true, \"targetUrl\": \"http://www.google.fr/soap\"},\n"
            + "        \"/services/service2\": {\"validation\": true, \"targetUrl\": \"bad url\"},\n"
            + "        \"/services/service3\": {\"validation\": false, \"targetUrl\": \"\"},\n"
            + "        \"/services/service4\": {\"validation\": true},\n"
            + "        \"/services/service5\": {},\n"
            + "        \"/ bad uri \": {}\n"
            + "    }\n"
            + "}\n";
    String jsonConf2 = "{\n"
            + "    \"globalHost\": \"\",\n"
            + "    \"globalPort\": 0,\n"
            + "    \"globalValidationActive\" : true,\n"
            + "    \"globalProtocol\" : \"\",\n"
            + "    \"uris\": [\n"
            + "        {\"uriSuffix\": \"services/service1\", \"validationActive\": true, \"targetUrl\": \"http://www.google.fr/soap\"},\n"
            + "        {\"uriSuffix\": \"/services/service2\", \"validationActive\": true, \"targetUrl\": \"bad url\"},\n"
            + "        {\"uriSuffix\": \"/services/service3\", \"validationActive\": false, \"targetUrl\": \"\"},\n"
            + "        {\"uriSuffix\": \"/services/service4\", \"validationActive\": true},\n"
            + "        {\"uriSuffix\": \"/services/service5\"},\n"
            + "        {\"uriSuffix\": \"/services/service5\"},\n"
            + "        {\"uriSuffix\": \"/ bad uri \"}\n"
            + "    ]\n"
            + "}";

    public ConfigurationRepositoryJsonTest() {
    }

    /**
     * Test of findConfigurationByPath method, of class
     * ConfigurationRepositoryJson.
     */
    @Test
    public void testBuildConfigurationFromJson() {
        ConfigurationRepositoryJson repo = new ConfigurationRepositoryJson();
        Configuration conf = repo.buildConfigurationFromJson(jsonConf2);
        assertNotNull(conf);
        assertEquals(0, conf.getGlobalPort());
        assertEquals("localhost", conf.getGlobalHost());
        assertEquals(5, conf.getUris().size());
        assertNotNull(conf.getConfigurationUri("/services/service1"));
        assertEquals(true, conf.getConfigurationUri("/services/service1").isValidationActive());
        assertEquals("http://www.google.fr/soap", conf.getConfigurationUri("/services/service1").getTargetUrl().toString());
        assertNotNull(conf.getConfigurationUri("/services/service2").getTargetUrl());
        assertTrue(conf.getConfigurationUri("/services/service2").getTargetUrl().toString().startsWith(conf.getGlobalProtocol() + "://" + conf.getGlobalHost() + "/"));
    }

    @Test
    public void testDefaultConf() {
        ConfigurationRepositoryJson repo = new ConfigurationRepositoryJson();
        Configuration conf = repo.buildConfigurationFromJson("");
        assertEquals(new Configuration(), conf);
    }

    /**
     * Test of findConfigurationByPath method, of class
     * ConfigurationRepositoryJson.
     */
    @Test
    public void testFindConfigurationByPath() {
        ConfigurationRepositoryJson repo = new ConfigurationRepositoryJson();
        Configuration conf = repo.findConfigurationByPath("classpath://conf.json");
        assertNotNull(conf);
        assertEquals(5, conf.getUris().size());
    }

    /**
     * Test of save method, of class ConfigurationRepositoryJson.
     */
    @Test
    public void testSave() throws Exception {
        ConfigurationRepositoryJson repo = new ConfigurationRepositoryJson();
        Configuration conf = repo.buildConfigurationFromJson(jsonConf2);
        conf.addUri("test", true, new java.net.URL("http://www.google.fr"));
        repo.save(conf);
    }
}
