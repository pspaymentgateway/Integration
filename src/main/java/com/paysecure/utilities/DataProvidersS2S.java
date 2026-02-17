package com.paysecure.utilities;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

public class DataProvidersS2S {
	

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
	            pspCardPath,
	            "Cards",
	            emailPath,
	            "EmailData" 
	    );
	}


	
	@DataProvider(name = "CityProvider")
	public Object[][] getCityProvider() {

	    String cityPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "TestData.xlsx"
	    ).toString();

	    String cardPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"
	    ).toString();

	    return ExcelUtils.getCartesianData(
	        cardPath,   // outer = Cards
	        "Cards",
	        cityPath,   // inner = City
	        "City"
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
	    		  pspCardPath, // NEW Excel path
		            "Cards" ,
	            emailPath,   // OLD Excel path
	            "Country"   // Sheet from OLD Excel
	            // Sheet from NEW Excel
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
	         // Sheet from OLD Excel
	            pspCardPath,        // NEW Excel path
	            "Cards"  ,
	            emailPath,          // OLD Excel path
	            "StreetAddress"// PSP sheet
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
	    		   pspCardPath,  // NEW Excel path
		            "Cards",
	            emailPath,    // OLD Excel path
	            "ZipCode"    // Sheet from OLD Excel
	            // PSP sheet
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
	    		  pspCardPath,   // NEW Excel path
		            "Cards" ,
	            dataPath  ,    // OLD Excel path
	            "Currency"   // Sheet from OLD Excel
	              // PSPCards sheet
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
	    	    pspCardPath,       // NEW Excel
	            "Cards" ,
	            dataPath,          // OLD Excel
	            "ProductName"    // Sheet name
	                // PSP sheet
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
	    		  pspCardPath,     // NEW Excel
		            "Cards" ,
	            dataPath,        // OLD Excel
	            "StateCode"     // Sheet name
	                // PSP sheet
	    );
	}
	

}
