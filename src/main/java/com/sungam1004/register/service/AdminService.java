package com.sungam1004.register.service;

import com.sungam1004.register.domain.Attendance;
import com.sungam1004.register.domain.User;
import com.sungam1004.register.dto.*;
import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;
import com.sungam1004.register.repository.AttendanceRepository;
import com.sungam1004.register.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    public void addUser(AddUserDto.Request requestDto) {
        if (userRepository.existsByName(requestDto.getName()))
            throw new CustomException(ErrorCode.DUPLICATE_USER_NAME);
        User user = requestDto.toEntity();
        userRepository.save(user);
    }

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

    public void toggleAttendance(Long userId, String strDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);

        Optional<Attendance> optionalAttendance
                = attendanceRepository.findByUserAndCreatedAtBetween(user, date.atStartOfDay(), date.atTime(LocalTime.MAX));
        if (optionalAttendance.isPresent()) {
            attendanceRepository.delete(optionalAttendance.get());
            user.decreaseAttendanceNumber();
            user.increaseAbsenceNumber();
            return;
        }

        attendanceRepository.save(new Attendance(user, date.atStartOfDay()));
        user.decreaseAbsenceNumber();
        user.increaseAttendanceNumber();
    }

    public void changeAttendance(Long userId, ChangeAttendanceDto.Request dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        LocalDate date = LocalDate.parse(dto.getDate(), DateTimeFormatter.ISO_DATE);
        /**
         * TODO 검증 필요
         */
        if (dto.getAttendance()) {
            attendanceRepository.save(new Attendance(user, date.atStartOfDay()));
            user.decreaseAbsenceNumber();
            user.increaseAttendanceNumber();
        }
        else {
            Optional<Attendance> optionalAttendance
                    = attendanceRepository.findByUserAndCreatedAtBetween(user, date.atStartOfDay(), date.atTime(LocalTime.MAX));
            if (optionalAttendance.isPresent()) {
                attendanceRepository.delete(optionalAttendance.get());
                user.decreaseAttendanceNumber();
                user.increaseAbsenceNumber();
            }
        }
    }
}
