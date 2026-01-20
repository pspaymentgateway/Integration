package com.paysecure.utilities;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

public class DataProvidersEndToEndFlow {

	
	@DataProvider(name = "EaseBuzzProvider")
	public Object[][] getEaseBuzzData() {

	    String cardsPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"
	    ).toString();

	    return ExcelUtils.getIntegrationData(
	            cardsPath,
	            "CardDataForEndtoEndFlow",//Sheet name
	            "easybuzz"        // integration name
	    );
	}
	
	@DataProvider(name = "EuroExchange")
	public Object[][] getEuroExchangeData() {

	    String cardsPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"
	    ).toString();

	    return ExcelUtils.getIntegrationData(
	            cardsPath,
	            "CardDataForEndtoEndFlow",//Sheet name
	            "EUROEXCHANGE"        // integration name
	    );
	}
	
	@DataProvider(name = "Matrix")
	public Object[][] getMatrixData() {

	    String cardsPath = Paths.get(
	            System.getProperty("user.dir"),
	            "src", "test", "resources", "ExcelResultsFolder",
	            "Cards.xlsx"
	    ).toString();

	    return ExcelUtils.getIntegrationData(
	            cardsPath,
	            "CardDataForEndtoEndFlow",//Sheet name
	            "Matrix"        // integration name
	    );
	}


}
