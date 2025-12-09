package com.paysecure.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigProperties {
    private static volatile ConfigProperties instance = null;
    private Properties properties;
    
    private ConfigProperties() {
        properties = new Properties();
        loadProperties();
    }
    
    public static ConfigProperties getInstance() {
        if (instance == null) {
            synchronized (ConfigProperties.class) {
                if (instance == null) {
                    instance = new ConfigProperties();
                }
            }
        }
        return instance;
    }
    
    private void loadProperties() {
        try {
            String configPath = "src/main/resources/config.properties";
            FileInputStream fis = new FileInputStream(configPath);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    // Convenience methods for commonly used properties
    public String getUrl() {
        return getProperty("url");
    }
    
    public String getBrowser() {
        return getProperty("browser");
    }
    
    public String getUserId() {
        return getProperty("id");
    }
    
    public String getPassword() {
        return getProperty("password");
    }
    
    public int getImplicitWait() {
        return Integer.parseInt(getProperty("implicitWait", "20"));
    }
    
    public int getExplicitWait() {
        return Integer.parseInt(getProperty("explicitWait", "20"));
    }
    
    public String getDatabaseUrl() {
        return getProperty("datasource.url");
    }
    
    public String getDatabaseDriver() {
        return getProperty("datasource.driverClassName");
    }
    
    public String getDatabaseUsername() {
        return getProperty("datasource.username");
    }
    
    public String getDatabasePassword() {
        return getProperty("datasource.password");
    }
}
