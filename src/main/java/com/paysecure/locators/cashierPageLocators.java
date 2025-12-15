package com.paysecure.locators;

public class cashierPageLocators {

	public static final String cardHolderName = "//input[@id='cardholderName']";
	public static final String  cardHolderNumber= "//input[@id='cardNumber']";
	public static final String cardMonthYear = "//input[@id='cardMonthyear']";
	public static final String  cardCvcNumber= "//input[@id='cardCvc']";
	public static final String pay = "//button[text()='Pay']";
	public static final String luhanAlgo  = "//h2[text()='Invalid card Number (Luhn algo)']";
	public static final String  emailID= "//input[@id='emailId']";
	public static final String  fullname= "//input[@id='full_name']";
	public static final String  address= "//input[@id='address']";
	public static final String  city= "//input[@id='emails2s']";
	public static final String zipcode ="//input[@id='zip_code']";
	public static final String total="//span[@id='totalLabel']";
	
	
	//Payment Method  
	public static final String Visa="//ul[@id='payMethodList']/li/p[text()='Visa']";
	public static final String Master="//ul[@id='payMethodList']/li/p[text()='Master']";
}
