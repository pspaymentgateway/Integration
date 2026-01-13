package com.paysecure.utilities;

import java.nio.file.Paths;

import org.testng.annotations.DataProvider;

public class DataProvidersS2S {
	

	@DataProvider(name = "Email")
	public Object[][] getEmailData() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"s2sResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "EmailData", "PSPCards");
	}
	
	@DataProvider(name = "CityData")
	public Object[][] getCityData() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"s2sResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "City", "PSPCards");
	}
	
	@DataProvider(name = "CountryData")
	public Object[][] getCountryData() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"s2sResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "Country", "PSPCards");
	}
	
	@DataProvider(name = "CurrencyData")
	public Object[][] getCurrencyData() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"s2sResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "Currency", "PSPCards");
	}
	
	@DataProvider(name = "ProductNameData")
	public Object[][] getProductNameData() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"s2sResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "ProductName", "PSPCards");
	}
	
	@DataProvider(name = "StateCodeData")
	public Object[][] getStateCodeData() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"s2sResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "StateCode", "PSPCards");
	}
	
	@DataProvider(name = "StreetAddressData")
	public Object[][] getStreetAddressData() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"s2sResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "StreetAddress", "PSPCards");
	}
	
	@DataProvider(name = "ZipCodeData")
	public Object[][] getZipCodeData() {
		String path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "ExcelResultsFolder",
				"s2sResults.xlsx").toString();

		return ExcelUtils.getCartesianData(path, "ZipCode", "PSPCards");
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
