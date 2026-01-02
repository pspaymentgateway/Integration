package refund;
import org.testng.annotations.Test;

import com.paysecure.Page.loginPage;
import com.paysecure.Page.CashierPage;
import com.paysecure.Page.payu3dPage;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;
import com.paysecure.utilities.jsonProvider;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
public class NewTest extends baseClass{
	
	loginPage lp;
	String checkoutUrl;
	String purchaseId;
	CashierPage mcp;
	transactionPage tp;
	payu3dPage pay;
    String status = "";
    String comment = "";
	
      private WebDriver driver;
	  @BeforeMethod
	  public void beforeMethod() throws InterruptedException {
			lp = new loginPage(getDriver());
			lp.login();
			mcp=new CashierPage(getDriver());
			tp=new transactionPage(getDriver());
			pay = new payu3dPage(getDriver());
	  }
	
  @Test(dataProvider ="CreateCCCardData", dataProviderClass = DataProviders.class)
  public void purchaseApi(String cardHolder, String cardNumber, String expiry, String cvv,String PSP) throws Exception {
      WebDriver driver=baseClass.getDriver();
      Reporter.log("Email test case will run for this PSP :- "+PSP, true);
		String baseUri = PropertyReader.getPropertyForPurchase("baseURI");
		RestAssured.baseURI =baseUri;
			String brandId = PropertyReader.getPropertyForPurchase("brandId");
			String token = PropertyReader.getPropertyForPurchase("token");
			String price = generateRandomTestData.generateRandomDouble();
			String currency =PropertyReader.getPropertyForPurchase("currency");
			String paymentMethod=PropertyReader.getPropertyForPurchase("paymentMethods");
			String firstName = generateRandomTestData.generateRandomFirstName();
			String emailId = generateRandomTestData.generateRandomEmail();
			String payu = PropertyReader.getPropertyForS2S("payu");
			String easybuzz = PropertyReader.getPropertyForPurchase("easybuzz");
			String expectedstatus = PropertyReader.getPropertyForPurchase("status");
			String country="IN";
			String city = "Paris";
			String stateCode="QLD";
			String streetAddress = "Main gate";
			String zipcode = "20001";
			String productname="Cricket bat";
			String requestBody = "{\n" +
		                "  \"client\": {\n" +
		                "    \"full_name\": \"" + firstName + "\",\n" +
		                "    \"email\": \"" + emailId + "\",\n" +
		                "    \"country\": \"" + country + "\",\n" +
		                "    \"city\": \"" + city + "\",\n" +
		                "    \"stateCode\": \"" + stateCode + "\",\n" +
		                "    \"street_address\": \"" + streetAddress + "\",\n" +
		                "    \"zip_code\": \"" + zipcode + "\",\n" +
		                "    \"phone\": \"+1111111111\"\n" +
		                "  },\n" +
		                "  \"purchase\": {\n" +
		                "    \"currency\": \"" + currency + "\",\n" +
		                "    \"products\": [\n" +
		                "      {\n" +
		                "        \"name\": \"" + productname + "\",\n" +
		                "        \"price\":" + price + "\n" +
		                "      }\n" +
		                "    ]\n" +
		                "  },\n" +
		                "  \"paymentMethod\": \"" + paymentMethod + "\",\n" +
		                "  \"brand_id\": \"" + brandId + "\",\n" +
				"  \"success_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=true\",\n" +
				"  \"failure_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=false\",\n" +
				"  \"success_callback\": \"https://www.google.com/\",\n" +
				"  \"failure_callback\": \"https://staging.paysecure.net/merchant\"\n" +
				"}";
		Response response = RestAssured.given().header("Authorization", "Bearer " + token).contentType(ContentType.JSON)
				.body(requestBody).when().post("/api/v1/purchases").then().log().all().extract()
				.response();
		  
		    // Extract values from POST response
		    String checkoutUrl = response.jsonPath().getString("checkout_url");
		    purchaseId = response.jsonPath().getString("purchaseId");
		   
            driver.get(checkoutUrl);
            mcp.userEnterCardInformationForPayment(cardHolder, cardNumber, expiry, cvv);
            mcp.clickOnPay();

            if(easybuzz.equalsIgnoreCase("easybuzz")) {
    	    	tp.enterOTpEasyBuzz();
    	    }
            Thread.sleep(2000);
            mcp.openBrowserForStaging(driver, baseUri);
            lp.login();
            tp.navigateUptoTransaction();
            tp.searchTheTransaction(purchaseId);

 
            
            String actualStatus = tp.getLastSTtaus();

        
 
	
        String checkoutUrlRefund;

	    Response refundResponse =
        	    RestAssured.given()
        	        .baseUri("https://staging.paysecure.net")
        	        .header("Authorization", "Bearer " + token) 
        	        .pathParam("purchaseId", purchaseId)   
        	        .contentType(ContentType.JSON)
        	    .when()
        	        .get("/api/v1/purchases/{purchaseId}/refund")
        	    .then()
        	        .log().all()
        	        .extract()
        	        .response();

        	System.out.println(refundResponse.asPrettyString());
        	checkoutUrlRefund= refundResponse.jsonPath().getString("checkout_url");

        	System.out.println("Checkout URL: " + checkoutUrlRefund);
  

            if (expectedstatus.equalsIgnoreCase(actualStatus)) {
                if (checkoutUrlRefund != null && !checkoutUrlRefund.isEmpty()) {
                	 mcp.openBrowserForStaging(driver, baseUri);
                	 Thread.sleep(3000);
                	 driver.get(
                			  refundResponse.jsonPath().getString("success_redirect")
                			);

                } else {
                    System.out.println("Refund checkout URL is null or empty");
                }

            } else {
                System.out.println("Transaction is not paid yet. Current status: " + actualStatus);
            }
  
  }
}