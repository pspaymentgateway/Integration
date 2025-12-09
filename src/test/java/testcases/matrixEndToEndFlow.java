package testcases;

import org.testng.annotations.Test;

import com.paysecure.Page.loginPage;
import com.paysecure.Page.matrixCashierPage;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.DataProviders;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;

public class matrixEndToEndFlow extends baseClass{
	private WebDriver driver;
	loginPage lp;
	String checkoutUrl;
	String purchaseId;
	matrixCashierPage mcp;
	transactionPage tp;
	
	  @BeforeMethod
	  public void beforeMethod() throws InterruptedException {
			lp = new loginPage(getDriver());
			lp.login();
			mcp=new matrixCashierPage(getDriver());
			tp=new transactionPage(getDriver());
	  }
	//String cardHolder, String cardNumber, String expiry, String cvc
  @Test(dataProvider ="cardData",dataProviderClass = DataProviders.class) 
  public void f(String cardHolder, String cardNumber, String expiry, String cvc) throws Exception {
      WebDriver driver=baseClass.getDriver();
		String baseUri = PropertyReader.getProperty("baseURI");
		RestAssured.baseURI =baseUri;
		String brandId = PropertyReader.getProperty("brandId");
		String token = PropertyReader.getProperty("token");
		String price = generateRandomTestData.generateRandomDouble();
		String currency =PropertyReader.getProperty("currency");
		String paymentMethod=PropertyReader.getProperty("paymentMethod");
		String firstName = generateRandomTestData.generateRandomFirstName();
		String emailId = generateRandomTestData.generateRandomEmail();
		String matrixPSPUrl=PropertyReader.getProperty("matrixPSPUrl");
		String UID=PropertyReader.getProperty("UID");
		String PASSWORD=PropertyReader.getProperty("PASSWORD");
		String requestBody = "{\n" +
		        "  \"client\": {\n" +
		        "    \"full_name\": \""+firstName+"\",\n" +
		        "    \"email\": \""+emailId+"\",\n" +
		        "    \"country\": \"DZ\",\n" +
		        "    \"city\": \"London\",\n" +
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
	    
	    System.out.println(response.asPrettyString());

		checkoutUrl = response.jsonPath().getString("checkout_url");
		purchaseId = response.jsonPath().getString("purchaseId");
		String merchantName = response.jsonPath().getString("merchantName");
		double total = response.jsonPath().getDouble("purchase.total");
		tp.validatePurchaseId(purchaseId);
        // Payment
        driver.get(checkoutUrl);
        mcp.userEnterCardInformationForPayment(cardHolder, cardNumber, expiry, cvc);
        mcp.clickOnPay();
        Thread.sleep(7000);
        String redirectUrl = driver.getCurrentUrl();
        Thread.sleep(2000);
        // FAILURE CASE
        if (redirectUrl.contains("issucces=false")) {
            Reporter.log("Payment Failed → URL shows issucces=false", true);
            Reporter.log("Stopping further execution and quitting browser.", true);
            driver.quit();
            return;
        }

        // SUCCESS CASE
        if (redirectUrl.contains("issucces=true")) {
            Reporter.log("Payment Successful → URL shows issucces=true", true);
            Reporter.log("Proceeding with transaction verification flow.", true);
        }
        mcp.openBrowserForStaging(driver,RestAssured.baseURI);
        lp.login();
        tp.navigateUptoTransaction();
        tp.searchTheTransaction(purchaseId);
        tp.verifyTxnId(purchaseId);
        tp.verifyMerchantName(merchantName);
        tp.verifyAmount(total);
        tp.verifyCurrency(currency);
        tp.getStatusFromUI();
        tp.maskCardForCashier(cardNumber);
        tp.verifyUsedCardOnUI(cardNumber);
        tp.clickOnTransactionId();
        tp.verifyPurchaseTransactionIDIsNotEmpty();
		tp.verifyCurrencyOnPaymentInfo();
		tp.verifyAmountFromPaymentInfo();
		mcp.openBrowserForStaging(driver,matrixPSPUrl);
		tp.doLoginOnThePSPSide(UID, PASSWORD);
		tp.navigateUptoPSPTransactionModule();
		tp.searchTheLatestTransactions(tp.purchaseTxnId);
		tp.verifyPurchaseTxnIdatPSP(tp.purchaseTxnId);
		tp.verifyCurrencyInPSP();
		tp.maskCardForPSP(cardNumber);
		tp.verifyUsedCardOnPSP(cardNumber);
		tp.getStatusFromPSP();
		tp.verifyStatus();
  }


}
