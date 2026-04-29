package com.paysecure.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import org.apache.poi.ss.usermodel.*;

public class ExcelUtl {

    private static final String FILEPATH =
            System.getProperty("user.dir")
            + "/src/test/resources/ExcelResultsFolder/TestData.xlsx";

    /* =========================================================
       SINGLE SHEET DATA PROVIDER (for CreateCustomerAndSession)
       ========================================================= */

    public static Object[][] getExcelDataAsMap(String filePath, String sheetName) {

        List<Map<String, String>> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found");
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row is null");
            }

            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            // ✅ Get actual number of columns with data
            int lastColumn = headerRow.getLastCellNum();

            System.out.println("📊 Reading Excel: " + sheetName);
            System.out.println("   Total Columns: " + lastColumn);
            System.out.println("   Data Rows: " + sheet.getLastRowNum());

            // Store headers
            List<String> headers = new ArrayList<>();
            for (int j = 0; j < lastColumn; j++) {
                Cell headerCell = headerRow.getCell(j);
                String headerValue = getCellValueAsString(headerCell, evaluator);
                headers.add(headerValue);
                System.out.println("   Column " + j + ": [" + headerValue + "]");
            }

            // Read data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row, evaluator)) {
                    continue;
                }

                Map<String, String> rowData = new LinkedHashMap<>();

                for (int j = 0; j < lastColumn; j++) {
                    String key = headers.get(j);
                    if (key.isEmpty()) continue;

                    Cell valueCell = row.getCell(j);
                    String value = getCellValueAsString(valueCell, evaluator);

                    rowData.put(key, value);
                }

                // ✅ Check RunFlag (if present)
                String runFlag = rowData.getOrDefault("RunFlag", "TRUE");
                if (runFlag.equalsIgnoreCase("TRUE")) {
                    dataList.add(rowData);
                    System.out.println("✓ Row " + (i + 1) + 
                                       ": PSP=" + rowData.get("PSP") + 
                                       " | PaymentMethod=" + rowData.get("PaymentMethod") +
                                       " | Currency=" + rowData.get("Currency") +
                                       " | Merchant=" + rowData.get("Merchant"));
                } else {
                    System.out.println("⊗ Row " + (i + 1) + " skipped (RunFlag=" + runFlag + ")");
                }
            }

            System.out.println("✅ Total rows loaded: " + dataList.size() + "\n");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read Excel: " + sheetName, e);
        }

        Object[][] result = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            result[i][0] = dataList.get(i);
        }

        return result;
    }

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
        System.out.println("Outer rows: " + outerData.size());
        System.out.println("Inner rows: " + innerData.size());

        for (Map<String, String> outerRow : outerData) {
            for (Map<String, String> innerRow : innerData) {
                finalData.add(new Object[]{outerRow, innerRow});
            }
        }

        System.out.println("===== TOTAL TEST CASES: " + finalData.size() + " =====\n");

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

        for (Map<String, String> row : readSheet(filePath, sheetName, true)) {
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

        for (Map<String, String> row : readSheet(filePath, sheetName, true)) {
            data.add(new Object[]{row});
        }
        return data.toArray(new Object[0][0]);
    }

    /* =========================================================
       CORE EXCEL READER (for Cartesian & Integration methods)
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
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row missing in sheet: " + sheetName);
            }

            FormulaEvaluator evaluator =
                    workbook.getCreationHelper().createFormulaEvaluator();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row, evaluator)) {
                    continue;
                }

                Map<String, String> dataMap = readRow(row, headerRow, evaluator);

                if (!runFlagRequired || isRunnable(dataMap)) {
                    rows.add(dataMap);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error reading sheet: " + sheetName, e);
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

        Map<String, String> map = new LinkedHashMap<>();

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            String key = getCellValueAsString(headerRow.getCell(i), evaluator);
            String value = getCellValueAsString(row.getCell(i), evaluator);
            map.put(key, value);
        }
        return map;
    }

    private static boolean isRunnable(Map<String, String> row) {
        for (Map.Entry<String, String> entry : row.entrySet()) {
            String header = entry.getKey().replaceAll("\\s+", "").trim();
            String value = entry.getValue() == null ? "" : entry.getValue().trim();

            if (header.equalsIgnoreCase("RunFlag") && value.equalsIgnoreCase("TRUE")) {
                return true;
            }
        }
        return false;
    }

    private static String getCellValueAsString(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) {
            return "";
        }

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
                return String.valueOf(cell.getBooleanCellValue()).toUpperCase();

            case FORMULA:
                return getCellValueAsString(evaluator.evaluateInCell(cell), evaluator);

            case BLANK:
                return "";

            default:
                return "";
        }
    }

    private static boolean isRowEmpty(Row row, FormulaEvaluator evaluator) {
        if (row == null) return true;
        
        for (int i = 0; i < row.getLastCellNum(); i++) {
            if (!getCellValueAsString(row.getCell(i), evaluator).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}