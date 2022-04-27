/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 *
 * @author EDWARD-PC
 */
public class ExportUtil {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Map<String, Object>> list;
    private String[] visibleObjects;
    private String[] cabecera;
    private static final int tamanioBody = 14;
    public ExportUtil(List<Map<String, Object>> list, String[] cabecera, String[] visibleObjects) {
        this.list = list;
        this.cabecera = cabecera;
        this.visibleObjects = visibleObjects;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        int cont = 0;
        for (String cabTitle : cabecera) {
            createCell(row, cont++, cabTitle, style);
        }

//        createCell(row, 1, "E-mail", style);       
//        createCell(row, 2, "Full Name", style);    
//        createCell(row, 3, "Roles", style);
//        createCell(row, 4, "Enabled", style);
    }

    private CellStyle styleDecimal() {
        String pattern = "#.00";
        CellStyle styleDecimal = workbook.createCellStyle(); // Font and alignment
        styleDecimal.setDataFormat(workbook.createDataFormat().getFormat(pattern));
        XSSFFont font = workbook.createFont();
        font.setFontHeight(tamanioBody);
        styleDecimal.setFont(font);
        return styleDecimal;
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof BigDecimal || value instanceof Double || value instanceof Float) {
            cell.setCellValue(new Double(String.valueOf(value)));
            cell.setCellType(CellType.NUMERIC);
            cell.setCellStyle(styleDecimal());
            return;
        } else {
            cell.setCellValue(String.valueOf(value));
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(tamanioBody);
        style.setFont(font);

        for (Map<String, Object> objectsArray : list) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (String visbleObject : visibleObjects) {
                for (Map.Entry<String, Object> entry : objectsArray.entrySet()) {
                    //                String key = entry.getKey();
                    if (visbleObject.equals(entry.getKey())) {
                        Object value = entry.getValue();
                        createCell(row, columnCount++, value, style);
                        break;
                    }

                }
            }

//            for (Object object1 : objectsArray) {
//                columnCount++;
//                createCell(row, columnCount++, object1, style);
//            }
//            createCell(row, columnCount++, user.getEmail(), style);
//            createCell(row, columnCount++, user.getFullName(), style);
//            createCell(row, columnCount++, user.getRoles().toString(), style);
//            createCell(row, columnCount++, user.isEnabled(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
