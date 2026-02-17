package CreateCustomerAndSession;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.paysecure.Page.CashierPage;
import com.paysecure.Page.RouteManager;
import com.paysecure.Page.loginPage;
import com.paysecure.Page.payu3dPage;
import com.paysecure.Page.pspOTPPage;
import com.paysecure.Page.routeManagerCC;
import com.paysecure.Page.transactionPage;
import com.paysecure.base.baseClass;
import com.paysecure.utilities.ExcelWriteUtility;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.dataProvidersForCC;
import com.paysecure.utilities.generateRandomTestData;
import com.paysecure.utilities.testData_CreateRoll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class createCustomerAndCreateSession extends baseClass {

    private WebDriver driver;
    private loginPage lp;
    private CashierPage mcp;
    private transactionPage tp;
    private pspOTPPage otp;
    private payu3dPage pay;

    private String customerId;

    private String status = "";
    private String comment = "";

    // ========================= CLEAR ROUTE CACHE =========================
    @BeforeSuite
    public void clearRouteCache() {
        RouteManager.clearCache();
    }

    // ========================= SETUP =========================
    @BeforeMethod
    public void beforeMethod() throws InterruptedException {
        driver = getDriver();
        lp = new loginPage(driver);
        mcp = new CashierPage(driver);
        tp = new transactionPage(driver);
        pay = new payu3dPage(driver);
        otp = new pspOTPPage();
        lp.login();
    }

    // ========================= CREATE CUSTOMER =========================
    @Test
    public void createCustomer() {

        String baseUri = PropertyReader.getPropertyForCreateCustomerSession("baseURICC");
        String token = PropertyReader.getPropertyForCreateCustomerSession("TokenCC");

        RestAssured.baseURI = baseUri;

        String requestBody = "{\n" +
                "  \"merchantCustomerId\": \"" + generateRandomTestData.generateRandomMerchantCustomerId() + "\",\n" +
                "  \"fullName\": \"" + generateRandomTestData.generateRandomFirstName() + "\",\n" +
                "  \"emailId\": \"" + generateRandomTestData.generateRandomEmail() + "\",\n" +
                "  \"phoneNo\": \"" + generateRandomTestData.generateRandomIndianMobileNumber() + "\",\n" +
                "  \"city\": \"juneau\",\n" +
                "  \"stateCode\": \"MH\",\n" +
                "  \"zipCode\": \"99812\",\n" +
                "  \"country\": \"IN\"\n" +
                "}";

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("api/v1/customer")
                .then()
                .extract()
                .response();

        Reporter.log(response.asPrettyString(), true);

        customerId = response.jsonPath().getString("customerId");
        Assert.assertNotNull(customerId, "Customer ID should not be null");

        Reporter.log("Customer created successfully → " + customerId, true);
    }

    // ========================= CREATE SESSION + PAYMENT =========================
    @Test(
            dependsOnMethods = "createCustomer",
            dataProvider = "RoutingData",
            dataProviderClass = dataProvidersForCC.class
    )
    public void createSession(Map<String, String> data) throws Exception {

        if (!data.getOrDefault("RunFlag", "TRUE").equalsIgnoreCase("TRUE")) {
            Reporter.log("Skipping due to RunFlag = FALSE", true);
            return;
        }

        // ---------- TEST DATA ----------
        String PSP = data.get("PSP");
        String PaymentMethod = data.get("PaymentMethod");
        String Currency = data.get("Currency");

        String cardHolder = data.get("CardholderName");
        String cardNumber = data.get("CardNumber");
        String expiry = data.get("Expiry");
        String cvv = data.get("CVV");

        String Merchant = data.get("Merchant");
        String RouteToMidOrBank = data.get("RouteToMidOrBank");
        String RouteToBankMid = data.get("RouteToBankMid");

        double minAmount = testData_CreateRoll.parseAmount(data.get("MinAmount"), 0.0);
        double maxAmount = testData_CreateRoll.parseAmount(data.get("MaxAmount"), 0.0);
        double defaultAmount = testData_CreateRoll.parseAmount(data.get("DefaultAmount"), 5.0);

        data.forEach((k, v) -> Reporter.log(k + " = " + v, true));

        // ========================= ROUTE MANAGER =========================
        routeManagerCC.ensureRoute(
                driver,
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

        // ========================= CREATE SESSION API =========================
        String baseUri = PropertyReader.getPropertyForCreateCustomerSession("baseURICC");
        String token = PropertyReader.getPropertyForCreateCustomerSession("TokenCC");
        String brandId = PropertyReader.getPropertyForCreateCustomerSession("BrandIDCC");

        String price = generateRandomTestData.generateRandomDoublePrice(
                minAmount, maxAmount, defaultAmount
        );

        RestAssured.baseURI = baseUri;

        String requestBody = "{\n" +
                "  \"customerId\": \"" + customerId + "\",\n" +
                "  \"currency\": \"" + Currency + "\",\n" +
                "  \"totalAmount\": \"" + price + "\",\n" +
                "  \"paymentMethod\": \"" + PaymentMethod + "\",\n" +
                "  \"success_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=true\",\n" +
                "  \"failure_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=false\"\n" +
                "}";

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .header("brandId", brandId)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("api/v1/createSession")
                .then()
                .extract()
                .response();

        Reporter.log(response.asPrettyString(), true);

        String sessionId = response.jsonPath().getString("sessionId");
        String sessionUrl = response.jsonPath().getString("sessionUrl");

        Assert.assertNotNull(sessionUrl, "Session URL is null");

        // ========================= UI PAYMENT =========================
        driver.get(sessionUrl);

        mcp.userEnterCardInformationForPayment(
                cardHolder, cardNumber, expiry, cvv
        );
        mcp.clickOnPay();

        if ("payu".equalsIgnoreCase(PSP)) {
            pay.payForPayu(Currency, sessionId, PaymentMethod, PaymentMethod);
        }

        otp.enterOTP(PSP);

        // ========================= RESULT CHECK (TRUE / FALSE / UNKNOWN) =========================
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.urlContains("issucces"));

        String redirectUrl = driver.getCurrentUrl();
        Reporter.log("Redirect URL → " + redirectUrl, true);

        String flag = Arrays.stream(redirectUrl.split("\\?")[1].split("&"))
                .filter(p -> p.startsWith("issucces="))
                .map(p -> p.split("=")[1])
                .findFirst()
                .orElse("");

        Boolean isSuccess;
        String actualOutcome;

        if (flag.equalsIgnoreCase("true")) {
            isSuccess = true;
            actualOutcome = "PASS";
            status = "PASS";
            comment = "Payment succeeded";
            ExcelWriteUtility.writeResult(
                    "CreateCustomerAndSession", Currency,status,actualOutcome,comment,sessionId,PSP,PaymentMethod
            );

        } else if (flag.equalsIgnoreCase("false")) {
            isSuccess = false;
            actualOutcome = "FAIL";
            status = "FAIL";
            comment = "Payment failed";
            ExcelWriteUtility.writeResult(
                    "CreateCustomerAndSession", Currency,status,actualOutcome,comment,sessionId,PSP,PaymentMethod
            );

        } else {
            isSuccess = null; // UNKNOWN
            actualOutcome = "UNKNOWN";
            status = "FAIL";
            comment = "Unknown result → issucces flag missing";
            ExcelWriteUtility.writeResult(
                    "CreateCustomerAndSession", Currency,status,actualOutcome,comment,sessionId,PSP,PaymentMethod
            );
        }

        Reporter.log(comment, true);


        // ========================= TRANSACTION VERIFICATION =========================
        if (Boolean.TRUE.equals(isSuccess)) {
            mcp.openBrowserForStaging(driver, baseUri);
            lp.login();
            tp.navigateUptoTransaction();
            tp.searchTheTransaction(sessionId);
            tp.clickOnTransactionId();
            tp.verifyPurchaseTransactionIDIsNotEmpty();
        }
    }
}
