package refund;

import org.testng.annotations.Test;

import com.paysecure.Page.MerchantRoutingPageLimitAndCharges;
import com.paysecure.Page.loginPage;
import com.paysecure.base.baseClass;

import org.testng.annotations.BeforeMethod;

public class limitAndCHarges extends baseClass{
	loginPage lp;	
	MerchantRoutingPageLimitAndCharges ml;
	  @BeforeMethod
	  public void beforeMethod() throws InterruptedException {
			lp= new loginPage(getDriver());
			lp.login();
			ml=new MerchantRoutingPageLimitAndCharges(getDriver());
			
	  }
	  
	  @Test  
	public void setLimitAndCHarges() throws InterruptedException {

		 ml.navigateUptoLimitAndCharges();
		 ml.selectMerchant("Denver","Denver");
		 ml.selectCurrency("EUR","EUR");
		 ml.selectPaymentMethod("VISA","VISA");
		 ml.getDetailsForLimitAndCharges();
		 ml.fillAllTextboxesWithTab();
		 ml.selectRouteTo("Route to Mid");
		 ml.selectRoutePoint("Payable-SBX");
		 ml.saveButton();
		 ml.finalsaveButton();
	  }
	  
}
