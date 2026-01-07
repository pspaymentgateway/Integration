package purchase;

import org.testng.annotations.Test;
import com.paysecure.Page.loginPage;
import com.paysecure.Page.CashierPage;
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
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
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
    String status = "";
    String comment = "";

    @BeforeMethod
    public void beforeMethod() throws InterruptedException {
        lp = new loginPage(getDriver());
        lp.login();
        mcp = new CashierPage(getDriver());
        tp = new transactionPage(getDriver());
        pay = new payu3dPage(getDriver());
    }

    @Test(dataProvider ="CityProvider", dataProviderClass = DataProviders.class)
    public void validationForCityField(Map<String, String> cityData, Map<String, String> cardData) {
        WebDriver driver = baseClass.getDriver();
        //
		String City = cityData.getOrDefault("TestData", "");
		String ExpectedStatus = cityData.getOrDefault("Status", "");
		String emailRunFlag = cityData.getOrDefault("RunFlag", "");

		//
		String CardHolder = cardData.getOrDefault("CardholderName", "");
		String CardNumber = cardData.getOrDefault("CardNumber", "");
		String Expiry = cardData.getOrDefault("Expiry", "");
		String CVV = cardData.getOrDefault("CVV", "");
		String PSP = cardData.getOrDefault("PSP", "");
		String cardRunFlag = cardData.getOrDefault("RunFlag", "");
		System.err.println(City +" "+ExpectedStatus+" "+CardHolder +" "+ CardNumber +" "+ Expiry +" "+ CVV +" "+ PSP);

		//Validate data is not empty
		if (City.isEmpty() || CardNumber.isEmpty()) {
			Reporter.log("Skipping test - Empty email or card number", true);
			throw new SkipException("Empty test data");
		}

		
		Reporter.log("Email test case will run for this PSP: " + PSP, true);
		Reporter.log("Testing Email: " + City + " with Card: " + CardNumber, true);
        Reporter.log("City test case will run for this PSPCardsIntegrations :- " + PSP, true);
        Reporter.log("City test case will run for this runflag:- " + cardRunFlag, true);
        
        String baseUri = PropertyReader.getPropertyForPurchase("baseURI");
        RestAssured.baseURI = baseUri;
        String brandId = PropertyReader.getPropertyForPurchase("brandId");
        String token = PropertyReader.getPropertyForPurchase("token");
        String price = generateRandomTestData.generateRandomDouble();
        String currency = PropertyReader.getPropertyForPurchase("currency");
        String paymentMethod = PropertyReader.getPropertyForPurchase("paymentMethods");
        String firstName = generateRandomTestData.generateRandomFirstName();
        String emailId = generateRandomTestData.generateRandomEmail();
        String payu = PropertyReader.getPropertyForS2S("payu");
        String easybuzz = PropertyReader.getPropertyForPurchase("easybuzz");
        String zaakpay = PropertyReader.getPropertyForPurchase("zaakpayNetBanking");
        
        String country = "IN";
        String city = City;
        String stateCode = "QLD";
        String streetAddress = "Main gate";
        String zipcode = "20001";
        String productname = "Cricket bat";
        
        String requestBody = "{\n" +
                "  \"client\": {\n" +
                "    \"full_name\": \"" + firstName + "\",\n" +
                "    \"email\": \"" + emailId + "\",\n" +
                "    \"country\": \"" + country + "\",\n" +
                "    \"city\": \"" + city + "\",\n" +
                "    \"stateCode\": \"" + stateCode + "\",\n" +
                "    \"street_address\": \"" + streetAddress + "\",\n" +
                "    \"zip_code\": \"" + zipcode + "\",\n" +
                "    \"phone\": \"+1111111111\"\n" +
                "  },\n" +
                "  \"purchase\": {\n" +
                "    \"currency\": \"" + currency + "\",\n" +
                "    \"products\": [\n" +
                "      {\n" +
                "        \"name\": \"" + productname + "\",\n" +
                "        \"price\":" + price + "\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"paymentMethod\": \"" + paymentMethod + "\",\n" +
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

        checkoutUrl = response.jsonPath().getString("checkout_url");
        purchaseId = response.jsonPath().getString("purchaseId");

        Reporter.log("City: " + City + " → Status: " + response.getStatusCode(), true);
        Reporter.log("Response Body: " + response.getBody().asPrettyString(), true);

        if (response.statusCode() == 202) {
            Reporter.log("City accepted by API: " + City, true);
        } else if (response.statusCode() == 400 || response.statusCode() == 422) {
            Reporter.log("City rejected by API: " + City, true);
            
            // City was rejected - check if this was expected
            if (ExpectedStatus != null && ExpectedStatus.equalsIgnoreCase("Fail")) {
                status = "PASS"; // Test passed because rejection was expected
                comment = "PASS → City rejected correctly as expected: " + City;
            } else {
                status = "FAIL"; // Test failed because rejection was NOT expected
                comment = "FAIL → City was rejected but expected to pass: " + City;
            }

            Reporter.log(comment, true);
            ExcelWriteUtility.writeResult("Purchase_Result", City, ExpectedStatus, "FAIL", comment, purchaseId,PSP);
            driver.quit();
            return;
        } else {
            Reporter.log("Unexpected response for City: " + City + " -> " + response.statusCode(), true);
        }

        try {
            if (response.statusCode() == 202 && checkoutUrl != null && !checkoutUrl.isEmpty()) {
                Reporter.log("API success → proceeding with full flow", true);

                driver.get(checkoutUrl);

                mcp.userEnterCardInformationForPayment(CardHolder, CardNumber, Expiry, CVV);
                mcp.clickOnPay();

                if (payu.equalsIgnoreCase("payu")) {
                 
                }
                
                if(easybuzz.equalsIgnoreCase("easybuzz")) {
        	    	tp.enterOTpEasyBuzz();
        	    }
                
        	    if(zaakpay.equalsIgnoreCase("zaakpayNetBanking")) {
        	    	mcp.zaakPayOtpEnterSuccessOrFailure();
        	    }

                if (mcp.isCardNumberInvalid()) {
  
     			   status = "FAIL";
                   comment = "Payment Failed Cause Of Luhn ";
             Reporter.log("Invalid card number → Luhn check failed", true);

                    Reporter.log("Invalid card number → Luhn check failed", true);
                    ExcelWriteUtility.writeResult("Purchase_Result", City, ExpectedStatus, "FAIL", comment, purchaseId,PSP);
                    driver.quit();
                    return;
                }

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                wait.until(ExpectedConditions.urlContains("issucces"));

                String redirectUrl = driver.getCurrentUrl();
                Reporter.log("Redirected URL: " + redirectUrl, true);

                String flag = Arrays.stream(redirectUrl.split("\\?")[1].split("&"))
                        .filter(p -> p.startsWith("issucces="))
                        .map(p -> p.split("=")[1])
                        .findFirst().orElse("");
                
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
                    ExcelWriteUtility.writeResult("Purchase_Result", City, ExpectedStatus, actualOutcome, comment, purchaseId,PSP);
                    driver.quit();
                    return;
                    
                } else if (flag.equalsIgnoreCase("true")) {
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
                    ExcelWriteUtility.writeResult("Purchase_Result", City, ExpectedStatus, actualOutcome, comment, purchaseId,PSP);

                } else {
                    actualOutcome = "UNKNOWN";
                    status = "FAIL";
                    comment = "FAIL → URL does not contain expected issucces parameter";

                    Reporter.log(comment, true);
                    ExcelWriteUtility.writeResult("Purchase_Result", City, ExpectedStatus, actualOutcome, comment, purchaseId,PSP);
                }

                // Continue with transaction verification only if payment succeeded
                if (flag.equalsIgnoreCase("true")) {
                    mcp.openBrowserForStaging(driver, baseUri);
                    lp.login();
                    tp.navigateUptoTransaction();
                    tp.searchTheTransaction(purchaseId);
                    tp.searchButton();
                    tp.clickOnTransactionId();
                    tp.verifyPurchaseTransactionIDIsNotEmpty();
                    Thread.sleep(4000);
                }
                
                return;
            }

        } catch (Exception e) {
            Assert.fail("Unexpected error: " + e.getMessage());
        } finally {
            if (driver != null) driver.quit();
        }
    }
}