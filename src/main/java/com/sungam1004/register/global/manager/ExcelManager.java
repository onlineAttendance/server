package com.sungam1004.register.global.manager;

import com.sungam1004.register.domain.admin.dto.StatisticsDto;
import com.sungam1004.register.domain.user.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class ExcelManager {

    @Value("${file.path}")
    public String filePath;
    private static final String sheetName = "출석부";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public String createExcelFile(StatisticsDto statistics) {
        String fileName = createFileName();
        createWorkBookAndSheet();

        int rowNum = 0;
        List<StatisticsDto.NameAndAttendance> nameAndAttendances = statistics.getNameAndAttendanceList();

        rowNum = fillDate(rowNum);
        rowNum = fillUser(rowNum, nameAndAttendances);
        rowNum = fillSumFunction(rowNum, nameAndAttendances);
        setBorderStyleBetweenMonth(rowNum);

        saveFile(fileName);
        return fileName;
    }

    private String createFileName() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTime = LocalDateTime.now().format(dateTimeFormatter);
        String fileExtension = ".xlsx";
        return dateTime + fileExtension;
    }

    private void createWorkBookAndSheet() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(sheetName);
    }

    private int fillDate(int rowNum) {
        Row row = sheet.createRow(rowNum++);

        CellStyle cellStyle_Center_And_BottomLine = workbook.createCellStyle();
        cellStyle_Center_And_BottomLine.setAlignment(HorizontalAlignment.CENTER);
        cellStyle_Center_And_BottomLine.setBorderBottom(BorderStyle.THIN);

        for (int i = 0; i < StatisticsDto.date.size(); i++) {
            String[] split = StatisticsDto.date.get(i).split("-");
            Cell cell = row.createCell(i + 1);
            String monthAndDay = split[1] + "-" + split[2];
            cell.setCellValue(monthAndDay);
            cell.setCellStyle(cellStyle_Center_And_BottomLine);
            sheet.setColumnWidth(i + 1, 2000);
        }
        return rowNum;
    }

    private int fillUser(int rowNum, List<StatisticsDto.NameAndAttendance> nameAndAttendances) {
        CellStyle cellStyle_Center = workbook.createCellStyle();
        cellStyle_Center.setAlignment(HorizontalAlignment.CENTER);

        for (StatisticsDto.NameAndAttendance nameAndAttendance : nameAndAttendances) {
            Row row = sheet.createRow(rowNum++);
            int cellNum = 0;
            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(nameAndAttendance.getName());

            CellStyle cellStyle_Team_Color = workbook.createCellStyle();
            cellStyle_Team_Color.setFillForegroundColor(getTeamColor(nameAndAttendance.getTeam()));  // 배경색
            cellStyle_Team_Color.setFillPattern(FillPatternType.SOLID_FOREGROUND);    //채우기 적용
            cell.setCellStyle(cellStyle_Team_Color);

            for (LocalDateTime time : nameAndAttendance.getDateTimeList()) {
                cell = row.createCell(cellNum++);
                cell.setCellStyle(cellStyle_Center);
                if (time != null) {
                    if (isConfiguredByAdmin(time))
                        cell.setCellValue("관리자");
                    else {
                        String timeString = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        cell.setCellValue(timeString);
                    }
                }
            }
        }
        return rowNum;
    }

    private short getTeamColor(Team team) {
        // https://t1.daumcdn.net/cfile/tistory/23448C3F5953472C20?original
        if (team == Team.복덕방) return IndexedColors.LEMON_CHIFFON.getIndex();
        if (team == Team.복덕복덕) return IndexedColors.TAN.getIndex();
        if (team == Team.복권) return IndexedColors.LIME.getIndex();
        if (team == Team.또복) return IndexedColors.AQUA.getIndex();
        if (team == Team.복통) return IndexedColors.SEA_GREEN.getIndex();
        else return IndexedColors.CORAL.getIndex();
    }

    private boolean isConfiguredByAdmin(LocalDateTime attendanceDateTime) {
        return attendanceDateTime.getHour() == 0 && attendanceDateTime.getMinute() == 0 && attendanceDateTime.getSecond() == 0;
    }

    private int fillSumFunction(int rowNum, List<StatisticsDto.NameAndAttendance> nameAndAttendances) {
        CellStyle cellStyle_Sum_Border = workbook.createCellStyle();
        cellStyle_Sum_Border.setBorderTop(BorderStyle.DOUBLE); //테두리 위쪽
        cellStyle_Sum_Border.setAlignment(HorizontalAlignment.CENTER);

        Row row = sheet.createRow(rowNum++);
        Cell cell = row.createCell(0);
        cell.setCellValue("합계");
        cell.setCellStyle(cellStyle_Sum_Border);

        for (int i = 1; i <= StatisticsDto.date.size(); i++) {
            String colAlphabet = getColAlphabet(i);
            String formula = "COUNTA(" + colAlphabet + "2:" + colAlphabet + (nameAndAttendances.size() + 1) + ")";
            cell = row.createCell(i);
            cell.setCellFormula(formula);
            cell.setCellStyle(cellStyle_Sum_Border);
        }
        return rowNum;
    }

    private String getColAlphabet(int index) {
        if (0 <= index && index < 26) return String.valueOf((char) (index + 65));
        else if (index < 52) return "A" + ((char) (index + 65 - 26));
        else if (index < 78) return "B" + ((char) (index + 65 - 26 * 2));
        else if (index < 104) return "C" + ((char) (index + 65 - 26 * 3));
        else return ""; // error
    }

    private void setBorderStyleBetweenMonth(int rowNum) {
        CellStyle cellStyle_Date_Border = workbook.createCellStyle();
        cellStyle_Date_Border.setBorderRight(BorderStyle.MEDIUM);
        cellStyle_Date_Border.setAlignment(HorizontalAlignment.CENTER);

        CellStyle cellStyle_Double_And_Border = workbook.createCellStyle();
        cellStyle_Double_And_Border.setBorderTop(BorderStyle.DOUBLE); //테두리 위쪽
        cellStyle_Double_And_Border.setAlignment(HorizontalAlignment.CENTER);
        cellStyle_Double_And_Border.setBorderRight(BorderStyle.MEDIUM);

        CellStyle cellStyle_Date_Limit = workbook.createCellStyle();
        cellStyle_Date_Limit.setBorderRight(BorderStyle.MEDIUM);
        cellStyle_Date_Limit.setBorderBottom(BorderStyle.THIN);
        cellStyle_Date_Limit.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < StatisticsDto.date.size(); i++) {
            if (isLastDayOfMonth(StatisticsDto.date.get(i))) {
                Cell cell = getCell(0, i + 1);
                cell.setCellStyle(cellStyle_Date_Limit);
                for (int j = 1; j < rowNum - 1; j++) {
                    cell = getCell(j, i + 1);
                    cell.setCellStyle(cellStyle_Date_Border);
                }
                cell = getCell(rowNum - 1, i + 1);
                cell.setCellStyle(cellStyle_Double_And_Border);
            }
        }
    }

    private Cell getCell(int rowNum, int cellNum) {
        Row row = sheet.getRow(rowNum);
        return row.getCell(cellNum);
    }

    private boolean isLastDayOfMonth(String date) {
        String[] split = date.split("-");
        // 9월을 제외하고는 24일 이후가 마지막 일요일이다.
        return 24 < Integer.parseInt(split[2]) || (Integer.parseInt(split[1]) == 9 && Integer.parseInt(split[2]) == 24);
    }

    private void saveFile(String fileName) {
        try {
            makeDirectory();
            FileOutputStream outputStream = getOutputStream(fileName);
            workbook.write(outputStream);
            outputStream.close();
            log.info("엑셀 파일이 저장되었습니다. fileFullPath={}", filePath + "/" + fileName);
        } catch (IOException e) {
            log.error("엑셀 저장에 실패했습니다.");
            e.printStackTrace();
        }
    }

    private void makeDirectory() {
        boolean isCreatedDirectory = new File(filePath).mkdir();
        if (isCreatedDirectory) {
            log.info("디렉터리가 생성되었습니다.");
        }
    }

    private FileOutputStream getOutputStream(String fileName) throws FileNotFoundException {
        File file = new File(filePath, fileName);
        return new FileOutputStream(file);
    }
}
