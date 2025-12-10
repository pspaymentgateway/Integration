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

public class ExcelWriteUtility {

    private static String getExcelPath(String fileName) {
        // Build path dynamically relative to project root
        String projectDir = System.getProperty("user.dir");
        String excelFolder = "src/test/resources/ExcelResultsFolder";
        File folder = new File(projectDir, excelFolder);
        if (!folder.exists()) {
            folder.mkdirs(); // create folder if it doesn't exist
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

    private static void writeExcel(String sheetName, String value1, String value2, String value3, String purchaseID, String filePath) {
        try {
            File file = new File(filePath);
            Workbook workbook;

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = WorkbookFactory.create(fis);
                fis.close();
            } else {
                workbook = WorkbookFactory.create(true); // create new workbook if not exists
            }

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }

            int lastRowNum = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(lastRowNum);

            row.createCell(0).setCellValue(value1);
            row.createCell(1).setCellValue(value2);
            row.createCell(2).setCellValue(value3);
            row.createCell(3).setCellValue(purchaseID);
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            row.createCell(4).setCellValue(timeStamp);

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            workbook.close();

            System.out.println("Excel Updated Successfully in Sheet: " + sheetName);
        } catch (Exception e) {
            System.out.println("Error writing result to Excel: " + e.getMessage());
        }
    }
}
