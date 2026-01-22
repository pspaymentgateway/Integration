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

public class currency extends baseClass {

	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	String purchaseId;
	CashierPage mcp;
	transactionPage tp;
	pspOTPPage otp;
	payu3dPage pay;
    String status = "";
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
	public void purchaseApi(Map<String, String> currencyData, Map<String, String> cardData) throws Exception {
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
		String PaymentMethod = cardData.getOrDefault("PaymentMethod","");
		//
		System.err.println(Currency +" "+ExpectedStatus+" "+CardHolder +" "+ CardNumber +" "+ Expiry +" "+ CVV +" "+ PSP+" "+PaymentMethod);

		if (Currency.isEmpty() || CardNumber.isEmpty()) {
			Reporter.log("Skipping test - Empty city or card number", true);
			throw new SkipException("Empty test data");
		}

		if (ExpectedStatus == null || ExpectedStatus.trim().isEmpty()) {
			ExpectedStatus = "Pass";
		}
		
        String token = PropertyReader.getPropertyForS2S("tokenS2S");
        String BrandID = PropertyReader.getPropertyForS2S("brandIdS2S");
		String price = generateRandomTestData.generateRandomDoublePrice();
		

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
            status = "Fail";
            comment = "PASS → currency rejected correctly " + Currency;

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("s2s_Result", Currency,ExpectedStatus, status, comment, purchaseId,PSP,PaymentMethod);
            driver.quit();
            return; 
        } else {
			Reporter.log("Unexpected response for currency: " + Currency + " -> " + response.statusCode(), true);
		}
	}

	public void s2sMethod(String Currency, String ExpectedStatus, String PSP, String CardNumber, String Expiry, String CVV,String PaymentMethod) throws Exception {

	    WebDriver driver = baseClass.getDriver();
	    lp = new loginPage(getDriver());
	    lp.login();

	    if (purchaseId == null || purchaseId.isEmpty()) {
	        Reporter.log("Skipping S2S → purchaseId is NULL (purchase failed)", true);
	        return;
	    }

	    // CRITICAL FIX: Reset RestAssured.baseURI to ensure proper URL construction
	    RestAssured.baseURI = baseUri;
	    
	    // Construct S2S endpoint
		String endpoint = "/api/v1/p/" + purchaseId + "?s2s=true";
		System.err.println("S2S Endpoint: " + endpoint);
		System.err.println("Full URL: " + baseUri + endpoint);

		// OPTION 1: Use same token as purchase creation (RECOMMENDED)
		String url=PropertyReader.getPropertyForS2S("url");
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

	    // Add retry logic for S2S call
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
	                    Thread.sleep(2000); // Wait 2 seconds before retry
	                    retryCount++;
	                } else {
	                    // Different error, don't retry
	                    break;
	                }
	            } else {
	                // Other error, don't retry
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
	        status = "FAIL";
	        comment = "S2S call failed with status: " + (response != null ? response.statusCode() : "NULL");
	        Reporter.log(comment, true);
	        ExcelWriteUtility.writeResults2s("s2s_Result", Currency,ExpectedStatus, status, comment, purchaseId,PSP,PaymentMethod);
	        driver.quit();
	        return;
	    }

	    // Validate callback URL
	    String callback_url = response.jsonPath().getString("callback_url");

	    if (callback_url == null || callback_url.isEmpty()) {
	        Reporter.log("callback_url is NULL → Payment failed due to S2S error", true);

	        status = "FAIL";
	        comment = "callback_url null for currency " + purchaseId;
	        ExcelWriteUtility.writeResults2s("s2s_Result", Currency,ExpectedStatus, status, comment, purchaseId,PSP,PaymentMethod);
	        driver.quit();
	        return;
	    }

	    driver.get(callback_url);
	    if(payu.equalsIgnoreCase("payu")) {
	    	pay.payForPayu(Currency,purchaseId,ExpectedStatus,PaymentMethod);
	    }

	    otp.enterOTP(PSP);
	    
	    Thread.sleep(7000);
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("issucces"));

        String redirectUrl = driver.getCurrentUrl();
        Reporter.log("Redirected URL: " + redirectUrl, true);

        String flag = Arrays.stream(redirectUrl.split("\\?")[1].split("&"))
                .filter(p -> p.startsWith("issucces="))
                .map(p -> p.split("=")[1])
                .findFirst().orElse("");
        System.err.println(flag);
        String actualOutcome;
        if (flag.equalsIgnoreCase("false")) {
        	actualOutcome = "FAIL";
            if (ExpectedStatus != null && ExpectedStatus.equalsIgnoreCase("Fail")) {
                status = "PASS"; // Test passed - failure was expected
                comment = "PASS → Payment failed as expected";
            } else {
                status = "FAIL"; // Test failed - expected success but got failure
                comment = "FAIL → Payment failed but expected to pass";
            }

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("s2s_Result", Currency,ExpectedStatus, actualOutcome, comment, purchaseId,PSP,PaymentMethod);
            driver.quit();
            return;
        }
        else if (flag.equalsIgnoreCase("true")) {
            actualOutcome = "PASS";
            
            // Payment succeeded - check if success was expected
            if (ExpectedStatus != null && ExpectedStatus.equalsIgnoreCase("Pass")) {
                status = "PASS"; // Test passed - success was expected
                comment = "PASS → Payment succeeded as expected";
            } else {
                status = "FAIL"; // Test failed - expected failure but got success
                comment = "FAIL → Payment succeeded but expected to fail";
            }
            

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("s2s_Result", Currency,ExpectedStatus, actualOutcome, comment, purchaseId,PSP,PaymentMethod);

        }
        else {
        	 actualOutcome = "UNKNOWN";
            status = "UNKNOWN";
            comment = "URL does not contain expected issucces parameter";

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("s2s_Result", Currency,ExpectedStatus, actualOutcome, comment, purchaseId,PSP,PaymentMethod);
        }
        
		mcp.openBrowserForStaging(driver,url);
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