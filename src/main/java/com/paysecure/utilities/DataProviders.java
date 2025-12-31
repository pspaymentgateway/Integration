package com.paysecure.utilities;


import org.testng.annotations.DataProvider;




public class DataProviders {

    
	@DataProvider(name = "emailProvider")
	public Object[][] emailProvider() {
	    return new Object[][]{

	        // Valid emails → Pass
	        {"suhasksdkk@gmail.com", "Pass", "Easybuzz"},
	        {"u.ser@co.uk", "Pass", "Easybuzz"},

	        // Invalid emails → Fail
	        {"suhas@.com", "Fail", "Easybuzz"},
	        {"@gmail.com", "Fail", "Easybuzz"},
	        {"suhas@", "Fail", "Easybuzz"},
	        {"suhas", "Fail", "Easybuzz"},
	        {"suhas@ gmail.com", "Fail", "Easybuzz"},
	        {"suhas @gmail.com", "Fail", "Easybuzz"},
	        {"suhas@gmail..com", "Fail", "Easybuzz"},
	        {"suhas@gmail.c", "Fail", "Easybuzz"},
	        {"suhas@gmail,com", "Fail", "Easybuzz"},
	        {"suhas@#mail.com", "Fail", "Easybuzz"},
	        {"suhas@@gmail.com", "Fail", "Easybuzz"},
	        {".suhas@gmail.com", "Fail", "Easybuzz"},
	        {"suhas.@gmail.com", "Fail", "Easybuzz"},
	        {"suhas..patil@gmail.com", "Fail", "Easybuzz"},
	        {"suhas@.gmail.com", "Fail", "Easybuzz"},
	        {"suhas@g_mail.com", "Fail", "Easybuzz"},
	        {"@@@", "Fail", "Easybuzz"},
	        {" ", "Fail", "Easybuzz"},
	        {"", "Fail", "Easybuzz"},
	        {null, "Fail", "Easybuzz"}
	    };
	}



	// 🔹 DataProvider: country codes (valid + invalid)
	@DataProvider(name = "countryProvider")
	public Object[][] countryProvider() {
	    return new Object[][]{

	        // Valid ISO-2 country codes → Pass
	        {"IN", "Pass", "Easybuzz"},   // India
//	      {"US", "Pass", "Easybuzz"},   // United States
//	      {"FR", "Pass", "Easybuzz"},   // France
//	      {"CA", "Pass", "Easybuzz"},   // Canada

	        // Invalid / malformed values → Fail
//	      {"ZZ", "Fail", "Easybuzz"},      // non-existent ISO
//	      {"XYZ", "Fail", "Easybuzz"},     // too long
//	      {"IND", "Fail", "Easybuzz"},     // 3-letter code
//	      {"India", "Fail", "Easybuzz"},   // full country name
//	      {"U S", "Fail", "Easybuzz"},     // contains space
//	      {"IN ", "Fail", "Easybuzz"},     // trailing space
//	      {" IN", "Fail", "Easybuzz"},     // leading space

	        {"IN 1", "Fail", "Easybuzz"},    // contains number
	        {"@N", "Fail", "Easybuzz"},      // special character
	        {"123", "Fail", "Easybuzz"},     // numeric
	        {"", "Fail", "Easybuzz"},        // empty
	        {null, "Fail", "Easybuzz"}       // null
	    };
	}


    
    
    
    
	@DataProvider(name = "cityProvider")
	public Object[][] getcityProvider() {
	    return new Object[][] {

	        // Valid city names → Pass
	        {"London", "Pass", "Easybuzz"},
//	      {"New York", "Pass", "Easybuzz"},
//	      {"Paris", "Pass", "Easybuzz"},
//	      {"San Francisco", "Pass", "Easybuzz"},
//	      {"Bengaluru", "Pass", "Easybuzz"},
//	      {"São Paulo", "Pass", "Easybuzz"},

	        // Invalid but commonly tested → Fail
//	        {"12345", "Fail", "Easybuzz"},
//	        {"ThisCityNameIsWayTooLongToBeValidBecauseItExceedsMaximumLengthLimit", "Fail", "Easybuzz"},
//
//	        // Invalid city names → Fail
//	        {"@City", "Fail", "Easybuzz"},
//	        {"City@Name", "Fail", "Easybuzz"},
//	        {"", "Fail", "Easybuzz"},
//	        {null, "Fail", "Easybuzz"},
//	        {"A", "Fail", "Easybuzz"}
	    };
	}


    
	@DataProvider(name = "stateCodeProvider")
	public Object[][] stateCodeProvider() {
	    return new Object[][]{

	        // Valid state codes → Pass
	        {"QLD", "Pass", "Easybuzz"},
//	      {"NSW", "Pass", "Easybuzz"},
//	      {"VIC", "Pass", "Easybuzz"},
//	      {"CA", "Pass", "Easybuzz"},
//	      {"NY", "Pass", "Easybuzz"},
//	      {"MH", "Pass", "Easybuzz"},
//	      {"DL", "Pass", "Easybuzz"},
//	      {"ON", "Pass", "Easybuzz"},
//	      {"BC", "Pass", "Easybuzz"},

	        // Invalid state codes → Fail
//	      {"qld", "Fail", "Easybuzz"},   // lowercase
//	      {"QLDD", "Fail", "Easybuzz"},  // more than 3 chars
//	      {"C4", "Fail", "Easybuzz"},    // alphanumeric
	        {"@", "Fail", "Easybuzz"},     // special char
	        {"", "Fail", "Easybuzz"},      // empty
	        {null, "Fail", "Easybuzz"},    // null
//	      {"XYZ", "Fail", "Easybuzz"},   // not allowed
	        {"MUM", "Fail", "Easybuzz"}    // city code, not state
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

        	{"Rohitman Sharma","5553042241984105","07/28","123","Easybuzz"},
        };
    }
    
