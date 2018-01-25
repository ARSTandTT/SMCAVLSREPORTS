package com.arsse.smc.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;


/**
 *  class to read the configuration file
 * @author Lekshmijraj
 *
 */


public class Configuration {

    public static final String CONFIG_FILE = "config.properties";

    private Properties properties;
    
    private static Configuration configuration = null;
   
    
    /**
     * Create an object of configuration having all configuration values read
     * from the configuration file
     * 
     */
    private Configuration() {
	try {
		DOMConfigurator.configure(getClass().getResource("/log4j.xml"));
	    properties = new Properties();
	    readConfigurationFile();
	   
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * Returns the instance of configuration.
     * 
     * @return
     */
    public static Configuration getInstance() {
	if(configuration == null) {
	    configuration = new Configuration();
	}
	return configuration; 
    }

    /**
     * Read configuration values from configuration file
     * 
     * @throws IOException
     *             if the configuration file is not found in class path
     */
    private void readConfigurationFile() throws IOException {
	
	InputStream is = this.getClass().getResourceAsStream("/" + CONFIG_FILE);
	if (is == null) {
	    throw new IOException();
	}

	properties.load(is);
    }

    /**
     * Get the value for a configuration property
     * 
     * @param key
     *            the key for the property
     * @return the value of the property or null if key is not found
     */
    public String getValue(String key) {
	return properties.getProperty(key);
    }
}