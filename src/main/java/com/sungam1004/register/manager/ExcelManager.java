package com.sungam1004.register.manager;

import com.sungam1004.register.domain.Team;
import com.sungam1004.register.dto.StatisticsDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
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
    public String fileName;

    public String createExcelFile(StatisticsDto statistics) {
        fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ".xlsx";

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("출석부");


        /**
         * 날짜
         */
        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);

        CellStyle cellStyle_Date_Border = workbook.createCellStyle();
        cellStyle_Date_Border.setBorderRight(BorderStyle.MEDIUM);
        cellStyle_Date_Border.setAlignment(HorizontalAlignment.CENTER);

        CellStyle cellStyle_Center = workbook.createCellStyle();
        cellStyle_Center.setAlignment(HorizontalAlignment.CENTER);

        CellStyle cellStyle_Center_And_BottomLine = workbook.createCellStyle();
        cellStyle_Center_And_BottomLine.setAlignment(HorizontalAlignment.CENTER);
        cellStyle_Center_And_BottomLine.setBorderBottom(BorderStyle.THIN);

        for (int i = 0; i < StatisticsDto.date.size(); i++) {
            String[] split = StatisticsDto.date.get(i).split("-");
            Cell cell = row.createCell(i + 1);
            cell.setCellValue(split[1] + "-" + split[2]);
            cell.setCellStyle(cellStyle_Center_And_BottomLine);
            sheet.setColumnWidth(i + 1, 2000);
        }

        /**
         * 유저
         */
        List<StatisticsDto.NameAndAttendance> nameAndAttendances = statistics.getNameAndAttendances();
        for (StatisticsDto.NameAndAttendance nameAndAttendance : nameAndAttendances) {
            row = sheet.createRow(rowNum++);
            int cellNum = 0;
            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(nameAndAttendance.getName());

            CellStyle cellStyle_Team_Color = workbook.createCellStyle();
            cellStyle_Team_Color.setFillForegroundColor(getTeamColor(nameAndAttendance.getTeam()));  // 배경색
            cellStyle_Team_Color.setFillPattern(FillPatternType.SOLID_FOREGROUND);    //채우기 적용
            cell.setCellStyle(cellStyle_Team_Color);

            for (LocalDateTime time : nameAndAttendance.getDateTimes()) {
                cell = row.createCell(cellNum++);
                cell.setCellStyle(cellStyle_Center);
                if (time != null) {
                    if (time.getHour() == 0 && time.getMinute() == 0 && time.getSecond() == 0)
                        cell.setCellValue("관리자");
                    else cell.setCellValue(time.format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss")));
                }
            }
        }


        /**
         * 합계 함수
         */
        CellStyle cellStyle_Sum_Border = workbook.createCellStyle();
        cellStyle_Sum_Border.setBorderTop(BorderStyle.DOUBLE); //테두리 위쪽
        cellStyle_Sum_Border.setAlignment(HorizontalAlignment.CENTER);

        row = sheet.createRow(rowNum++);
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

        CellStyle cellStyle_Double_And_Border = workbook.createCellStyle();
        cellStyle_Double_And_Border.setBorderTop(BorderStyle.DOUBLE); //테두리 위쪽
        cellStyle_Double_And_Border.setAlignment(HorizontalAlignment.CENTER);
        cellStyle_Double_And_Border.setBorderRight(BorderStyle.MEDIUM);

        CellStyle cellStyle_Date_Limit = workbook.createCellStyle();
        cellStyle_Date_Limit.setBorderRight(BorderStyle.MEDIUM);
        cellStyle_Date_Limit.setBorderBottom(BorderStyle.THIN);
        cellStyle_Date_Limit.setAlignment(HorizontalAlignment.CENTER);

        for (int i = 0; i < StatisticsDto.date.size(); i++) {
            String[] split = StatisticsDto.date.get(i).split("-");
            if (24 < Integer.parseInt(split[2]) || Integer.parseInt(split[1]) == 9 && Integer.parseInt(split[2]) == 24) {
                row = sheet.getRow(0);
                cell = row.getCell(i + 1);
                cell.setCellStyle(cellStyle_Date_Limit);
                for (int j = 1; j < rowNum - 1; j++) {
                    row = sheet.getRow(j);
                    cell = row.getCell(i + 1);
                    cell.setCellStyle(cellStyle_Date_Border);
                }
                row = sheet.getRow(rowNum - 1);
                cell = row.getCell(i + 1);
                cell.setCellStyle(cellStyle_Double_And_Border);
            }
        }

        saveFile(workbook);
        return fileName;
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

    private void saveFile(XSSFWorkbook workbook) {
        try {
            if (new File(filePath).mkdir()) log.info("디렉터리가 생성되었습니다.");
            FileOutputStream out = new FileOutputStream(new File(filePath, fileName));
            workbook.write(out);
            out.close();
            log.info("엑셀 파일이 저장되었습니다. fileFullPath={}", filePath + "/" + fileName);
        } catch (IOException e) {
            log.error("엑셀 저장에 실패했습니다.");
            e.printStackTrace();
        }
    }

    private String getColAlphabet(int index) {
        if (index >= 0 && index < 26) return String.valueOf((char) (index + 65));
        else if (index < 52) return "A" + ((char) (index + 65 - 26));
        else if (index < 78) return "B" + ((char) (index + 65 - 26 * 2));
        else if (index < 104) return "C" + ((char) (index + 65 - 26 * 3));
        else return "";
    }
}
