package com.paysecure.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
    


    public static void writeResult(String sheetName, String value1, String value2, String value3, String purchaseID) {
        String filePath = getExcelPath("purchaseResults.xlsx");
        writeExcel(sheetName, value1, value2, value3, purchaseID, filePath);
    }

    public static void writeResults2s(String sheetName, String value1, String value2, String value3, String purchaseID) {
        String filePath = getExcelPath("s2sResults.xlsx");
        writeExcel(sheetName, value1, value2, value3, purchaseID, filePath);
    }

    public static void writeExcel(String sheetName, String value1, String value2, String value3, String purchaseID, String filePath) {
        File file = new File(filePath);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        XSSFWorkbook workbook = null;

        try {
            // Create workbook if not exists
            if (!file.exists()) {
                workbook = new XSSFWorkbook();
                fos = new FileOutputStream(file);
                workbook.write(fos);
                fos.close();
            }

            fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);

            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);

                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Test Data");
                header.createCell(1).setCellValue("Result");
                header.createCell(2).setCellValue("Status");
                header.createCell(3).setCellValue("Transaction ID");
                header.createCell(4).setCellValue("Timestamp");
            }

            int lastRow = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(lastRow);

            row.createCell(0).setCellValue(value1);
            row.createCell(1).setCellValue(value2);
            row.createCell(2).setCellValue(value3);
            row.createCell(3).setCellValue(purchaseID);
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            row.createCell(4).setCellValue(timeStamp);

            fis.close(); // Very important
            fos = new FileOutputStream(file);
            workbook.write(fos);

        } catch (Exception e) {
            System.out.println("Error writing result to Excel: " + e.getMessage());
        } finally {
            try { if (fis != null) fis.close(); } catch (Exception ignore) {}
            try { if (fos != null) fos.close(); } catch (Exception ignore) {}
            try { if (workbook != null) workbook.close(); } catch (Exception ignore) {}
        }
    }

}

