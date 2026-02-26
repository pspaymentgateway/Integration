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
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.paysecure.Page.loginPage;
import com.paysecure.Page.CashierPage;
import com.paysecure.Page.RouteManager;
import com.paysecure.Page.payu3dPage;
import com.paysecure.Page.pspOTPPage;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;
import com.paysecure.utilities.ExcelWriteUtility;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;
import com.paysecure.utilities.testData_CreateRoll;

import io.restassured.response.Response;

import io.restassured.RestAssured;

public class email extends baseClass {

	loginPage lp;
	String checkoutUrl;
	String purchaseId;
	CashierPage mcp;
	transactionPage tp;
	pspOTPPage otp;
	payu3dPage pay;
	String status = "";
	String comment = "";
	
	@BeforeSuite
	public void clearOnce() {
		RouteManager.clearCache();
	}

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
	public void Purchase(Map<String, String>cardData , Map<String, String>emailData ) 
	        throws InterruptedException {
	    
	    WebDriver driver = baseClass.getDriver();
	    
	    String Email = emailData.getOrDefault("TestData", "").trim();
	    String ExpectedStatus = emailData.getOrDefault("Status", "").trim();
	    String CardHolder = cardData.getOrDefault("CardholderName", "").trim();
	    String CardNumber = cardData.getOrDefault("CardNumber", "").trim();
	    String Expiry = cardData.getOrDefault("Expiry", "").trim();
	    String CVV = cardData.getOrDefault("CVV", "").trim();
	    String PSP = cardData.getOrDefault("PSP", "").trim();
	    String PaymentMethod = cardData.getOrDefault("PaymentMethod", "").trim();
	    String Currency = cardData.getOrDefault("Currency", "").trim();
	    String minAmountStr = cardData.getOrDefault("MinAmount", "").trim();
	    String maxAmountStr = cardData.getOrDefault("MaxAmount", "").trim();
	    String defaultAmountStr = cardData.getOrDefault("DefaultAmount", "").trim();
	    
	    double minAmount = testData_CreateRoll.parseAmount(minAmountStr, 0.0);
	    double maxAmount = testData_CreateRoll.parseAmount(maxAmountStr, 0.0);
	    double defaultAmount = testData_CreateRoll.parseAmount(defaultAmountStr, 5.6);
	    
	    String Merchant = cardData.getOrDefault("Merchant", "").trim();
	    String RouteToBankMid = cardData.getOrDefault("RouteToBankMid", "").trim();
	    String RouteToMidOrBank = cardData.getOrDefault("RouteToMidOrBank", "").trim();

	    System.err.println(Email + " " + ExpectedStatus + " " + CardHolder + " " + CardNumber + " " + Expiry + " " + CVV + " " + PSP);

	    // Validate data is not empty
	    if (Email.isEmpty() || CardNumber.isEmpty()) {
	        Reporter.log("Skipping test - Empty email or card number", true);
	        throw new SkipException("Empty test data");
	    }

//	    RouteManager.ensureRoute(
//	        getDriver(),
//	        Merchant,
//	        Merchant,
//	        PaymentMethod,
//	        PaymentMethod,
//	        Currency,
//	        Currency,
//	        PSP,
//	        RouteToBankMid,
//	        RouteToMidOrBank
//	    );

	    Reporter.log("Email test case will run for this PSP: " + PSP, true);
	 //   Reporter.log("Testing Email: " + uniqueEmail + " with Card: " + CardNumber, true);

	    String baseUri = PropertyReader.getPropertyForPurchase("baseURI");
	    RestAssured.baseURI = baseUri;
	    String brandId = PropertyReader.getPropertyForPurchase("brandId");
	    String token = PropertyReader.getPropertyForPurchase("token");
	    String price = generateRandomTestData.generateRandomDoublePrice(minAmount, maxAmount, defaultAmount);
	    String firstName = generateRandomTestData.generateRandomFirstName();
	    String country = "US";
	    String city = "Paris";
	    String stateCode = "QLD";
	    String streetAddress = "Main gate";
	    String zipcode = "20001";
	    String productname = "Cricket bat";

	    System.err.println(baseUri);

	    String requestBody = "{\n" +
	            "  \"client\": {\n" +
	            "    \"full_name\": \"" + firstName + "\",\n" +
	            "    \"email\": \"" + Email + "\",\n" +
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
	    purchaseId = response.jsonPath().getString("purchaseId");       // ⭐ NEW

	    Reporter.log("Purchase ID: " + purchaseId, true);
	    Reporter.log("Checkout URL: " + checkoutUrl, true);
	    Reporter.log("Email: " + Email + " → HTTP Status: " + response.getStatusCode(), true);

	    Reporter.log("Response Body: " + response.getBody().asPrettyString(), true);

	    if (response.statusCode() == 202) {
	        Reporter.log("Email accepted by API: " + Email, true);
	    } else if (response.statusCode() == 400 || response.statusCode() == 422) {
	        Reporter.log("Email rejected by API: " + Email, true);
	        
	        if (ExpectedStatus != null && ExpectedStatus.equalsIgnoreCase("Fail")) {
	            status = "PASS";
	            comment = "PASS → Email rejected correctly as expected";
	        } else {
	            status = "FAIL";
	            comment = "FAIL → Email was rejected but expected to pass";
	        }
	        
	        Reporter.log(comment, true);
	        ExcelWriteUtility.writeResult("Purchase_Result", Email, ExpectedStatus, "FAIL", comment, purchaseId, PSP, PaymentMethod);
	        return;
	    }

	    try {
	        if (response.statusCode() == 202 && checkoutUrl != null && !checkoutUrl.isEmpty()) {
	            Reporter.log("✓ Transaction status OK → proceeding with payment flow", true);
	            
	            driver.get(checkoutUrl);
	            
	            mcp.userEnterCardInformationForPayment(CardHolder, CardNumber, Expiry, CVV);
	            mcp.clickOnPay();

	            if (PSP.equalsIgnoreCase("payu")) {
	                pay.payForPayu(Currency, purchaseId, ExpectedStatus, PaymentMethod);
	            }

	            if(PSP.equalsIgnoreCase("telr")) {
	            	Thread.sleep(8500);
	            }
	            otp.enterOTP(PSP);

	            if (mcp.isCardNumberInvalid()) {
	                status = "FAIL";
	                comment = "Payment Failed Cause Of Luhn";
	                Reporter.log(comment, true);
	                ExcelWriteUtility.writeResult("Purchase_Result", Email, ExpectedStatus, "Fail", comment, purchaseId, PSP, PaymentMethod);
	                return;
	            }

	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	            wait.until(ExpectedConditions.urlContains("issucces"));

	            String redirectUrl = driver.getCurrentUrl();
	            Reporter.log("Redirected URL: " + redirectUrl, true);

	            String flag = "";
	            if (redirectUrl.contains("?")) {
	                flag = Arrays.stream(redirectUrl.split("\\?")[1].split("&"))
	                        .filter(p -> p.startsWith("issucces="))
	                        .map(p -> p.split("=")[1])
	                        .findFirst()
	                        .orElse("");
	            }

	            if (flag.isEmpty()) {
	                status = "FAIL";
	                comment = "FAIL → Redirect URL does not contain issucces flag";
	                Reporter.log(comment, true);
	                ExcelWriteUtility.writeResult("Purchase_Result", Email, ExpectedStatus, "UNKNOWN", comment, purchaseId, PSP, PaymentMethod);
	                return;
	            }

	            boolean isActualSuccess = flag.equalsIgnoreCase("true");
	            boolean isExpectedSuccess = ExpectedStatus != null && ExpectedStatus.equalsIgnoreCase("Pass");
	            String actualOutcome = isActualSuccess ? "Pass" : "Fail";

	            if (isActualSuccess == isExpectedSuccess) {
	                status = "PASS";
	                comment = "PASS → Payment outcome matched expectation";
	            } else {
	                status = "FAIL";
	                comment = "FAIL → Payment outcome did not match expectation";
	            }

	            Reporter.log(comment, true);
	            ExcelWriteUtility.writeResult("Purchase_Result", Email, ExpectedStatus, actualOutcome, comment, purchaseId, PSP, PaymentMethod);

	            if (isActualSuccess) {
	                mcp.openBrowserForStaging(driver, baseUri);
	                lp.login();
	                tp.navigateUptoTransaction();
	                tp.searchTheTransaction(purchaseId);
	                tp.searchButton();
	                tp.clickOnTransactionId();
	                tp.verifyPurchaseTransactionIDIsNotEmpty();
	                Thread.sleep(4000);
	            }
	        }
	    } catch (Exception e) {
	        status = "FAIL";
	        comment = "FAIL → Exception: " + e.getMessage();
	        Reporter.log(comment, true);
	        ExcelWriteUtility.writeResult("Purchase_Result", Email, ExpectedStatus, "FAIL", comment, purchaseId, PSP, PaymentMethod);
	        Assert.fail(comment, e);
	    } finally {
	        System.out.println("Test completed for purchaseId: " + purchaseId);
	    }
	}

}
