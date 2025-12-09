package com.paysecure.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelWriteUtility {
	public static void writeResult(String sheetName, String value1, String value2,String value3,String purchaseID) {
		String filePath = "C:\\Users\\LENOVO\\eclipse-workspace\\integrationApiUiTesting\\src\\test\\resource\\ExcelResultsFolder\\EmailData.xlsx";
	    try {
	        FileInputStream fis = new FileInputStream(filePath);
	        Workbook workbook = WorkbookFactory.create(fis);

	        Sheet sheet = workbook.getSheet(sheetName);

	        // If sheet doesn’t exist, create new sheet
	        if (sheet == null) {
	            sheet = workbook.createSheet(sheetName);
	        }

	        int lastRowNum = sheet.getLastRowNum() + 1;
	        Row row = sheet.createRow(lastRowNum);

	        row.createCell(0).setCellValue(value1); // Email / City / Anything
	        row.createCell(1).setCellValue(value2); // Status
	        row.createCell(2).setCellValue(value3); // Comment
	        row.createCell(3).setCellValue(purchaseID);
	        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	        row.createCell(4).setCellValue(timeStamp);
	        fis.close();
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
