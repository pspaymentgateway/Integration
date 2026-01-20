package com.paysecure.utilities;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

public class DataProviders {

//	@DataProvider(name = "cardsData")
//	public Object[][] cardsData() {
//		return new Object[][] { { "4444444444444448" } };
//	}

	@DataProvider(name = "cardData")
	public Object[][] cardData() {
		return new Object[][] {

			//	{ "Rohitman Sharma", "4111111111111111", "07/29", "123", "Zaakpay" }, 
			//	{ "Rohitman Sharma", "4999992100017063", "08/28", "870", "EuroExchange" }, 
			{"Pass","Yash SHarma","5553042241984105","07/28","123","Easybuzz"},
		//	{ "Pass", "Rohitman Sharma", "4012888888881881", "10/27", "000", "Matrix" }
		};
	}

	@DataProvider(name = "CreateCCCardData")
	public Object[][] CreateCustomerSessioncardData() {
		return new Object[][] {

				{ "Rohitman Sharma", "4111111111111111", "07/29", "123", "Zaakpay" }
		};
	}

	@DataProvider(name = "EmailData")
	public Object[][] getCartesianData() {

	    String emailPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "TestData.xlsx"
	    ).toString();

	    String pspCardPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"
	    ).toString();

	    return ExcelUtils.getCartesianData(
	            emailPath,     // OLD excel
	            "EmailData",   // Email sheet
	            pspCardPath,   // NEW excel
	            "Cards"     // PSP sheet
	    );
	}

	
	@DataProvider(name = "CityProvider")
	public Object[][] getCityProvider() {

	    String emailPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "TestData.xlsx"     // OLD Excel
	    ).toString();

	    String pspCardPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"             // NEW Excel
	    ).toString();

	    return ExcelUtils.getCartesianData(
	            emailPath,   // OLD Excel path
	            "City",      // Sheet from OLD Excel
	            pspCardPath, // NEW Excel path
	            "Cards"   // Sheet from NEW Excel
	    );
	}

	
	@DataProvider(name = "CountryProvider")
	public Object[][] getCountryProvider() {

	    String emailPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "TestData.xlsx"     // OLD Excel
	    ).toString();

	    String pspCardPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"             // NEW Excel
	    ).toString();

	    return ExcelUtils.getCartesianData(
	            emailPath,   // OLD Excel path
	            "Country",   // Sheet from OLD Excel
	            pspCardPath, // NEW Excel path
	            "Cards"   // Sheet from NEW Excel
	    );
	}

	@DataProvider(name = "StreetAddressProvider")
	public Object[][] getStreetAddressProvider() {

	    String emailPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "TestData.xlsx"     // OLD Excel
	    ).toString();

	    String pspCardPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"             // NEW Excel
	    ).toString();

	    return ExcelUtils.getCartesianData(
	            emailPath,          // OLD Excel path
	            "StreetAddress",    // Sheet from OLD Excel
	            pspCardPath,        // NEW Excel path
	            "Cards"          // PSP sheet
	    );
	}


	@DataProvider(name = "ZipCodeProvider")
	public Object[][] getZipCodeProvider() {

	    String emailPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "TestData.xlsx"     // OLD Excel
	    ).toString();

	    String pspCardPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"             // NEW Excel
	    ).toString();

	    return ExcelUtils.getCartesianData(
	            emailPath,    // OLD Excel path
	            "ZipCode",    // Sheet from OLD Excel
	            pspCardPath,  // NEW Excel path
	            "Cards"    // PSP sheet
	    );
	}

	
	@DataProvider(name = "CurrencyProvider")
	public Object[][] getCurrencyProvider() {

	    String dataPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "TestData.xlsx"     // OLD Excel (Currency sheet)
	    ).toString();

	    String pspCardPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"             // NEW Excel (PSP Cards)
	    ).toString();

	    return ExcelUtils.getCartesianData(
	            dataPath,      // OLD Excel path
	            "Currency",    // Sheet from OLD Excel
	            pspCardPath,   // NEW Excel path
	            "Cards"     // PSPCards sheet
	    );
	}

	
	@DataProvider(name = "ProductNameProvider")
	public Object[][] getProductNameProvider() {

	    String dataPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "TestData.xlsx"      // OLD Excel (ProductName sheet)
	    ).toString();

	    String pspCardPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"              // NEW Excel (PSP Cards)
	    ).toString();

	    return ExcelUtils.getCartesianData(
	            dataPath,          // OLD Excel
	            "ProductName",     // Sheet name
	            pspCardPath,       // NEW Excel
	            "Cards"         // PSP sheet
	    );
	}

	
	@DataProvider(name = "StateCodeProvider")
	public Object[][] getStateCodeProvider() {

	    String dataPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "TestData.xlsx"     // OLD Excel (StateCode)
	    ).toString();

	    String pspCardPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"            // NEW Excel (PSP Cards)
	    ).toString();

	    return ExcelUtils.getCartesianData(
	            dataPath,        // OLD Excel
	            "StateCode",     // Sheet name
	            pspCardPath,     // NEW Excel
	            "Cards"       // PSP sheet
	    );
	}

	
	 @DataProvider(name = "emailValidationData")
	    public Object[][] emailValidationData() {

	        return new Object[][] {

	            // ✅ VALID EMAILS
	        	 {"test@gmail.com", "Pass", "Matrix"},
	             {"user.name@yahoo.com", "Pass", "Matrix"},
	             {"user_123@outlook.com", "Pass", "Matrix"},
	             {"user-name@domain.co.in", "Pass", "Matrix"},

	             // ❌ INVALID EMAILS
	             {"testgmail.com", "Fail", "Matrix"},     // missing @
	             {"@gmail.com", "Fail", "Matrix"},        // missing username
	             {"test@gmail", "Fail", "Matrix"},        // missing domain
	             {"test@ gmail.com", "Fail", "Matrix"},   // space in email
	             {"test@domain@com", "Fail", "Matrix"},   // multiple @
	             {"", "Fail", "Matrix"},                  // empty
	             {null, "Fail", "Matrix"}                   // null
	        };
	    }
}
