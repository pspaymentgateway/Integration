package s2s;

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
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;

public class productName extends baseClass {

	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	String purchaseId;
	matrixCashierPage mcp;
	transactionPage tp;
	payu3dPage pay;
    String status = "";
    String comment = "";
    String productname;
    
    // Store base URI to reuse in S2S call
    String baseUri;
    
	@BeforeMethod
	public void beforeMethod() throws InterruptedException {
		mcp = new matrixCashierPage(getDriver());
		tp = new transactionPage(getDriver());
		pay=new payu3dPage(getDriver());
	}

	
	@Test(dataProvider ="productNameProvider", dataProviderClass = DataProviders.class)
	public void purchaseApi(String productname) throws Exception {
		WebDriver driver = baseClass.getDriver();
		this.productname = productname;
		
		// Store baseUri for later use
		baseUri = PropertyReader.getPropertyForS2S("baseURI");
		RestAssured.baseURI = baseUri;
		
        String token = PropertyReader.getPropertyForS2S("tokenS2S");
        String BrandID = PropertyReader.getPropertyForS2S("brandIdS2S");
		String price = generateRandomTestData.generateRandomDouble();
		String currency = PropertyReader.getPropertyForS2S("currencyS2S");
		String paymentMethod = PropertyReader.getPropertyForS2S("paymentMethodS2S");
		String firstName = generateRandomTestData.generateRandomFirstName();
		String emailId=generateRandomTestData.generateRandomEmail();
		String country = "IN";
		String city = "Paris";
		String stateCode = "QLD";
		
		String zipcode="10001";
		
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
		        "    \"currency\": \""+currency+"\",\n" +
		        "    \"products\": [\n" +
		        "      {\n" +
		        "        \"name\": \""+productname+"\",\n" +
		        "        \"price\":"+ price + "\n" +
		        "      }\n" +
		        "    ]\n" +
		        "  },\n" +
		        "  \"paymentMethod\": \""+paymentMethod+"\",\n" +
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
		

		if (response.statusCode() == 202) {
		    purchaseId = response.jsonPath().getString("purchaseId");
		    Reporter.log("Purchase created successfully with ID: " + purchaseId, true);
		    
		    // Add a small delay to ensure purchase is fully persisted
		    Thread.sleep(2000);
		    
		    s2sMethod();
		}

		if (response.statusCode() == 202) {
			Reporter.log("productname accepted by API: " + productname, true);
		} else if (response.statusCode() == 400 || response.statusCode() == 422) {
            Reporter.log("productname rejected by API: " + productname, true);
            status = "PASS";
            comment = "PASS → productname rejected correctly " + productname;

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("Productname_Result", productname, status, comment, purchaseId);
            driver.quit();
            return; 
        } else {
			Reporter.log("Unexpected response for productname: " + productname + " -> " + response.statusCode(), true);
		}
	}

	public void s2sMethod() throws Exception {

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
	    String currency = PropertyReader.getPropertyForS2S("currency");
	    String cardNumber = PropertyReader.getPropertyForS2S("cardNumber");
	    String mmyy = PropertyReader.getPropertyForS2S("mmyy");
	    String cvv = PropertyReader.getPropertyForS2S("cvv");

	    String requestBody =
	    		"{\n" +
	    		"  \"cardholder_name\": \"Rahul Agarwal\",\n" +
	    		"  \"card_number\": \"" + cardNumber + "\",\n" +
	    		"  \"expires\": \"" + mmyy + "\",\n" +
	    		"  \"cvc\": \"" + cvv + "\",\n" +
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
	        ExcelWriteUtility.writeResults2s("Productname_Result", productname, status, comment, purchaseId);
	        driver.quit();
	        return;
	    }

	    // Validate callback URL
	    String callback_url = response.jsonPath().getString("callback_url");

	    if (callback_url == null || callback_url.isEmpty()) {
	        Reporter.log("callback_url is NULL → Payment failed due to S2S error", true);

	        status = "FAIL";
	        comment = "callback_url null for currency " + purchaseId;
	        ExcelWriteUtility.writeResults2s("Productname_Result", productname, status, comment, purchaseId);
	        driver.quit();
	        return;
	    }

	    driver.get(callback_url);
	    if(payu.equalsIgnoreCase("payu")) {
	    	pay.payForPayu(currency,purchaseId);
	    }
	    
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

        if (flag.equalsIgnoreCase("false")) {
            status = "FAIL";
            comment = "Payment Failed";

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("Productname_Result", productname, status, comment, purchaseId);
            driver.quit();
            return;
        }
        else if (flag.equalsIgnoreCase("true")) {
            status = "PASS";
            comment = "Payment Successfully";
            

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("Productname_Result", productname, status, comment, purchaseId);

        }
        else {
            status = "UNKNOWN";
            comment = "URL does not contain expected issucces parameter";

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResults2s("Productname_Result", productname, status, comment, purchaseId);
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