package com.paysecure.locators;

public class cashierPageLocators {

	public static final String cardHolderName = "(//input[contains(@id,'input')])[1]";
	public static final String  cardHolderNumber= "(//input[contains(@id,'input')])[2]";
	public static final String cardMonthYear = "(//input[contains(@id,'input')])[3]";
	public static final String  cardCvcNumber= "(//input[contains(@id,'input')])[4]";
	public static final String pay = "//span[text()='Pay']";
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
	
	//for pay u integration 
	public static final String password="//input[@id='password']";
	public static final String payPayu="//input[@id='submitBtn']";
	
	//for easybuzz Integration 
	public static final String OTPEasyBuzz="//span[@id='random-number']";
	public static final String EnterOtpEasyBuzz="//input[@id='digit1']";
	
	public static final String easySuccess="//button[text()='Success']";
	public static final String easyFailure="//button[text()='Failure']";
	public static final String easyCancel="//button[text()='Cancel']";
	public static final String easySessionOut="//button[text()='Session Timeout']";
	public static final String easyDelayedSuccess="//span[@id='random-number']";
	public static final String easyDelayedFailure="//button[text()='Delayed Failure']";
	
	//zaakpayNetBanking OTP 
	public static final String zaakPayOTPEnter="//input[@id='pass']";
	public static final String zaakpaySuccessfullBtn="//input[@class='btn success']";
	public static final String zaakpayFailureBtn="//input[@class='btn alert']";
	
	//zaakpay - Netbanking Integration
	public static final String zaakPaySelectbank="//span[@id='select2-providerselect-container']";
	public static final String zaakPaySelectbankAllList="//ul[@id='select2-providerselect-results']/li";
	public static final String zaakpaySearchField="//input[@class='select2-search__field']";
	public static final String zaakpaySubmitButton="//button[text()='Submit']";
	
	//euro exchange Integration 
	public static final String possibleUseCasesEuroExchnage="//select[@id='pucOptions']";
	public static final String statusEuroExchnage="//select[@id='mdStatus']";
	public static final String cvvEuroExchnage="//input[@id='fcavv']";
	public static final String submitButtonEuroExchnage="//input[@class='btn btn-primary']";
	
	//easybuzz netbanking 
	public static final String easybuzzNetBankingOTP="//input[@id='pass']";
	public static final String easybuzzFailureBtn="//input[@class='btn alert']";
	public static final String easybuzzSuccessBtn="//input[@class='btn success']";
	
	//panetics
	public static final String paneticsOTP="//input[@id='password']";
	public static final String paneticsBtn="//button[text()='Submit']";
	
	//Telr
	public static final String telrEnterOTP="//input[@class='input-field']";
	public static final String telrSumbitButton="//input[@value='SUBMIT']";
	
	//manta pay card 
	
	public static final String mantapayOTP="//input[@name='PaRes']";
	public static final String mantapayBtn="//input[@value='Continue']";
	
	//unicorn 3ds page 
	
	public static final String unicornOTP="//input[@name='password']";
	public static final String unicornBtn="//button[text()='SUBMIT']";
	
	//cc avenue
	public static final String ccavenueSelectClass="//select[@id='selectAuthResult']";
	public static final String ccavenueSubmitButton="//input[@id='acssubmit']";
	
	//Shift 4 
	
	public static final String shift4Otp="//input[@id='otp']";
	public static final String shift4payButton="//button[text()='Pay']";
	
	//payaza-card
	public static final String payazaDropdown="//select[@id='selectAuthResult']";
	public static final String payazaSubmitBtn="//input[@id='acssubmit']";
	
	//payable-sbx
	public static final String sbxPayaEnterOTP="//input[@name='password']";
	public static final String sbxSubmitBtn="//button[@type='submit']";
	
	
	//yaspa - netbanking 
	public static final String YaspaTestBank="(//span[text()='Yaspa Test Bank'])[1]";
	public static final String ccYaspa="//div[text()='Confirm and continue']";
	public static final String urlYaspa="//button[text()='Or continue on your bank website']";
	public static final String approve="//button[text()='Approve']";
	public static final String returnToPayse="//button[text()='Return to PAYSECURE TECHNOLOGY LIMITED']";

    //confirm purchase for Crypto flow
	
	public static final String confirmPurchases="//button[contains(text(),'Confirm purchase')]";
	public static final String confirmSubmit="//button[text()='Submit']";
	//public static final String easybuzzSuccessBtn="//input[@class='btn success']";
	public static final String remConsent="//input[@id='rememberConsent']";
	public static final String confirmConsent="//button[@id='confirmBtn']";
	
	
	//yoya pay payin 
	
	public static final String enterPassYoya="//input[@id='password']";
	public static final String confirmYoya="//input[@name='UsernamePasswordEntry']";
	
	
	
}
