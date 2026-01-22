package purchase;

import io.restassured.http.ContentType;

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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.paysecure.Page.loginPage;
import com.paysecure.Page.CashierPage;
import com.paysecure.Page.payu3dPage;
import com.paysecure.Page.pspOTPPage;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;
import com.paysecure.utilities.ExcelWriteUtility;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;
import com.paysecure.utilities.jsonProvider;

import io.restassured.response.Response;

import io.restassured.RestAssured;

public class email extends baseClass {
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

	@BeforeMethod
	public void beforeMethod() throws InterruptedException {
		lp = new loginPage(getDriver());
		lp.login();
		mcp = new CashierPage(getDriver());
		tp = new transactionPage(getDriver());
		pay = new payu3dPage(getDriver());
		otp=new pspOTPPage();
	}

	
	@Test(dataProvider = "EmailData", dataProviderClass = DataProviders.class)
	public void Purchase(Map<String, String> emailData, Map<String, String> cardData) throws InterruptedException {

		String Email = emailData.getOrDefault("TestData", "");
		String ExpectedStatus = emailData.getOrDefault("Status", "");
		
		String CardHolder = cardData.getOrDefault("CardholderName", "");
		String CardNumber = cardData.getOrDefault("CardNumber", "");
		String Expiry = cardData.getOrDefault("Expiry", "");
		String CVV = cardData.getOrDefault("CVV", "");
		String PSP = cardData.getOrDefault("PSP", "");
		String cardRunFlag = cardData.getOrDefault("RunFlag", "");
		String PaymentMethod=cardData.getOrDefault("PaymentMethod","");
		String Currency=cardData.getOrDefault("Currency", "");

		System.err.println(Email +" "+ExpectedStatus+" "+CardHolder +" "+ CardNumber +" "+ Expiry +" "+ CVV +" "+ PSP);

		//Validate data is not empty
		if (Email.isEmpty() || CardNumber.isEmpty()) {
			Reporter.log("Skipping test - Empty email or card number", true);
			throw new SkipException("Empty test data");
		}

		WebDriver driver = baseClass.getDriver();
		Reporter.log("Email test case will run for this PSP: " + PSP, true);
		Reporter.log("Testing Email: " + Email + " with Card: " + CardNumber, true);
		String baseUri = PropertyReader.getPropertyForPurchase("baseURI");
		RestAssured.baseURI = baseUri;
		String brandId = PropertyReader.getPropertyForPurchase("brandId");
		String token = PropertyReader.getPropertyForPurchase("token");
		String price = generateRandomTestData.generateRandomDoublePrice();
      	String firstName = generateRandomTestData.generateRandomFirstName();
		String payu = PropertyReader.getPropertyForS2S("payu");
		String country = "IN";
		String city = "Paris";
		String stateCode = "QLD";
		String streetAddress = "Main gate";
		String zipcode = "20001";
		String productname = "Cricket bat";

		System.out.println("Brand ID :- " +brandId);
		System.out.println("Token Id :- "+token);
		System.err.println(baseUri);
		String requestBody = "{\n" +
		   "  \"client\": {\n" +
		   "    \"full_name\": \"" + firstName + "\",\n" +
		   "    \"email\": \"" + Email + "\",\n" +  // Changed from emailId to Email
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
		Response response = RestAssured.given().header("Authorization", "Bearer " + token).contentType(ContentType.JSON)
				.body(requestBody).when().post("api/v1/purchases").then().extract().response();

		checkoutUrl = response.jsonPath().getString("checkout_url");
		purchaseId = response.jsonPath().getString("purchaseId");

		Reporter.log("Email: " + Email + " → Status: " + response.getStatusCode(), true); // Changed
		Reporter.log("Response Body: " + response.getBody().asPrettyString(), true);

		checkoutUrl = response.jsonPath().getString("checkout_url");
		System.err.println(checkoutUrl);

		if (response.statusCode() == 202) {
			Reporter.log("Email accepted by API: " + Email, true);
		} else if (response.statusCode() == 400 || response.statusCode() == 422) {
			Reporter.log("Email rejected by API: " + Email, true);

			// Email was rejected - check if this was expected
			if (ExpectedStatus != null && ExpectedStatus.equalsIgnoreCase("Fail")) {
				status = "PASS"; // Test passed because rejection was expected
				comment = "PASS → Email rejected correctly as expected: " + Email;
			} else {
				status = "FAIL"; // Test failed because rejection was NOT expected
				comment = "FAIL → Email was rejected but expected to pass: " + Email;
			}

			Reporter.log(comment, true);
			ExcelWriteUtility.writeResult("Purchase_Result", Email, ExpectedStatus, "FAIL", comment, purchaseId,
					PSP,PaymentMethod);
			return;
		} else {
			Reporter.log("Unexpected response for email: " + Email + " -> " + response.statusCode(), true);
		}

		try {
			if (response.statusCode() == 202 && checkoutUrl != null && !checkoutUrl.isEmpty()) {
				Reporter.log("API success → proceeding with full flow", true);
				System.err.println(checkoutUrl);

				driver.get(checkoutUrl);

				mcp.userEnterCardInformationForPayment(CardHolder, CardNumber, Expiry, CVV);
				mcp.clickOnPay();

				if (payu.equalsIgnoreCase("payu")) {
					pay.payForPayu(Currency, purchaseId, ExpectedStatus,PaymentMethod);
				}
                
				otp.enterOTP(PSP);
				if (mcp.isCardNumberInvalid()) {

					status = "FAIL";
					comment = "Payment Failed Cause Of Luhn ";
					Reporter.log("Invalid card number → Luhn check failed", true);

					Reporter.log(comment, true);
					ExcelWriteUtility.writeResult("Purchase_Result", Email, ExpectedStatus, "Fail", comment,
							purchaseId, PSP,PaymentMethod);
					driver.quit();
					return;
				}

				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
				wait.until(ExpectedConditions.urlContains("issucces"));

				
				String redirectUrl = driver.getCurrentUrl();
				Reporter.log("Redirected URL: " + redirectUrl, true);

				String flag = Arrays.stream(redirectUrl.split("\\?")[1].split("&"))
						.filter(p -> p.startsWith("issucces=")).map(p -> p.split("=")[1]).findFirst().orElse("");

				String actualOutcome;

				if (flag.equalsIgnoreCase("false")) {
					actualOutcome = "Fail";

					// Check if failure was expected
					if (ExpectedStatus != null && ExpectedStatus.equalsIgnoreCase("Fail")) {
						status = "PASS"; // Test passed - failure was expected
						comment = "PASS → Payment failed as expected";
					} else {
						status = "FAIL"; // Test failed - expected success but got failure
						comment = "FAIL → Payment failed but expected to pass";
					}

					Reporter.log(comment, true);
					ExcelWriteUtility.writeResult("Purchase_Result", Email, ExpectedStatus, actualOutcome, comment,
							purchaseId, PSP,PaymentMethod);
					return;

				} else if (flag.equalsIgnoreCase("true")) {
					actualOutcome = "Pass";

					// Check if success was expected
					if (ExpectedStatus != null && ExpectedStatus.equalsIgnoreCase("Pass")) {
						status = "Pass"; // Test passed - success was expected
						comment = "Pass → Payment succeeded as expected";
					} else {
						status = "Fail"; // Test failed - expected failure but got success
						comment = "Fail → Payment succeeded but expected to fail";
					}

					Reporter.log(comment, true);
					ExcelWriteUtility.writeResult("Purchase_Result", Email, ExpectedStatus, actualOutcome, comment,
							purchaseId, PSP,PaymentMethod);

				} else {
					actualOutcome = "UNKNOWN";
					status = "FAIL";
					comment = "FAIL → URL does not contain expected issucces parameter";

					Reporter.log(comment, true);
					ExcelWriteUtility.writeResult("Purchase_Result", Email, ExpectedStatus, actualOutcome, comment,
							purchaseId, PSP,PaymentMethod);
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
        	status = "FAIL";
            comment = "FAIL → Exception occurred during payment flow: " + e.getMessage();

            Reporter.log(comment, true);

            // Write failure into Excel
            ExcelWriteUtility.writeResult(
                    "Purchase_Result",   // Sheet name
                    Email,                // Test data
                    ExpectedStatus,      // Expected
                    "FAIL",              // Actual outcome
                    comment,             // Comment
                    purchaseId,
                    PaymentMethod,         // Purchase ID (may be null)
                    PSP                  // PSP name
            );

            // Fail the test in TestNG
            Assert.fail(comment, e);
		} finally {
			//if (driver != null)
				//driver.quit();
System.out.println("driver closed");
		}

	}
}
