package com.paysecure.base;

import com.paysecure.enums.OSType;
import com.paysecure.utilities.SystemUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverFactory {

    public static WebDriver createInstance(String browser) {
    	
    	if (browser.equalsIgnoreCase("chrome")) {

    	    WebDriverManager.chromedriver().setup();
    	    ChromeOptions options = new ChromeOptions();

    	    options.addArguments("--headless");
    	    options.addArguments("--no-sandbox");
    	    options.addArguments("--disable-dev-shm-usage");
    	    options.addArguments("--disable-gpu");
    	    options.addArguments("--window-size=1920,1080");
    	    options.addArguments("--remote-allow-origins=*");
    	    options.addArguments("--disable-notifications");

    	    Map<String, Object> prefs = new HashMap<>();

    	    if (OSType.LINUX == SystemUtil.getOperatingSystemType()) {
    	        prefs.put("download.default_directory", "/home/ubuntu/Downloads");
    	        options.setBinary("/usr/bin/google-chrome");
    	    } else {
    	        prefs.put("download.default_directory", "C:\\Downloads");
    	    }

    	    prefs.put("download.prompt_for_download", false);
    	    prefs.put("plugins.always_open_pdf_externally", true);

    	    options.setExperimentalOption("prefs", prefs);

    	    return new ChromeDriver(options);
    
    	}
            else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
            options.addArguments("--disable-notifications");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            return new FirefoxDriver(options);

        }
            //else if (browser.equalsIgnoreCase("edge")) {
//            WebDriverManager.edgedriver().setup();
//            EdgeOptions options = new EdgeOptions();
//            options.addArguments("--disable-gpu");
//            options.addArguments("--window-size=1920,1080");
//            options.addArguments("--disable-notifications");
//            options.addArguments("--no-sandbox");
//            options.addArguments("--disable-dev-shm-usage");
//            return new EdgeDriver(options);
        	else if (browser.equalsIgnoreCase("edge")) {

        	    // 🔹 OPTION 1: Use manual driver (Recommended for your case)
        	    System.setProperty("webdriver.edge.driver", "C:\\WebDrivers\\msedgedriver.exe");

        	    EdgeOptions options = new EdgeOptions();
        	    options.addArguments("--start-maximized");
        	    options.addArguments("--disable-notifications");
        	    options.addArguments("--disable-gpu");
        	    options.addArguments("--no-sandbox");
        	    options.addArguments("--disable-dev-shm-usage");

        	    return new EdgeDriver(options);
        	}

        else {
            throw new IllegalArgumentException("Browser Not Supported: " + browser);
        }
    }
}