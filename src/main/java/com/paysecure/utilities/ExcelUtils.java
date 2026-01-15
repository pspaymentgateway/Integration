package com.paysecure.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;

public class ExcelUtils {

    /* =========================================================
       CARTESIAN DATA (Email × PSPCards)
       ========================================================= */

    public static Object[][] getCartesianData(
            String filePath,
            String emailSheet,
            String cardSheet) {

        // ❌ Email → NO RunFlag check
        List<Map<String, String>> emailData =
                getAllRows(filePath, emailSheet);

        // ✅ PSPCards → RunFlag = TRUE
        List<Map<String, String>> cardData =
                getRunnableRows(filePath, cardSheet);

        List<Object[]> finalData = new ArrayList<>();

        for (Map<String, String> emailRow : emailData) {
            for (Map<String, String> cardRow : cardData) {
                finalData.add(new Object[]{emailRow, cardRow});
            }
        }

        return finalData.toArray(new Object[0][0]);
    }

    /* =========================================================
       READ ALL ROWS (NO RUNFLAG) → EMAIL SHEET
       ========================================================= */

    private static List<Map<String, String>> getAllRows(
            String filePath,
            String sheetName) {

        List<Map<String, String>> allData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                Map<String, String> dataMap = new HashMap<>();

                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String key = getCellValueAsString(headerRow.getCell(j));
                    String value = getCellValueAsString(row.getCell(j));
                    dataMap.put(key, value);
                }

                allData.add(dataMap);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error reading Email sheet", e);
        }

        return allData;
    }

    /* =========================================================
       READ ONLY RUNFLAG = TRUE → PSPCARDS SHEET
       ========================================================= */

    private static List<Map<String, String>> getRunnableRows(
            String filePath,
            String sheetName) {

        List<Map<String, String>> runnableData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            Row headerRow = sheet.getRow(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                Map<String, String> dataMap = new HashMap<>();

                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String key = getCellValueAsString(headerRow.getCell(j));
                    String value = getCellValueAsString(row.getCell(j));
                    dataMap.put(key, value);
                }

                // ✅ RunFlag check ONLY HERE
                String runFlag = dataMap.getOrDefault("RunFlag", "").trim();
                if ("TRUE".equalsIgnoreCase(runFlag)) {
                    runnableData.add(dataMap);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error reading PSPCards sheet", e);
        }

        return runnableData;
    }

    /* =========================================================
       UTILITY METHODS
       ========================================================= */

    private static String getCellValueAsString(Cell cell) {
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
                return String.valueOf(cell.getBooleanCellValue()).toUpperCase();

            case FORMULA:
                try {
                    FormulaEvaluator evaluator =
                            cell.getSheet().getWorkbook()
                                    .getCreationHelper()
                                    .createFormulaEvaluator();
                    return getCellValueAsString(evaluator.evaluateInCell(cell));
                } catch (Exception e) {
                    return "";
                }

            default:
                return "";
        }
    }

    private static boolean isRowEmpty(Row row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !getCellValueAsString(cell).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
