package com.paysecure.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriteUtility {

   

    private static String getExcelPath(String fileName) {
        String projectDir = System.getProperty("user.dir");
        String excelFolder = "src/test/resources/ExcelResultsFolder";

        File folder = new File(projectDir, excelFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return Paths.get(folder.getAbsolutePath(), fileName).toString();
    }

  

    public static void writeResult(
            String sheetName,
            String testData,
            String expected,
            String actual,
            String status,
            String transactionId,
            String psp,
            String paymentMethod) {

        String filePath = getExcelPath("Results.xlsx");
        writeExcel(sheetName, testData, expected, actual, status,
                transactionId, psp, paymentMethod, filePath);
    }

    public static void writeResults2s(
            String sheetName,
            String testData,
            String expected,
            String actual,
            String status,
            String transactionId,
            String psp,
            String paymentMethod) {

        String filePath = getExcelPath("Results.xlsx");
        writeExcel(sheetName, testData, expected, actual, status,
                transactionId, psp, paymentMethod, filePath);
    }


    public static synchronized void writeExcel(
            String sheetName,
            String testData,
            String expected,
            String actual,
            String status,
            String transactionId,
            String psp,
            String paymentMethod,
            String filePath) {

        File file = new File(filePath);
        XSSFWorkbook workbook = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {

            /* ===== Create file if not exists ===== */
            if (!file.exists()) {
                workbook = new XSSFWorkbook();
                try (FileOutputStream temp = new FileOutputStream(file)) {
                    workbook.write(temp);
                }
                workbook.close();
                workbook = null;
            }

            /* ===== Open workbook ===== */
            fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);

            /* ===== Create styles ===== */
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle passStyle = createStatusStyle(
                    workbook, IndexedColors.GREEN, IndexedColors.WHITE);
            CellStyle failStyle = createStatusStyle(
                    workbook, IndexedColors.RED, IndexedColors.WHITE);

            /* ===== Get or create sheet ===== */
            XSSFSheet sheet = workbook.getSheet(sheetName);
            boolean isNewSheet = false;

            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
                createHeader(sheet, headerStyle);
                isNewSheet = true;
            }

            /* ===== Create data row ===== */
            int rowNum = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(rowNum);

            row.createCell(0).setCellValue(testData);
            row.createCell(1).setCellValue(expected);
            row.createCell(2).setCellValue(actual);

            /* ===== STATUS CELL (COLOR BASED ON EXPECTED VS ACTUAL) ===== */
            Cell statusCell = row.createCell(3);

            if (expected != null && actual != null) {
                String exp = expected.trim().toUpperCase();
                String act = actual.trim().toUpperCase();
                
                // Test PASSED: Expected and Actual match
                if (exp.equals(act)) {
                    statusCell.setCellStyle(passStyle);  // GREEN
                } 
                // Test FAILED: Expected and Actual don't match
                else {
                    statusCell.setCellStyle(failStyle);  // RED
                }
            }

            statusCell.setCellValue(status);

            row.createCell(4).setCellValue(transactionId);
            row.createCell(5).setCellValue(psp);
            row.createCell(6).setCellValue(paymentMethod);

            String timeStamp =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            row.createCell(7).setCellValue(timeStamp);

            /* ===== Auto-size only for new sheets ===== */
            if (isNewSheet) {
                for (int i = 0; i <= 7; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            /* ===== Write back ===== */
            fis.close();
            fis = null;

            fos = new FileOutputStream(file);
            workbook.write(fos);

        } catch (Exception e) {
            System.err.println("Error writing to Excel: " + e.getMessage());
            e.printStackTrace();
        } finally {
            close(fis);
            close(fos);
            close(workbook);
        }
    }

    /* ================= STYLES ================= */

    private static CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle createStatusStyle(
            XSSFWorkbook workbook,
            IndexedColors bgColor,
            IndexedColors fontColor) {

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(bgColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(fontColor.getIndex());
        style.setFont(font);

        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }

    /* ================= HEADER ================= */

    private static void createHeader(XSSFSheet sheet, CellStyle style) {
        Row header = sheet.createRow(0);

        String[] headers = {
                "Test Data",
                "Expected Outcome",
                "Actual Outcome",
                "Status",
                "Transaction ID",
                "PSP",
                "Payment Method",
                "Timestamp"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }

        sheet.createFreezePane(0, 1);
    }

    private static void close(AutoCloseable c) {
        try {
            if (c != null) c.close();
        } catch (Exception ignored) {}
    }
}