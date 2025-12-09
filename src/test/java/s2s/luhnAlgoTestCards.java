package s2s;

import org.testng.annotations.Test;

import com.paysecure.Page.luhnAlgo;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;

import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;

public class luhnAlgoTestCards {
	
//	  @BeforeMethod
@SuppressWarnings("static-access")
//	  public void beforeMethod() {
//	  }
  @Test (dataProvider ="cardsData",dataProviderClass=DataProviders.class)
  public void f(String cardNumber) {
	  luhnAlgo la=new luhnAlgo();
	  boolean isValid = la.isValidCardNumber(cardNumber);
	  

       if (isValid) {
           System.out.println("Card: " + cardNumber + " → VALID");
          // Reporter.log("Card: " + cardNumber + " → VALID", true);
       } else {
           System.err.println("Card: " + cardNumber + " → INVALID");
         //  Reporter.log("Card: " + cardNumber + " → INVALID", true);
       }
  }


}
