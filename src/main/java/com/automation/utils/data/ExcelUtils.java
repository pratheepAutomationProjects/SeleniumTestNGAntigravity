package com.automation.utils.data;

import com.automation.utils.logging.Log;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Apache POI utility for reading Excel spreadsheets into TestNG DataProviders.
 */
public final class ExcelUtils {

    private ExcelUtils() {}

    /**
     * Reads an Excel sheet and returns a 2D Object array suitable for TestNG DataProvider.
     * Each row after the header is returned as a Map of columnName -> cellValue.
     */
    public static Object[][] getSheetDataAsMap(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                Log.error("Sheet " + sheetName + " not found in " + filePath);
                return new Object[0][0];
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return new Object[0][0];
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = headerRow.getLastCellNum();

            List<String> headers = new ArrayList<>();
            DataFormatter formatter = new DataFormatter();

            for (int c = 0; c < colCount; c++) {
                headers.add(formatter.formatCellValue(headerRow.getCell(c)));
            }

            for (int r = 1; r < rowCount; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int c = 0; c < colCount; c++) {
                    String colName = headers.get(c);
                    String cellValue = formatter.formatCellValue(row.getCell(c));
                    rowData.put(colName, cellValue);
                }
                dataList.add(rowData);
            }

        } catch (IOException e) {
            Log.error("Failed to read Excel file from " + filePath + ": " + e.getMessage(), e);
        }

        Object[][] result = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            result[i][0] = dataList.get(i);
        }
        return result;
    }
}
