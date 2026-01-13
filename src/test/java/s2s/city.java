package s2s;

import org.testng.annotations.Test;

import com.paysecure.Page.loginPage;
import com.paysecure.Page.CashierPage;
import com.paysecure.Page.payu3dPage;
import com.paysecure.Page.pspOTPPage;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;
import com.paysecure.utilities.DataProvidersS2S;
import com.paysecure.utilities.ExcelWriteUtility;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;
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

public class city extends baseClass {

	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	String purchaseId;
	CashierPage mcp;
	transactionPage tp;
	payu3dPage pay;
	pspOTPPage otp;
    String testResult = "";
    String comment = "";
    
    String baseUri;
    
	@BeforeMethod
	public void beforeMethod() throws InterruptedException {
		mcp = new CashierPage(getDriver());
		tp = new transactionPage(getDriver());
		pay = new payu3dPage(getDriver());
		otp= new pspOTPPage();
	}

	@Test(dataProvider ="CityData", dataProviderClass = DataProvidersS2S.class)
	public void purchaseApi(Map<String, String> cityData, Map<String, String> cardData) throws Exception {
		WebDriver driver = baseClass.getDriver();
		
		String City = cityData.getOrDefault("TestData", "");
		String ExpectedStatus = cityData.getOrDefault("Status", "");
		String CardHolder = cardData.getOrDefault("CardholderName", "");
		String CardNumber = cardData.getOrDefault("CardNumber", "");
		String Expiry = cardData.getOrDefault("Expiry", "");
		String CVV = cardData.getOrDefault("CVV", "");
		String PSP = cardData.getOrDefault("PSP", "");
		String PaymentMethod = cardData.getOrDefault("PaymentMethod","");
		String Currency = cardData.getOrDefault("Currency", "");
		System.err.println(City +" "+ExpectedStatus+" "+CardHolder +" "+ CardNumber +" "+ Expiry +" "+ CVV +" "+ PSP);

		if (City.isEmpty() || CardNumber.isEmpty()) {
			Reporter.log("Skipping test - Empty city or card number", true);
			throw new SkipException("Empty test data");
		}

		if (ExpectedStatus == null || ExpectedStatus.trim().isEmpty()) {
			ExpectedStatus = "Pass";
		}

		baseUri = PropertyReader.getPropertyForS2S("baseURI");
		RestAssured.baseURI = baseUri;
		
        String token = PropertyReader.getPropertyForS2S("tokenS2S");
        String BrandID = PropertyReader.getPropertyForS2S("brandIdS2S");
		String price = generateRandomTestData.generateRandomDouble();
		String firstName = generateRandomTestData.generateRandomFirstName();
		String emailId = generateRandomTestData.generateRandomEmail();
		
		String country = "IN";
		String stateCode = "QLD";
		String zipcode = "10001";
		String productname = "Cricket bat";
		String streetAddress = "Lal kothi";

		String requestBody = "{\n" +
		        "  \"client\": {\n" +
		        "    \"full_name\": \""+firstName+"\",\n" +
		        "    \"email\": \""+emailId+"\",\n" +
		        "    \"country\": \""+country+"\",\n" +
		        "    \"city\": \""+City+"\",\n" +
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
		int statusCode = response.getStatusCode();

		if (statusCode == 202) {
		    purchaseId = response.jsonPath().getString("purchaseId");
		    Reporter.log("Purchase created successfully with ID: " + purchaseId, true);
		    
		    Thread.sleep(2000);
		    s2sMethod(City, ExpectedStatus, PSP, CardNumber, Expiry, CVV,PaymentMethod);
		    
		} else if (statusCode == 400 || statusCode == 422) {
            Reporter.log("City rejected by API: " + City, true);
            
            String actualOutcome = "FAIL";
            
            if (ExpectedStatus.equalsIgnoreCase("Fail")) {
                testResult = "PASS";
                comment = "City rejected as expected";
            } else {
                testResult = "FAIL";
                comment = "City rejected but expected to pass ";
            }

            Reporter.log(comment, true);
            ExcelWriteUtility.writeResults2s("s2s_Result", City, ExpectedStatus, actualOutcome, comment, purchaseId != null ? purchaseId : "N/A", PSP,PaymentMethod);
            driver.quit();
            return;
            
        } else {
			Reporter.log("Unexpected response for city: " + City + " -> Status: " + statusCode, true);
			testResult = "FAIL";
			comment = "Unexpected API response (Status: " + statusCode + ")";
			ExcelWriteUtility.writeResults2s("s2s_Result", City, ExpectedStatus, "UNKNOWN", comment, purchaseId != null ? purchaseId : "N/A", PSP,PaymentMethod);
			driver.quit();
			return;
		}
	}

