package com.paysecure.utilities;

import org.testng.annotations.DataProvider;

public class jsonProvider {

    @DataProvider(name ="email")
    public Object[][] emailData() throws Exception {
        return new JsonUtils().readJsonData(
                pathHelper.getResourcePath("TestData_Json/email.json"),
                "Email"
        );
    }

    @DataProvider(name ="country")
    public Object[][] countryData() throws Exception {
        return new JsonUtils().readJsonData(
        		pathHelper.getResourcePath("TestData_Json/country.json"),
                "Country"
        );
    }

    @DataProvider(name ="StateCodeData")
    public Object[][] StateCodeData() throws Exception {
        return new JsonUtils().readJsonData(
        		pathHelper.getResourcePath("TestData_Json/statecode_json.json"),
                "StateCode"
        );
    }

    @DataProvider(name ="StreetAddressData")
    public Object[][] StreetAddressData() throws Exception {
        return new JsonUtils().readJsonData(
        		pathHelper.getResourcePath("TestData_Json/streetAddress.json"),
                "StreetAddress"
        );
    }

    @DataProvider(name ="cityData")
    public Object[][] cityData() throws Exception {
        return new JsonUtils().readJsonData(
        		pathHelper.getResourcePath("TestData_Json/city.json"),
                "City"
        );
    }

    @DataProvider(name = "zipCodeData")
    public Object[][] zipCodeData() throws Exception {
        return new JsonUtils().readJsonData(
        		pathHelper.getResourcePath("TestData_Json/zipcode.json"),
                "ZipCode"
        );
    }

    @DataProvider(name = "currencyData")
    public Object[][] currencyData() throws Exception {
        return new JsonUtils().readJsonData(
        		pathHelper.getResourcePath("TestData_Json/currency.json"),
                "Currency"
        );
    }

    @DataProvider(name = "brandIDData")
    public Object[][] brandIDData() throws Exception {
        return new JsonUtils().readJsonData(
        		pathHelper.getResourcePath("TestData_Json/brandID.json"),
                "BrandID"
        );
    }

    @DataProvider(name = "productNameData")
    public Object[][] productNameData() throws Exception {
        return new JsonUtils().readJsonData(
        		pathHelper.getResourcePath("TestData_Json/ProductName.json"),
                "ProductName"
        );
    }

    // Static one line data providers
    @DataProvider(name = "cardsData")
    public Object[][] cardsData() {
        return new Object[][]{{"3622722633333330"}};
    }

    @DataProvider(name = "cardData")
    public Object[][] cardData() {
        return new Object[][]{
                {"Abraham Benjamin de Villiers", "5555555555554444", "12/27", "111"}
        };
    }
}
