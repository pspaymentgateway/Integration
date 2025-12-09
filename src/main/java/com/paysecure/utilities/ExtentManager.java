package com.paysecure.utilities;




import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.paysecure.utilities.ResourcePathUtil;

@Slf4j
public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Initialize the Extent Report
	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = ResourcePathUtil.findAndCreateReportPath() + "ExtentReport.html";
		//	log.info("[getReporter] Report will be generated on path - {}", reportPath);
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("PaySecure Test Report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(spark);
			extent.setSystemInfo("OS", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User", System.getProperty("user.name"));
		}
		return extent;
	}

	// Start a new test and attach to current thread
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// Flush the report
	public synchronized static void endTest() {
		if (extent != null) {
			extent.flush();
		}
	}

	public void onTestStart(ITestResult result) {
	    String testName = result.getMethod().getMethodName();
	    ExtentTest test = ExtentManager.startTest(testName);
	}
	
	// Get current thread's ExtentTest
	public synchronized static ExtentTest getTest() {
		return test.get();
	}

	// Return current test name
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		return currentTest != null ? currentTest.getModel().getName() : "No active test";
	}

	// Log a step (safe)
	public static void logStep(String logMessage) {
		if (getTest() != null) {
			getTest().info(logMessage);
		} else {
			System.out.println("[WARN] ExtentTest not initialized! logStep skipped: " + logMessage);
		}
	}

	// Log step with screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage) {
		if (getTest() != null) {
			getTest().pass(logMessage);
			attachScreenshot(driver, screenShotMessage);
		} else {
			System.out.println("[WARN] ExtentTest is null in logStepWithScreenshot");
		}
	}

	// For API logging
	public static void logStepValidationForAPI(String logMessage) {
		if (getTest() != null) {
			getTest().pass(logMessage);
		}
	}

	// Log failure with screenshot
	public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) {
		if (getTest() != null) {
			String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
			getTest().fail(colorMessage);
			attachScreenshot(driver, screenShotMessage);
		}
	}

	public static void logFailureAPI(String logMessage) {
		if (getTest() != null) {
			String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
			getTest().fail(colorMessage);
		}
	}

	public static void logSkip(String logMessage) {
		if (getTest() != null) {
			String colorMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
			getTest().skip(colorMessage);
		}
	}

	// Take screenshot with timestamp
	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		String destPath = ResourcePathUtil.getResourcePath("screenshots/" + screenshotName + "_" + timeStamp + ".png").replace("main", "test");
		File finalPath = new File(destPath);
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return convertToBase64(src);
	}

	public static String convertToBase64(File screenShotFile) {
		String base64Format = "";
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(screenShotFile);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64Format;
	}

	public synchronized static void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenShotBase64 = takeScreenshot(driver, getTestName());
			if (getTest() != null) {
				getTest().info(message, MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
			}
		} catch (Exception e) {
			System.out.println("Failed to attach screenshot: " + message);
			e.printStackTrace();
		}
	}

	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
}
