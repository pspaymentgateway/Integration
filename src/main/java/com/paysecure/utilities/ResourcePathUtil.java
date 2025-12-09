package com.paysecure.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.paysecure.enums.OSType;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for handling resource path resolution dynamically
 */
@Slf4j
public class ResourcePathUtil {
    
    /**
     * Get the dynamic resource path for a given resource file
     * @param resourceName The name of the resource file
     * @return The full path to the resource file
     */
    public static String getResourcePath(String resourceName) {
        // Get the current working directory
        String currentDir = System.getProperty("user.dir");
        
        // Construct the path to src/main/resources
        String resourcePath = currentDir + File.separator + "src" + File.separator + 
                             "main" + File.separator + "resources" + File.separator + resourceName;
        
      //  log.info("Resource path constructed: " + resourcePath);
        return resourcePath;
    }
    
    /**
     * Get resource as InputStream from classpath
     * @param resourceName The name of the resource file
     * @return InputStream for the resource, or null if not found
     */
    public static InputStream getResourceAsStream(String resourceName) {
        return ResourcePathUtil.class.getClassLoader().getResourceAsStream(resourceName);
    }
    
    /**
     * Check if a resource exists in classpath
     * @param resourceName The name of the resource file
     * @return true if resource exists, false otherwise
     */
    public static boolean resourceExists(String resourceName) {
        return getResourceAsStream(resourceName) != null;
    }
    
    /**
     * Get resource path with fallback to file system if not found in classpath
     * @param resourceName The name of the resource file
     * @return The resource path (classpath or file system)
     */
    public static String getResourcePathWithFallback(String resourceName) {
        // First try to get from classpath
        if (resourceExists(resourceName)) {
         //   log.info("Resource found in classpath: " + resourceName);
            return resourceName; // Return just the name for classpath resources
        } else {
            // Fallback to file system
            String filePath = getResourcePath(resourceName);
         //   log.info("Resource not found in classpath, using file system: " + filePath);
            return filePath;
        }
    }
    
    /**
     * Get the dynamic resource path for test resources
     * @param resourceName The name of the resource file
     * @return The full path to the test resource file
     */
    public static String getTestResourcePath(String resourceName) {
        // Get the current working directory
        String currentDir = System.getProperty("user.dir");
        
        // Construct the path to src/test/resources
        String resourcePath = currentDir + File.separator + "src" + File.separator + 
                             "test" + File.separator + "resources" + File.separator + resourceName;
        
     //   log.info("Test resource path constructed: " + resourcePath);
        return resourcePath;
    }

    public static String findAndCreateReportPath() {
        String homePath = System.getProperty("user.home");
        OSType os = SystemUtil.getOperatingSystemType();
        Path reportPath;
        if(os == OSType.LINUX || os == OSType.MAC) {
            reportPath = Paths.get(homePath, "Documents", "reports");
        } else if (os == OSType.WINDOWS) {
            reportPath = Paths.get(homePath, "Documents", "reports");
        } else {
           // log.error("OS type is - {}. Only Windows, Linux, and Mac supported", os);
            throw new IllegalArgumentException("Please use known OS.");
        }
        try {
            Files.createDirectories(reportPath); // Creates all non-existent directories
        } catch (IOException e) {
           // log.error("Failed to create reports directory at {}", reportPath, e);
            throw new RuntimeException("Failed to create reports directory", e);
        }
        return reportPath + File.separator; // Ensures an ending slash/backslash if desired
    }
} 