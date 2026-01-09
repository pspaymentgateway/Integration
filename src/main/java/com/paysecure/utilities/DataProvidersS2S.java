package com.paysecure.utilities;

import org.testng.annotations.DataProvider;

public class DataProvidersS2S {
	
	@DataProvider(name = "cardData")
	public Object[][] cardData() {
		return new Object[][] {

				{ "Rohitman Sharma", "4111111111111111", "07/29", "123", "Zaakpay" }, 
				{ "Rohitman Sharma", "4999992100017063", "08/28", "870", "EuroExchange" }, 
		};
	}

    @DataProvider(name = "cityStatusProvider")
    public Object[][] cityStatusProvider() {
        return new Object[][]{

            // City       Status   Expected
            {"Mumbai",   "Pass",  true},
            {"New York", "Pass",  true},
            {"Chennai",  "Pass",  true},

            {"C4",       "Fail",  false},
            {"@City",    "Fail",  false},
            {"123",      "Fail",  false},
            {"QLDD@",    "Fail",  false},
            {"",         "Fail",  false},
            {null,       "Fail",  false}
        };
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
