package com.sungam1004.register.domain.user.application;

import com.sungam1004.register.domain.attendance.entity.Attendance;
import com.sungam1004.register.domain.attendance.repository.AttendanceRepository;
import com.sungam1004.register.domain.admin.dto.StatisticsDto;
import com.sungam1004.register.domain.user.dto.UserDetailDto;
import com.sungam1004.register.domain.user.dto.UserManagerDto;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.global.exception.CustomException;
import com.sungam1004.register.global.exception.ErrorCode;
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
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        StatisticsDto statistics = new StatisticsDto();
        statistics.setName(List.of(user));

        List<Attendance> attendances = attendanceRepository.findByUser(user);
        for (Attendance attendance : attendances) {
            statistics.addAttendance(user.getName(), attendance.getCreatedAt());
        }

        List<LocalDateTime> dateTimes = statistics.getNameAndAttendances().get(0).getDateTimes();
        List<String> date = StatisticsDto.date;
        List<UserDetailDto.AttendanceDate> attendanceDates = new ArrayList<>();
        for (int i = 0; i < dateTimes.size(); i++) {
            attendanceDates.add(new UserDetailDto.AttendanceDate(date.get(i), dateTimes.get(i)));
        }

        return UserDetailDto.of(user, attendanceDates);
    }


}
