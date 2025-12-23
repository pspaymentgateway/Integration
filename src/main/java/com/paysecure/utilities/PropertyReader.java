package com.paysecure.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {


	    private static Properties configProps = new Properties();
	    private static Properties s2sProps = new Properties();

	    static {
	        try {
	            String projectPath = System.getProperty("user.dir");

	            FileInputStream configFis =
	                    new FileInputStream(projectPath + "/src/test/resources/propertiesFolder/config.properties");
	            configProps.load(configFis);

	            FileInputStream s2sFis =
	                    new FileInputStream(projectPath + "/src/test/resources/propertiesFolder/s2s.properties");
	            s2sProps.load(s2sFis);

	        } catch (IOException e) {
	            throw new RuntimeException("Failed to load property files", e);
	        }
	    }

	    public static String getProperty(String key) {
	        return configProps.getProperty(key);
	    }

	    public static String getPropertyForS2S(String key) {
	        return s2sProps.getProperty(key);
	    }
	}


