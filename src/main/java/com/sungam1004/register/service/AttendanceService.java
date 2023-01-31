package com.sungam1004.register.service;

import com.sungam1004.register.domain.Attendance;
import com.sungam1004.register.domain.Team;
import com.sungam1004.register.domain.User;
import com.sungam1004.register.dto.AttendanceDto;
import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;
import com.sungam1004.register.manager.PasswordManager;
import com.sungam1004.register.repository.AttendanceRepository;
import com.sungam1004.register.repository.UserRepository;
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

    public String saveAttendance(String name, String password) {
        if (!passwordManager.isCorrectUserPassword(password)) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
        //if (!validSunday()) throw new CustomException(ErrorCode.INVALID_DAY_OF_WEEK);

        User user = userRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (attendanceRepository.existsByUserAndCreatedAtAfter(user, LocalDate.now().atStartOfDay())) {
            throw new CustomException(ErrorCode.DUPLICATE_ATTENDANCE);
        }

        Attendance attendance = new Attendance(user);
        attendanceRepository.save(attendance);
        user.increaseAttendanceNumber();
        log.info("출석이 성공적으로 저장되었습니다. name={}, dateTime={}", name, attendance.getCreatedAt());
        return user.getTeam().toString();
    }

    private boolean validSunday() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        return dayOfWeek.getValue() == 7; // 월=1, 일=7
    }

    public AttendanceDto.Response findTodayAttendanceByTeam(String strTeam) {
        Team team = Team.convertTeamByString(strTeam);
        List<User> users = userRepository.findByTeam(team);

        AttendanceDto.Response response = new AttendanceDto.Response(strTeam);
        LocalDateTime startDatetime = LocalDate.now().atStartOfDay();
        for (User user : users) {
            if (attendanceRepository.existsByUserAndCreatedAtAfter(user, startDatetime))
                response.getAttendanceNames().add(user.getName());
            else response.getNotAttendanceNames().add(user.getName());
        }
        double ratio = (double) response.getAttendanceNames().size()*100 / users.size();
        response.setRatio((int) Math.round(ratio));
        System.out.println(ratio);
        return response;
    }
}
