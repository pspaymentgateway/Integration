package com.paysecure.utilities;

import java.nio.file.Paths;

public class pathHelper {

    public static String getResourcePath(String relativePath) {
        return System.getProperty("user.dir") + "/src/test/resources/" + relativePath;
    }

    public static String getProjectRoot() {
        return System.getProperty("user.dir");
    }

    public static String getConfigFilePath() {
        return Paths.get(getProjectRoot(),
                "src", "test", "resources", "propertiesFolder", "config.properties")
                .toString();
    }

    public static String getJsonFilePath(String fileName) {
        return Paths.get(getProjectRoot(),
                "src", "test", "resources", "TestData_Json", fileName)
                .toString();
    }
}


