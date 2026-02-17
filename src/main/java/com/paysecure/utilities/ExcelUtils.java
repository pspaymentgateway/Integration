package com.paysecure.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import org.apache.poi.ss.usermodel.*;

public class ExcelUtils {

    /* =========================================================
       CARTESIAN DATA PROVIDER
       (PSP/Cards × City/Email/TestData)
       ========================================================= */

	public static Object[][] getCartesianData(
	        String outerFilePath,
	        String outerSheet,
	        String innerFilePath,
	        String innerSheet) {

	    List<Map<String, String>> outerData =
	            readSheet(outerFilePath, outerSheet, true);

	    List<Map<String, String>> innerData =
	            readSheet(innerFilePath, innerSheet, false);

	    List<Object[]> finalData = new ArrayList<>();

	    
	    System.out.println("\n===== CARTESIAN DATA PROVIDER =====");
	    System.out.println("Card Data  rows: " + outerData.size());
	    System.out.println("Test Data rows: " + innerData.size());

	    for (Map<String, String> outerRow : outerData) {

	        System.out.println("\n--- Outer Row ---");

	        for (Map<String, String> innerRow : innerData) {

	            finalData.add(new Object[]{outerRow, innerRow});

	            System.out.println("  --> "
	                    + outerRow + " + " + innerRow);
	        }
	    }
	    

	    System.out.println("\n===== TOTAL TEST CASES: "
	            + finalData.size() + " =====\n");

	    return finalData.toArray(new Object[0][0]);
	}


    /* =========================================================
       INTEGRATION-SPECIFIC DATA
       ========================================================= */

    public static Object[][] getIntegrationData(
            String filePath,
            String sheetName,
            String integrationName) {

        List<Object[]> data = new ArrayList<>();

        for (Map<String, String> row :
                readSheet(filePath, sheetName, true)) {

            if (integrationName.equalsIgnoreCase(
                    row.getOrDefault("PSP", "").trim())) {
                data.add(new Object[]{row});
            }
        }
        return data.toArray(new Object[0][0]);
    }

    public static Object[][] getAllIntegrationData(
            String filePath,
            String sheetName) {

        List<Object[]> data = new ArrayList<>();

        for (Map<String, String> row :
                readSheet(filePath, sheetName, true)) {
            data.add(new Object[]{row});
        }
        return data.toArray(new Object[0][0]);
    }

    /* =========================================================
       CORE EXCEL READER
       ========================================================= */

    private static List<Map<String, String>> readSheet(
            String filePath,
            String sheetName,
            boolean runFlagRequired) {

        List<Map<String, String>> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException(
                        "Sheet not found: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException(
                        "Header row missing in sheet: " + sheetName);
            }

            FormulaEvaluator evaluator =
                    workbook.getCreationHelper()
                            .createFormulaEvaluator();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row, evaluator)) {
                    continue;
                }

                Map<String, String> dataMap =
                        readRow(row, headerRow, evaluator);

                // Debug (optional)
                // System.out.println("HEADERS = " + dataMap.keySet());
                // System.out.println("RunFlag = [" + dataMap.get("RunFlag") + "]");

                if (!runFlagRequired || isRunnable(dataMap)) {
                    rows.add(dataMap);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error reading sheet: " + sheetName, e);
        }

        return rows;
    }

    /* =========================================================
       ROW & CELL HELPERS
       ========================================================= */

    private static Map<String, String> readRow(
            Row row,
            Row headerRow,
            FormulaEvaluator evaluator) {

        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            String key = getCellValue(headerRow.getCell(i), evaluator);
            String value = getCellValue(row.getCell(i), evaluator);
            map.put(key, value);
        }
        return map;
    }

    private static boolean isRunnable(Map<String, String> row) {

        for (Map.Entry<String, String> entry : row.entrySet()) {

            String header = entry.getKey()
                                 .replaceAll("\\s+", "")
                                 .trim();

            String value = entry.getValue() == null
                    ? ""
                    : entry.getValue().trim();

            if (header.equalsIgnoreCase("RunFlag")
                    && value.equalsIgnoreCase("TRUE")) {
                return true;
            }
        }
        return false;
    }


    private static String getCellValue(
            Cell cell,
            FormulaEvaluator evaluator) {

        if (cell == null) return "";

        switch (cell.getCellType()) {

            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double num = cell.getNumericCellValue();
                return (num == (long) num)
                        ? String.valueOf((long) num)
                        : String.valueOf(num);

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue())
                        .toUpperCase();

            case FORMULA:
                return getCellValue(
                        evaluator.evaluateInCell(cell), evaluator);

            default:
                return "";
        }
    }

    private static boolean isRowEmpty(
            Row row,
            FormulaEvaluator evaluator) {

        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (!getCellValue(row.getCell(i), evaluator).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public static List<Map<String, String>> readSheetAsMap(
            String filePath,
            String sheetName) {

        List<Map<String, String>> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new LinkedHashMap<>();

                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String key = headerRow.getCell(j).getStringCellValue();
                    String value = row.getCell(j) == null
                            ? ""
                            : row.getCell(j).toString();

                    rowData.put(key, value);
                }
                dataList.add(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }


}
