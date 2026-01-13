package com.paysecure.utilities;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

public class DataProviders {

	@DataProvider(name = "cardsData")
	public Object[][] cardsData() {
		return new Object[][] { { "4444444444444448" } };
	}

	@DataProvider(name = "cardData")
	public Object[][] cardData() {
		return new Object[][] {

			//	{ "Rohitman Sharma", "4111111111111111", "07/29", "123", "Zaakpay" }, 
			//	{ "Rohitman Sharma", "4999992100017063", "08/28", "870", "EuroExchange" }, 
			{"Pass","Yash SHarma","5553042241984105","07/28","123","Easybuzz"}
		};
	}

	@DataProvider(name = "CreateCCCardData")
	public Object[][] CreateCustomerSessioncardData() {
		return new Object[][] {

				{ "Rohitman Sharma", "4111111111111111", "07/29", "123", "Zaakpay" }, };
	}

	@DataProvider(name = "EmailData")
	public Object[][] getCartesianData() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"purchaseResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "EmailData", "PSPCards");
	}
	
	@DataProvider(name = "CityProvider")
	public Object[][] getCityProvider() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"purchaseResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "City", "PSPCards");
	}
	
	@DataProvider(name = "CountryProvider")
	public Object[][] getCountryProvider() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"purchaseResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "Country", "PSPCards");
	}
	
	@DataProvider(name = "StreetAddressProvider")
	public Object[][] getStreetAddressProvider() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"purchaseResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "StreetAddress", "PSPCards");
	}

	@DataProvider(name = "ZipCodeProvider")
	public Object[][] getZipCodeProvider() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"purchaseResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "ZipCode", "PSPCards");
	}
	
	@DataProvider(name = "CurrencyProvider")
	public Object[][] getCurrencyProvider() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"purchaseResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "Currency", "PSPCards");
	}
	
	@DataProvider(name = "ProductNameProvider")
	public Object[][] getProductNameProvider() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"purchaseResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "ProductName", "PSPCards");
	}
	
	@DataProvider(name = "StateCodeProvider")
	public Object[][] getStateCodeProvider() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"purchaseResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "StateCode", "PSPCards");
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
