/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlm.tools.proxy.soap.configuration.repository;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.jlm.tools.proxy.soap.configuration.entity.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author julamand
 */
public class ConfigurationRepositoryJson implements ConfigurationRepository {

    private static final Logger LOG =
            LoggerFactory.getLogger(ConfigurationRepositoryJson.class);
    private static ResourceLoader loader = new ResourceLoader();

    @Override
    public Configuration findConfigurationByPath(String path) {
        String configString = loader.load(path);
        // TODO : use name to retrieve conf file
        return buildConfigurationFromJson(configString);
    }

    JAXBContext createJAXBContext() throws JAXBException {
        System.out.println("JAXBCONTEXT : " + JAXBContext.newInstance(Configuration.class).getClass());
        Map<String, Object> properties = new HashMap<String, Object>(3);
        //properties.put(JAXBContextFactory.ECLIPSELINK_OXM_XML_KEY, "org/example/bindingfile/bindings.json");
        properties.put("eclipselink.media-type", "application/json");
        properties.put("eclipselink.json.include-root", false);
        JAXBContext jc = JAXBContext.newInstance(
                new Class[]{
            Configuration.class
        }, properties);
        return jc;
    }

    Configuration buildConfigurationFromJson(String json) {
        try {
            JAXBContext jc = createJAXBContext();
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            // ignore the root element
            unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
            //StreamSource json = new StreamSource(new File("src/org/example/bindingfile/input.json"));
            Configuration conf = unmarshaller.unmarshal(new StreamSource(new java.io.StringReader(json)), Configuration.class).getValue();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Conf from JSON : " + conf);
            }
            return conf;
        } catch (JAXBException e) {
            LOG.error(e.getMessage(), e);
            return new Configuration();
        }
    }

    @Override
    public void save(Configuration configuration) {
        try {
            JAXBContext jc = createJAXBContext();
            Marshaller marshaller = jc.createMarshaller();
            // nice output
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // ignore the root element
            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
            marshaller.marshal(configuration, System.out);
        } catch (JAXBException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
