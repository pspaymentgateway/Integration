package purchase;

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

public class zipcode extends baseClass{
	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	String purchaseId;
	matrixCashierPage mcp;
	transactionPage tp;
	payu3dPage pay;
    String status = "";
    String comment = "";
	
	  @BeforeMethod
	  public void beforeMethod() throws InterruptedException {
			lp = new loginPage(getDriver());
			lp.login();
			mcp=new matrixCashierPage(getDriver());
			tp=new transactionPage(getDriver()); 
			pay = new payu3dPage(getDriver());
	  }
	  
  @Test(dataProvider ="zipCodeData", dataProviderClass = jsonProvider.class)
  public void validationForZipcodeField(String zipcode,String cardHolder, String cardNumber, String expiry, String cvv,String runFlag,String PSP) {
		WebDriver driver=baseClass.getDriver();
        Reporter.log("streetAddres test case will run for this PSP :- "+PSP, true);
        Reporter.log("streetAddres test case will run for this runflag:- "+runFlag, true);
		 String baseUri = PropertyReader.getPropertyForPurchase("baseURI");
		RestAssured.baseURI =baseUri;
		String brandId = PropertyReader.getPropertyForPurchase("brandId");
	
		String token = PropertyReader.getPropertyForPurchase("token");
		String price = generateRandomTestData.generateRandomDouble();
		String currency =PropertyReader.getPropertyForPurchase("currency");
		String paymentMethod=PropertyReader.getPropertyForPurchase("paymentMethods");
		String firstName = generateRandomTestData.generateRandomFirstName();
		String emailId = generateRandomTestData.generateRandomEmail();
		String master=PropertyReader.getPropertyForPurchase("Master");
		String visa=PropertyReader.getPropertyForPurchase("Visa");
		String payu = PropertyReader.getPropertyForS2S("payu");
		String country="IN";
		String city = "Paris";
		String stateCode="QLD";
		String streetAddress = "Main gate";
		
		String productname="Cricket bat";
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

		Reporter.log("zipcode: " + zipcode + " → Status: " + response.getStatusCode(), true);
		Reporter.log("Response Body: " + response.getBody().asPrettyString(), true);
        
        
        checkoutUrl = response.jsonPath().getString("checkout_url");
        
        
        if (response.statusCode() == 202) {
            Reporter.log("zipcode accepted by API: " + zipcode, true);
        }else if (response.statusCode() == 400 || response.statusCode() == 422) {
            Reporter.log("zipcode rejected by API: " + zipcode, true);
            status = "PASS";
            comment = "PASS → zipcode rejected correctly   " + zipcode;

            Reporter.log(comment, true);

            ExcelWriteUtility.writeResult("zipcode_Result", zipcode, status, comment,purchaseId);
            driver.quit();
            return; 
        }  else {
            Reporter.log("Unexpected response for zipcode: " + zipcode + " -> " + response.statusCode(), true);
        }
    
        try {


            //SUCCESS CASE (202 + checkout_url exists)
            if (response.statusCode()  == 202 && checkoutUrl != null && !checkoutUrl.isEmpty()) {

                Reporter.log("API success -> proceeding with full flow", true);

                // Payment
                driver.get(checkoutUrl);

                mcp.userEnterCardInformationForPayment(cardHolder, cardNumber, expiry, cvv);
                mcp.clickOnPay();
                
                if(payu.equalsIgnoreCase("payu")) {
        	    	pay.payForPayu(currency,purchaseId);
        	    }
                
                
                if (mcp.isCardNumberInvalid()) {
             	   status = "FAIL";
                   comment = "Payment Failed Cause Of Luhn ";
              Reporter.log("Invalid card number → Luhn check failed", true);
              ExcelWriteUtility.writeResult("Zipcode_Result", zipcode, status, comment,purchaseId);
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

                    ExcelWriteUtility.writeResult("Zipcode_Result", zipcode, status, comment,purchaseId);
                    driver.quit();
                    return;
                }
                else if (flag.equalsIgnoreCase("true")) {
                    status = "PASS";
                    comment = "Payment Successfully";

                    Reporter.log(comment, true);

                    ExcelWriteUtility.writeResult("Zipcode_Result", zipcode, status, comment,purchaseId);

                }
                else {
                    status = "UNKNOWN";
                    comment = "URL does not contain expected issucces parameter";

                    Reporter.log(comment, true);

                    ExcelWriteUtility.writeResult("Zipcode_Result", zipcode, status, comment,purchaseId);


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
