package com.paysecure.utilities;

import java.io.File;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    public Object[][] readJsonData(String filePath, String jsonPath) throws Exception {

        String runFlagColumnName = PropertyReader.getProperty("runFlagColumnName");
        String requiredRunFlag = System.getProperty("runFlag", runFlagColumnName);

        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);

        List<Map<String, Object>> allRows = mapper.readValue(
                file, new TypeReference<List<Map<String, Object>>>() {});

        List<Object[]> filteredData = new ArrayList<>();

        for (Map<String, Object> row : allRows) {

            Map<String, Object> lowerCaseRow = new HashMap<>();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                lowerCaseRow.put(entry.getKey().toLowerCase(), entry.getValue());
            }

            if (lowerCaseRow.get("runflag").toString().equalsIgnoreCase(requiredRunFlag)) {

                filteredData.add(new Object[]{
                        lowerCaseRow.get(jsonPath.toLowerCase()), // DYNAMIC FIELD 🔥
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
