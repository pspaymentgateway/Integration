package com.paysecure.Page;

import org.openqa.selenium.WebDriver;

import com.paysecure.base.baseClass;

public class pspOTPPage{
	
	 public void enterOTP(String psp) {

	        WebDriver driver = baseClass.getDriver();
	        transactionPage tp = new transactionPage(driver);
	        CashierPage mcp = new CashierPage(driver);

	        switch (psp.toLowerCase()) {

	            case "easybuzz":
	                tp.enterOTpEasyBuzz();
	                break;

	            case "zaakpaynetbanking":
	                 mcp.zaakPayOtpEnterSuccessOrFailure();
	                break;


	            default:
	                throw new IllegalArgumentException("Unsupported PSP: " + psp);
	        }
	    }
	
}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	