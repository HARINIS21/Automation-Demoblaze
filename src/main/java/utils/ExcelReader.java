package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    private static final String FILE_PATH = System.getProperty("user.dir") + "/src/test/resources/testdata.xlsx";

    public Object[][] getTestData(String sheetName) {
        List<Object[]> records = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            int colCount = sheet.getRow(0).getLastCellNum(); // Header row defines column count

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Object[] rowData = new Object[colCount];
                boolean isEmpty = true;

                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    String value = "";

                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                value = cell.getStringCellValue().trim();
                                break;
                            case NUMERIC:
                                double numValue = cell.getNumericCellValue();
                                if (numValue == Math.floor(numValue)) {
                                    value = String.valueOf((int) numValue);
                                } else {
                                    value = String.valueOf(numValue);
                                }
                                break;
                            case BOOLEAN:
                                value = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                value = cell.getCellFormula();
                                break;
                            default:
                                value = "";
                        }
                    }

                    rowData[j] = value;
                    if (!value.isEmpty()) {
                        isEmpty = false;
                    }
                }

                if (!isEmpty) {
                    records.add(rowData);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return records.toArray(new Object[0][0]);
    }
}