    @DataProvider(name = "CreateCCCardData")
    public Object[][] CreateCustomerSessioncardData() {
        return new Object[][] {
     
        	{"Rohitman Sharma","5553042241984105","07/28","123","Easybuzz"},
        };
    }
    @DataProvider(name = "streetAddressProvider")
    public static Object[][] streetAddressProvider() {
        return new Object[][]{

            // Valid street addresses → Pass
//          {"123 Main St", "Pass", "Easybuzz"},
            {"221B Baker Street", "Pass", "Easybuzz"},
//          {"742 Evergreen Terrace", "Pass", "Easybuzz"},
//          {"1600 Amphitheatre Parkway", "Pass", "Easybuzz"},
//          {"5th Avenue", "Pass", "Easybuzz"},
//          {"MG Road", "Pass", "Easybuzz"},
//          {"No. 45, High Street", "Pass", "Easybuzz"},
//          {"10 Downing St", "Pass", "Easybuzz"},

            // Invalid street addresses → Fail
            {null, "Fail", "Easybuzz"},
            {"", "Fail", "Easybuzz"},
            {"     ", "Fail", "Easybuzz"},   // only spaces
            {"@", "Fail", "Easybuzz"},
            {"####", "Fail", "Easybuzz"},
//          {"1234567890123456789012345678901234567890123456789012345678901234567890", "Fail", "Easybuzz"}, // too long
//          {"12", "Fail", "Easybuzz"},      // too short
//          {"Main", "Fail", "Easybuzz"},    // no number
//          {"123", "Fail", "Easybuzz"}      // number only
        };
    }


    @DataProvider(name = "zipCodeProvider")
    public Object[][] zipCodeProvider() {
        return new Object[][] {

            // Valid ZIP / Postal codes → Pass
//          {"10001", "Pass", "Easybuzz"},          // US ZIP
//          {"94105-1234", "Pass", "Easybuzz"},     // US ZIP+4
//          {"M5V 3L9", "Pass", "Easybuzz"},        // Canada
//          {"SW1A 1AA", "Pass", "Easybuzz"},       // UK
//          {"560001", "Pass", "Easybuzz"},         // India
//          {"10115", "Pass", "Easybuzz"},          // Germany

            // Invalid ZIP / Postal codes → Fail
//          {"12", "Fail", "Easybuzz"},             // too short
//          {"12345678901", "Fail", "Easybuzz"},    // too long
//          {"123@#", "Fail", "Easybuzz"},          // special chars
            {"", "Fail", "Easybuzz"},               // empty
            {"     ", "Fail", "Easybuzz"},          // spaces only
            {null, "Fail", "Easybuzz"}              // null
        };
    }


    @DataProvider(name = "currencyProvider")
    public static Object[][] currencyProvider() {
        return new Object[][]{

            // Valid ISO currency codes → Pass
//          {"USD", "Pass", "Easybuzz"},
//          {"INR", "Pass", "Easybuzz"},
//          {"JPY", "Pass", "Easybuzz"},

            // Invalid currency codes → Fail
            {"inr", "Fail", "Easybuzz"},     // lowercase
            {"XYZ", "Fail", "Easybuzz"},     // not supported
            {" US D ", "Fail", "Easybuzz"},  // spaces
            {"US1", "Fail", "Easybuzz"},     // alphanumeric
            {"EU@", "Fail", "Easybuzz"},     // special char
            {"123", "Fail", "Easybuzz"},     // numbers only
            {"@#$", "Fail", "Easybuzz"},     // special chars
            {"", "Fail", "Easybuzz"},        // empty
            {null, "Fail", "Easybuzz"},      // null
            {"USDIND", "Fail", "Easybuzz"}   // length > 3
        };
    }


    @DataProvider(name = "productNameProvider")
    public static Object[][] productNameProvider() {
        return new Object[][]{

            // Valid product names → Pass
            {"Book", "Pass", "Easybuzz"},
//          {"Laptop", "Pass", "Easybuzz"},
//          {"New Ebook Gaming Cards", "Pass", "Easybuzz"},

            // Invalid product names → Fail
            {"X", "Fail", "Easybuzz"},     // too short
            {"", "Fail", "Easybuzz"},      // empty
            {null, "Fail", "Easybuzz"}     // null
        };
    }



    @DataProvider(name = "brandIdProvider")
    public static Object[][] brandIdProvider() {
        return new Object[][]{

            // Valid Brand IDs (UUID) → Pass
            {"858d754d-106a-4c1d-b36a-820d82c84b8b", "Pass", "Easybuzz"},
            {"858D754D-106A-4C1D-B36A-820D82C84B8B", "Pass", "Easybuzz"},
            {"858d754d-106a-4C1D-b36a-820D82C84B8B", "Pass", "Easybuzz"},

            // Invalid Brand IDs → Fail
            {"858d754d-106a-41-b36a-82082c84b8b", "Fail", "Easybuzz"},
            {"858d754d-106a4c1db36a820d82c84b8b", "Fail", "Easybuzz"},
            {"858d754d-106a-4c1d-b36a-820d82c84b8b123", "Fail", "Easybuzz"},
            {"858d754d-106a-4c1d-b36a-820d82c84b8bz", "Fail", "Easybuzz"},
            {"@@@d858d754d-106a-4c1d-b36a-820d82c84b8b", "Fail", "Easybuzz"},
            {"huagsuyush", "Fail", "Easybuzz"},
            {"", "Fail", "Easybuzz"},
            {null, "Fail", "Easybuzz"},
            {"aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee", "Fail", "Easybuzz"}
        };
    }

    
    

}
