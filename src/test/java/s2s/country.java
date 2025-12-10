package s2s;

import org.testng.annotations.Test;

import com.paysecure.Page.loginPage;
import com.paysecure.Page.matrixCashierPage;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;
import com.paysecure.utilities.ExcelWriteUtility;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;

public class country extends baseClass{
	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	 String purchaseId;
	matrixCashierPage mcp;
	transactionPage tp;	
    String status = "";
    String comment = "";
    String country;
	@BeforeMethod
	public void beforeMethod() throws InterruptedException {
		mcp = new matrixCashierPage(getDriver());
		tp = new transactionPage(getDriver());
	}
  @Test(dataProvider ="countryProvider", dataProviderClass = DataProviders.class)
  public void purchase(String country) throws Exception {
		WebDriver driver = baseClass.getDriver();
		this.country=country;
		String baseUri = PropertyReader.getProperty("baseURI");
		RestAssured.baseURI =baseUri;
        String token = PropertyReader.getProperty("token");
        String brandId = PropertyReader.getProperty("brandId");
		String price = generateRandomTestData.generateRandomDouble();
		String currency = PropertyReader.getProperty("currency");
		String paymentMethod = PropertyReader.getProperty("paymentMethod");
		String firstName = generateRandomTestData.generateRandomFirstName();
		String emailId = generateRandomTestData.generateRandomEmail();
		String city = "Paris";
		String streetAddress = "Main gate";
		String zipcode = "20001";
		String productname="Cricket bat";

		String requestBody = "{\n" +
		        "  \"client\": {\n" +
		        "    \"full_name\": \""+firstName+"\",\n" +
		        "    \"email\": \""+emailId+"\",\n" +
		        "    \"country\": \""+country+"\",\n" +
		        "    \"city\": \""+city+"\",\n" +
		        "    \"stateCode\": \"QLD\",\n" +
		        "    \"street_address\": \""+streetAddress+"\",\n" +
		        "    \"zip_code\": \""+zipcode+"\",\n" +
		        "    \"phone\": \"+1111111111\"\n" +
		        "  },\n" +
		        "  \"purchase\": {\n" +
		        "    \"currency\": \""+currency+"\",\n" +
		        "    \"products\": [\n" +
		        "      {\n" +
		        "        \"name\": \""+productname+"\",\n" +
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

		Response response = RestAssured.given().header("Authorization", "Bearer " + token).contentType(ContentType.JSON)
				.body(requestBody).when().post("api/v1/purchases").then().extract().response();

		System.out.println(response.asPrettyString());
		checkoutUrl = response.jsonPath().getString("checkout_url");
		purchaseId = response.jsonPath().getString("purchaseId");  
		System.out.println(response.asPrettyString());
		purchaseId = response.jsonPath().getString("purchaseId");
		
		
		if (response.statusCode() == 202) {
		    purchaseId = response.jsonPath().getString("purchaseId");
		    s2sMethod();  // only executed when valid email
		}

		if (response.statusCode()==202) {
			Reporter.log("country accepted by API: " + country, true);
		}else if (response.statusCode() == 400 || response.statusCode() == 422) {
            Reporter.log("Country rejected by API: " + country, true);
            status = "PASS";
            comment = "PASS → Country rejected correctly   " + country;

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("Country_Result", country, status, comment,purchaseId);
            driver.quit();
            return; 
        }else {
			Reporter.log("Unexpected response for country: " + country + " -> " + response.statusCode(), true);
		}
  }
  
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

				.header("brandId", brandId).
		        contentType(ContentType.JSON).
				body(requestBody).
				when().
                post(endpoint).
				then()
				.log().all().extract().response();

		String callback_url = response.jsonPath().getString("callback_url");
		System.out.println(callback_url);
		driver.get(callback_url);
		
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

            ExcelWriteUtility.writeResults2s("Country_Result", country, status, comment,purchaseId);
            driver.quit();
            return;
        }
        else if (flag.equalsIgnoreCase("true")) {
            status = "PASS";
            comment = "Payment Successfully";

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("Country_Result", country, status, comment,purchaseId);

        }
        else {
            status = "UNKNOWN";
            comment = "URL does not contain expected issucces parameter";

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("Country_Result", country, status, comment,purchaseId);


        }

			mcp.openBrowserForStaging(driver,"http://staging.choicepay.ca/");
			lp.login();
			tp.navigateUptoTransaction();
			tp.searchTheTransaction( purchaseId);
			tp.searchButton();
			tp.clickOnTransactionId();
            tp.verifyPurchaseTransactionIDIsNotEmpty();
            tp.verifyTxnId(purchaseId);

            Thread.sleep(3000);
            driver.quit();


}
}
