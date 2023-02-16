package com.sungam1004.register.domain.attendance.application;

import com.sungam1004.register.domain.attendance.dto.AttendanceDto;
import com.sungam1004.register.domain.attendance.entity.Attendance;
import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.attendance.repository.AttendanceRepository;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.global.exception.ApplicationException;
import com.sungam1004.register.global.exception.ErrorCode;
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
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final PasswordManager passwordManager;

    public Team saveAttendance(Long userId, String password) {
        if (!passwordManager.isCorrectAttendancePassword(password)) {
            throw new ApplicationException(ErrorCode.INCORRECT_PASSWORD);
        }
        //if (!validSunday()) throw new CustomException(ErrorCode.INVALID_DAY_OF_WEEK);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER));

        if (attendanceRepository.existsByUserAndCreatedAtAfter(user, LocalDate.now().atStartOfDay())) {
            throw new ApplicationException(ErrorCode.DUPLICATE_ATTENDANCE);
        }

        Attendance attendance = new Attendance(user);
        attendanceRepository.save(attendance);
        user.increaseAttendanceNumber();
        log.info("출석이 성공적으로 저장되었습니다. name={}, dateTime={}", user.getName(), attendance.getCreatedAt());
        return user.getTeam();
    }

    private boolean validSunday() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        return dayOfWeek.getValue() == 7; // 월=1, 일=7
    }

    @Transactional(readOnly = true)
    public AttendanceDto.Response findTodayAttendanceByTeam(Team team) {
        List<User> users = userRepository.findByTeam(team);

        AttendanceDto.Response response = new AttendanceDto.Response(team.name());
        LocalDateTime startDatetime = LocalDate.now().atStartOfDay();
        for (User user : users) {
            boolean isAttendance = attendanceRepository.existsByUserAndCreatedAtAfter(user, startDatetime);
            response.addPerson(isAttendance, user.getName(), user.getFaceImageUri());
        }
        return response;
    }

    public void changeUserPassword(String password) {
        passwordManager.changeAttendancePassword(password);
    }

    public void toggleAttendance(Long userId, String strDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER));
        LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);

        Optional<Attendance> optionalAttendance
                = attendanceRepository.findByUserAndCreatedAtBetween(user, date.atStartOfDay(), date.atTime(LocalTime.MAX));
        if (optionalAttendance.isPresent()) {
            attendanceRepository.delete(optionalAttendance.get());
            user.decreaseAttendanceNumber();
            return;
        }

        attendanceRepository.save(new Attendance(user, date.atStartOfDay()));
        user.increaseAttendanceNumber();
    }
}
