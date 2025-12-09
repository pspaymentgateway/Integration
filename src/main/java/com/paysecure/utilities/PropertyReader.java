package com.paysecure.utilities;



import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("C:\\Users\\LENOVO\\eclipse-workspace\\integrationApiUiTesting\\src\\test\\resource\\propertiesFolder\\config.properties");
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    
  
    
    
    
}
