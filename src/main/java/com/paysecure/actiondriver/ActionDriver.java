package com.paysecure.actiondriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.paysecure.base.baseClass;
import com.paysecure.utilities.ExtentManager;





@Slf4j
public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;

	private final Logger log = LogManager.getLogger(ActionDriver.class);


	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(baseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		log.info("WebDriver instance is created.");
	}

	// Method to click an element
	public void click(By by) {
	    String elementDescription = getElementDescription(by);
	    try {
	        applyBorder(by, "green");

	        // Explicit wait for the element to be clickable
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));

	        element.click();

	        ExtentManager.logStep("Clicked an element: " + elementDescription);
	        log.info("Clicked an element: " , elementDescription);
	        log.info("Clicked an element --> " + elementDescription);
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.warn("Unable to click element:", e);
	        ExtentManager.logFailure(baseClass.getDriver(), "Unable to click element:", elementDescription + " - unable to click");
	        log.error("Unable to click element --> " + elementDescription, e);
	    }
	}
	
	


	// Method to enter text into an input field --Avoid Code Duplication - fix the
	// multiple calling method
	public void enterText(By by, String value) {
	    String elementDescription = getElementDescription(by);
	    try {
	        // Explicit wait for the element to be visible
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

	        applyBorder(by, "green");

	        element.clear();
	        element.sendKeys(value);

	        log.info("Entered text on " + elementDescription + " --> " + value);
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error(String.format("Unable to enter the value on " + elementDescription + ": " + e.getMessage(), e));
	    }
	}

	

	// Method to get text from an input field
	public String getText(By by) {
	    String elementDescription = getElementDescription(by);
	    try {
	        // Explicit wait for visibility of the element
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

	        applyBorder(by, "green");

	        String text = element.getText();
	        log.info("Fetched text from " + elementDescription + " --> " + text);
	        return text;
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to get the text from " + elementDescription + ": " + e.getMessage(), e);
	        return "";
	    }
	}


	// Method to compare Two text -- changed the return type
	public boolean compareText(By by, String expectedText) {
	    String elementDescription = getElementDescription(by);
	    try {
	        // Explicit wait for the element to be visible
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

	        String actualText = element.getText().trim(); // Trimming to avoid false mismatches due to whitespace

	        if (expectedText.equals(actualText)) {
	            applyBorder(by, "green");
	            log.info("Texts are matching: " + actualText + " equals " + expectedText);
	            ExtentManager.logStepWithScreenshot(baseClass.getDriver(), "Compare Text", 
	                "Text Verified Successfully! " + actualText + " equals " + expectedText);
	            return true;
	        } else {
	            applyBorder(by, "red");
	            log.error("Texts are not matching: " + actualText + " not equals " + expectedText);
	            ExtentManager.logFailure(baseClass.getDriver(), "Text Comparison Failed!", 
	                "Text Comparison Failed! " + actualText + " not equals " + expectedText);
	            return false;
	        }
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to compare texts for " + elementDescription + ": " + e.getMessage(), e);
	        return false;
	    }
	}


	/*
	 * Method to check if an element is displayed public boolean isDisplayed(By by)
	 * { try { waitForElementToBeVisible(by); boolean isDisplayed =
	 * driver.findElement(by).isDisplayed(); if (isDisplayed) {
	 * System.out.println("Element is Displayed"); return isDisplayed; } else {
	 * return isDisplayed; } } catch (Exception e) {
	 * System.out.println("Element is not displayed:"+e.getMessage()); return false;
	 * } }
	 */

	// Simplified the method and remove redundant conditions
	public boolean isDisplayed(By by) {
	    String elementDescription = getElementDescription(by);
	    try {
	        // Wait explicitly for the element to be visible
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

	        applyBorder(by, "green");
	        log.info("Element is displayed: " + elementDescription);
	        ExtentManager.logStep("Element is displayed: " + elementDescription);
	        ExtentManager.logStepWithScreenshot(baseClass.getDriver(), "Element is displayed:", "Element is displayed: " + elementDescription);

	        return element.isDisplayed(); // Safe to call after visibility wait
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Element is not displayed: " + e.getMessage(), e);
	        ExtentManager.logFailure(baseClass.getDriver(), "Element is not displayed:", "Element is not displayed: " + elementDescription);
	        return false;
	    }
	}
	
	
	//is enabled method
	public boolean isEnabled(By by) {
	    String elementDescription = getElementDescription(by);
	    try {
	        // Wait for the element to be present and visible
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

	        boolean isEnabled = element.isEnabled();

	        if (isEnabled) {
	            applyBorder(by, "green");
	            log.info("Element is enabled: " + elementDescription);
	            ExtentManager.logStep("Element is enabled: " + elementDescription);
	            ExtentManager.logStepWithScreenshot(baseClass.getDriver(), "Element is enabled", "Element is enabled: " + elementDescription);
	        } else {
	            applyBorder(by, "red");
	            log.warn("Element is **disabled**: " + elementDescription);
	                                                             
	        }

	        return isEnabled;

	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to determine if element is enabled: " + e.getMessage(), e);
	        ExtentManager.logFailure(baseClass.getDriver(), "Element is not enabled", "Element is not enabled: " + elementDescription);
	        return false;
	    }
	}

	public boolean isEmpty(By by) {
	    String elementDescription = getElementDescription(by);

	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

	        String text = element.getText().trim();
	        boolean notEmpty = text != null && !text.isEmpty();

	        if (notEmpty) {
	            applyBorder(by, "green");
	            log.info("Text is NOT empty for: " + elementDescription + " → Value: [" + text + "]");
	            ExtentManager.logStep("Text is NOT empty for: " + elementDescription);
	            ExtentManager.logStepWithScreenshot(baseClass.getDriver(), 
	                                                "Text is NOT empty", 
	                                                "Value: " + text);
	        } else {
	            applyBorder(by, "red");
	            log.warn("Text is EMPTY for: " + elementDescription);
	            ExtentManager.logFailure(baseClass.getDriver(),
	                    "Text is EMPTY",
	                    "Empty text for element: " + elementDescription);
	        }

	        return notEmpty;

	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to get text for: " + elementDescription + " → " + e.getMessage(), e);
	        ExtentManager.logFailure(baseClass.getDriver(),
	                "Exception while checking text empty/not empty",
	                e.getMessage());
	        return false;
	    }
	}



	// Wait for the page to load
	public void waitForPageLoad(int timeOutInSec) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSec));
	        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
	                .executeScript("return document.readyState").equals("complete"));

	        log.info("Page loaded successfully.");
	    } catch (Exception e) {
	        log.error("Page did not load within " + timeOutInSec + " seconds. Exception: " + e.getMessage(), e);
	    }
	}


	// Scroll to an element -- Added a semicolon ; at the end of the script string
	public void scrollToElement(By by) {
	    String elementDescription = getElementDescription(by);
	    try {
	        // Wait for element to be visible before scrolling
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

	        applyBorder(by, "green");

	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", element);

	        log.info("Scrolled to element: " + elementDescription);
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to scroll to element: " + elementDescription + ". Exception: " + e.getMessage(), e);
	    }
	}


	// Wait for Element to be clickable
	private void waitForElementToBeClickable(WebDriver driver, By by, int timeoutInSeconds) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
	        wait.until(ExpectedConditions.elementToBeClickable(by));
	    } catch (Exception e) {
	        log.error("Element is not clickable within " + timeoutInSeconds + " seconds: " + e.getMessage());
	    }
	}

	

	// Wait for Element to be Visible
	private void waitForElementToBeVisible(WebDriver driver, By by, int timeoutInSeconds) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
	        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	    } catch (Exception e) {
	        log.error("Element is not visible within " + timeoutInSeconds + " seconds: " + e.getMessage());
	    }
	}


	// Method to get the description of an element using By locator
	public String getElementDescription(By locator) {
	    if (driver == null) {
	        return "Driver is not initialized.";
	    }
	    if (locator == null) {
	        return "Locator is null.";
	    }

	    try {
	        // Wait for the element to be visible
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

	        // Get element attributes
	        String name = element.getDomProperty("name");
	        String id = element.getDomProperty("id");
	        String text = element.getText();
	        String className = element.getDomProperty("class");
	        String placeholder = element.getDomProperty("placeholder");

	        // Return a description based on available attributes
	        if (isNotEmpty(name)) {
	            return "Element with name: " + name;
	        } else if (isNotEmpty(id)) {
	            return "Element with ID: " + id;
	        } else if (isNotEmpty(text)) {
	            return "Element with text: " + truncate(text, 50);
	        } else if (isNotEmpty(className)) {
	            return "Element with class: " + className;
	        } else if (isNotEmpty(placeholder)) {
	            return "Element with placeholder: " + placeholder;
	        } else {
	            return "Element located using: " + locator.toString();
	        }

	    } catch (TimeoutException e) {
	        return "Element not visible after waiting " + 10 + " seconds.";
	    } catch (Exception e) {
	        e.printStackTrace(); // Replace with logger
	        return "Unable to describe element due to error: " + e.getMessage();
	    }
	}


	// Utility method to check if a string is not null or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility method to truncate long strings
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}
	
	//Utility Method to Border an element
	public void applyBorder(By by,String color) {
		try {
			//Locate the element
			WebElement element = driver.findElement(by);
			//Apply the border
			String script = "arguments[0].style.border='3px solid "+color+"'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			log.info("Applied the border with color "+color+ " to element: "+getElementDescription(by));
		} catch (Exception e) {
			log.warn("Failed to apply the border to an element: "+getElementDescription(by),e);
		}
	}
	
	 // ===================== Select Methods =====================
	
    // Method to select a dropdown by visible text
	public void selectByVisibleText(By by, String value) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by)); // Wait until dropdown is ready

	        new Select(element).selectByVisibleText(value);
	        applyBorder(by, "green");
	        log.info("Selected dropdown value: " + value);
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to select dropdown value: " + value, e);
	    }
	}

    
    // Method to select a dropdown by value
	public void selectByValue(By by, String value) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by)); // Ensures it's visible and enabled

	        new Select(element).selectByValue(value);
	        applyBorder(by, "green");
	        log.info("Selected dropdown value by actual value: " + value);
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to select dropdown by value: " + value, e);
	    }
	}

    
    // Method to select a dropdown by index
	public void selectByIndex(By by, int index) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by)); // Wait until dropdown is ready

	        new Select(element).selectByIndex(index);
	        applyBorder(by, "green");
	        log.info("Selected dropdown value by index: " + index);
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to select dropdown by index: " + index, e);
	    }
	}

	public String getFirstSelectedOption(By by) {
	    String selectedOptionText = "";
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	        WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

	        Select select = new Select(dropdownElement);
	        WebElement selectedOption = select.getFirstSelectedOption();
	        selectedOptionText = selectedOption.getText().trim();

	        applyBorder(by, "green");
	        log.info("Selected option for " + getElementDescription(by) + ": " + selectedOptionText);
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to get selected dropdown option: " + e.getMessage());
	    }
	    return selectedOptionText;
	}

    
 // Method to get all options from a dropdown
	public List<String> getDropdownOptions(By by) {
	    List<String> optionsList = new ArrayList<>();
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

	        Select select = new Select(dropdownElement);
	        for (WebElement option : select.getOptions()) {
	            optionsList.add(option.getText().trim());
	        }

	        applyBorder(by, "green");
	        log.info("Retrieved dropdown options for " + getElementDescription(by));
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to get dropdown options: " + e.getMessage());
	    }
	    return optionsList;
	}


    
    // ===================== JavaScript Utility Methods =====================
    
	public void clickUsingJS(By by) {
		WebDriver driver=baseClass.getDriver();
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));

	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	        applyBorder(by, "green");                                                                                                                                                                                                   
	        log.info("Clicked element using JavaScript: " + getElementDescription(by));
	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to click using JavaScript on element: " + by.toString(), e);
	    }
	}
	
	public void pressEnter(By by) {
	    WebDriver driver = baseClass.getDriver();
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

	        element.sendKeys(Keys.ENTER);

	        applyBorder(by, "green");
	        log.info("Pressed ENTER key on element: " + getElementDescription(by));

	    } catch (Exception e) {
	        applyBorder(by, "red");
	        log.error("Unable to press ENTER on element: " + by.toString(), e);
	    }
	}

	
	//safe click for - headless mode 
	public void safeClick(By locator,int timeoutSeconds) {
		WebDriver driver=baseClass.getDriver();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    try {
	        // Wait for element to be present in DOM
	        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

	        // Scroll element into view
	        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

	        // Extra wait for overlays/loaders to disappear
	        try {
	            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay")));
	        } catch (TimeoutException ignored) {
	            // Overlay not found or never invisible, ignore
	        }

	        // Wait until clickable
	        element = wait.until(ExpectedConditions.elementToBeClickable(locator));

	        // Small delay (headless can be too fast)
	        Thread.sleep(200);

	        // Click
	        element.click();

	    } catch (Exception e) {
	        System.err.println("Safe click failed: " + e.getMessage());
	        // Last-resort JS click
	        WebElement element = driver.findElement(locator);
	        js.executeScript("arguments[0].click();", element);
	    }
	}
	
	//safe gettext --headless mode
	public String safeGetText(By locator, WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

	    // Try normal getText()
	    String text = element.getText().trim();

	    // If empty, try textContent directly from DOM
	    if (text.isEmpty()) {
	        text = element.getAttribute("textContent").trim();
	    }

	    // Final fallback using JavaScript
	    if (text.isEmpty()) {
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        text = ((String) js.executeScript(
	            "return arguments[0].textContent;", element)).trim();
	    }

	    return text;
	}

	//zoom for in or out  for Headless mode
	public void zoomPage(WebDriver driver, String action, int times) throws InterruptedException {
	    Actions actions = new Actions(driver);

	    switch (action.toLowerCase()) {
	        case "in":
	            for (int i = 0; i < times; i++) {
	                actions.keyDown(Keys.CONTROL).sendKeys(Keys.ADD).keyUp(Keys.CONTROL).perform();
	                Thread.sleep(300);
	            }
	            break;

	        case "out":
	            for (int i = 0; i < times; i++) {
	                actions.keyDown(Keys.CONTROL).sendKeys(Keys.SUBTRACT).keyUp(Keys.CONTROL).perform();
	                Thread.sleep(300);
	            }
	            break;

	        case "reset":
	            actions.keyDown(Keys.CONTROL).sendKeys("0").keyUp(Keys.CONTROL).perform();
	            break;

	        default:
	            System.out.println("Invalid zoom action. Use 'in', 'out', or 'reset'.");
	    }
	}

	public void setZoomPercent(WebDriver driver, int percent) {
	    String script = "document.body.style.zoom = '" + percent + "%';";
	    ((JavascriptExecutor) driver).executeScript(script);
	}
	
	// Scale using CSS transform (keeps layout origin at top-left)
	public void setScale(WebDriver driver, double scale) {
	    String script = "document.body.style.transform = 'scale(" + scale + ")';"
	                  + "document.body.style.transformOrigin = '0 0';";
	    ((JavascriptExecutor) driver).executeScript(script);
	}
	
	   // Method to apply zoom using Chrome DevTools Protocol (CDP)
    public void setZoom(ChromeDriver driver, double zoomLevel) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageScaleFactor", zoomLevel); // Correct parameter name
        driver.executeCdpCommand("Emulation.setPageScaleFactor", params);
    }
    
    // Method to scroll to the bottom of the page
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        log.info("Scrolled to the bottom of the page.");
    }
    
    // Method to highlight an element using JavaScript
    public void highlightElementJS(By by) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
            log.info("Highlighted element using JavaScript: " + getElementDescription(by));
        } catch (Exception e) {
            log.error("Unable to highlight element using JavaScript", e);
        }
    }
    
    
    
    public String getAttribute(By by, String attributeName) {
        String elementDescription = getElementDescription(by);
        String attributeValue = null;

        try {
            applyBorder(by, "green");

            // Explicit wait for the element to be visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

            attributeValue = element.getAttribute(attributeName);

            ExtentManager.logStep("Fetched attribute '" + attributeName + "' from element: " + elementDescription 
                                  + " | Value: " + attributeValue);
            log.info("Fetched attribute '{}' from element: {} | Value: {}", attributeName, elementDescription, attributeValue);

        } catch (Exception e) {
            applyBorder(by, "red");
            log.warn("Unable to fetch attribute '{}' from element: {}", attributeName, elementDescription, e);
            ExtentManager.logFailure(baseClass.getDriver(),
                    "Unable to fetch attribute from element:",
                    elementDescription + " | attribute: " + attributeName);
            log.error("Error fetching attribute '{}' from element: {}", attributeName, elementDescription, e);
        }

        return attributeValue;
    }

    
    // ===================== Window and Frame Handling =====================
    
    // Method to switch between browser windows
    public void switchToWindow(String windowTitle) {
        try {
            Set<String> windows = driver.getWindowHandles();
            for (String window : windows) {
                driver.switchTo().window(window);
                if (driver.getTitle().equals(windowTitle)) {
                    log.info("Switched to window: " + windowTitle);
                    return;
                }
            }
            log.warn("Window with title " + windowTitle + " not found.");
        } catch (Exception e) {
            log.error("Unable to switch window", e);
        }
    }
    
    // Method to switch to an iframe
    public void switchToFrame(By by) {
        try {
            driver.switchTo().frame(driver.findElement(by));
            log.info("Switched to iframe: " + getElementDescription(by));
        } catch (Exception e) {
            log.error("Unable to switch to iframe", e);
        }
    }
    
    // Method to switch back to the default content
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        log.info("Switched back to default content.");
    }
    
    // ===================== Alert Handling =====================
    
    // Method to accept an alert popup
    public void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
            log.info("Alert accepted.");
        } catch (Exception e) {
            log.error("No alert found to accept", e);
        }
    }
    
    // Method to dismiss an alert popup
    public void dismissAlert() {
        try {
            driver.switchTo().alert().dismiss();
            log.info("Alert dismissed.");
        } catch (Exception e) {
            log.error("No alert found to dismiss", e);
        }
    }
    
    // Method to get alert text
    public String getAlertText() {
        try {
            return driver.switchTo().alert().getText();
        } catch (Exception e) {
            log.error("No alert text found", e);
            return "";
        }
    }
    
    // ===================== Browser Actions =====================
    
    public void refreshPage() {
        try {
            driver.navigate().refresh();
            ExtentManager.logStep("Page refreshed successfully.");
            log.info("Page refreshed successfully.");
        } catch (Exception e) {
            ExtentManager.logFailure(baseClass.getDriver(), "Unable to refresh page", "refresh_page_failed");
            log.error("Unable to refresh page: " + e.getMessage());
        }
    }

    public String getCurrentURL() {
        try {
            String url = driver.getCurrentUrl();
            ExtentManager.logStep("Current URL fetched: " + url);
            log.info("Current URL fetched: " + url);
            return url;
        } catch (Exception e) {
            ExtentManager.logFailure(baseClass.getDriver(), "Unable to fetch current URL", "get_current_url_failed");
            log.error("Unable to fetch current URL: " + e.getMessage());
            return null;
        }
    }

    public void maximizeWindow() {
        try {
            driver.manage().window().maximize();
            ExtentManager.logStep("Browser window maximized.");
            log.info("Browser window maximized.");
        } catch (Exception e) {
            ExtentManager.logFailure(baseClass.getDriver(), "Unable to maximize window", "maximize_window_failed");
            log.error("Unable to maximize window: " + e.getMessage());
        }
    }
    
 // ===================== Advanced WebElement Actions =====================

    public void moveToElement(By by) {
        String elementDescription = getElementDescription(by); // Description uses same timeout
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by)); // Wait for visibility

            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();

            ExtentManager.logStep("Moved to element: " + elementDescription);
            log.info("Moved to element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(baseClass.getDriver(), "Unable to move to element", elementDescription + "_move_failed");
            log.error("Unable to move to element: " + e.getMessage());
        }
    }


    public void dragAndDrop(By source, By target) {
        String sourceDescription = getElementDescription(source);
        String targetDescription = getElementDescription(target);
        try {
            Actions actions = new Actions(driver);
            actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).perform();
            ExtentManager.logStep("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
            log.info("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(baseClass.getDriver(), "Unable to drag and drop", sourceDescription + "_drag_failed");
            log.error("Unable to drag and drop: " + e.getMessage());
        }
    }

    public void doubleClick(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.doubleClick(driver.findElement(by)).perform();
            ExtentManager.logStep("Double-clicked on element: " + elementDescription);
            log.info("Double-clicked on element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(baseClass.getDriver(), "Unable to double-click element", elementDescription + "_doubleclick_failed");
            log.error("Unable to double-click element: " + e.getMessage());
        }
    }

    public void rightClick(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.contextClick(driver.findElement(by)).perform();
            ExtentManager.logStep("Right-clicked on element: " + elementDescription);
            log.info("Right-clicked on element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(baseClass.getDriver(), "Unable to right-click element", elementDescription + "_rightclick_failed");
            log.error("Unable to right-click element: " + e.getMessage());
        }
    }

    public void sendKeysWithActions(By by, String value) {
        int timeoutInSeconds = 30;
        String elementDescription = getElementDescription(by);
        
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by)); // Wait for element to be visible

            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().sendKeys(value).perform();

            ExtentManager.logStep("Sent keys to element: " + elementDescription + " | Value: " + value);
            log.info("Sent keys to element --> " + elementDescription + " | Value: " + value);
        } catch (Exception e) {
            ExtentManager.logFailure(baseClass.getDriver(), "Unable to send keys", elementDescription + "_sendkeys_failed");
            log.error("Unable to send keys to element: " + e.getMessage());
        }
    }


    public void clearText(By by) {
        String elementDescription = getElementDescription(by);
        try {
            driver.findElement(by).clear();
            ExtentManager.logStep("Cleared text in element: " + elementDescription);
            log.info("Cleared text in element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(baseClass.getDriver(), "Unable to clear text", elementDescription + "_clear_failed");
            log.error("Unable to clear text in element: " + e.getMessage());
        }
    }   
    
 // Method to upload a file
    public void uploadFile(By by, String filePath) {
        try {
            driver.findElement(by).sendKeys(filePath);
            applyBorder(by, "green");
            log.info("Uploaded file: " + filePath);
        } catch (Exception e) {
            applyBorder(by, "red");
            log.error("Unable to upload file: " + e.getMessage());
        }
    }

//---------------------------------------------------------------
    public List<String> validateAndGetTableHeaders(WebDriver driver, By headerLocator, List<String> expectedHeaders) {
        // Fetch headers from UI
        List<WebElement> headers = driver.findElements(headerLocator);
        List<String> actualHeaders = new ArrayList<>();

        for (WebElement h : headers) {
            actualHeaders.add(h.getText().trim());
        }

        Reporter.log("Fetched Headers from UI: " + actualHeaders, true);
        Reporter.log("Expected Headers: " + expectedHeaders, true);

        // Assertion
        Assert.assertEquals(actualHeaders, expectedHeaders,
                "Mismatch in table headers! Expected: " + expectedHeaders + " but got: " + actualHeaders);

        Reporter.log("Header validation successful. Headers matched: " + actualHeaders, true);

        // Return actual headers for further use
        return actualHeaders;
    }


    
}