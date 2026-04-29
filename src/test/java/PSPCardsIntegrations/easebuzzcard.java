package PSPCardsIntegrations;

import org.testng.annotations.Test;

import com.paysecure.Page.loginPage;
import com.paysecure.Page.CashierPage;
import com.paysecure.Page.RouteManager;
import com.paysecure.Page.payu3dPage;
import com.paysecure.Page.pspOTPPage;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;
import com.paysecure.utilities.DataProvidersEndToEndFlow;
import com.paysecure.utilities.ExcelWriteUtility;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;
import com.paysecure.utilities.testData_CreateRoll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;

public class easebuzzcard extends baseClass{
	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	String purchaseId;
	CashierPage mcp;
	transactionPage tp;
	payu3dPage pay;
	pspOTPPage otp;
    String status = "";
    String comment = "";
    
    
	  @BeforeMethod
	  public void beforeMethod() throws InterruptedException {
			lp = new loginPage(getDriver());
			lp.login();
			mcp=new CashierPage(getDriver());
			tp=new transactionPage(getDriver());
			pay = new payu3dPage(getDriver());
			otp= new pspOTPPage();
	  }
	 
  @Test(dataProvider ="EaseBuzzProvider",dataProviderClass = DataProvidersEndToEndFlow.class) 
  public void purchase(Map<String, String> data) throws Exception {
      WebDriver driver=baseClass.getDriver();
		String baseUri = PropertyReader.getPropertyForPurchase("baseURI");
		RestAssured.baseURI =baseUri;
		
		    String ExpectedStatus =data.get("ExpectedOutcome");
		    String cardHolder=data.get("CardholderName");
		    String cardNumber    = data.get("CardNumber");
	        String expiry   = data.get("Expiry");
	        String cvv      = data.get("CVV");
	        String PSP      =data.get("PSP");
		    String PaymentMethod = data.getOrDefault("PaymentMethod", "");
		    String Currency = data.getOrDefault("Currency", "");
		    String minAmountStr = data.getOrDefault("MinAmount", "");
		    String maxAmountStr = data.getOrDefault("MaxAmount", "");
		    String defaultAmountStr = data.getOrDefault("DefaultAmount", "");
		    
		    double minAmount = testData_CreateRoll.parseAmount(minAmountStr, 0.0);
		    double maxAmount = testData_CreateRoll.parseAmount(maxAmountStr, 0.0);
		    double defaultAmount = testData_CreateRoll.parseAmount(defaultAmountStr, 5.6);
		    
		    String Merchant = data.getOrDefault("Merchant", "");
		    String RouteToBankMid = data.getOrDefault("RouteToBankMid", "");
		    String RouteToMidOrBank = data.getOrDefault("RouteToMidOrBank", "");
	        
		String brandId = PropertyReader.getPropertyForPurchase("brandId");
		String token = PropertyReader.getPropertyForPurchase("token");
		String price = generateRandomTestData.generateRandomDoublePrice(minAmount,maxAmount,defaultAmount);
		String firstName = generateRandomTestData.generateRandomFirstName();
		String emailId = generateRandomTestData.generateRandomEmail();
		
	    RouteManager.ensureRoute(
		        getDriver(),
		        Merchant,
		        Merchant,
		        PaymentMethod,
		        PaymentMethod,
		        Currency,
		        Currency,
		        PSP,
		        RouteToBankMid,
		        RouteToMidOrBank
		    );
		
		String country="IN";
		String city = "Paris";
		String stateCode="QLD";
		String streetAddress = "Main gate";
		String zipcode = "20001";
		String productname="Cricket bat";
		
		
        System.err.println(baseUri);
        String requestBody = "{\n" +
		        "  \"client\": {\n" +
		        "    \"full_name\": \""+firstName+"\",\n" +
		        "    \"email\": \""+emailId+"\",\n" +
		        "    \"country\": \""+country+"\",\n" +
		        "    \"city\": \""+city+"\",\n" +
		        "    \"stateCode\": \""+stateCode+"\",\n" +
		        "    \"street_address\": \""+streetAddress+"\",\n" +
		        "    \"zip_code\": \""+zipcode+"\",\n" +
		        "    \"phone\": \"+1111111111\"\n" +
		        "  },\n" +
		        "  \"purchase\": {\n" +
		        "    \"currency\": \""+Currency+"\",\n" +
		        "    \"products\": [\n" +
		        "      {\n" +
		        "        \"name\": \""+productname+"\",\n" +
		        "        \"price\":"+ price + "\n" +  // "        \"price\": " + price + "\n" +
		        "      }\n" +
		        "    ]\n" +
		        "  },\n" +
		        "  \"paymentMethod\": \""+PaymentMethod+"\",\n" +
		        "  \"brand_id\": \"" + brandId + "\",\n" +
		        "  \"success_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=true\",\n" +
		        "  \"failure_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=false\",\n" +
		        "  \"success_callback\": \"https://www.google.com/\",\n" +
		        "  \"failure_callback\": \"https://staging.paysecure.net/merchant\"\n" +
		        "}";

	    Response  response = RestAssured.given()
              .header("Authorization", "Bearer " + token)
              .contentType(ContentType.JSON)
              .body(requestBody)
              .when()
              .post("api/v1/purchases")
              .then()
              .extract()
              .response();
	    
	    System.out.println(response.asPrettyString());

		checkoutUrl = response.jsonPath().getString("checkout_url");
		purchaseId = response.jsonPath().getString("purchaseId");
		String merchantName = response.jsonPath().getString("merchantName");
		System.err.println("merchantName"+merchantName);
		double total = response.jsonPath().getDouble("purchase.total");
		tp.validatePurchaseId(purchaseId);
        // Payment
        driver.get(checkoutUrl);

        mcp.userEnterCardInformationForPayment(cardHolder, cardNumber, expiry, cvv);
        
        mcp.clickOnPay();
	    
	    otp.enterOTP(PSP);
	    
        Thread.sleep(4000);
		 // Wait until parameter appears in URL
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("issucces"));

