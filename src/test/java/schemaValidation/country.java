package schemaValidation;

import org.testng.annotations.Test;

import com.paysecure.Page.loginPage;
import com.paysecure.Page.matrixCashierPage;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;
import com.paysecure.utilities.ExcelWriteUtility;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;
import com.paysecure.utilities.jsonProvider;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;

public class country extends baseClass {
	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	String purchaseId;
	matrixCashierPage mcp;
	transactionPage tp;
	
    String status = "";
    String comment = "";
	
	
	  @BeforeMethod
	  public void beforeMethod() throws InterruptedException {
			lp = new loginPage(getDriver());
			lp.login();
			mcp=new matrixCashierPage(getDriver());
			tp=new transactionPage(getDriver());
	  }
	  
	  
	  
  @Test (dataProvider ="country", dataProviderClass = jsonProvider.class)
  public void validationForCountryField(String Country,String cardHolder, String cardNumber, String expiry, String cvv,String runFlag,String PSP) {
      WebDriver driver=baseClass.getDriver();
      Reporter.log("Email test case will run for this PSP :- "+PSP, true);
      Reporter.log("Email test case will run for this runflag:- "+runFlag, true);
		String baseUri = PropertyReader.getProperty("baseURI");
		RestAssured.baseURI =baseUri;
			String brandId = PropertyReader.getProperty("brandId");
			String token = PropertyReader.getProperty("token");
			String price = generateRandomTestData.generateRandomDouble();
			String currency =PropertyReader.getProperty("currency");
			String paymentMethod=PropertyReader.getProperty("paymentMethod");
			String firstName = generateRandomTestData.generateRandomFirstName();
			String emailId = generateRandomTestData.generateRandomEmail();
			String master=PropertyReader.getProperty("Master");
			String visa=PropertyReader.getProperty("Visa");
			String city="Paris";
			String requestBody =
					"{\n" +
			        "  \"client\": {\n" +
			        "    \"full_name\": \""+firstName+"\",\n" +
			        "    \"email\": \""+emailId+"\",\n" +
			        "    \"country\": \""+Country+"\",\n" +
			        "    \"city\": \""+city+"\",\n" +
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
			        "        \"price\":"+ price + "\n" +
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

			Reporter.log("Country: " + Country + " → Status: " + response.getStatusCode(), true);
			Reporter.log("Response Body: " + response.getBody().asPrettyString(), true);
	        
	        
	        checkoutUrl = response.jsonPath().getString("checkout_url");
	        
	        
	        if (response.statusCode() == 202) {
	            Reporter.log("Country accepted by API: " + Country, true);
	        }else if (response.statusCode() == 400 || response.statusCode() == 422) {
	            Reporter.log("Country rejected by API: " + Country, true);
	            status = "PASS";
	            comment = "PASS → Country rejected correctly   " + Country;

	            Reporter.log(comment, true);

	            ExcelWriteUtility.writeResult("Country_Result", Country, status, comment,purchaseId);
	            driver.quit();
	            return; 
	        }  else {
	            Reporter.log("Unexpected response for Country: " + Country + " -> " + response.statusCode(), true);
	        }
	    
	        try {

	            //SUCCESS CASE (202 + checkout_url exists)
	            if (response.statusCode()  == 202 && checkoutUrl != null && !checkoutUrl.isEmpty()) {

	                Reporter.log("API success → proceeding with full flow", true);

	                // Payment
	                driver.get(checkoutUrl);
	       
	                
	                mcp.userEnterCardInformationForPayment(cardHolder, cardNumber, expiry, cvv);
	                mcp.clickOnPay();
	                
	                if (mcp.isCardNumberInvalid()) {
	                    Reporter.log("Invalid card number → Luhn check failed", true);
	                    driver.quit();
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

	                    ExcelWriteUtility.writeResult("Country_Result", Country, status, comment,purchaseId);
	                    driver.quit();
	                    return;
	                }
	                else if (flag.equalsIgnoreCase("true")) {
	                    status = "PASS";
	                    comment = "Payment Successfully";

	                    Reporter.log(comment, true);

	                    ExcelWriteUtility.writeResult("Country_Result", Country, status, comment,purchaseId);

	                }
	                else {
	                    status = "UNKNOWN";
	                    comment = "URL does not contain expected issucces parameter";

	                    Reporter.log(comment, true);

	                    ExcelWriteUtility.writeResult("Country_Result", Country, status, comment,purchaseId);


	                }
	                // Login + Transaction check
	                mcp.openBrowserForStaging(driver,baseUri);
	                lp.login();
	                tp.navigateUptoTransaction();
	                tp.searchTheTransaction(purchaseId);
	                tp.searchButton();
	                tp.clickOnTransactionId();
                    tp.verifyPurchaseTransactionIDIsNotEmpty();
	                Thread.sleep(4000);

	                return; // PASS
	            }


	        } catch (Exception e) {
	          // System.out.println("Unexpected error: " + e.getMessage());
	        	  Assert.fail("Unexpected error: " + e.getMessage()); // keep this
	        } 
	        finally {
	            if (driver != null) driver.quit();
	        }
  }


}
