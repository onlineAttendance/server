package com.sungam1004.register.domain.attendance.application;

import com.sungam1004.register.domain.attendance.dto.AttendanceDto;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
            throw new IncorrectPasswordException();
        }
        validSunday();

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (attendanceRepository.existsByUserAndCreatedAtAfter(user, LocalDate.now().atStartOfDay())) {
            throw new DuplicateAttendanceException();
        }

        Attendance attendance = new Attendance(user);
        attendanceRepository.save(attendance);
        user.increaseAttendanceNumber();
        log.info("출석이 성공적으로 저장되었습니다. name={}, dateTime={}", user.getName(), attendance.getCreatedAt());
        return user.getTeam();
    }

    private void validSunday() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        // 월=1, 일=7
        if (dayOfWeek.getValue() == 7) {
            throw new InvalidDayOfWeekException();
        }
    }

    @Transactional(readOnly = true)
    public AttendanceDto.Response findTodayAttendanceByTeam(Team team) {
        List<User> users = userRepository.findByTeam(team);

        AttendanceDto.Response response = new AttendanceDto.Response(team.name());
        LocalDateTime startDatetime = LocalDate.now().atStartOfDay();

        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        List<Attendance> attendances = attendanceRepository.findAttendanceByTeamAndDate(userIds, startDatetime);

        for (User user : users) {
            boolean isAttendance = attendances.stream().anyMatch(a -> a.getUser().getId().equals(user.getId()));
            response.addPerson(isAttendance, user.getName(), user.getFaceImageUri());
        }
        return response;
    }

    public void changeAttendancePassword(String password) {
        passwordManager.changeAttendancePassword(password);
    }

    public void toggleAttendance(Long userId, String strDate) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
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

    public void saveAttendanceForController(String name, String password) {
        User user = userRepository.findByName(name)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordManager.isCorrectAttendancePassword(password)) {
            throw new IncorrectPasswordException();
        }

        //validSunday();

        if (attendanceRepository.existsByUserAndCreatedAtAfter(user, LocalDate.now().atStartOfDay())) {
            throw new DuplicateAttendanceException();
        }

        Attendance attendance = new Attendance(user);
        attendanceRepository.save(attendance);
        user.increaseAttendanceNumber();
        log.info("출석이 성공적으로 저장되었습니다. name={}, dateTime={}", user.getName(), attendance.getCreatedAt());
    }
}
