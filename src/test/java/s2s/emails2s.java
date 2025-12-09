package s2s;

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

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;

public class emails2s extends baseClass {

	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	 String purchaseId;
	matrixCashierPage mcp;
	transactionPage tp;


	@BeforeMethod
	public void beforeMethod() throws InterruptedException {
		mcp = new matrixCashierPage(getDriver());
		tp = new transactionPage(getDriver());
	}

	@Test(dataProvider ="emailProvider", dataProviderClass = DataProviders.class)
	public void purchaseApi(String emailId) throws Exception {
		WebDriver driver = baseClass.getDriver();
		RestAssured.baseURI = "http://staging.choicepay.ca/";
		String token = PropertyReader.getProperty("token");
		String brandId = PropertyReader.getProperty("brandId");
		String price = generateRandomTestData.generateRandomDouble();
		String currency = PropertyReader.getProperty("currency");
		String paymentMethod = PropertyReader.getProperty("paymentMethod");
		String firstName = generateRandomTestData.generateRandomFirstName();
		String city = "Paris";
		String streetAddress = "Main gate";
		String zipcode = "20001";
		String productname = "Cricket bat";

		String requestBody = "{\n" + "  \"client\": {\n" + "    \"full_name\": \"" + firstName + "\",\n"
				+ "    \"email\": \"" + emailId + "\","
			    + "\n" + "    \"country\": \"DZ\","
			    + "\n" + "    \"city\": \"" + city+ "\","
			    + "\n" + "    \"stateCode\": \"QLD\","
			    + "\n" + "    \"street_address\": \"" + streetAddress + "\","
			    + "\n"
				+ "    \"zip_code\": \"" + zipcode + "\","
				+ "\n" + "    \"phone\": \"+1111111111\"\n" + "  },"
						+ "\n"
				+ "  \"purchase\": {\n" + "    \"currency\": \"" + currency + "\","
				+ "\n" + "    \"products\": [\n"
				+ "      {\n" + "        \"name\": \"" + productname + "\","
				+ "\n" + "        \"price\":" + price + "\n" + // "																							
				"      }\n" + "    ]\n" + "  },"
				+ "\n" + "  \"paymentMethod\": \"" + paymentMethod + "\",\n"
				+ "  \"brand_id\": \"" + brandId + "\",\n"
				+ "  \"success_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=true\",\n"
				+ "  \"failure_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=false\",\n"
				+ "  \"success_callback\": \"https://www.google.com/\",\n"
				+ "  \"failure_callback\": \"https://staging.paysecure.net/merchant\"\n" + "}";

		Response response = RestAssured.given().header("Authorization", "Bearer " + token).contentType(ContentType.JSON)
				.body(requestBody).when().post("api/v1/purchases").then().extract().response();

		System.out.println(response.asPrettyString());
		purchaseId = response.jsonPath().getString("purchaseId");
		int status = response.getStatusCode();
		
		if (response.statusCode() == 202) {
		    purchaseId = response.jsonPath().getString("purchaseId");
		    s2sMethod();
		}

		if (response.statusCode()==202) {
			Reporter.log("emailId accepted by API: " + emailId, true);
		} else if (response.statusCode() == 400 || response.statusCode() == 422) {
			Reporter.log("emailId rejected by API: " + emailId, true);
		} else {
			Reporter.log("Unexpected response for emailId: " + emailId + " -> " + response.statusCode(), true);
		}
	}

	@Test
	public void s2sMethod() throws Exception {
		WebDriver driver = baseClass.getDriver();
		lp = new loginPage(getDriver());
		 lp.login();
		if (purchaseId == null) {
			Reporter.log("Skipping S2S → purchaseId is NULL (purchase failed)", true);
			return;
		}

		RestAssured.baseURI = "http://staging.choicepay.ca/";
		String token = PropertyReader.getProperty("token");
		String cardNumber = PropertyReader.getProperty("cardNumber");
		String mmyy = PropertyReader.getProperty("mmyy");
		String cvv = PropertyReader.getProperty("cvv");
		String brandId = PropertyReader.getProperty("brandId");

		String endpoint = "api/v1/p/" + purchaseId + "?s2s=true";
		System.err.println(endpoint);
		String requestBody = "{\n" + "  \"cardholder_name\": \"Rahul Agarwal\",\n" + " \"card_number\": \"" + cardNumber
				+ "\"," + "\n" + " \"expires\": \"" + mmyy + "\"," + "\n" + " \"cvc\": \"" + cvv + "\",\n"
				+ "  \"remember_card\": \"on\",\n" + "  \"remote_ip\": \"157.38.242.7\",\n"
				+ "  \"user_agent\": \"Mozilla/5.0\",\n" + "  \"accept_header\": \"text/html\",\n"
				+ "  \"language\": \"en-US\",\n" + "  \"java_enabled\": false,\n" + "  \"javascript_enabled\": true,\n"
				+ "  \"color_depth\": 24,\n" + "  \"utc_offset\": 0,\n" + "  \"screen_width\": 1920,\n"
				+ "  \"screen_height\": 1080\n" + "}";

		Response response = RestAssured.given().header("Authorization", "Bearer " + token)

				.header("brandId", brandId).contentType(ContentType.JSON).body(requestBody).when().post(endpoint).then()
				.log().all().statusCode(202).extract().response();

		String callback_url = response.jsonPath().getString("callback_url");
		System.out.println(callback_url);
		driver.get(callback_url);
		Thread.sleep(7000);
		  String redirectUrl = driver.getCurrentUrl();
		  System.out.println(redirectUrl);
		  Thread.sleep(3000);
		   if (redirectUrl.contains("issucces=false")) {
               Reporter.log("Payment Failed → URL shows issucces=false", true);
               Reporter.log("Stopping further execution and quitting browser.", true);
               driver.quit();
               return;
           }

           // SUCCESS CASE
           if (redirectUrl.contains("issucces=true")) {
        
        	   Reporter.log("Payment Successful → URL shows issucces=true", true);
               Reporter.log("Proceeding with transaction verification flow.", true);
           }
			mcp.openBrowserForStaging(driver,"http://staging.choicepay.ca/");
			lp.login();
			tp.navigateUptoTransaction();
			tp.searchTheTransaction( purchaseId);
			tp.searchButton();
			tp.clickOnTransactionId();
            tp.verifyPurchaseTransactionIDIsNotEmpty();
            Thread.sleep(3000);
            driver.quit();
}

}