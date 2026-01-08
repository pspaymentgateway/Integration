package com.paysecure.Page;

import org.openqa.selenium.WebDriver;

import com.paysecure.base.baseClass;

public class pspOTPPage{
	
	public void enterOTP(String psp) {
	    WebDriver driver = baseClass.getDriver();
	    transactionPage tp = new transactionPage(driver);
	    CashierPage mcp = new CashierPage(driver);

	    // Normalize the PSP to lowercase for case-insensitive switch
	    String normalizedPsp = psp.toLowerCase();

	    switch (normalizedPsp) {
	        case "easybuzz" -> tp.enterOTpEasyBuzz();
	        case "zaakpay" -> mcp.zaakPayOtpEnterSuccessOrFailure();
	        case "matrix" -> System.err.println("So there is no switch case for this 'Matrix Integration' ");
	        default -> throw new IllegalArgumentException("Unsupported PSP: " + psp);
	    }
	}}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	