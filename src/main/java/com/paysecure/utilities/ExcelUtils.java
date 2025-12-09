package com.paysecure.utilities;


import java.io.File;
import java.io.FileInputStream;


import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

	 public static Object[][] readExcel(String filePath, String sheetName) {
		 String runFlagColumnName = PropertyReader.getProperty("runFlagColumnName");
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



}

