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
		 ml.selectMerchant("SuhasM","SuhasM");
		 ml.selectCurrency("MXN","MXN");
		 ml.selectPaymentMethod("PAYOUT-SPEI","PAYOUT-SPEI");
		 ml.getDetailsForLimitAndCharges();
		 ml.fillAllTextboxesWithTab();
		 ml.selectRouteTo("Route to Mid");
		 ml.selectRoutePoint("BitsoMidSpeiPayoutLive");
		 ml.saveButton();
		 ml.finalsaveButton();
	  }
	  
}
