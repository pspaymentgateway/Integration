package com.paysecure.Page;

import java.time.Duration;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.paysecure.actiondriver.ActionDriver;
import com.paysecure.base.baseClass;
import com.paysecure.locators.cashierPageLocators;
import com.paysecure.utilities.PropertyReader;


public class CashierPage {

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
	
	//Payment Method
	private By Visa=By.xpath(cashierPageLocators.Visa);
	private By Master=By.xpath(cashierPageLocators.Master);
	
	//zaakpayNetBanking cards Integration 
	private By zaakPayOTPEnter=By.xpath(cashierPageLocators.zaakPayOTPEnter);
	private By zaakpaySuccessfullBtn=By.xpath(cashierPageLocators.zaakpaySuccessfullBtn);
	private By zaakpayFailureBtn=By.xpath(cashierPageLocators.zaakpayFailureBtn);
	
	//zaakpay- Netbanking non cards integration
	private By zaakPaySelectbank=By.xpath(cashierPageLocators.zaakPaySelectbank);
	private By zaakPaySelectbankAllList=By.xpath(cashierPageLocators.zaakPaySelectbankAllList);
	private By zaakpaySearchField=By.xpath(cashierPageLocators.zaakpaySearchField);
	private By zaakpaySubmitButton=By.xpath(cashierPageLocators.zaakpaySubmitButton);
	
	//Euro exchnage
	private By possibleUseCasesEuroExchnage=By.xpath(cashierPageLocators.possibleUseCasesEuroExchnage);
	private By statusEuroExchnage=By.xpath(cashierPageLocators.statusEuroExchnage);
	private By cvvEuroExchnage=By.xpath(cashierPageLocators.cvvEuroExchnage);
	private By submitButtonEuroExchnage=By.xpath(cashierPageLocators.submitButtonEuroExchnage);
	
	//Easybuzz Netbanking 
	private By easybuzzNetBankingOTP=By.xpath(cashierPageLocators.easybuzzNetBankingOTP);
	private By easybuzzFailureBtn=By.xpath(cashierPageLocators.easybuzzFailureBtn);
	private By easybuzzSuccessBtn=By.xpath(cashierPageLocators.easybuzzSuccessBtn);
	
	//panetics
	private By paneticsOTP=By.xpath(cashierPageLocators.paneticsOTP);
	private By paneticsBtn=By.xpath(cashierPageLocators.paneticsBtn);
	
	//Telr
	private By telrEnterOTP=By.xpath(cashierPageLocators.telrEnterOTP);
	private By telrSumbitButton=By.xpath(cashierPageLocators.telrSumbitButton);
	
	//manta pay 
	private By mantapayOTP=By.xpath(cashierPageLocators.mantapayOTP);
	private By mantapayBtn=By.xpath(cashierPageLocators.mantapayBtn);
	
	//unicorn 3ds page
	//private By unicornOTP=By.xpath(cashierPageLocators.unicornOTP);
	private By unicornBtn=By.xpath(cashierPageLocators.unicornBtn);
	
	//cc avenue 
	private By selectClassCCAvenue=By.xpath(cashierPageLocators.ccavenueSelectClass);
	@FindBy (xpath="//select[@id='selectAuthResult']") private WebElement selectClassCCAvnue;
	private By ccavenueSubmitButton=By.xpath(cashierPageLocators.ccavenueSubmitButton);
	@FindBy(xpath="//input[@name='password']") private WebElement unicornOTP;
	
	
	//Shift 4 
	private By shift4Otp=By.xpath(cashierPageLocators.shift4Otp);
	private By shift4payButton=By.xpath(cashierPageLocators.shift4payButton);
	
	//payaza card
	private By payazaDropdown=By.xpath(cashierPageLocators.payazaDropdown);
	private By payazaSubmitBtn=By.xpath(cashierPageLocators.payazaSubmitBtn);
	
