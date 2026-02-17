package s2s;

import org.testng.annotations.Test;

import com.paysecure.Page.loginPage;
import com.paysecure.Page.CashierPage;
import com.paysecure.Page.RouteManager;
import com.paysecure.Page.payu3dPage;
import com.paysecure.Page.pspOTPPage;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;
import com.paysecure.utilities.DataProvidersS2S;
import com.paysecure.utilities.ExcelWriteUtility;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;
import com.paysecure.utilities.testData_CreateRoll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;

public class currency extends baseClass {

	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	String purchaseId;
	CashierPage mcp;
	transactionPage tp;
	pspOTPPage otp;
	payu3dPage pay;
	String testResult = "";
	String comment = "";
  
    
    // Store base URI to reuse in S2S call
    String baseUri;
    
	@BeforeMethod
	public void beforeMethod() throws InterruptedException {
		mcp = new CashierPage(getDriver());
		tp = new transactionPage(getDriver());
		pay=new payu3dPage(getDriver());
		otp= new pspOTPPage();
	}

	
	@Test(dataProvider ="CurrencyProvider", dataProviderClass = DataProvidersS2S.class)
	public void purchaseApi(Map<String, String>cardData , Map<String, String> currencyData) throws Exception {
		WebDriver driver = baseClass.getDriver();
		
		
		// Store baseUri for later use
		baseUri = PropertyReader.getPropertyForS2S("baseURI");
		// Store baseUri for later use
		baseUri = PropertyReader.getPropertyForS2S("baseURI");
		RestAssured.baseURI = baseUri;
		String Currency = currencyData.getOrDefault("TestData", "");
		String ExpectedStatus = currencyData.getOrDefault("Status", "");
		String CardHolder = cardData.getOrDefault("CardholderName", "");
		String CardNumber = cardData.getOrDefault("CardNumber", "");
		String Expiry = cardData.getOrDefault("Expiry", "");
		String CVV = cardData.getOrDefault("CVV", "");
		String PSP = cardData.getOrDefault("PSP", "");
		String cardRunFlag = cardData.getOrDefault("RunFlag", "");
		String Merchant = cardData.getOrDefault("Merchant", "");
		String RouteToBankMid = cardData.getOrDefault("RouteToBankMid", "");
		String RouteToMidOrBank = cardData.getOrDefault("RouteToMidOrBank", "");

		String minAmountStr = cardData.getOrDefault("MinAmount", "");
		String maxAmountStr = cardData.getOrDefault("MaxAmount", "");
		String defaultAmountStr = cardData.getOrDefault("DefaultAmount", "");

		double minAmount = testData_CreateRoll.parseAmount(minAmountStr, 0.0);
		double maxAmount = testData_CreateRoll.parseAmount(maxAmountStr, 0.0);
		double defaultAmount = testData_CreateRoll.parseAmount(defaultAmountStr, 100.0);
		String PaymentMethod = cardData.getOrDefault("PaymentMethod","");
		//
		System.err.println(Currency +" "+ExpectedStatus+" "+CardHolder +" "+ CardNumber +" "+ Expiry +" "+ CVV +" "+ PSP+" "+PaymentMethod);

		if (Currency.isEmpty() || CardNumber.isEmpty()) {
			Reporter.log("Skipping test - Empty city or card number", true);
			throw new SkipException("Empty test data");
		}

		// ---------- Login BEFORE RouteManager ----------
		lp = new loginPage(getDriver());
		lp.login();

		// ---------- Route Manager Configuration ----------
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
		
        String token = PropertyReader.getPropertyForS2S("tokenS2S");
        String BrandID = PropertyReader.getPropertyForS2S("brandIdS2S");
		String price = generateRandomTestData.generateRandomDoublePrice(minAmount,maxAmount,defaultAmount);
		

		String firstName = generateRandomTestData.generateRandomFirstName();
		String emailId=generateRandomTestData.generateRandomEmail();
		String country = "IN";
		String city = "Paris";
		String stateCode = "QLD";
		
		String zipcode="10001";
		String productname = "Cricket bat";
		String streetAddress="Lal kothi";

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
		        "        \"price\":"+ price + "\n" +
		        "      }\n" +
		        "    ]\n" +
		        "  },\n" +
		        "  \"paymentMethod\": \""+PaymentMethod+"\",\n" +
		        "  \"brand_id\": \"" + BrandID + "\",\n" +
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
				.post("/api/v1/purchases")
				.then()
				.extract()
				.response();

		System.out.println(response.asPrettyString());
		purchaseId = response.jsonPath().getString("purchaseId");
		int status1 = response.getStatusCode();

		if (response.statusCode() == 202) {
		    purchaseId = response.jsonPath().getString("purchaseId");
		    Reporter.log("Purchase created successfully with ID: " + purchaseId, true);
		    
		    // Add a small delay to ensure purchase is fully persisted
		    Thread.sleep(2000);
		    
		    s2sMethod(Currency, ExpectedStatus, PSP, CardNumber, Expiry, CVV,PaymentMethod);
		}

		if (response.statusCode() == 202) {
			Reporter.log("currency accepted by API: " + Currency, true);
		} else if (response.statusCode() == 400 || response.statusCode() == 422) {
            Reporter.log("currency rejected by API: " + Currency, true);
            testResult = "Fail";
            comment = "PASS → currency rejected correctly " + Currency;

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("s2s_Result", Currency,ExpectedStatus, testResult, comment, purchaseId,PSP,PaymentMethod);
            driver.quit();
            return; 
        } else {
			Reporter.log("Unexpected response for currency: " + Currency + " -> " + response.statusCode(), true);
		}
	}

