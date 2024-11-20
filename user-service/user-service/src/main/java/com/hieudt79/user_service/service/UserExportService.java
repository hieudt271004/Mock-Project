package com.hieudt79.user_service.service;

import com.hieudt79.user_service.dto.res.UserRes;
import com.hieudt79.user_service.model.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserExportService {

    private XSSFWorkbook xssfWorkbook;

    private XSSFSheet xssfSheet;

    private List<UserRes> userResList;

    @Autowired
    private UserService userService;

    public UserExportService(List<UserRes> listUsers) {
        this.userResList = listUsers;
        xssfWorkbook = new XSSFWorkbook();
    }


    public void writeHeadline() {
        xssfSheet = xssfWorkbook.createSheet("User");
        Row row = xssfSheet.createRow(0);
        CellStyle cellStyle = xssfWorkbook.createCellStyle();
        XSSFFont xssfFont = xssfWorkbook.createFont();
        xssfFont.setBold(true);
        xssfFont.setFontHeight(16);
        cellStyle.setFont(xssfFont);

        createCell(row, 0, "Id", cellStyle);
        createCell(row, 1, "Full name", cellStyle);
        createCell(row, 2, "Gender", cellStyle);
        createCell(row, 3, "Date of birth", cellStyle);
        createCell(row, 4, "Phone", cellStyle);
        createCell(row, 5, "Email", cellStyle);
        createCell(row, 6, "Status", cellStyle);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        xssfSheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }


    public void writeDataLine() {
        int rowCount = 1;
        CellStyle style = xssfWorkbook.createCellStyle();
        XSSFFont font = xssfWorkbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for(UserRes userRes : userResList) {

            Row row = xssfSheet.createRow(rowCount++);
            int column = 0;
            createCell(row, column++, String.valueOf(userRes.getUserId()), style);
            createCell(row, column++, userRes.getFullName(), style);
            createCell(row, column++, userRes.getGender().toString(), style);
            createCell(row, column++, userRes.getBod().toString(), style);
            createCell(row, column++, userRes.getPhone(), style);
            createCell(row, column++, userRes.getEmail(), style);
            createCell(row, column++, userRes.getStatus().toString(), style);

        }

    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeadline();
        writeDataLine();

        ServletOutputStream stream = response.getOutputStream();
        xssfWorkbook.write(stream);
        xssfWorkbook.close();
        stream.close();
    }

}
