package com.paysecure.utilities;

import java.nio.file.Paths;
import org.testng.annotations.DataProvider;

public class dataProvidersForCC {

	@DataProvider(name = "RoutingData")
	public static Object[][] getRoutingData() {

	    String testDataPath = Paths.get(
	        System.getProperty("user.dir"),
	        "src", "test", "resources", "ExcelResultsFolder",
	        "Cards.xlsx"
	    ).toString();

	    return ExcelUtils.getAllIntegrationData(
	        testDataPath,
	        "CC"
	        
	    );
	}


}
