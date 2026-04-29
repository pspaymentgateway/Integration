package Payout;

import org.testng.annotations.Test;

import com.paysecure.base.baseClass;
import com.paysecure.utilities.PropertyReader;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;

public class payout extends baseClass{
	
	  @BeforeMethod
	  public void beforeMethod() {
	  }
	  
  @Test
  public void payoutMethod() {
	  WebDriver driver=baseClass.getDriver();
	   String baseUri = PropertyReader.getPropertyForPurchase("baseURI");
	   RestAssured.baseURI = baseUri;
	   String brandId = PropertyReader.getPropertyForPurchase("brandId");
	   String token = PropertyReader.getPropertyForPurchase("token");
	   String email = "badalsonatest1@gmail.com";
	   String country = "US";
	   String phone = "+14377717874";
	   String stateCode = "CA";
	   String zipCode = "123456";
	   String city = "Rampur";
	   String streetAddress = "Random city address";
	   String fullName = "rohit yadav";
	   String documentId = "pablo@pabloklein.com.br";
	   String documentType = "email";

	   // Bank Details
	   String bankName = "iban";
	   String bankAccountName = "Ivy GmbH";
	   String bankCurrency = "USD";
	   String accountNumber = "DE84100101234535423376";
	   String bankAccountCurrency = "EUR";
	   String bankCountryCode = "40106";
	   String speiClabe = "646180110400000007";

	   // Transaction Details
	   String purpose = "test payout";
	   String payoutMethod = "PAYOUT-SPEI";
	   String currency = "MXN";
	   int amount = 1;

	   // Extra Param
	   String testNumber = "12";

	   // Redirect URLs
	   String successRedirect = "https://newmodules2.choicepay.ca/getResponse.jsp?success=true";
	   String failureRedirect = "https://newmodules2.choicepay.ca/getResponse.jsp?success=false";
	   String pendingRedirect = "https://newmodules2.choicepay.ca/getResponse.jsp?success=in process";
	  
	  
	  
	  String requestBody = "{\n" +
			    "  \"client\": {\n" +
			    "    \"email\": \"" + email + "\",\n" +
			    "    \"country\": \"" + country + "\",\n" +
			    "    \"phone\": \"" + phone + "\",\n" +
			    "    \"stateCode\": \"" + stateCode + "\",\n" +
			    "    \"zip_code\": \"" + zipCode + "\",\n" +
			    "    \"city\": \"" + city + "\",\n" +
			    "    \"street_address\": \"" + streetAddress + "\",\n" +
			    "    \"full_name\": \"" + fullName + "\",\n" +
			    "    \"documentId\": \"" + documentId + "\",\n" +
			    "    \"documentType\": \"" + documentType + "\"\n" +
			    "  },\n" +
			    "  \"beneficiaryDetail\": {\n" +
			    "    \"bankName\": \"" + bankName + "\",\n" +
			    "    \"bankAccountName\": \"" + bankAccountName + "\",\n" +
			    "    \"bankAccountCurrency\": \"" + bankAccountCurrency + "\",\n" +
			    "    \"bankAccountNumber\": \"" + accountNumber + "\",\n" +
			    "    \"bankCountryCode\": \"" + bankCountryCode + "\",\n" +
			    "    \"speiClabe\": \"" + speiClabe + "\"\n" +
			    "  },\n" +
			    "  \"purpose\": \"" + purpose + "\",\n" +
			    "  \"payoutMethod\": \"" + payoutMethod + "\",\n" +
			    "  \"currency\": \"" + currency + "\",\n" +
			    "  \"amount\": " + amount + ",\n" +
			    "  \"extraParam\": {\n" +
			    "    \"Testnumber\": \"" + testNumber + "\"\n" +
			    "  },\n" +
			    "  \"success_redirect\": \"" + successRedirect + "\",\n" +
			    "  \"failure_redirect\": \"" + failureRedirect + "\",\n" +
			    "  \"pending_redirect\": \"" + pendingRedirect + "\"\n" +
			    "}";
	  
	  Response response = RestAssured.given()
		        .header("Authorization", "Bearer " + token)
		        .header("brandId", brandId)   // 👈 ADD THIS
		        .contentType(ContentType.JSON)
		        .body(requestBody)
		.when()
		        .post("api/v1/payout/")
		.then()
		        .extract()
		        .response();

	   String checkoutUrl = response.jsonPath().getString("checkout_url");
	   String purchaseId = response.jsonPath().getString("purchaseId"); 
	   System.err.println(purchaseId);
	   System.out.println(response.asPrettyString());
	   driver.get(checkoutUrl);
	 
  }


}
