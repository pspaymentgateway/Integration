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

    	        options.addArguments("--remote-allow-origins=*");
    	        options.addArguments("--disable-gpu");
    	        options.addArguments("--window-size=800,600");
    	        options.addArguments("--disable-notifications");
    	        options.addArguments("--no-sandbox");
    	      
    	       // options.addArguments("--headless");
    	        options.addArguments("--disable-dev-shm-usage");

    	        Map<String, Object> prefs = new HashMap<>();
    	        prefs.put("download.default_directory", "C:\\Downloads");
    	        prefs.put("download.prompt_for_download", false);
    	        prefs.put("plugins.always_open_pdf_externally", true);
    	        prefs.put("credentials_enable_service", false);
    	        prefs.put("profile.password_manager_enabled", false);

    	        options.setExperimentalOption("prefs", prefs);
    	        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
    	        options.setExperimentalOption("useAutomationExtension", false);

    	        if (OSType.LINUX == SystemUtil.getOperatingSystemType()) {
    	            options.setBinary("/usr/bin/google-chrome");
    	        }

    	        WebDriver driver = new ChromeDriver(options);

    
    	        try {
    	            Robot robot = new Robot();
    	            robot.keyPress(KeyEvent.VK_ENTER);
    	            robot.keyRelease(KeyEvent.VK_ENTER);
    	        } catch (AWTException e) {
    	            e.printStackTrace();
    	        }

    	        return driver;
    	   
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

        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-notifications");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            return new EdgeDriver(options);

        } else {
            throw new IllegalArgumentException("Browser Not Supported: " + browser);
        }
    }
}