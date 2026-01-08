package com.paysecure.utilities;


import java.io.File;
import java.io.FileInputStream;


import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

	 public static Object[][] readExcel(String filePath, String sheetName) {
		 String runFlagColumnName = PropertyReader.getPropertyForPurchase("runFlagColumnName");
	        try {
	            FileInputStream fis = new FileInputStream(new File(filePath));
	            Workbook workbook = WorkbookFactory.create(fis);
	            Sheet sheet = workbook.getSheet(sheetName);

	            int lastRow = sheet.getLastRowNum();
	            List<Object[]> dataList = new ArrayList<>();

	            // Read header to get column count
	            Row header = sheet.getRow(0);
	            int totalCols = header.getLastCellNum();

	            // RunFlag column → last column
	            int runFlagIndex = totalCols - 1;

	            for (int i = 1; i <= lastRow; i++) {
	                Row row = sheet.getRow(i);
	                if (row == null) continue;

	                // Check RunFlag column
	                Cell runCell = row.getCell(runFlagIndex);
	                String runFlag = (runCell == null) ? "" : runCell.getStringCellValue().trim();

	                // Skip rows where RunFlag != YES
	                if (!runFlag.equalsIgnoreCase(runFlagColumnName)) {
	                    continue;
	                }

	                // Prepare row data excluding RunFlag column
	                Object[] rowData = new Object[runFlagIndex];

	                for (int j = 0; j < runFlagIndex; j++) {
	                    Cell cell = row.getCell(j);
	                    String value = "";

	                    if (cell != null) {
	                        switch (cell.getCellType()) {
	                            case STRING:
	                                value = cell.getStringCellValue().trim();
	                                break;
	                            case NUMERIC:
	                                value = new java.math.BigDecimal(cell.getNumericCellValue()).toPlainString();
	                                break;
	                            case BOOLEAN:
	                                value = String.valueOf(cell.getBooleanCellValue());
	                                break;
	                            default:
	                                value = "";
	                        }
	                    }

	                    rowData[j] = value;
	                }

	                dataList.add(rowData);
	            }

	            workbook.close();
	            return dataList.toArray(new Object[0][]);

	        } catch (Exception e) {
	            e.printStackTrace();
	            return new Object[0][];  
	        }
	    }

	 public static Object[][] readExcelData(String filePath, String sheetName) {
		    try {
		        FileInputStream fis = new FileInputStream(new File(filePath));
		        Workbook workbook = WorkbookFactory.create(fis);
		        Sheet sheet = workbook.getSheet(sheetName);

		        int lastRow = sheet.getLastRowNum();

		        // Read header → column count
		        Row header = sheet.getRow(0);
		        int totalCols = header.getLastCellNum();

		        List<Object[]> dataList = new ArrayList<>();

		        // Loop through rows (start from row 1 – skip header)
		        for (int i = 1; i <= lastRow; i++) {
		            Row row = sheet.getRow(i);
		            if (row == null) continue;

		            Object[] rowData = new Object[totalCols];

		            for (int j = 0; j < totalCols; j++) {
		                Cell cell = row.getCell(j);
		                String value = "";

		                if (cell != null) {
		                    switch (cell.getCellType()) {
		                        case STRING:
		                            value = cell.getStringCellValue().trim();
		                            break;
		                        case NUMERIC:
		                            value = new java.math.BigDecimal(cell.getNumericCellValue()).toPlainString();
		                            break;
		                        case BOOLEAN:
		                            value = String.valueOf(cell.getBooleanCellValue());
		                            break;
		                        default:
		                            value = "";
		                    }
		                }

		                rowData[j] = value;
		            }

		            dataList.add(rowData);
		        }

		        workbook.close();
		        return dataList.toArray(new Object[0][]);

		    } catch (Exception e) {
		        e.printStackTrace();
		        return new Object[0][];
		    }
		}
	 
	 public static Object[][] readFirstColumn(String filePath, String sheetName) {
		    try {
		        FileInputStream fis = new FileInputStream(new File(filePath));
		        Workbook workbook = WorkbookFactory.create(fis);
		        Sheet sheet = workbook.getSheet(sheetName);

		        int rowCount = sheet.getLastRowNum();
		        Object[][] data = new Object[rowCount + 1][1]; // one column

		        for (int i = 0; i <= rowCount; i++) {
		            data[i][0] = new DataFormatter().formatCellValue(sheet.getRow(i).getCell(0)); // column 0 only
		        }

		        workbook.close();
		        return data;

		    } catch (Exception e) {
		        e.printStackTrace();
		        return null;
		    }
		}

	
	 public static Object[][] getCartesianData(
	            String filePath,
	            String emailSheet,
	            String cardSheet) {

	        List<Map<String, String>> emailData =
	                getRunnableRows(filePath, emailSheet);

	        List<Map<String, String>> cardData =
	                getRunnableRows(filePath, cardSheet);

	        List<Object[]> finalData = new ArrayList<>();

	        // Cartesian product: Every email × Every card (both with RunFlag=TRUE)
	        for (Map<String, String> emailRow : emailData) {
	            for (Map<String, String> cardRow : cardData) {
	                finalData.add(new Object[]{emailRow, cardRow});
	            }
	        }

	        return finalData.toArray(new Object[0][0]);
	    }

	    // 🔹 Read sheet and return ONLY rows where RunFlag = TRUE
	    private static List<Map<String, String>> getRunnableRows(
	            String filePath,
	            String sheetName) {

	        List<Map<String, String>> runnableData = new ArrayList<>();

	        try (FileInputStream fis = new FileInputStream(filePath);
	             Workbook workbook = WorkbookFactory.create(fis)) {

	            Sheet sheet = workbook.getSheet(sheetName);
	            
	            if (sheet == null) {
	                throw new RuntimeException("Sheet not found: " + sheetName);
	            }
	            
	            Row headerRow = sheet.getRow(0);
	            
	            if (headerRow == null) {
	                throw new RuntimeException("Header row is empty in sheet: " + sheetName);
	            }

	            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	                Row row = sheet.getRow(i);
	                
	                // Skip completely empty rows
	                if (row == null || isRowEmpty(row)) {
	                    continue;
	                }

	                Map<String, String> dataMap = new HashMap<>();

	                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
	                    Cell headerCell = headerRow.getCell(j);
	                    Cell dataCell = row.getCell(j);

	                    String key = getCellValueAsString(headerCell);
	                    String value = getCellValueAsString(dataCell);

	                    dataMap.put(key, value);
	                }

	                // Only add rows where RunFlag = TRUE
	                String runFlag = dataMap.getOrDefault("RunFlag", "").trim();
	                if ("TRUE".equalsIgnoreCase(runFlag)) {
	                    runnableData.add(dataMap);
	                }
	            }

	        } catch (Exception e) {
	            throw new RuntimeException("Error reading Excel: " + sheetName, e);
	        }

	        return runnableData;
	    }

	    // 🔹 Safely get cell value as String (handles numeric, string, boolean, formula, blank)
	    private static String getCellValueAsString(Cell cell) {
	        if (cell == null) {
	            return "";
	        }

	        switch (cell.getCellType()) {
	            case STRING:
	                return cell.getStringCellValue().trim();

	            case NUMERIC:
	                if (DateUtil.isCellDateFormatted(cell)) {
	                    // Handle date cells
	                    return cell.getDateCellValue().toString();
	                } else {
	                    // Handle numeric cells (CVV, Card Number, etc.)
	                    double numValue = cell.getNumericCellValue();
	                    
	                    // ✅ Remove .0 from whole numbers
	                    if (numValue == (long) numValue) {
	                        return String.valueOf((long) numValue);  // 123.0 → "123"
	                    }
	                    return String.valueOf(numValue);  // 12.5 → "12.5"
	                }

	            case BOOLEAN:
	                return String.valueOf(cell.getBooleanCellValue()).toUpperCase();  // TRUE/FALSE

	            case FORMULA:
	                // Evaluate formula and return result
	                try {
	                    FormulaEvaluator evaluator = cell.getSheet().getWorkbook()
	                            .getCreationHelper().createFormulaEvaluator();
	                    CellValue cellValue = evaluator.evaluate(cell);
	                    
	                    switch (cellValue.getCellType()) {
	                        case STRING:
	                            return cellValue.getStringValue().trim();
	                        case NUMERIC:
	                            double formulaNum = cellValue.getNumberValue();
	                            if (formulaNum == (long) formulaNum) {
	                                return String.valueOf((long) formulaNum);
	                            }
	                            return String.valueOf(formulaNum);
	                        case BOOLEAN:
	                            return String.valueOf(cellValue.getBooleanValue()).toUpperCase();
	                        default:
	                            return "";
	                    }
	                } catch (Exception e) {
	                    return "";
	                }

	            case BLANK:
	                return "";

	            case ERROR:
	                return "";

	            default:
	                return "";
	        }
	    }

	    // 🔹 Check if entire row is empty
	    private static boolean isRowEmpty(Row row) {
	        if (row == null) {
	            return true;
	        }

	        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
	            Cell cell = row.getCell(i);
	            if (cell != null && cell.getCellType() != CellType.BLANK) {
	                String value = getCellValueAsString(cell);
	                if (!value.isEmpty()) {
	                    return false;
	                }
	            }
	        }
	        return true;
	    }
		


}

