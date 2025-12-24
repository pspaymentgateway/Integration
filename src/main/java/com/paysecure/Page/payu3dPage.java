package com.paysecure.Page;

import java.time.Duration;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.paysecure.actiondriver.ActionDriver;
import com.paysecure.base.baseClass;
import com.paysecure.locators.cashierPageLocators;
import com.paysecure.utilities.ExcelWriteUtility;
import com.paysecure.utilities.PropertyReader;

public class payu3dPage {

	
	private By password=By.xpath(cashierPageLocators.password);
	private By payPayu=By.xpath(cashierPageLocators.payPayu);	
	
	
	private ActionDriver actionDriver;
	// page factory constructor
	public payu3dPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.actionDriver = new ActionDriver(driver);
	}
	
	public void payForPayu(String currency, String purchaseId) {

	    WebDriver driver = baseClass.getDriver();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	    String payuOTP= PropertyReader.getPropertyForS2S("PayUOTP");
	    // Wait for either 3DS page OR final response
	    wait.until(ExpectedConditions.or(
	            ExpectedConditions.urlContains("Cyber3DS"),
	            ExpectedConditions.urlContains("issucces=true"),
	            ExpectedConditions.urlContains("issucces=false")
	    ));

	    String url = driver.getCurrentUrl();
	    System.out.println("Current URL: " + url);

	    // If PayU 3DS page
	    if (url.contains("Cyber3DS")) {

	        actionDriver.enterText(password,payuOTP);
	        actionDriver.click(payPayu);

	        // Wait for final redirect after 3DS
	        wait.until(ExpectedConditions.or(
	                ExpectedConditions.urlContains("issucces=true"),
	                ExpectedConditions.urlContains("issucces=false")
	        ));
	    }

	    String finalUrl = driver.getCurrentUrl();

	    if (finalUrl.contains("issucces=false")) {

	        String status = "FAIL";
	        String comment = "Payment Failed";

	        Reporter.log(comment, true);
	        ExcelWriteUtility.writeResults2s(
	                "Currency_Result", currency, status, comment, purchaseId
	        );

	      //  driver.quit();
	        return;
	    }

	    Reporter.log("Payment Success", true);
	}
	


	
}
