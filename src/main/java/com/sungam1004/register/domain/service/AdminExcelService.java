package com.sungam1004.register.domain.service;

import com.sungam1004.register.domain.dto.StatisticsDto;
import com.sungam1004.register.domain.entity.Attendance;
import com.sungam1004.register.domain.entity.User;
import com.sungam1004.register.domain.repository.AttendanceRepository;
import com.sungam1004.register.domain.repository.UserRepository;
import com.sungam1004.register.global.manager.ExcelManager;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminExcelService {
    private final ExcelManager excelManager;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;


    public String statistics() {
        StatisticsDto statistics = new StatisticsDto();
        statistics.setName(userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getTeam))
                .toList());

        List<Attendance> attendances = attendanceRepository.findAll();
        for (Attendance attendance : attendances) {
            statistics.addAttendance(attendance.getUser().getName(), attendance.getCreatedAt());
        }

        /**
         * Debug
         */
        {
            List<StatisticsDto.NameAndAttendance> nameAndAttendances = statistics.getNameAndAttendances();
            for (StatisticsDto.NameAndAttendance nameAndAttendance : nameAndAttendances) {
                System.out.print(nameAndAttendance.getName() + " : ");
                for (LocalDateTime time : nameAndAttendance.getDateTimes()) {
                    if (time != null) System.out.print("O ");
                    else System.out.print("X ");
                }
                System.out.println();
            }
        }
        return excelManager.createExcelFile(statistics);
    }
}