	public void s2sMethod(String City, String ExpectedStatus, String PSP, String CardNumber, String Expiry, String CVV,String PaymentMethod) throws Exception {

	    WebDriver driver = baseClass.getDriver();
	    lp = new loginPage(getDriver());
	    lp.login();

	    if (purchaseId == null || purchaseId.isEmpty()) {
	        Reporter.log("Skipping S2S → purchaseId is NULL (purchase failed)", true);
	        testResult = "FAIL";
	        comment = "Purchase ID is null, cannot proceed with S2S";
	        ExcelWriteUtility.writeResults2s("s2s_Result", City, ExpectedStatus, "FAIL", comment, "N/A", PSP,PaymentMethod);
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
	    String payu = PropertyReader.getPropertyForS2S("payu");

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

	    Response response = null;
	    int maxRetries = 3;
	    int retryCount = 0;
	    boolean success = false;
	    
	    while (retryCount < maxRetries && !success) {
	        try {
	            response = RestAssured.given()
	                    .header("Authorization", "Bearer " + token)
	                    .header("Accept", "application/json")
	                    .header("brandId", brandId)
	                    .contentType(ContentType.JSON)
	                    .body(requestBody)
	                    .when()
	                    .post(endpoint)
	                    .then()
	                    .log().all()
	                    .extract()
	                    .response();
	            
	            if (response.statusCode() == 202) {
	                success = true;
	                Reporter.log("S2S call successful on attempt " + (retryCount + 1), true);
	            } else if (response.statusCode() == 400) {
	                String errorMsg = response.jsonPath().getString("message");
	                if (errorMsg != null && errorMsg.contains("PurchaseId Not found")) {
	                    Reporter.log("Retry " + (retryCount + 1) + ": Purchase not yet available", true);
	                    Thread.sleep(2000);
	                    retryCount++;
	                } else {
	                    break;
	                }
	            } else {
	                break;
	            }
	        } catch (Exception e) {
	            Reporter.log("Exception on retry " + (retryCount + 1) + ": " + e.getMessage(), true);
	            retryCount++;
	            if (retryCount < maxRetries) {
	                Thread.sleep(2000);
	            }
	        }
	    }
	    
	    if (response == null || response.statusCode() != 202) {
	        String actualOutcome = "FAIL";
	        testResult = "FAIL";
	        comment = "S2S call failed (Status: " + (response != null ? response.statusCode() : "NULL") + ")";
	        Reporter.log(comment, true);
	        ExcelWriteUtility.writeResults2s("s2s_Result", City, ExpectedStatus, actualOutcome, comment, purchaseId, PSP,PaymentMethod);
	        driver.quit();
	        return;
	    }

	    String callback_url = response.jsonPath().getString("callback_url");

	    if (callback_url == null || callback_url.isEmpty()) {
	        Reporter.log("callback_url is NULL → Payment failed due to S2S error", true);

	        String actualOutcome = "FAIL";
	        testResult = "FAIL";
	        comment = "callback_url is null (purchaseId: " + purchaseId + ")";
	        ExcelWriteUtility.writeResults2s("s2s_Result", City, ExpectedStatus, actualOutcome, comment, purchaseId, PSP,PaymentMethod);
	        driver.quit();
	        return;
	    }

	    driver.get(callback_url);
	    otp.enterOTP(PSP);
	    
	    
	    if (payu.equalsIgnoreCase("payu")) {
	    	pay.payForPayu(City, purchaseId, ExpectedStatus,PaymentMethod);
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
            ExcelWriteUtility.writeResults2s("s2s_Result", City, ExpectedStatus, actualOutcome, comment, purchaseId, PSP,PaymentMethod);
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
            ExcelWriteUtility.writeResults2s("s2s_Result", City, ExpectedStatus, actualOutcome, comment, purchaseId, PSP,PaymentMethod);

        } else {
            actualOutcome = "UNKNOWN";
            testResult = "UNKNOWN";
            comment = "URL does not contain valid issucces parameter (flag: " + flag + ")";

            Reporter.log(comment, true);
            ExcelWriteUtility.writeResults2s("s2s_Result", City, ExpectedStatus, actualOutcome, comment, purchaseId, PSP,PaymentMethod);
        }
        
		mcp.openBrowserForStaging(driver, url);
		lp.login();
		tp.navigateUptoTransaction();
		tp.searchTheTransaction(purchaseId);
		tp.searchButton();
		tp.clickOnTransactionId();
        tp.verifyPurchaseTransactionIDIsNotEmpty();
        Thread.sleep(3000);
        driver.quit();
	}
}