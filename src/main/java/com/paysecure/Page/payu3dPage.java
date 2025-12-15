package com.paysecure.Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.paysecure.actiondriver.ActionDriver;
import com.paysecure.base.baseClass;
import com.paysecure.locators.cashierPageLocators;

public class payu3dPage {

	
	private By password=By.xpath(cashierPageLocators.password);
	private By payPayu=By.xpath(cashierPageLocators.payPayu);	
	
	
	private ActionDriver actionDriver;
	// page factory constructor
	public payu3dPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.actionDriver = new ActionDriver(driver);
	}
	
	public void payForPayu() {
		WebDriver driver=baseClass.getDriver();
		actionDriver.enterText(password, "123456");
		actionDriver.click(payPayu);
	}
	
	
}
