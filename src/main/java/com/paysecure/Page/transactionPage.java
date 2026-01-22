package com.paysecure.Page;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.Reporter;

import com.paysecure.actiondriver.ActionDriver;
import com.paysecure.base.baseClass;
import com.paysecure.locators.cashierPageLocators;
import com.paysecure.locators.payUPSP;
import com.paysecure.locators.transactionPageLocators;
import com.paysecure.utilities.PropertyReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class transactionPage {

	private By analytics = By.xpath(transactionPageLocators.analytics);
	private By report = By.xpath(transactionPageLocators.report);
	private By transactions = By.xpath(transactionPageLocators.transactions);
	private By searchPurchaseID = By.xpath(transactionPageLocators.searchPurchaseID);
	private By firstTransactionID = By.xpath(transactionPageLocators.firstTransactionID);
	private By searchButton = By.xpath(transactionPageLocators.searchButton);
	private By purchaseTransactionID = By.xpath(transactionPageLocators.purchaseTransactionID);

	// verify response vs UI
	private By txnID = By.xpath(transactionPageLocators.firsttxnBtn);
	private By merchantNameFromTXNPage = By.xpath(transactionPageLocators.merchantNameFromTXNPage);
	private By transactionTableAmount = By.xpath(transactionPageLocators.transactionTableAmount);
	private By transactionTableCurrency = By.xpath(transactionPageLocators.transactionTableCurrency);
	private By currencyFrompaymentInfo = By.xpath(transactionPageLocators.currencyFrompaymentInfo);
	private By currnecyPayUPaymentInfo = By.xpath(transactionPageLocators.currnecyPayUPaymentInfo);
	private By amountFrompaymentInfo = By.xpath(transactionPageLocators.amountFrompaymentInfo);
	private By amountForPayUPaymentInfo = By.xpath(transactionPageLocators.amountForPayUPaymentInfo);
	private By maskedCardOnUI = By.xpath(transactionPageLocators.maskedCardOnUI);

	// matrix psp login
	private By emailPSP = By.xpath(transactionPageLocators.emailPSP);
	private By passwordPSP = By.xpath(transactionPageLocators.passwordPSP);
	private By signinButtonPSP = By.xpath(transactionPageLocators.signinButtonPSP);
	private By transactionPSPModule = By.xpath(transactionPageLocators.transactionPSPModule);
	private By txID = By.xpath(transactionPageLocators.txID);
	private By searchTXIDPSP = By.xpath(transactionPageLocators.searchTXIDPSP);

	private By txinPSP = By.xpath(transactionPageLocators.txinPSP);
	private By amountAndCurrencyPSP = By.xpath(transactionPageLocators.amountAndCurrency);
	private By maskCardPSP = By.xpath(transactionPageLocators.maskCardPSP);
	private By successStatusFromPSP = By.xpath(transactionPageLocators.successStatusFromPSP);
	private By lastStatusFromTxnPage = By.xpath(transactionPageLocators.lastStatusFromTxnPage);

	// Pay U
	private By loginPayUButton = By.xpath(transactionPageLocators.loginPayUButton);
	private By skipFlowPayU = By.xpath(transactionPageLocators.skipFlowPayU);
	private By transactionPayU = By.xpath(transactionPageLocators.transactionPayU);
	private By submitButton = By.xpath(transactionPageLocators.submitButton);

	@FindBy(xpath = "//p[text()='Transactions']")
	private WebElement transactionPAyu;
	@FindBy(xpath = "//p[text()='Search']")
	private WebElement searchTransactionPAyu;
	@FindBy(xpath = "//p[text()='Search']")
	private WebElement SearchButtontransactionPAyu;

	// Easybuzz
	private By OTPEasyBuzz = By.xpath(cashierPageLocators.OTPEasyBuzz);
	private By EnterOtpEasyBuzz = By.xpath(cashierPageLocators.EnterOtpEasyBuzz);

	private By easySuccess = By.xpath(cashierPageLocators.easySuccess);
	private By easyFailure = By.xpath(cashierPageLocators.easyFailure);
	private By easyCancel = By.xpath(cashierPageLocators.easyCancel);
	private By easySessionOut = By.xpath(cashierPageLocators.easySessionOut);
	private By easyDelayedSuccess = By.xpath(cashierPageLocators.easyDelayedSuccess);
	private By easyDelayedFailure = By.xpath(cashierPageLocators.easyDelayedFailure);
	

	private ActionDriver actionDriver;

	// page factory constructor
	public transactionPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.actionDriver = new ActionDriver(driver);
	}

	public void navigateUptoTransaction() throws InterruptedException {
		WebDriver driver=baseClass.getDriver();
		String html = driver.getPageSource();
	//	System.out.println(html); // print in console

		// Define the folder and file name
		String folderPath = "C:\\Downloads";
		String filePath = folderPath + "\\page.html";

		try {
		    // Create the folder if it doesn't exist
		    File folder = new File(folderPath);
		    if (!folder.exists()) {
		        folder.mkdirs(); // Create all necessary directories
		        System.out.println("Created folder: " + folderPath);
		    }

		    // Write the HTML content to file
		    try (FileWriter writer = new FileWriter(filePath)) {
		        writer.write(html);
		        System.out.println("HTML saved at: " + filePath);
		    }

		} catch (IOException e) {
		    e.printStackTrace();
		}

		Thread.sleep(500);
		driver.manage().window().setSize(new Dimension(1280, 720));// 1920 × 1080

		actionDriver.scrollToElement(analytics);
		actionDriver.clickUsingJS(report);

		actionDriver.clickUsingJS(transactions);
		// Assert.fail();
		driver.manage().window().setSize(new Dimension(1920, 1080));

	}

	public void searchTheTransaction(String transactionID) throws Exception {

		actionDriver.enterText(searchPurchaseID, transactionID);
		Reporter.log(" Entered Transaction ID: " + transactionID, true);
		Thread.sleep(200);
		actionDriver.clickUsingJS(searchButton);
		Reporter.log(" Clicked on Search button", true);
		Thread.sleep(1200);

	}

	public void clickOnTransactionId() {

		actionDriver.click(firstTransactionID);
		Reporter.log(" Clicked on first Transaction ID from list", true);

	}

	public String purchaseTxnId;

	public void verifyPurchaseTransactionIDIsNotEmpty() throws InterruptedException {
		Thread.sleep(3000);
		purchaseTxnId = actionDriver.getText(purchaseTransactionID);

		if (purchaseTxnId != null && !purchaseTxnId.trim().isEmpty()) {
			Reporter.log("Purchase Transaction ID is not empty or not null", true);
		} else {
			Reporter.log("Purchase Transaction ID is empty or null", true);
		}
	}

	public void validatePurchaseId(String purchaseId) {

		// String purchaseId = response.jsonPath().getString("purchaseId");

		// Null check
		Assert.assertNotNull(purchaseId, "purchaseId is NULL");
		Reporter.log("purchaseId is not null", true);

		// Empty check
		Assert.assertFalse(purchaseId.trim().isEmpty(), "purchaseId is EMPTY");
		Reporter.log("purchaseId is not empty", true);

		// Regex format check
		boolean isValidFormat = purchaseId.matches("^[a-fA-F0-9]{24}$");
		Assert.assertTrue(isValidFormat, "Invalid purchaseId format: " + purchaseId);
		Reporter.log("purchaseId format is valid: " + purchaseId, true);

		// Length check
		Assert.assertEquals(purchaseId.length(), 24, "purchaseId length is not 24");
		Reporter.log("purchaseId length is 24", true);

		Reporter.log("purchaseId validated successfully: " + purchaseId, true);

	}

	public String transactionID;

	public void verifyTxnId(String purchaseTxnId) {

		transactionID = actionDriver.getText(txnID);

		Reporter.log("UI transaction ID: " + transactionID, true);
		Reporter.log("API transaction ID: " + purchaseTxnId, true);

		Assert.assertEquals(transactionID, purchaseTxnId, "Transaction ID mismatch");

		Reporter.log("Transaction ID verified successfully", true);
	}

	public String merchantName;

	public void verifyMerchantName(String mername) {

		merchantName = actionDriver.getText(merchantNameFromTXNPage);

		Reporter.log("UI Merchant Name: " + merchantName, true);
		Reporter.log("API Merchant Name: " + mername, true);

		Assert.assertEquals(merchantName, mername, "Merchant name mismatch");

		Reporter.log("Merchant name verified successfully", true);
	}

	 public static double parseUiAmount(String uiAmountText) {

	        if (uiAmountText == null || uiAmountText.trim().isEmpty()) {
	            throw new IllegalArgumentException("UI amount is null or empty");
	        }

	        try {
	            // Remove currency symbols, keep numbers, comma & dot
	            String sanitized = uiAmountText
	                    .replaceAll("[^0-9,\\.]", "")
	                    .trim();

	            NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
	            Number number = format.parse(sanitized);

	            return number.doubleValue();

	        } catch (ParseException e) {
	            throw new RuntimeException(
	                    "Unable to parse UI amount: " + uiAmountText, e);
	        }
	    }
	public double uiAmount;

	public void verifyAmount(Double apiAmount) {

        String uiAmountText = actionDriver.getText(transactionTableAmount);

        Reporter.log("UI Amount (raw): " + uiAmountText, true);
        Reporter.log("API Amount: " + apiAmount, true);

        // ✅ Parse UI amount safely
        uiAmount =parseUiAmount(uiAmountText);

        Reporter.log("UI Amount (parsed): " + uiAmount, true);

        // ✅ Delta for floating comparison
        double delta = 0.01;

        // Informational logging
        if (Math.abs(uiAmount - apiAmount) < delta) {
            Reporter.log("Currency conversion NOT applied", true);
        } else {
            Reporter.log("Currency conversion APPLIED or mismatch", true);
        }

        // ✅ Assertion (correct way)
        Assert.assertEquals(
                uiAmount,
                apiAmount,
                delta,
                "Amount mismatch between UI and API"
        );

        Reporter.log("Amount verified successfully", true);
    }

	public String uiCurrency;

	public void verifyCurrency(String expectedCurrency) {
		actionDriver.scrollToElement(transactionTableCurrency);
		uiCurrency = actionDriver.getText(transactionTableCurrency).trim();

		Reporter.log("UI Currency: " + uiCurrency, true);
		Reporter.log("API Currency: " + expectedCurrency, true);

		if (uiCurrency.equalsIgnoreCase(expectedCurrency)) {
			Reporter.log("Currency matched correctly", true);
		} else {
			Reporter.log("Currency mismatch detected", true);
		}

		Assert.assertEquals(uiCurrency, expectedCurrency, "Currency mismatch");

		Reporter.log("Currency verified successfully", true);
	}

	public void verifyCurrencyOnPaymentInfo() {

		String curPaymentInfo = actionDriver.getText(currencyFrompaymentInfo);

		Reporter.log("UI Currency: " + uiCurrency, true);
		Reporter.log("Payment Info Currency: " + curPaymentInfo, true);

		Assert.assertEquals(curPaymentInfo, uiCurrency, "Currency mismatch on Payment Info");

		Reporter.log("Currency on Payment Info verified successfully", true);
	}

	public void verifyCurrencyOnPaymentInfoPayU() {

		String curPaymentInfo = actionDriver.getText(currnecyPayUPaymentInfo);

		Reporter.log("UI Currency: " + uiCurrency, true);
		Reporter.log("Payment Info Currency: " + curPaymentInfo, true);

		Assert.assertEquals(curPaymentInfo, uiCurrency, "Currency mismatch on Payment Info");

		Reporter.log("Currency on Payment Info verified successfully", true);
	}

	public void verifyAmountFromPaymentInfo() {

		String amtPaymentInfo = actionDriver.getText(amountFrompaymentInfo);
		double paymentInfoAmount = Double.parseDouble(amtPaymentInfo.trim());

		Reporter.log("UI Amount: " + uiAmount, true);
		Reporter.log("Payment Info Amount: " + paymentInfoAmount, true);

		Assert.assertEquals(paymentInfoAmount, uiAmount, "Amount mismatch on Payment Info");

		Reporter.log("Amount on Payment Info verified successfully", true);
	}

	public void verifyAmountFromPaymentInfoPayU() {

		String amtPaymentInfo = actionDriver.getText(amountForPayUPaymentInfo);
		double paymentInfoAmount = Double.parseDouble(amtPaymentInfo.trim());

		Reporter.log("UI Amount: " + uiAmount, true);
		Reporter.log("Payment Info Amount: " + paymentInfoAmount, true);

		Assert.assertEquals(paymentInfoAmount, uiAmount, "Amount mismatch on Payment Info");

		Reporter.log("Amount on Payment Info verified successfully", true);
	}

	public String maskCardForCashier(String entercardNumber) {

		String first8 = entercardNumber.substring(0, 8);
		String last4 = entercardNumber.substring(entercardNumber.length() - 4);

		return first8 + "****" + last4;
	}

	public void verifyUsedCardOnUI(String entercardNumber) {

		// Generate expected masked card number
		String expectedMasked = maskCardForCashier(entercardNumber);

		// Read masked card number from UI
		String uiMaskedCard = actionDriver.getText(maskedCardOnUI).trim();

		Reporter.log("Expected Masked Card: " + expectedMasked, true);
		Reporter.log("UI Masked Card: " + uiMaskedCard, true);

		Assert.assertEquals(uiMaskedCard, expectedMasked, "Masked card number does not match!");
	}

	public void doLoginOnThePSPSide(String emailID, String password) {

		Reporter.log("Entering email on PSPCardsIntegrations side: " + emailID, true);
		actionDriver.enterText(emailPSP, emailID);

		Reporter.log("Entering password on PSPCardsIntegrations side", true);
		actionDriver.enterText(passwordPSP, password);

		Reporter.log("Clicking Sign In button on PSPCardsIntegrations side", true);
		actionDriver.clickUsingJS(signinButtonPSP);

		Reporter.log("Login action performed successfully on PSPCardsIntegrations side", true);
	}

	public void doLoginOnThePSPSideForPayU(String emailID, String password) {
		WebDriver driver = baseClass.getDriver();
		WebElement signInBtn = driver.findElement(By.xpath("//button[@type='submit']"));
		Reporter.log("Entering email on PSPCardsIntegrations side: " + emailID, true);
		actionDriver.enterText(emailPSP, emailID);

		actionDriver.enterText(passwordPSP, password);
		Reporter.log("Entering password on PSPCardsIntegrations side", true);

		signInBtn.click();
		Reporter.log("Clicking Sign In button on PSPCardsIntegrations side", true);

		Reporter.log("Login action performed successfully on PSPCardsIntegrations side", true);
	}

	public void navigateUptoPSPTransactionModule() {
		actionDriver.clickUsingJS(transactionPSPModule);
	}

	public void searchTheLatestTransactions(String txNID) {

		Reporter.log("Clicking on TX ID field to search latest transaction", true);
		actionDriver.clickUsingJS(txID);

		Reporter.log("Entering Transaction ID for search: " + txNID, true);
		actionDriver.enterText(searchTXIDPSP, txNID);

		Reporter.log("Pressing ENTER to start transaction search", true);
		actionDriver.pressEnter(searchTXIDPSP);

		Reporter.log("Search action performed for Transaction ID: " + txNID, true);
	}

	public void verifyPurchaseTxnIdatPSP(String txNID) {

		Reporter.log("Verifying Transaction ID in PSPCardsIntegrations UI for: " + txNID, true);
		String txnPSP = actionDriver.getText(txinPSP);

		Reporter.log("PSPCardsIntegrations displayed Transaction ID: " + txnPSP, true);
		Assert.assertEquals(txnPSP, txNID, "Transaction ID mismatch!!!");

		Reporter.log("Transaction ID verification successful", true);
	}

	public void verifyCurrencyInPSP() {

		Reporter.log("Fetching Amount and Currency text from PSPCardsIntegrations UI", true);
		String acPSP = actionDriver.getText(amountAndCurrencyPSP);

		Reporter.log("Raw Amount + Currency text received: " + acPSP, true);

		String[] parts = acPSP.split(" ");
		String amount = parts[0];
		String currencyPSP = parts[1];

		double amountPSP = Double.parseDouble(amount);

		Reporter.log("Extracted Amount from PSPCardsIntegrations: " + amountPSP, true);
		Reporter.log("Extracted Currency from PSPCardsIntegrations: " + currencyPSP, true);

		Reporter.log("Comparing Amount with UI value: " + uiAmount, true);
		Assert.assertEquals(amountPSP, uiAmount, "Amount mismatch!!!");

		Reporter.log("Comparing Currency with UI value: " + uiCurrency, true);
		Assert.assertEquals(currencyPSP, uiCurrency, "Currency mismatch!!!");

		Reporter.log("Amount and Currency verification successful", true);
	}

	String successPSP;

	public void getStatusFromPSP() {
		successPSP = actionDriver.getText(successStatusFromPSP);
	}

	String successUI;

	public void getStatusFromUI() {
		successUI = actionDriver.getText(lastStatusFromTxnPage);
	}

	public String normalizeStatus(String status) {
		if (status == null)
			return "UNKNOWN";

		switch (status.trim().toUpperCase()) {
		case "PAID":
		case "SUCCESS":
		case "COMPLETED":
		case "DONE":
			return "SUCCESS";

		case "FAILED":
		case "ERROR":
		case "DECLINED":
			return "FAILED";

		case "PENDING":
		case "IN_PROGRESS":
			return "PENDING";

		default:
			return "UNKNOWN";
		}
	}

	public void verifyStatus() {

		String successUi = normalizeStatus(successUI);
		String successpsp = normalizeStatus(successPSP);
		Assert.assertEquals(successUi, successpsp);
	}

	public String maskCardForPSP(String cardNumber) {

		String first4 = cardNumber.substring(0, 4); // 5555
		String next2 = cardNumber.substring(4, 6); // 55
		String last4 = cardNumber.substring(cardNumber.length() - 4); // 4444

		return first4 + " " + next2 + "** **** " + last4;
	}

	public void verifyUsedCardOnPSP(String entercardNumber) {

		// Generate expected masked card number
		String expectedMasked = maskCardForPSP(entercardNumber);

		// Read masked card number from UI
		String uiMaskedCard = actionDriver.getText(maskCardPSP).trim();

		Reporter.log("Expected Masked Card: " + expectedMasked, true);
		Reporter.log("UI Masked Card: " + uiMaskedCard, true);

		Assert.assertEquals(uiMaskedCard, expectedMasked, "Masked card number does not match!");
	}

	public void searchButton() {
		actionDriver.clickUsingJS(searchButton);
		Reporter.log(" Clicked on Search button", true);
	}

	public void skipFlowForPayU() throws InterruptedException {
		Thread.sleep(6000);
		if (actionDriver.isDisplayed(skipFlowPayU)) {
			actionDriver.clickUsingJS(skipFlowPayU);
		}
	}

	public void TransactionPayu() {
		// transactionPAyu.click();
		// actionDriver.clickUsingJS(transactionPayU);
		actionDriver.click(transactionPayU);
	}

	public void checkTransactionPayuIndian(String paymentTxnID) {
		searchTransactionPAyu.sendKeys(paymentTxnID);
		SearchButtontransactionPAyu.click();
	}

	public void clickONSubmitButton() throws InterruptedException {
		Thread.sleep(2000);
		actionDriver.clickUsingJS(submitButton);
		Thread.sleep(2000);
		actionDriver.clickUsingJS(submitButton);
	}

	public void verifyCurrencyOnPaymentInfoPEN() {

		String curPaymentInfo = actionDriver.getText(currencyFrompaymentInfo);

		Reporter.log("UI Currency: " + uiCurrency, true);
		Reporter.log("Payment Info Currency: " + curPaymentInfo, true);

		Assert.assertEquals(curPaymentInfo, uiCurrency, "Currency mismatch on Payment Info");

		Reporter.log("Currency on Payment Info verified successfully", true);
	}
	
	public void enterOTpEasyBuzz() {
		String otp = actionDriver.getText(OTPEasyBuzz);
		System.out.println(otp);
		actionDriver.enterText(EnterOtpEasyBuzz, otp);
		easyBuzzSwitchFunctionality();
	}
	
	public void easyBuzzSwitchFunctionality() {
		String easybuzzKey = PropertyReader.getPropertyForS2S("easybuzzKey").trim().toLowerCase();
		String key =easybuzzKey;
		switch (key) {
		case "success": {
			actionDriver.click(easySuccess);
			break;
		}
		
		case "failure": {
			actionDriver.click(easyFailure);
			break;
		}
		
		case "cancel": {
			actionDriver.click(easyCancel);
			break;
		}
		case "session timeout": {
			actionDriver.click(easySessionOut);
			break;
		}
		
		case "delayed success": {
			actionDriver.click(easyDelayedSuccess);
			break;
		}
		case "delayed failure": {
			actionDriver.click(easyDelayedFailure);
			break;
		}
		
		default:
			throw new IllegalArgumentException("Unexpected value: " + key);
			
		}
	}
public	String LastSTatus;
public String getLastSTtaus() {
	return LastSTatus= actionDriver.getText(lastStatusFromTxnPage);
}


}
