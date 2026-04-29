package com.paysecure.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private static final Properties configProps = new Properties();
    private static final Properties s2sProps = new Properties();
    private static final Properties createCustomerAndSession = new Properties();
    private static final Properties purchase = new Properties();

    static {
        try {
            load("propertiesFolder/config.properties", configProps);
            load("propertiesFolder/s2s.properties", s2sProps);
            load("propertiesFolder/createCustomerAndSession.properties", createCustomerAndSession);
            load("propertiesFolder/purchase.properties", purchase);
        } catch (IOException e) {
            throw new RuntimeException(" Failed to load property files from classpath", e);
        }
    }

    private static void load(String fileName, Properties props) throws IOException {

        try (InputStream input = PropertyReader.class
                .getClassLoader()
                .getResourceAsStream(fileName)) {

            if (input == null) {
                throw new RuntimeException(" Property file not found in classpath: " + fileName);
            }

            props.load(input);
        }
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