        String redirectUrl = driver.getCurrentUrl();
        Reporter.log("Redirected URL: " + redirectUrl, true);

        String flag = Arrays.stream(redirectUrl.split("\\?")[1].split("&"))
                .filter(p -> p.startsWith("issucces="))
                .map(p -> p.split("=")[1])
                .findFirst().orElse("");


        if (flag.equalsIgnoreCase("false")) {
            status = "FAIL";
            comment = "Payment Failed";

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResult("EndToEnd_Result",Currency +" "+PaymentMethod,ExpectedStatus,  status, comment,purchaseId,PSP,PaymentMethod);
            driver.quit();
            return;
        }
        else if (flag.equalsIgnoreCase("true")) {
            status = "PASS";
            comment = "Payment Successfully";

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResult("EndToEnd_Result",Currency +" "+PaymentMethod,ExpectedStatus,    status, comment,purchaseId,PSP,PaymentMethod);

        }
        else {
            status = "UNKNOWN";
            comment = "URL does not contain expected issucces parameter";

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResult("EndToEnd_Result",Currency +" "+PaymentMethod,ExpectedStatus,    status, comment,purchaseId,PSP,PaymentMethod);


        }
        
        mcp.openBrowserForStaging(driver,RestAssured.baseURI);
        lp.login();
        tp.navigateUptoTransaction();
        tp.searchTheTransaction(purchaseId);
        tp.verifyTxnId(purchaseId);
        tp.verifyAmount(total);
        tp.verifyCurrency(Currency);
        tp.getStatusFromUI();
        tp.maskCardForCashier(cardNumber);
        tp.verifyUsedCardOnUI(cardNumber);
        tp.clickOnTransactionId();
        tp.verifyPurchaseTransactionIDIsNotEmpty();
		tp.verifyCurrencyOnPaymentInfo();
		tp.verifyAmountFromPaymentInfo();

  }


}
