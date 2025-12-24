package CreateCustomerAndSession;

import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.paysecure.Page.loginPage;
import com.paysecure.Page.matrixCashierPage;
import com.paysecure.Page.payu3dPage;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;
import com.paysecure.utilities.ExcelWriteUtility;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;



public class createCustomerAndCreateSession extends baseClass{

	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	//String purchaseId;
	matrixCashierPage mcp;
	transactionPage tp;
	payu3dPage pay;
	String stateCode;
	String customerId;
	String	sessionId;
	String	sessionUrl;
    String status = "";
    String comment = "";
    String city;
    
    // Store base URI to reuse in S2S call
    String baseUri;
    
	@BeforeMethod
	public void beforeMethod() throws InterruptedException {
		lp = new loginPage(getDriver());
		mcp = new matrixCashierPage(getDriver());
		tp = new transactionPage(getDriver());
		pay=new payu3dPage(getDriver());
	
	}
	
	@Test
	public void createCustomer() {
		String brandId =PropertyReader.getPropertyForCreateCustomerSession("BrandIDCC");
		String token =PropertyReader.getPropertyForCreateCustomerSession("TokenCC");
		String firstName = generateRandomTestData.generateRandomFirstName();
		
		String emailId = generateRandomTestData.generateRandomEmail();
		String mobileNumber=generateRandomTestData.generateRandomIndianMobileNumber();
		String merchantCSID=generateRandomTestData.generateRandomMerchantCustomerId();
		
		baseUri = PropertyReader.getPropertyForCreateCustomerSession("baseURICC");
		String country="IN";
		RestAssured.baseURI = baseUri;
		String requestBody = "{\n" +
				"  \"merchantCustomerId\": \""+merchantCSID+"\",\n" +
				"  \"fullName\": \""+firstName+"\",\n" +
				"  \"emailId\": \""+emailId+"\",\n" +
				"  \"phoneNo\": \""+mobileNumber+"\",\n" +
				"  \"city\": \"juneau\",\n" +
				"  \"stateCode\": \"MH\",\n" +
				"  \"zipCode\": \"99812\",\n" +
				"  \"country\": \""+country+"\",\n" +
				"  \"custRegDate\": \"2024-11-23\",\n" +
				"  \"successTrans\": \"23\",\n" +
				"  \"extraParam\": {}\n" +
				"}";
		
		Response response = RestAssured.given().header("Authorization", "Bearer " + token).contentType(ContentType.JSON)
				.body(requestBody).when().post("api/v1/customer").then().log().all().statusCode(202).extract()
				.response();
		
		customerId = response.jsonPath().getString("customerId");
		System.out.println(customerId);
	}
	
	
	@Test(dependsOnMethods ="createCustomer",invocationCount = 1,dataProvider = "cardData", dataProviderClass = DataProviders.class)
	public void createSession(String cardHolder, String cardNumber, String expiry, String cvv) throws Exception {
		WebDriver driver=baseClass.getDriver();
		String brandId =PropertyReader.getPropertyForCreateCustomerSession("BrandIDCC");
		String currency =PropertyReader.getPropertyForCreateCustomerSession("currencyCC");
		String token =PropertyReader.getPropertyForCreateCustomerSession("TokenCC");
		String baseUri = PropertyReader.getPropertyForCreateCustomerSession("baseURICC");
		String paymentMethod=PropertyReader.getPropertyForCreateCustomerSession("paymentMethod");
		String price = generateRandomTestData.generateRandomDoublePrice();
		String payu = PropertyReader.getPropertyForCreateCustomerSession("payu");
		String payhost = PropertyReader.getPropertyForCreateCustomerSession("payhost");
		RestAssured.baseURI = baseUri;
		
		String requestBody = "{\n" +
		        "  \"customerId\": \"" + customerId + "\",\n" +
		        "  \"spei_clabe\": \"646180110400000007\",\n" +
		        "  \"spei_debitCard\": \"4189143173884480\",\n" +
		        "  \"spei_mobileNo\": \"8180833954\",\n" +
		    
		        "  \"tax_number\": \"39933551809\",\n" +
		        "  \"currency\": \"" + currency + "\",\n" +
		        "  \"totalAmount\": \"" + price + "\",\n" +
		        "  \"paymentMethod\": \"" + paymentMethod + "\",\n" +
		        "  \"success_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=true\",\n" +
		        "  \"failure_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=false\"\n" +
		        "}";

		
		Response response = 
				RestAssured.given().header("Authorization", "Bearer " + token)
				
				.header("brandId", brandId)
				.contentType(ContentType.JSON)
				.body(requestBody).when().post("api/v1/createSession").then().log().all().statusCode(200).extract()
				.response();
		
		// Assign to global variables
		sessionUrl = response.jsonPath().getString("sessionUrl");
		sessionId = response.jsonPath().getString("sessionId");

		// Log API response details
		Reporter.log("Purchase API call successful", true);
		Reporter.log("Purchase ID: " + sessionId, true);
		Reporter.log("Checkout URL received: " + sessionUrl, true);

		// Navigate to checkout page
		driver.get(sessionUrl);
		Reporter.log("Navigated to checkout page URL successfully", true);
	
	       mcp.userEnterCardInformationForPayment( cardHolder, cardNumber, expiry, cvv);
           mcp.clickOnPay();
           
//           if(payhost.equalsIgnoreCase("Payhost")) {
//        	   tp.clickONSubmitButton();
//           }
          
	
//           if(payu.equalsIgnoreCase("payu")) {
//   	    	pay.payForPayu(currency,sessionId);
//   	    }
           
           if (mcp.isCardNumberInvalid()) {
        	   status = "FAIL";
               comment = "Payment Failed Cause Of Luhn ";
          Reporter.log("Invalid card number → Luhn check failed", true);
          ExcelWriteUtility.writeResult("CreateCustomerAndSession", currency + " " +paymentMethod, status, comment,sessionId);
              
             //  driver.quit();
               return;
           }
           
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

               ExcelWriteUtility.writeResult("CreateCustomerAndSession",  currency + " " +paymentMethod, status, comment,sessionId);
             //  driver.quit();
               return;
           }
           else if (flag.equalsIgnoreCase("true")) {
               status = "PASS";
               comment = "Payment Successfully";

               Reporter.log(comment, true);

               ExcelWriteUtility.writeResult("CreateCustomerAndSession", currency + " " +paymentMethod, status, comment,sessionId);

           }
           else {
               status = "UNKNOWN";
               comment = "URL does not contain expected issucces parameter";

               Reporter.log(comment, true);

               ExcelWriteUtility.writeResult("CreateCustomerAndSession", currency + " " +paymentMethod, status, comment,sessionId);


           }


           // Login + Transaction check
           mcp.openBrowserForStaging(driver,baseUri);
           lp.login();
           tp.navigateUptoTransaction();
           tp.searchTheTransaction(sessionId);
           tp.searchTheTransaction( sessionId);
           tp.searchButton();
           tp.clickOnTransactionId();
           tp.verifyPurchaseTransactionIDIsNotEmpty();
           Thread.sleep(4000);
           

           
	
	}
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
	
	

