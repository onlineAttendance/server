package com.sungam1004.register.domain.service;

import com.sungam1004.register.domain.dto.AttendanceDto;
import com.sungam1004.register.domain.entity.Attendance;
import com.sungam1004.register.domain.entity.Team;
import com.sungam1004.register.domain.entity.User;
import com.sungam1004.register.domain.repository.AttendanceRepository;
import com.sungam1004.register.domain.repository.UserRepository;
import com.sungam1004.register.global.exception.CustomException;
import com.sungam1004.register.global.exception.ErrorCode;
import com.sungam1004.register.global.manager.PasswordManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


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
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
        //if (!validSunday()) throw new CustomException(ErrorCode.INVALID_DAY_OF_WEEK);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (attendanceRepository.existsByUserAndCreatedAtAfter(user, LocalDate.now().atStartOfDay())) {
            throw new CustomException(ErrorCode.DUPLICATE_ATTENDANCE);
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
}
