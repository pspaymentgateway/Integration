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

				{ "Rohitman Sharma", "4111111111111111", "07/29", "123", "Zaakpay" }, };
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

}
