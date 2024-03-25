package com.findToilet.dataInserter.data.reader;

import lombok.Getter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Getter
public class XlsxReader implements DataReader {

    List<Map<String, String>> mapList = new ArrayList<>();

    public XlsxReader(String filePath, Map<String, String> fileToDatabaseMap) {
        readXlsxFile(filePath, fileToDatabaseMap);
    }

    private void readXlsxFile(String filePath, Map<String, String> fileToDatabaseMap) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            fillMapList(rowIterator, fileToDatabaseMap);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillMapList(Iterator<Row> rowIterator, Map<String, String> fileToDatabaseMap) {
        Map<Integer, String> indexMap = getIndexMapByFirstRow(fileToDatabaseMap, rowIterator.next());

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            Map<String, String> map = new HashMap<>();
            int index = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (indexMap.containsKey(index)) {
                    String key = indexMap.get(index);
                    String cellValue = getCellValue(cell);

                    if (cellValue.isEmpty() || cellValue.equals("")) {
                        index++;
                        continue;
                    }

                    if (!map.containsKey(key))
                        map.put(key, cellValue);
                }
                index++;
            }
            mapList.add(map);
        }
    }

    private Map<Integer, String> getIndexMapByFirstRow(Map<String, String> fileToDatabaseMap, Row row) {
        Map<Integer, String> map = new HashMap<>();
        Iterator<Cell> cellIterator = row.cellIterator();
        int index = 0;
        while (cellIterator.hasNext()) {
            String cellValue = getCellValue(cellIterator.next());
            if (fileToDatabaseMap.containsKey(cellValue)) {
                String databaseColumnName = fileToDatabaseMap.get(cellValue);
                map.put(index, databaseColumnName);
            }
            index++;
        }
        return map;
    }

    private String getCellValue(Cell cell) {
        cell.getCellType();
        return switch (cell.getCellType()) {
            case FORMULA -> cell.getCellFormula();
            case NUMERIC -> cell.getNumericCellValue() + "";
            case STRING -> cell.getStringCellValue() + "";
            case BLANK -> cell.getBooleanCellValue() + "";
            case ERROR -> cell.getErrorCellValue() + "";
            default -> null;
        };
    }

}