	private By sbxPayaEnterOTP=By.xpath(cashierPageLocators.sbxPayaEnterOTP);
	private By sbxSubmitBtn=By.xpath(cashierPageLocators.sbxSubmitBtn);
	private ActionDriver actionDriver;
	// page factory constructor
	public CashierPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.actionDriver = new ActionDriver(driver);
	}
	
	public String entercardNumber;
	public void userEnterCardInformationForPayment(
			String cardHolder, String cardNumber,String expiry, String cvv) throws InterruptedException {
		WebDriver driver=baseClass.getDriver();
		Thread.sleep(500);
		driver.manage().window().setSize(new Dimension(1080, 720));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.body.style.zoom='65%';");
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
		actionDriver.enterText(cardCvcNumber, cvv);
		Reporter.log("Entered Card CVV: " + cvv, true);

	}
	
	public void clickOnPay() {
		actionDriver.scrollToElement(pay);
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
	
	public void clickONVisa() {
		actionDriver.clickUsingJS(Visa);
	}
	
	public void clickONMaster() {
		actionDriver.clickUsingJS(Master);
	}


	public void zaakPayOtpEnterSuccessOrFailure() {

		String ZaakPaykey = PropertyReader.getPropertyForS2S("ZaakPaykey");
		
		switch (ZaakPaykey) {
		case "success": {
			actionDriver.enterText(zaakPayOTPEnter, "1234");
			actionDriver.click(zaakpaySuccessfullBtn);
			break;
		}case "failure": {
			actionDriver.enterText(zaakPayOTPEnter, "1234");
			actionDriver.click(zaakpayFailureBtn);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + ZaakPaykey);
		}
	}
	
	public void selectZakpayBank(String PartialBank, String ZaakPaybank) throws InterruptedException {

	    WebDriver driver = baseClass.getDriver();
	    WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(30));

	    actionDriver.click(zaakPaySelectbank);
	    Reporter.log("Clicked on ZaakPay bank select dropdown", true);

	    actionDriver.enterText(zaakpaySearchField, PartialBank);
	    Reporter.log("Entered partial bank name: " + PartialBank, true);

	    List<WebElement> suggestions = w.until(
	            ExpectedConditions.visibilityOfAllElementsLocatedBy(
	                    By.xpath("//ul[@id='select2-providerselect-results']/li"))
	    );
	    Reporter.log("ZaakPay bank suggestions loaded. Count: " + suggestions.size(), true);

	    for (WebElement s : suggestions) {

	        if (ZaakPaybank.equalsIgnoreCase(s.getText().trim())) {
	            Thread.sleep(2000);
	            s.click();
	            Reporter.log("Selected ZaakPay bank name: " + ZaakPaybank, true);
	            break;
	        }
	    }
	}

	
	public void zaakpaySubmitButtonOnBankPage() {
	    actionDriver.click(zaakpaySubmitButton);
	    Reporter.log("Clicked on submit button on bank page", true);
	}
	
	public void panaticsSubmitButtonOnBankPage() {
		actionDriver.enterText(paneticsOTP, "123456");
	    actionDriver.click(paneticsBtn);
	    Reporter.log("Clicked on ZaakPay submit button on bank page", true);
	}

	public void handlePSPPageForEuroExchange(String PossibleUseCases,String status,String CAVV) {
		actionDriver.selectByVisibleText(possibleUseCasesEuroExchnage,PossibleUseCases);
		actionDriver.selectByVisibleText(statusEuroExchnage,status);
		actionDriver.clearText(cvvEuroExchnage);
		actionDriver.enterText(cvvEuroExchnage, CAVV);
		actionDriver.click(submitButtonEuroExchnage);
		
	}
	
	public void easyBuzzNetbankingEnterOTP() {
		actionDriver.enterText(easybuzzNetBankingOTP, "1234");
		String key = "success";
		switch (key) {
		case "success": {
			actionDriver.click(easybuzzSuccessBtn);
		break;
		}
		
		case "failure": {
			actionDriver.click(easybuzzFailureBtn);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + key);
		}
	}
	
	public void handleTelrs3dsPage(){
		//actionDriver.enterText(telrEnterOTP, "1234");
		
		actionDriver.enterText(telrEnterOTP, "1234");
		actionDriver.click(telrSumbitButton);
	}
	
	public void mantapay3dsPage(String mantapayOtp){
		actionDriver.sendKeysJS(mantapayOTP, mantapayOtp);
		actionDriver.click(mantapayBtn);
	}
	
	
	public void unicorn3dsPage() throws InterruptedException {

	    WebDriver driver = baseClass.getDriver();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

	    // 1️ Wait for 3DS URL
	    wait.until(ExpectedConditions.urlContains("threed"));

	    // 2️ Wait for page to fully load (fixes the 20-22 sec delay issue)
	    wait.until(webDriver -> ((JavascriptExecutor) webDriver)
	            .executeScript("return document.readyState").equals("complete"));

	    // 3️ Small buffer after page ready (3DS pages often load content after readyState)
	    Thread.sleep(2000);

	    // 4️ Check for iframe
	    List<WebElement> frames = driver.findElements(By.tagName("iframe"));
	    System.out.println("Iframe count: " + frames.size());

	    if (frames.size() > 0) {
	        // Wait until iframe is available AND switch into it (don't do both wait + switchTo manually)
	        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
	        System.out.println("Switched to 3DS iframe");
	    } else {
	        System.out.println("No iframe found, continuing on main page");
	    }

	    // 5️ Wait for OTP field
	    WebElement otpField = wait.until(
	            ExpectedConditions.visibilityOf(unicornOTP));
	    otpField.clear();
	    otpField.sendKeys("123456");

	    // 6️ Click submit
	    wait.until(ExpectedConditions.elementToBeClickable(unicornBtn)).click();

	    // 7️ Switch back to main page
	    driver.switchTo().defaultContent();
	}
	
	public void thirdPartyPageForCCAvenue(String authResult) {

	    WebDriver driver = baseClass.getDriver();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

	    System.out.println("CCAvenue 3DS page loaded");

	    try {
	        driver.switchTo().defaultContent();

	        // ✅ Try direct iframe switch first
	        boolean dropdownFound = false;

	        // Attempt 1: Switch directly by ID
	        try {
	            System.out.println("Attempt 1: Switching to challengeFrame by ID...");
	            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("challengeFrame")));
	            
	            if (!driver.findElements(By.id("selectAuthResult")).isEmpty()) {
	                dropdownFound = true;
	                System.out.println("✓ Dropdown found inside challengeFrame");
	            }
	        } catch (Exception e) {
	            System.out.println("Attempt 1 failed: " + e.getMessage());
	            driver.switchTo().defaultContent();
	        }

	        // Attempt 2: Loop through ALL iframes on page
	        if (!dropdownFound) {
	            System.out.println("Attempt 2: Looping through all iframes...");
	            driver.switchTo().defaultContent();

	            List<WebElement> allIframes = driver.findElements(By.tagName("iframe"));
	            System.out.println("Total iframes found: " + allIframes.size());

	            for (int i = 0; i < allIframes.size(); i++) {
	                try {
	                    driver.switchTo().defaultContent();
	                    driver.switchTo().frame(i); // Switch by index (more stable)
	                    System.out.println("Checking iframe index: " + i);

	                    if (!driver.findElements(By.id("selectAuthResult")).isEmpty()) {
	                        dropdownFound = true;
	                        System.out.println("✓ Dropdown found in iframe index: " + i);
	                        break;
	                    }

	                    // Check nested iframes inside this iframe
	                    List<WebElement> nestedIframes = driver.findElements(By.tagName("iframe"));
	                    for (int j = 0; j < nestedIframes.size(); j++) {
	                        try {
	                            driver.switchTo().frame(j);
	                            System.out.println("Checking nested iframe index: " + j);

	                            if (!driver.findElements(By.id("selectAuthResult")).isEmpty()) {
	                                dropdownFound = true;
	                                System.out.println("✓ Dropdown found in nested iframe [" + i + "][" + j + "]");
	                                break;
	                            }
	                        } catch (Exception ex) {
	                            driver.switchTo().parentFrame(); // Go back up one level
	                        }
	                    }

	                    if (dropdownFound) break;

	                } catch (Exception e) {
	                    System.out.println("Error checking iframe " + i + ": " + e.getMessage());
	                }
	            }
	        }

	        if (!dropdownFound) {
	            throw new RuntimeException("✗ selectAuthResult dropdown NOT found in any iframe!");
	        }

	        // ✅ Now we are inside the correct iframe — interact with elements
	        WebElement dropdown = wait.until(
	                ExpectedConditions.visibilityOfElementLocated(By.id("selectAuthResult"))
	        );
	        Select select = new Select(dropdown);
	        select.selectByValue(authResult);
	        System.out.println("✓ Selected authResult: " + authResult);

	        // Click submit button
	        WebElement submitButton = wait.until(
	                ExpectedConditions.elementToBeClickable(By.id("acssubmit"))
	        );

	        try {
	            submitButton.click();
	            System.out.println("✓ Submit clicked (normal)");
	        } catch (Exception e) {
	            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
	            System.out.println("✓ Submit clicked (JS fallback)");
	        }

	    } catch (Exception e) {
	        System.out.println("✗ Exception: " + e.getMessage());
	        e.printStackTrace();
	        throw new RuntimeException(e);

	    } finally {
	        driver.switchTo().defaultContent();
	        System.out.println("✓ Returned to default content");
	    }
	}
	
	public void handleShift4Otp() {
		actionDriver.enterText(shift4Otp, "0101");
		actionDriver.click(shift4payButton);
	}
	
	public void handleForPayazaCard() {
		actionDriver.selectByIndex(payazaDropdown, 0);
		actionDriver.click(payazaSubmitBtn);
	}
	
	public void handleForPayableSBX() {
		actionDriver.enterText(sbxPayaEnterOTP, "test");
		actionDriver.click(sbxSubmitBtn);
	}

	
}
