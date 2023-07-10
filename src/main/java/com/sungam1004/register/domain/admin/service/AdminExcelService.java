package com.sungam1004.register.domain.admin.service;

import com.sungam1004.register.domain.admin.dto.StatisticsDto;
import com.sungam1004.register.domain.attendance.repository.AttendanceRepository;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.global.manager.ExcelManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<User> users = userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getTeam))
                .toList();

        StatisticsDto statistics = createStatistic(users);

        // 출석 현황을 콘솔에 출력합니다. (debug)
        // printConsole(statistics);

        return excelManager.createExcelFile(statistics);
    }

    private StatisticsDto createStatistic(List<User> users) {
        StatisticsDto statistics = StatisticsDto.FromUsers(users);
        attendanceRepository.findAll().forEach(attendance -> {
            String userName = attendance.getUser().getName();
            statistics.addAttendance(userName, attendance.getCreatedAt());
        });
        return statistics;
    }

    private void printConsole(StatisticsDto statistics) {
        List<StatisticsDto.NameAndAttendance> nameAndAttendances = statistics.getNameAndAttendanceList();
        for (StatisticsDto.NameAndAttendance nameAndAttendance : nameAndAttendances) {
            System.out.print(nameAndAttendance.getName() + " : ");
            for (LocalDateTime time : nameAndAttendance.getDateTimeList()) {
                if (time != null) System.out.print("O ");
                else System.out.print("X ");
            }
            System.out.println();
        }
    }
}
