package S2sForLiveCo;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.paysecure.base.baseClass;
import com.paysecure.utilities.PropertyReader;
import com.paysecure.utilities.generateRandomTestData;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class purchaseWithS2S extends baseClass{

    String checkoutUrl;
    String purchaseId;
    String baseUri;

    @Test
    public void purchase() {

        baseUri ="https://api.paysecure.co";
        RestAssured.baseURI = baseUri;

        String brandId = "f6339b6b-1eb2-4f59-86fb-f3b03b5e5005";
        String token = "b4b3e57e7072a2f028973ca5cffec07c883528577372e62cec4eccb2b4b55b76";

        String price = generateRandomTestData.generateRandomDoublePrice(10.00, 100.00, 50.00);
        String firstName = generateRandomTestData.generateRandomFirstName();
        String emailId = generateRandomTestData.generateRandomEmail();

        String paymentMethod = "VISA";
        String currency = "MXN";

        String country = "IN";
        String city = "Dausa";
        String stateCode = "RAJ";
        String streetAddress = "Somnath nagar";
        String zipcode = "303304";
        String productname = "Astrologer";

        String requestBody = "{\n" +
                "  \"client\": {\n" +
                "    \"full_name\": \"" + firstName + "\",\n" +
                "    \"email\": \"" + emailId + "\",\n" +
                "    \"country\": \"" + country + "\",\n" +
                "    \"city\": \"" + city + "\",\n" +
                "    \"stateCode\": \"" + stateCode + "\",\n" +
                "    \"street_address\": \"" + streetAddress + "\",\n" +
                "    \"zip_code\": \"" + zipcode + "\"\n" +
                "  },\n" +
                "  \"purchase\": {\n" +
                "    \"currency\": \"" + currency + "\",\n" +
                "    \"products\": [\n" +
                "      {\n" +
                "        \"name\": \"" + productname + "\",\n" +
                "        \"price\": \"" + price + "\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"status\": \"created\",\n" +
                "  \"brand_id\": \"" + brandId + "\",\n" +
                "  \"send_receipt\": \"\",\n" +
                "  \"skip_capture\": \"\",\n" +
                "  \"extraParam\": {\n" +
                "    \"showCryptoConversion\": \"yes\",\n" +
                "    \"cryptoCurrencies\": \"[\\\"BTC\\\",\\\"ETH\\\",\\\"USDT\\\",\\\"XRP\\\",\\\"LTC\\\",\\\"DOGE\\\"]\"\n" +
                "  },\n" +
                "  \"paymentMethod\": \"" + paymentMethod + "\",\n" +
                "  \"success_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=true\",\n" +
                "  \"failure_redirect\": \"https://staging.paysecure.net/getResponse.jsp?issucces=false\",\n" +
                "  \"success_callback\": \"https://webhook.site/48d379ca-1bfa-4908-9ca9-ee48915f1389\",\n" +
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

        System.out.println("Purchase ID: " + purchaseId);
        System.out.println("Checkout URL: " + checkoutUrl);
    }

    
    
    

    @Test(dependsOnMethods = "purchase")
    public void superS2S() {

        WebDriver driver = baseClass.getDriver();
        RestAssured.baseURI = baseUri;

        String endpoint = "/api/v1/p/" + purchaseId + "?s2s=true";

        System.out.println("S2S Endpoint: " + endpoint);
        System.out.println("Full URL: " + baseUri + endpoint);

        String brandId = "f6339b6b-1eb2-4f59-86fb-f3b03b5e5005";
        String token = "b4b3e57e7072a2f028973ca5cffec07c883528577372e62cec4eccb2b4b55b76";

        String cardnumber = "4079722589318630";
     //   String cardnumber = "5116213113429327";

        String requestBody = "{\n" +
                "  \"cardholder_name\": \"jkaurav\",\n" +
                "  \"card_number\": \"" + cardnumber + "\",\n" +
                "  \"expires\": \"09/31\",\n" +
                "  \"cvc\": \"100\",\n" +
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

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .header("brandId", brandId)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();

        response.prettyPrint();

        String callback_url = response.jsonPath().getString("callback_url");

        System.out.println("Callback URL: " + callback_url);

      //  driver.get(callback_url);
    }

}