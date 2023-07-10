package com.sungam1004.register.domain.user.application;

import com.sungam1004.register.domain.admin.dto.StatisticsDto;
import com.sungam1004.register.domain.attendance.entity.Attendance;
import com.sungam1004.register.domain.attendance.repository.AttendanceRepository;
import com.sungam1004.register.domain.user.dto.UserDetailDto;
import com.sungam1004.register.domain.user.dto.UserManagerDto;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.global.exception.NotFoundException;
import com.sungam1004.register.global.manager.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserManageService {
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserManagerDto> findUserAll() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getTeam))
                .map(UserManagerDto::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDetailDto userDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);

        StatisticsDto statistics = createStatistic(user);
        List<UserDetailDto.AttendanceDate> attendanceDates = createAttendanceDates(statistics);

        return UserDetailDto.of(user, attendanceDates);
    }

    private List<UserDetailDto.AttendanceDate> createAttendanceDates(StatisticsDto statistics) {
        List<LocalDateTime> dateTimes = statistics.getNameAndAttendanceList().get(0).getDateTimeList();
        List<String> sundayDates = StatisticsDto.date;

        List<UserDetailDto.AttendanceDate> attendanceDates = new ArrayList<>();
        for (int i = 0; i < dateTimes.size(); i++) {
            String sundayDate = sundayDates.get(i);
            LocalDateTime attendanceDateTime = dateTimes.get(i);
            UserDetailDto.AttendanceDate attendanceDate
                    = UserDetailDto.AttendanceDate.fromDateAndDateTime(sundayDate, attendanceDateTime);
            attendanceDates.add(attendanceDate);
        }
        return attendanceDates;
    }

    private StatisticsDto createStatistic(User user) {
        StatisticsDto statistics = StatisticsDto.FromUser(user);
        List<Attendance> attendances = attendanceRepository.findByUser(user);
        for (Attendance attendance : attendances) {
            statistics.addAttendance(user.getName(), attendance.getCreatedAt());
        }
        return statistics;
    }

    public void resetPassword(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);

        final String initUserPassword = "1234";
        String password = passwordEncoder.encrypt(initUserPassword);
        user.updateUserPassword(password);
    }
}
