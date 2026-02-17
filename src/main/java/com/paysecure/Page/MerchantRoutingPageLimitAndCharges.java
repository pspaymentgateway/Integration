package com.paysecure.Page;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.paysecure.actiondriver.ActionDriver;
import com.paysecure.base.baseClass;
import com.paysecure.locators.limitAndCharges;


public class MerchantRoutingPageLimitAndCharges {

	private By merchants = By.xpath(limitAndCharges.merchant);
	private By limitCharges = By.xpath(limitAndCharges.LimitsCharges);

	//select merchant
	private By sM=By.xpath(limitAndCharges.sm);
	private By searchMerchant=By.xpath(limitAndCharges.searchMerchant);
	private By selectOption=By.xpath(limitAndCharges.selectOption);
	private By selectCurrency=By.xpath(limitAndCharges.selectCurrency);
	private By searchCurrency=By.xpath(limitAndCharges.searchCurrency);
	private By selectPaymentMethod=By.xpath(limitAndCharges.selectPaymentMethod);
	private By searchPaymentmethod=By.xpath(limitAndCharges.searchPaymentmethod);
	private By get=By.xpath(limitAndCharges.get);
	private By routeTo=By.xpath(limitAndCharges.routeTo);
	private By routingPoint=By.xpath(limitAndCharges.routingPoint);
	private By minus=By.xpath(limitAndCharges.minus);
	private By save1=By.xpath(limitAndCharges.save1);
	private By OK2=By.xpath(limitAndCharges.OK2);
	private By FinalSave=By.xpath(limitAndCharges.FinalSave);
	//
	private By selectClassMerchant=By.xpath(limitAndCharges.selectClassMerchant);
	private By selectClassCurrency=By.xpath(limitAndCharges.selectClassCurrency);
	private By selectClassPaymentMethod=By.xpath(limitAndCharges.selectClassPaymentMethod);

	private ActionDriver actionDriver;
	public MerchantRoutingPageLimitAndCharges(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.actionDriver = new ActionDriver(driver);
	}
	
	
	public void navigateUptoLimitAndCharges() throws InterruptedException {
		WebDriver driver = baseClass.getDriver();
		Thread.sleep(500);
		driver.manage().window().setSize(new Dimension(1280, 720));// 1920 × 1080

		actionDriver.click(merchants);
		actionDriver.click(limitCharges);
		

	}
	
	public void getDetailsForLimitAndCharges() throws InterruptedException {
	    Reporter.log("Fetching details for Limit and Charges", true);
	    Thread.sleep(1500);
	    actionDriver.click(get);
	    Reporter.log("Details fetched successfully", true);
	    
	}
	
	
	public void selectMerchant(String partialMerchant, String merchant) throws InterruptedException {
		WebDriver driver=baseClass.getDriver();
		actionDriver.click(sM);
		actionDriver.enterText(searchMerchant, partialMerchant);
		Thread.sleep(1500);
		actionDriver.sendKeys(searchMerchant, Keys.BACK_SPACE);
		
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(30));
		List<WebElement> suggestions = w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				By.xpath("//ul[@id='select2-merchantSelect-results']/li")));
		
		for (WebElement s : suggestions) {
			if (merchant.equalsIgnoreCase(s.getText().trim())) {
				Thread.sleep(1000);
				s.click();
				Reporter.log("select merchant name :- " + merchant, true);
				break;

			}
		}
	}

	
	public void selectCurrency(String partialCurrency, String currency) throws InterruptedException {
		WebDriver driver=baseClass.getDriver();
		actionDriver.click(selectCurrency);
		actionDriver.enterText(searchCurrency, partialCurrency);
		Thread.sleep(1500);
		actionDriver.sendKeys(searchCurrency, Keys.BACK_SPACE);
		
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(30));
		List<WebElement> suggestions = w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				By.xpath("//ul[@id='select2-currencySelect-results']/li")));
		
		for (WebElement s : suggestions) {
			if (currency.equalsIgnoreCase(s.getText().trim())) {
				Thread.sleep(1000);
				s.click();
				Reporter.log("select merchant name :- " + currency, true);
				break;

			}
		}
	}

	
	public void selectPaymentMethod(String partialPaymentMethod, String paymentMethod) throws InterruptedException {
		WebDriver driver=baseClass.getDriver();
		actionDriver.click(selectPaymentMethod);
		actionDriver.enterText(searchPaymentmethod, partialPaymentMethod);
		Thread.sleep(1500);
		actionDriver.sendKeys(searchPaymentmethod, Keys.BACK_SPACE);
		
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(30));
		List<WebElement> suggestions = w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				By.xpath("//ul[@id='select2-paymentMethodSelect-results']/li")));
		
		for (WebElement s : suggestions) {
			if (paymentMethod.equalsIgnoreCase(s.getText().trim())) {
				Thread.sleep(1000);
				s.click();
				Reporter.log("select merchant name :- " + paymentMethod, true);
				break;

			}
		}
	}

	
	public void selectRouteTo(String routing) {
		actionDriver.selectByVisibleText(routeTo, routing);
	}
	
	public void selectRoutePoint(String routingValue) {
		actionDriver.selectByVisibleText(routingPoint, routingValue);
	}

	
	public void saveButton() {
		actionDriver.moveToElement(save1);
		actionDriver.click(save1);
		actionDriver.click(OK2);
	}
	
	public void finalsaveButton() {
		actionDriver.moveToElement(FinalSave);
		actionDriver.click(FinalSave);
		
	}
	

	 public void fillAllTextboxesWithTab() throws InterruptedException {
	        WebDriver driver = baseClass.getDriver();
	       
	        actionDriver.click(minus);
	        
	        // Loop through all 28 fields using TAB
	        for (int i = 0; i < 28; i++) {
	            // Get currently focused element
	            WebElement activeElement = driver.switchTo().activeElement();
	            
	            // Clear and enter -1
	            activeElement.clear();
	            activeElement.sendKeys("-1");
	            
	            // Press TAB to move to next field
	            activeElement.sendKeys(Keys.TAB);
	            
	            // Optional: small wait for stability
	            Thread.sleep(200);
	        }
	        
	        System.out.println("All 28 fields filled with -1");
	    }
	 
	 public void handleLimitAndChargesForMerchant(String Mercahnt,String Currency,String PaymentMethod,String RouteToBankOrMid,String RouteOnBankOrMid) throws InterruptedException {
		 navigateUptoLimitAndCharges();
		 selectMerchant(Mercahnt,Mercahnt);
		 selectCurrency(Currency,Currency);
		 selectPaymentMethod(PaymentMethod,PaymentMethod);
		 getDetailsForLimitAndCharges();
		 fillAllTextboxesWithTab();
		 selectRouteTo(RouteToBankOrMid);
		 selectRoutePoint(RouteOnBankOrMid);
		 saveButton();
		 finalsaveButton();
	 }
	 
}
