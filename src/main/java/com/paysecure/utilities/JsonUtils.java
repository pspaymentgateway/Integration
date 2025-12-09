package com.paysecure.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	
	public Object[][] readJsonData(String FilePath,String jsonPath) throws Exception {
		 String runFlagColumnName = PropertyReader.getProperty("runFlagColumnName");
	    String requiredRunFlag = System.getProperty("runFlag",runFlagColumnName); // default

	    ObjectMapper mapper = new ObjectMapper();
	    File file = new File(FilePath);

	    List<Map<String, String>> allRows = mapper.readValue(file,
	            new TypeReference<List<Map<String, String>>>() {});

	    List<Object[]> filteredData = new ArrayList<>();

	    for (Map<String, String> row : allRows) {

	        // Convert all keys to lowercase
	        Map<String, String> lowerCaseRow = new java.util.HashMap<>();
	        for (Map.Entry<String, String> entry : row.entrySet()) {
	            lowerCaseRow.put(entry.getKey().toLowerCase(), entry.getValue());
	        }

	        if (lowerCaseRow.get("runflag").equalsIgnoreCase(requiredRunFlag)) {
	            filteredData.add(new Object[]{
	                    lowerCaseRow.get(jsonPath.toLowerCase()),
	                    lowerCaseRow.get("cardholder"),
	                    lowerCaseRow.get("cardnumber"),
	                    lowerCaseRow.get("expiry"),
	                    lowerCaseRow.get("cvc"),
	                    lowerCaseRow.get("runflag"),
	                    lowerCaseRow.get("psp")
	            });
	        }
	    }


	    return filteredData.toArray(new Object[0][0]);
	}

	
}
