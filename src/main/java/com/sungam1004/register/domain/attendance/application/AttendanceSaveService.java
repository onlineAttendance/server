package com.sungam1004.register.domain.attendance.application;

import com.sungam1004.register.domain.attendance.entity.Attendance;
import com.sungam1004.register.domain.attendance.exception.DuplicateAttendanceException;
import com.sungam1004.register.domain.attendance.exception.IncorrectPasswordException;
import com.sungam1004.register.domain.attendance.exception.InvalidDayOfWeekException;
import com.sungam1004.register.domain.attendance.repository.AttendanceRepository;
import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.exception.UserNotFoundException;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.global.manager.PasswordManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AttendanceSaveService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final PasswordManager passwordManager;

    public Team saveAttendance(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        validToSaveAttendance(user, password);
        saveAttendanceInDB(user);

        return user.getTeam();
    }

    public void saveAttendanceForController(String name, String password) {
        User user = userRepository.findByName(name)
                .orElseThrow(UserNotFoundException::new);

        validToSaveAttendance(user, password);
        saveAttendanceInDB(user);
    }


    private void saveAttendanceInDB(User user) {
        Attendance attendance = new Attendance(user);
        attendanceRepository.save(attendance);
        user.increaseAttendanceNumber();
        log.info("출석이 성공적으로 저장되었습니다. name={}, dateTime={}", user.getName(), attendance.getCreatedAt());
    }

    private void validToSaveAttendance(User user, String password) {
        validPassword(password);
        validSunday();
        validDuplicateAttendance(user);
    }

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

    private void validSunday() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        // 월=1, 일=7
        if (dayOfWeek.getValue() != DayOfWeek.SUNDAY.getValue()) {
            throw new InvalidDayOfWeekException();
        }
    }

    private void validPassword(String password) {
        if (!passwordManager.isCorrectAttendancePassword(password)) {
            throw new IncorrectPasswordException();
        }
    }

    private void validDuplicateAttendance(User user) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        if (attendanceRepository.existsByUserAndCreatedAtAfter(user, startOfToday)) {
            throw new DuplicateAttendanceException();
        }
    }
}
