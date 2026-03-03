package com.paysecure.locators;

public class limitAndCharges {

	public static final String merchant="//p[text()='Merchant']";
	public static final String LimitsCharges="//p[text()='Limits/Charges']";
	
	public static final String sm="//span[text()='Select Merchant']";
	public static final String searchMerchant="//input[@class='select2-search__field']";
	public static final String selectOption="//li[contains(@id,'select2-merchantSelect')]";
	public static final String selectCurrency="//span[text()='Select Currency']";
	public static final String searchCurrency="//input[@role='searchbox']";
	public static final String selectCurrencyFromOption="//ul[@id='select2-currencySel-results']";
	public static final String selectPaymentMethod="//span[text()='Select Payment Method']";
	public static final String searchPaymentmethod="//input[@class='select2-search__field']";
	public static final String get="//button[text()='GET']";
	
	public static final String selectClassMerchant="//select[@id='merchantSelect']";
	public static final String selectClassCurrency="//select[@id='currencySelect']";
	public static final String selectClassPaymentMethod="//select[@id='paymentMethodSelect']";
	//route to 
	public static final String routeTo="//select[@id='routeToSelect']";
	public static final String routingPoint="//select[@id='routeToValue']";
	
	//enter -1
	public static final String minus="(//input[@type='number'])[1]";
	
	//save button
	public static final String save1="//button[@id='btnFinalSave']";
	public static final String OK2="//button[text()='OK']";
	public static final String FinalSave="//button[@id='btnSaveChanges']";
	
	//check dropdown values 
	public static final String navigateUpto="//div[@id='currentRouteInfo']";
	public static final String getButton="//button[text()='GET']";
	
	
}
