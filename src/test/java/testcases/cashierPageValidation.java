package testcases;

import org.testng.annotations.Test;

import com.paysecure.Page.loginPage;
import com.paysecure.Page.matrixCashierPage;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;

public class cashierPageValidation extends baseClass{
	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	String purchaseId;
	matrixCashierPage mcp;
	transactionPage tp;
	
	  @BeforeMethod
	  public void beforeMethod() throws InterruptedException{
			lp = new loginPage(getDriver());
			lp.login();
			mcp=new matrixCashierPage(getDriver());
			tp=new transactionPage(getDriver());
	  }
	  
	  @Test(dataProvider ="paymentData", dataProviderClass = DataProviders.class)
	  public void f(String cardHolder,
	                String cardNumber,
	                String expiry,
	                String cvc,
	                String email,
	                String fullname,
	                String address,
	                String city,
	                String zipcode,
	                String expectedError) throws InterruptedException {

	        WebDriver driver = baseClass.getDriver();
			String baseUri = PropertyReader.getPropertyForPurchase("baseURI");
			RestAssured.baseURI =baseUri;
			String brandId = PropertyReader.getPropertyForPurchase("brandId");
			String token = PropertyReader.getPropertyForPurchase("token");
			String price = generateRandomTestData.generateRandomDouble();
			String currency =PropertyReader.getPropertyForPurchase("currency");
			String paymentMethod=PropertyReader.getPropertyForPurchase("paymentMethod");
			String firstName = generateRandomTestData.generateRandomFirstName();
			String emailId = generateRandomTestData.generateRandomEmail();
			String matrixPSPUrl=PropertyReader.getPropertyForPurchase("matrixPSPUrl");
			String UID=PropertyReader.getPropertyForPurchase("UID");
			String PASSWORD=PropertyReader.getPropertyForPurchase("PASSWORD");
			String requestBody = "{\n" +
			        "  \"client\": {\n" +
			        "    \"full_name\": \""+firstName+"\",\n" +
			        "    \"email\": \""+emailId+"\",\n" +
			        "    \"country\": \"DZ\",\n" +
			        "    \"emails2s\": \"London\",\n" +
			        "    \"stateCode\": \"QLD\",\n" +
			        "    \"street_address\": \"GGNH JAIPUR\",\n" +
			        "    \"zip_code\": \"W1S 3BE\",\n" +
			        "    \"phone\": \"+1111111111\"\n" +
			        "  },\n" +
			        "  \"purchase\": {\n" +
			        "    \"currency\": \""+currency+"\",\n" +
			        "    \"products\": [\n" +
			        "      {\n" +
			        "        \"name\": \"New Ebook Gaming cards\",\n" +
			        "        \"price\":"+ price + "\n" +  // "        \"price\": " + price + "\n" +
			        "      }\n" +
			        "    ]\n" +
			        "  },\n" +
			        "  \"paymentMethod\": \""+paymentMethod+"\",\n" +
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

			checkoutUrl = response.jsonPath().getString("checkout_url");
			purchaseId = response.jsonPath().getString("purchaseId");
			String merchantName = response.jsonPath().getString("merchantName");
			double total = response.jsonPath().getDouble("purchase.total");
            
			tp.validatePurchaseId(purchaseId);
	        // Payment
	        driver.get(checkoutUrl);
	        mcp.userEnterCardInformationForPayment(cardHolder, cardNumber, expiry, cvc);
	        mcp.clearThePrefilled();
	        mcp.enterEmail(email);
	        mcp.enterFullName(fullname);
	        mcp.enterAddress(address);
	        mcp.enterCity(city);
	        mcp.enterZipcode(zipcode);
	        mcp.clickOnPay();
	        Thread.sleep(2500);
	        String actualError = mcp.getCashierError();


	     // Log expected vs actual for debugging
	        Reporter.log("Expected Error: " + expectedError, true);
	        Reporter.log("Actual Error: " + actualError, true);
	        Reporter.log("Card Number Used: " + cardNumber, true);

	        // Validate
	        Assert.assertEquals(
	                actualError,
	                expectedError,
	                "Validation message mismatch for card number: " + cardNumber
	        );

	      //  driver.quit();
	        
  }


}
