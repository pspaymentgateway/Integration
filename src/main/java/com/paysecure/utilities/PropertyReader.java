package com.paysecure.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private static Properties configProps = new Properties();
    private static Properties s2sProps = new Properties();
    private static Properties createCustomerAndSession = new Properties();
    private static Properties purchase = new Properties();

    static {
        try {
            loadProperties("propertiesFolder/config.properties", configProps);
            loadProperties("propertiesFolder/s2s.properties", s2sProps);
            loadProperties("propertiesFolder/createCustomerAndSession.properties", createCustomerAndSession);
            loadProperties("propertiesFolder/purchase.properties", purchase);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load property files", e);
        }
    }

    private static void loadProperties(String fileName, Properties props) throws IOException {
        InputStream input = PropertyReader.class
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (input == null) {
            throw new RuntimeException("Property file not found: " + fileName);
        }

        props.load(input);
    }

    public static String getPropertyForconfigProps(String key) {
        return configProps.getProperty(key);
    }

    public static String getPropertyForS2S(String key) {
        return s2sProps.getProperty(key);
    }

    public static String getPropertyForPurchase(String key) {
        return purchase.getProperty(key);
    }

    public static String getPropertyForCreateCustomerSession(String key) {
        return createCustomerAndSession.getProperty(key);
    }
}
