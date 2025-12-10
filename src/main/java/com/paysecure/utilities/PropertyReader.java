package com.paysecure.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {

    private static Properties properties = new Properties();

    static {
        try {
            String projectPath = System.getProperty("user.dir");
            String configPath = projectPath + "/src/test/resources/propertiesFolder/config.properties";

            FileInputStream fis = new FileInputStream(configPath);
            properties.load(fis);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
