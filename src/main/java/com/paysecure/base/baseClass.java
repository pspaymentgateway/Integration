package com.paysecure.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.paysecure.actiondriver.ActionDriver;
import com.paysecure.utilities.ExtentManager;
import com.paysecure.utilities.ResourcePathUtil;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class baseClass {

	protected static Properties prop;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	public static final Logger log = LogManager.getLogger(baseClass.class);

	// Getter method for soft assert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load the configuration file
		prop = new Properties();

		// Try to load from classpath first (for JAR deployment)
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("propertiesFolder\\\\config.properties");

		if (inputStream != null) {
			// Load from classpath
			prop.load(inputStream);
			inputStream.close();
			log.info("config.properties loaded from classpath");
		} else {
			// Fallback to file system (for development)
			String configPath = ResourcePathUtil.getResourcePath("propertiesFolder\\config.properties");
			FileInputStream fis = new FileInputStream(configPath);
			prop.load(fis);
			fis.close();
			log.info("config.properties loaded from file system: " + configPath);
		}

		// Start the Extent Report
		// ExtentManager.getReporter(); --This has been implemented in TestListener
	}

	@BeforeMethod
	public synchronized void setup() throws IOException {
		log.info("Setting up WebDriver for: {}", this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);
		// Sample logger message
		log.info("WebDriver Initialized and Browser Maximized");
		log.trace("This is a Trace message");
		log.error("This is a error message");
		log.debug("This is a debug message");
		log.error("This is a fatal message");
		log.warn("This is a warm message");

		/*
		 * // Initialize the actionDriver only once if (actionDriver == null) {
		 * actionDriver = new ActionDriver(driver);
		 * log.info("ActionDriver instance is created. "+Thread.currentThread().getId
		 * ()); }
		 */

		// Initialize ActionDriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));
		log.info("ActionDriver initlialized for thread: " + Thread.currentThread().getId());

	}

	/*
	 * Initialize the WebDriver based on browser defined in config.properties file
	 */
	private synchronized void launchBrowser() {
		String browser = prop.getProperty("browser");
		driver.set(DriverFactory.createInstance(browser));
		ExtentManager.registerDriver(getDriver());
		log.info(browser + " Driver Instance is created.");
	}

	/*
	 * Configure browser settings such as implicit wait, maximize the browser and
	 * navigate to the URL
	 */

	private void configureBrowser() {
		// Implicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// maximize the browser
		getDriver().manage().window().maximize();

		// Navigate to URL
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			log.error("Failed to Navigate to the URL: {}", e.getMessage());
		}
	}

	@AfterMethod
	public synchronized void tearDown(ITestResult result) {
		// if (result != null && !result.isSuccess()) {
		// captureScreenshotOnFailure(result);
		// }
		if (getDriver() != null) {
			try {
				Thread.sleep(3000);
			//	getDriver().quit();
			} catch (Exception e) {
				log.warn("Unable to quit the driver", e);
			}
		}
		log.info("WebDriver instance is closed.");
		driver.remove();
		actionDriver.remove();
		// driver = null;
		// actionDriver = null;
		// ExtentManager.endTest(); --This has been implemented in TestListener
	}

	/**
	 * Capture screenshot on test failure and attach to Extent report
	 */
	// protected void captureScreenshotOnFailure(ITestResult result) {
	// try {
	// WebDriver driver = getDriver();
	// String testName = result.getMethod().getMethodName();
	// String message = "Test failed: " + testName;
	// ExtentManager.attachScreenshot(driver, message);
	// log.info("Screenshot captured for failed test: {}", testName);
	// } catch (Exception e) {
	// log.error("Failed to capture screenshot on failure", e);
	// }
	// }

	/*
	 * 
	 * 
	 * //Driver getter method public WebDriver getDriver() { return driver; }
	 */

	// Getter Method for WebDriver
	public static WebDriver getDriver() {

		if (driver.get() == null) {
			log.error("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();

	}
	


	// Getter Method for ActionDriver
	public static ActionDriver getActionDriver() {

		if (actionDriver.get() == null) {
			log.error("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actionDriver.get();

	}

	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}

	// Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}