	public void s2sMethod(String Currency, String ExpectedStatus, String PSP, String CardNumber, String Expiry, String CVV,String PaymentMethod) throws Exception {
		WebDriver driver = baseClass.getDriver();

		if (purchaseId == null || purchaseId.isEmpty()) {
			Reporter.log("Skipping S2S → purchaseId is NULL (purchase failed)", true);
			testResult = "FAIL";
			comment = "Purchase ID is null, cannot proceed with S2S";
			ExcelWriteUtility.writeResults2s("s2s_Result", Currency, ExpectedStatus, "FAIL", comment, "N/A", PSP, PaymentMethod);
			driver.quit();
			return;
		}

		RestAssured.baseURI = baseUri;
		
		String endpoint = "/api/v1/p/" + purchaseId + "?s2s=true";
		System.err.println("S2S Endpoint: " + endpoint);
		System.err.println("Full URL: " + baseUri + endpoint);

		String url = PropertyReader.getPropertyForS2S("url");
		String token = PropertyReader.getPropertyForS2S("tokenS2S");
		String brandId = PropertyReader.getPropertyForS2S("brandIdS2S");

		String requestBody =
				"{\n" +
				"  \"cardholder_name\": \"Rahul Agarwal\",\n" +
				"  \"card_number\": \"" + CardNumber + "\",\n" +
				"  \"expires\": \"" + Expiry + "\",\n" +
				"  \"cvc\": \"" + CVV + "\",\n" +
				"  \"remember_card\": \"on\",\n" +
				"  \"remote_ip\": \"157.38.242.7\",\n" +
				"  \"user_agent\": \"Mozilla/5.0\",\n" +
				"  \"accept_header\": \"text/html\",\n" +
				"  \"language\": \"en-US\",\n" +
				"  \"java_enabled\": false,\n" +
				"  \"javascript_enabled\": true,\n" +
				"  \"color_depth\": 24,\n" +
				"  \"utc_offset\": 0,\n" +
				"  \"screen_width\": 1920,\n" +
				"  \"screen_height\": 1080\n" +
				"}";
		
		Response response = RestAssured.given()
				.header("Authorization", "Bearer " + token)
				.header("Accept", "application/json")
				.header("brandId", brandId)
				.contentType(ContentType.JSON)
				.body(requestBody)
				.post(endpoint);

		if (response == null || response.statusCode() != 202) {
			String actualOutcome = "FAIL";
			testResult = "FAIL";
			comment = "S2S call failed (Status: " + (response != null ? response.statusCode() : "NULL") + ")";
			Reporter.log(comment, true);
			ExcelWriteUtility.writeResults2s("s2s_Result", Currency, ExpectedStatus, actualOutcome, comment, purchaseId, PSP, PaymentMethod);
			driver.quit();
			return;
		}

		String callback_url = response.jsonPath().getString("callback_url");

		if (callback_url == null || callback_url.isEmpty()) {
			Reporter.log("callback_url is NULL → Payment failed due to S2S error", true);

			String actualOutcome = "FAIL";
			testResult = "FAIL";
			comment = "callback_url is null (purchaseId: " + purchaseId + ")";
			ExcelWriteUtility.writeResults2s("s2s_Result", Currency, ExpectedStatus, actualOutcome, comment, purchaseId, PSP, PaymentMethod);
			driver.quit();
			return;
		}

		driver.get(callback_url);
		otp.enterOTP(PSP);

		if (PSP.equalsIgnoreCase("payu")) {
			pay.payForPayu(Currency, purchaseId, ExpectedStatus, PaymentMethod);
		}

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		wait.until(ExpectedConditions.urlContains("issucces"));

		String redirectUrl = driver.getCurrentUrl();
		Reporter.log("Redirected URL: " + redirectUrl, true);

		String flag = Arrays.stream(redirectUrl.split("\\?")[1].split("&"))
				.filter(p -> p.startsWith("issucces="))
				.map(p -> p.split("=")[1])
				.findFirst().orElse("");
		
		System.err.println("Payment flag: " + flag);
		
		String actualOutcome;
		
		if (flag.equalsIgnoreCase("false")) {
			actualOutcome = "FAIL";
			
			if (ExpectedStatus.equalsIgnoreCase("Fail")) {
				testResult = "PASS";
				comment = "Payment failed as expected";
			} else {
				testResult = "FAIL";
				comment = "Payment failed but expected to pass";
			}

			Reporter.log(comment, true);
			ExcelWriteUtility.writeResults2s("s2s_Result", Currency, ExpectedStatus, actualOutcome, comment, purchaseId, PSP, PaymentMethod);
			driver.quit();
			return;
			
		} else if (flag.equalsIgnoreCase("true")) {
			actualOutcome = "PASS";
			
			if (ExpectedStatus.equalsIgnoreCase("Pass")) {
				testResult = "PASS";
				comment = "Payment succeeded as expected";
			} else {
				testResult = "FAIL";
				comment = "Payment succeeded but expected to fail";
			}

			Reporter.log(comment, true);
			ExcelWriteUtility.writeResults2s("s2s_Result", Currency, ExpectedStatus, actualOutcome, comment, purchaseId, PSP, PaymentMethod);

		} else {
			actualOutcome = "UNKNOWN";
			testResult = "UNKNOWN";
			comment = "URL does not contain valid issucces parameter (flag: " + flag + ")";

			Reporter.log(comment, true);
			ExcelWriteUtility.writeResults2s("s2s_Result", Currency, ExpectedStatus, actualOutcome, comment, purchaseId, PSP, PaymentMethod);
		}


		mcp.openBrowserForStaging(driver, url);
		lp.login();
		tp.navigateUptoTransaction();
		tp.searchTheTransaction(purchaseId);
		tp.searchButton();
		tp.clickOnTransactionId();
		tp.verifyPurchaseTransactionIDIsNotEmpty();
		
	
		
		driver.quit();
	}
}