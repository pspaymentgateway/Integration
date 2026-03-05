package com.paysecure.Page;

import org.openqa.selenium.WebDriver;

import com.paysecure.base.baseClass;
import com.paysecure.utilities.PropertyReader;


public class pspOTPPage{
	
	public void enterOTP(String psp) throws InterruptedException {
	    WebDriver driver = baseClass.getDriver();
	    transactionPage tp = new transactionPage(driver);
	    CashierPage mcp = new CashierPage(driver);
	    
	    String PossibleUseCases = PropertyReader.getPropertyForPurchase("PossibleUseCases");
	    String Status = PropertyReader.getPropertyForPurchase("Status");
	    String Cvv = PropertyReader.getPropertyForPurchase("Cvv");
	    String mantapayOtp = PropertyReader.getPropertyForPurchase("mantapayOtp");
	    // Normalize the PSP to lowercase for case-insensitive switch
	    String normalizedPsp = psp.toLowerCase();

	    switch (normalizedPsp) {
	        case "easybuzz" -> tp.enterOTpEasyBuzz();
	        case "zaakpay" -> mcp.zaakPayOtpEnterSuccessOrFailure();
	        case "matrix" -> System.err.println("So there is no 3ds page for this 'Matrix Integration' ");
	        case "network_international" -> System.err.println("So there is no switch case for this 'Network_International' ");
	        case "euroexchange" -> mcp.handlePSPPageForEuroExchange(PossibleUseCases,Status,Cvv);
	        case "paynetics" -> mcp.panaticsSubmitButtonOnBankPage();
	      //  case "telr" -> mcp.handleTelrs3dsPage(); 
	        case "mantapay" -> mcp.mantapay3dsPage(mantapayOtp); 
	        case "pxp" -> System.err.println("So there is no 3ds page for this 'PXP Integration' ");
	        case "payerworld_payin" -> System.err.println("So there is no 3ds page for this 'PXP Integration' ");
	        case "trustpayments" -> System.err.println("So there is no 3ds page for this 'trustpayments Integration' ");
	        case "unicornpayment" -> System.err.println("So there is no 3ds page for this 'unicornpayment Integration' ");
	        case "unicornpaymentds" -> mcp.unicorn3dsPage();
	       
	        default -> throw new IllegalArgumentException("Unsupported PSP: " + psp);
	    }
	}
	
}
	
	

	