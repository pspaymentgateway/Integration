package com.paysecure.utilities;


import org.testng.annotations.DataProvider;




public class DataProviders {

    
    @DataProvider(name = "emailProvider")
    public Object[][] emailProvider() {
        return new Object[][]{
            //Valid emails
           {"suhasksdkk@gmail.com"},
    //        {"suhas.patil@gmail.com"},
//            {"gaurav.singh@paysecure.dev"},
//            {"john.doe@example.com"},
//            {"qa.engineer@company.org"},
//            {"support@mysite.net"},
//            {"hello@brand.io"},
//            {"automation@test.dev"},
//            {"team@startup.co"},
//            {"info@domain.tech"},
//
//            //Edge valid cases
//            {"x@x.io"},
//            {"name@sub.domain.com"},
//            {"user+alias@gmail.com"},
//            {"user_name@mail.co"},
//            {"u.ser@co.uk"},

            //Invalid emails
//            {"suhas@.com"},
//            {"@gmail.com"},
//           {"suhas@"},
//            {"suhas"},
//            {"suhas@ gmail.com"},
//            {"suhas @gmail.com"},
//            {"suhas@gmail..com"},
//            {"suhas@gmail.c"},
//            {"suhas@gmail,com"},
//            {"suhas@#mail.com"},
//            {"suhas@@gmail.com"},
//            {".suhas@gmail.com"},
//            {"suhas.@gmail.com"},
//            {"suhas..patil@gmail.com"},
//            {"suhas@-gmail.com"},
//            {"suhas@gmail-.com"},
//            {"suhas@.gmail.com"},
//           {"suhas@g_mail.com"},
 //           {"@@@"},
   //         {" "},
//            {""},
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
//            {"GB"},   // Great Britain
           
           
           
           
//            {"DE"},   // Germany
//            {"FR"},   // France
//            {"CA"},   // Canada
//            {"AU"},   // Australia
//            {"NZ"},   // New Zealand
//            {"JP"},   // Japan
//            {"SG"},   // Singapore
//            {"AE"},   // United Arab Emirates
  //          {"ZA"},   // South Africa
//            {"BR"},   // Brazil
//
//            //Borderline or uncommon valid codes
//            {"AX"},   // Åland Islands
//            {"CC"},   // Cocos (Keeling) Islands
 //           {"GG"},   // Guernsey
//            {"IM"},   // Isle of Man
//            {"JE"},   // Jersey
   //         {"XK"},   // Kosovo (unofficial ISO)
//
//            //Invalid / malformed values
//            {"ZZ"},       // Non-existent ISO
//            {"XYZ"},      // Too long
//            {"IND"},      // 3-letter form
//            {"India"},    // Full name instead of code
           {"U S"},      // Contains space
 //           {"IN "},      // Trailing space
//            {" IN"},      // Leading space
 //          {"IN 1"},      // Contains number
//            {"@N"},       // Special character
//            {"123"},      // Numeric value
//            {""},         // Empty string
           {null}        // Null value
        };
    }
    
    
    
    
    @DataProvider(name = "cityProvider")
    public Object[][] getcityProvider() {
        return new Object[][] {
            //Valid city names
            {"London"},
//            {"New York"},
//            {"Paris"},
//            {"San Francisco"},
//            {"Bengaluru"},
            
//            {"São Paulo"},
//            {"12345"},
//            {"ThisCityNameIsWayTooLongToBeValidBecauseItExceedsMaximumLengthLimit"},

            //Invalid city names
            {"@City"},
//            {"City@Name"},
//            {""},
//            {null},
//            {"A"}
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
        	{"4444444444444448"}
        };
    }
    
    @DataProvider(name = "cardData")
    public Object[][] cardData() {
        return new Object[][] {
        	// {"Abraham Benjamin de Villiers", "5555555555554444", "12/27", "111"},
        	 {"Abraham Benjamin de Villiers", "4012000300001003", "01/29", "030"},
        };
    }
    
    @DataProvider(name = "streetAddressProvider")
    public static Object[][] streetAddressProvider() {
        return new Object[][]{
            {"123 Main St"},
//            {"221B Baker Street"},
//            {"742 Evergreen Terrace"},
//            {"1600 Amphitheatre Parkway"},
//            {"5th Avenue"},
//            {"MG Road"},
//            {"No. 45, High Street"},
//            {"10 Downing St"},
//            {null},
//            {""},
//            {"     "},
//            {"@"},
//            {"####"},
//            {"1234567890123456789012345678901234567890123456789012345678901234567890"},
//            {"12"},
//            {"Main"},
//            {"123"}
        };
    }

    @DataProvider(name = "zipCodeProvider")
    public Object[][] zipCodeProvider() {
        return new Object[][] {
            {"10001"},
            {"94105-1234"},
//            {"M5V 3L9"},
//            {"SW1A 1AA"},
//            {"560001"},
//            {"10115"},
//            {"12"},
//            {"12345678901"},
//            {"123@#"},
//            {""},
//            {"     "},
            {null}
        };
    }



}
