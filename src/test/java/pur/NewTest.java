package pur;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.paysecure.Page.CashierPage;

import com.paysecure.base.baseClass;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class NewTest extends baseClass{
	CashierPage mcp;
	@BeforeMethod
	public void beforeMethod() throws InterruptedException {
		
		mcp = new CashierPage(getDriver());

	}
	
  @Test (invocationCount = 1)
  public void f() throws InterruptedException {
	    String baseUri = PropertyReader.getPropertyForPurchase("baseURI");
	    RestAssured.baseURI = baseUri;
	    
	    WebDriver driver = baseClass.getDriver();
	    String brandId = PropertyReader.getPropertyForPurchase("brandId");
	    String token = PropertyReader.getPropertyForPurchase("token");
	    String price = generateRandomTestData.generateRandomDoublePrice(1.00,500.00,1.00);
	    String firstName = generateRandomTestData.generateRandomFirstName();
	    String country = "US";
	    String city = "Paris";
	    String stateCode = "QLD";
	    String Email ="suhaspowar+501@paysecure.dev";
	    
	    String streetAddress = "Main gate";
	    String zipcode = "20001";
	    String productname = "Cricket bat";
	    
	    String Currency="USD";
	    String PaymentMethod="Master";

	    System.err.println(baseUri);

	    String requestBody = "{\n" +
	            "  \"client\": {\n" +
	            "    \"full_name\": \"" + firstName + "\",\n" +
	            "    \"email\": \"" + Email + "\",\n" +
	            "    \"country\": \"" + country + "\",\n" +
	            "    \"city\": \"" + city + "\",\n" +
	            "    \"stateCode\": \"" + stateCode + "\",\n" +
	            "    \"street_address\": \"" + streetAddress + "\",\n" +
	            "    \"zip_code\": \"" + zipcode + "\",\n" +
	            "    \"phone\": \"+1111111111\"\n" +
	            "  },\n" +
	            "  \"purchase\": {\n" +
	            "    \"currency\": \"" + Currency + "\",\n" +
	            "    \"products\": [\n" +
	            "      {\n" +
	            "        \"name\": \"" + productname + "\",\n" +
	            "        \"price\":" + price + "\n" +
	            "      }\n" +
	            "    ]\n" +
	            "  },\n" +
	            "  \"paymentMethod\": \"" + PaymentMethod + "\",\n" +
	            "  \"brand_id\": \"" + brandId + "\",\n" +
	            "  \"success_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=true\",\n" +
	            "  \"failure_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=false\",\n" +
	            "  \"success_callback\": \"https://www.google.com/\",\n" +
	            "  \"failure_callback\": \"https://staging.paysecure.net/merchant\"\n" +
	            "}";

	    Response response = RestAssured.given()
	            .header("Authorization", "Bearer " + token)
	            .contentType(ContentType.JSON)
	            .body(requestBody)
	            .when()
	            .post("api/v1/purchases")
	            .then()
	            .extract()
	            .response();

	    String checkoutUrl = response.jsonPath().getString("checkout_url");
	    String purchaseId = response.jsonPath().getString("purchaseId");
	    System.out.println("Getting purchaseID From Json Response :-" + purchaseId);
	    driver.get(checkoutUrl);
	    mcp.userEnterCardInformationForPayment("Tere naina", "5200000000000015", "09/30", "123");
	    mcp.clickOnPay();
//	    mcp.rememberConsent();
//	    mcp.confirmPurchase();
	    
  }
}
