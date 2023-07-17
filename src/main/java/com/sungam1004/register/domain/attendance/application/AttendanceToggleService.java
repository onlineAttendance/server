package com.sungam1004.register.domain.attendance.application;

import com.sungam1004.register.domain.attendance.entity.Attendance;
import com.sungam1004.register.domain.attendance.repository.AttendanceRepository;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.exception.UserNotFoundException;
import com.sungam1004.register.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AttendanceToggleService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public void toggleAttendance(Long userId, String strDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        attendanceRepository.findByUserAndCreatedAtBetween(user, startOfDay, endOfDay)
                .ifPresentOrElse(
                        attendance -> deleteAttendance(user, attendance),
                        () -> saveAttendance(user, startOfDay)
                );
    }

    private void deleteAttendance(User user, Attendance attendance) {
        attendanceRepository.delete(attendance);
        user.decreaseAttendanceNumber();
    }

    private void saveAttendance(User user, LocalDateTime attendanceDateTime) {
        Attendance attendance = new Attendance(user, attendanceDateTime);
        attendanceRepository.save(attendance);
        user.increaseAttendanceNumber();
    }
}
