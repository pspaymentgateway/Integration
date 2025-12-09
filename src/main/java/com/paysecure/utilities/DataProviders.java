package com.paysecure.utilities;


import org.testng.annotations.DataProvider;




public class DataProviders {

    @DataProvider(name ="email")
    public Object[][] emailData() throws Exception {
        JsonUtils json = new JsonUtils();
        return json.readJsonData("C:\\Users\\LENOVO\\eclipse-workspace\\integrationApiUiTesting\\src\\test\\resource\\TestData_Json\\email.json","Email");
    }
    
    @DataProvider(name ="country")
    public Object[][] countryData() throws Exception {
        JsonUtils json = new JsonUtils();
        return json.readJsonData("C:\\Users\\LENOVO\\eclipse-workspace\\integrationApiUiTesting\\src\\test\\resource\\TestData_Json\\country.json","Country");
    }
    
    @DataProvider(name ="StateCodeData")
    public Object[][] StateCodeData() throws Exception {
        JsonUtils json = new JsonUtils();
        return json.readJsonData("C:\\Users\\LENOVO\\eclipse-workspace\\integrationApiUiTesting\\src\\test\\resource\\TestData_Json\\statecode_json.json","StateCode");
    }
    
    @DataProvider(name ="StreetAddressData")
    public Object[][] StreetAddressData() throws Exception {
        JsonUtils json = new JsonUtils();
        return json.readJsonData("C:\\Users\\LENOVO\\eclipse-workspace\\integrationApiUiTesting\\src\\test\\resource\\TestData_Json\\streetAddress.json","StreetAddress");
    }
    
    @DataProvider(name = "emailProvider")
    public Object[][] emailProvider() {
        return new Object[][]{
            //Valid emails
           {"suhasksdknjk@gmail.com"},
            {"suhas.patil@gmail.com"},
            {"gaurav.singh@paysecure.dev"},
            {"john.doe@example.com"},
            {"qa.engineer@company.org"},
            {"support@mysite.net"},
            {"hello@brand.io"},
            {"automation@test.dev"},
            {"team@startup.co"},
            {"info@domain.tech"},

            //Edge valid cases
            {"x@x.io"},
            {"name@sub.domain.com"},
            {"user+alias@gmail.com"},
            {"user_name@mail.co"},
            {"u.ser@co.uk"},

            //Invalid emails
            {"suhas@.com"},
            {"@gmail.com"},
           {"suhas@"},
            {"suhas"},
            {"suhas@ gmail.com"},
            {"suhas @gmail.com"},
            {"suhas@gmail..com"},
            {"suhas@gmail.c"},
            {"suhas@gmail,com"},
            {"suhas@#mail.com"},
            {"suhas@@gmail.com"},
            {".suhas@gmail.com"},
            {"suhas.@gmail.com"},
            {"suhas..patil@gmail.com"},
            {"suhas@-gmail.com"},
            {"suhas@gmail-.com"},
            {"suhas@.gmail.com"},
           {"suhas@g_mail.com"},
            {"@@@"},
            {" "},
            {""},
            {null}
        };
    }

    // 🔹 DataProvider: country codes (valid + invalid)
    @DataProvider(name = "countryProvider")
    public Object[][] countryProvider() {
        return new Object[][]{
            //Valid ISO country codes
  //          {"IN"},   // India
           {"US"},   // United States
            {"GB"},   // Great Britain
            {"DE"},   // Germany
            {"FR"},   // France
            {"CA"},   // Canada
            {"AU"},   // Australia
            {"NZ"},   // New Zealand
            {"JP"},   // Japan
            {"SG"},   // Singapore
            {"AE"},   // United Arab Emirates
            {"ZA"},   // South Africa
            {"BR"},   // Brazil

            //Borderline or uncommon valid codes
            {"AX"},   // Åland Islands
            {"CC"},   // Cocos (Keeling) Islands
            {"GG"},   // Guernsey
            {"IM"},   // Isle of Man
            {"JE"},   // Jersey
            {"XK"},   // Kosovo (unofficial ISO)

            //Invalid / malformed values
            {"ZZ"},       // Non-existent ISO
            {"XYZ"},      // Too long
            {"IND"},      // 3-letter form
            {"India"},    // Full name instead of code
           {"U S"},      // Contains space
            {"IN "},      // Trailing space
//            {" IN"},      // Leading space
           {"IN 1"},      // Contains number
//            {"@N"},       // Special character
//            {"123"},      // Numeric value
//            {""},         // Empty string
           {null}        // Null value
        };
    }
    
    @DataProvider(name = ""
    		+ ""
    		+ "")
    public Object[][] cityProvider() {
        return new Object[][] {
            //Valid city names
            {"London"},
            {"New York"},
            {"Paris"},
            {"San Francisco"},
            {"Bengaluru"},
            {"São Paulo"},
            {"12345"},
            {"ThisCityNameIsWayTooLongToBeValidBecauseItExceedsMaximumLengthLimit"},

            //Invalid city names
            {"@City"},
            {"City@Name"},
            {""},
            {null},
            {"A"}
        };
    }
    
    
    @DataProvider(name = "stateCodeProvider")
    public Object[][] stateCodeProvider() {
        return new Object[][]{
            {"QLD"},
            {"NSW"},
            {"VIC"},
            {"CA"},
            {"NY"},
            {"MH"},
            {"DL"},
            {"ON"},
            {"BC"},
            {"qld"},
            {"QLDD"},
            {"C4"},
            {"@"},
            {""},
            {null},
            {"XYZ"},
            {"MUM"}
        };
    }
    @DataProvider(name = "cardsData")
    public Object[][] cardsData() {
        return new Object[][] {
        	{"3622722633333330"}
        };
    }
    
    @DataProvider(name = "cardData")
    public Object[][] cardData() {
        return new Object[][] {
        	 {"Abraham Benjamin de Villiers", "5555555555554444", "12/27", "111"},
        };
    }

    @DataProvider(name = "cityData")
    public Object[][] getData() throws Exception {
        JsonUtils json = new JsonUtils();
        return json.readJsonData("C:\\Users\\LENOVO\\eclipse-workspace\\integrationApiUiTesting\\src\\test\\resource\\TestData_Json\\city.json","City");
    }
    
    @DataProvider(name = "zipCodeData")
    public Object[][] getzipCodeDataData() throws Exception {
        JsonUtils json = new JsonUtils();
        return json.readJsonData("C:\\Users\\LENOVO\\eclipse-workspace\\integrationApiUiTesting\\src\\test\\resource\\TestData_Json\\zipcode.json","ZipCode");
    }
    
    @DataProvider(name = "currencyData")
    public Object[][] getcurrencyData() throws Exception {
        JsonUtils json = new JsonUtils();
        return json.readJsonData("C:\\Users\\LENOVO\\eclipse-workspace\\integrationApiUiTesting\\src\\test\\resource\\TestData_Json\\currency.json","Currency");
    }
    
    @DataProvider(name = "brandIDData")
    public Object[][] getbrandIDData() throws Exception {
        JsonUtils json = new JsonUtils();
        return json.readJsonData("C:\\Users\\LENOVO\\eclipse-workspace\\integrationApiUiTesting\\src\\test\\resource\\TestData_Json\\brandID.json","BrandID");
    }
    
    @DataProvider(name = "productNameData")
    public Object[][] getproductNameData() throws Exception {
        JsonUtils json = new JsonUtils();
        return json.readJsonData("C:\\Users\\LENOVO\\eclipse-workspace\\integrationApiUiTesting\\src\\test\\resource\\TestData_Json\\ProductName.json","ProductName");
    }


}
