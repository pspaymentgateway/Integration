package com.paysecure.Page;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.paysecure.actiondriver.ActionDriver;
import com.paysecure.base.baseClass;
import com.paysecure.locators.cashierPageLocators;


public class matrixCashierPage {

	private By cardHolderName=By.xpath(cashierPageLocators.cardHolderName);
	private By cardHolderNumber=By.xpath(cashierPageLocators.cardHolderNumber);
	private By cardMonthYear=By.xpath(cashierPageLocators.cardMonthYear);
	private By cardCvcNumber=By.xpath(cashierPageLocators.cardCvcNumber);
	private By pay=By.xpath(cashierPageLocators.pay);
	private By luhanAlgo=By.xpath(cashierPageLocators.luhanAlgo);
	
	private By emailID=By.xpath(cashierPageLocators.emailID);
	private By fullname=By.xpath(cashierPageLocators.fullname);
	private By address=By.xpath(cashierPageLocators.address);
	private By city=By.xpath(cashierPageLocators.city);
	private By zipcode=By.xpath(cashierPageLocators.zipcode);
	private By total=By.xpath(cashierPageLocators.total);
	private By swalError=By.xpath("//div[@id='swal2-html-container']");

	private ActionDriver actionDriver;
	// page factory constructor
	public matrixCashierPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.actionDriver = new ActionDriver(driver);
	}
	
	public String entercardNumber;
	public void userEnterCardInformationForPayment(
			String cardHolder, String cardNumber,String expiry, String cvc) throws InterruptedException {
		WebDriver driver=baseClass.getDriver();
//		Thread.sleep(500);
//		driver.manage().window().setSize(new Dimension(1280, 720));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.body.style.zoom='80%';");
		actionDriver.enterText(cardHolderName, cardHolder);
		Reporter.log("Entered Card Holder Name: " + cardHolder, true);

		entercardNumber=cardNumber;
		actionDriver.enterText(cardHolderNumber, cardNumber);
		Reporter.log("Entered Card Number: " + cardNumber, true);
		System.err.println(entercardNumber);

	//	Thread.sleep(5000);
		actionDriver.enterText(cardMonthYear, expiry);
		Reporter.log("Entered Card Expiry Date: " + expiry, true);
	//	Thread.sleep(5000);
		actionDriver.enterText(cardCvcNumber, cvc);
		Reporter.log("Entered Card CVC: " + cvc, true);

	}
	
	public void clickOnPay() {
		actionDriver.scrollToElement(total);
		Reporter.log("Moved to the Pay button", true);
		actionDriver.click(pay);
		Reporter.log("Clicked on the Pay button to proceed with payment", true);
	}
	
	
	
	public void openBrowserForStaging(WebDriver driver,String url) {
	    try {
	        WebDriver newTab = driver.switchTo().newWindow(WindowType.TAB);
	        newTab.get(url);

	        Reporter.log(" Opened staging environment in a new browser tab", true);
	        System.out.println(" Opened staging environment in a new browser tab");
	    } catch (Exception e) {
	        Reporter.log(" Failed to open staging environment in new tab - " + e.getMessage(), true);
	        System.out.println(" Failed to open staging environment: " + e.getMessage());
	    }
	}
	
	
	public boolean isCardNumberInvalid() {
		WebDriver driver=baseClass.getDriver();
	    try {
	        List<WebElement> elements = driver.findElements(luhanAlgo);
	        return !elements.isEmpty() && elements.get(0).isDisplayed();
	    } catch (Exception e) {
	        return false; 
	    }
	}


	

	public void waitForBillingSection() {
		WebDriverWait wait = new WebDriverWait(baseClass.getDriver(), Duration.ofSeconds(40));
	    // Wait until billing form is actually rendered in DOM
	    wait.until(ExpectedConditions.visibilityOfElementLocated(
	            By.cssSelector("div.bottomPay")));
	}

	public void clearThePrefilled() {

	    waitForBillingSection(); // NEW → ensures elements exist before interacting

	    clearField(emailID);
	    clearField(fullname);
	    clearField(address);
	    clearField(city);
	    clearField(zipcode);
	    
	}

	private void clearField(By locator) {
		WebDriverWait wait = new WebDriverWait(baseClass.getDriver(), Duration.ofSeconds(40));
	    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
	    actionDriver.scrollToElement(locator);
	    actionDriver.clickUsingJS(locator);
	    actionDriver.clearText
	    (locator);
	}


	private void enterValue(By locator, String value, String fieldName) {
		WebDriverWait wait = new WebDriverWait(baseClass.getDriver(), Duration.ofSeconds(40));
	    waitForBillingSection();  // ensures form is ready

	    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

	    actionDriver.scrollToElement(locator);
	    Reporter.log("Entering " + fieldName + ": " + value, true);

	    element.clear();  // safer than your JS clear if element exists
	    element.sendKeys(value);
	}

	public void enterEmail(String email) {
	    enterValue(emailID, email, "email");
	}

	public void enterFullName(String name) {
	    enterValue(fullname, name, "full name");
	}

	public void enterAddress(String addr) {
	    enterValue(address, addr, "addres");
	}

	public void enterCity(String cityName) {
	    enterValue(city, cityName, "emails2s");
	}

	public void enterZipcode(String zip) {
	    enterValue(zipcode, zip, "zipcode");
	}

	
	public String getCashierError() {
	    WebDriver driver = baseClass.getDriver();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    try {
	        // Wait until the swal popup AND text are visible
	        WebElement errorMsg = wait.until(
	                ExpectedConditions.visibilityOfElementLocated(swalError)
	        );

	        return errorMsg.getText().trim();

	    } catch (Exception e) {
	        return "NO_ERROR_FOUND";
	    }
	}


	
}
