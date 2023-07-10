package com.sungam1004.register.domain.attendance.application;

import com.sungam1004.register.domain.attendance.dto.AttendanceDto;
import com.sungam1004.register.domain.attendance.entity.Attendance;
import com.sungam1004.register.domain.attendance.repository.AttendanceRepository;
import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceFindService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public AttendanceDto.Response findTodayAttendanceByTeam(Team team) {
        List<User> users = userRepository.findByTeam(team);
        List<Attendance> attendances = findAttendanceByUserList(users);
        AttendanceDto.Response response = AttendanceDto.Response.FromTeam(team);
        for (User user : users) {
            addPersonToResponseBody(user, attendances, response);
        }
        return response;
    }

    private void addPersonToResponseBody(User user, List<Attendance> attendances, AttendanceDto.Response response) {
        boolean isAttendance = attendances.stream()
                .anyMatch(attendance -> {
                    Long userId = attendance.getUser().getId();
                    return userId.equals(user.getId());
                });

        response.addPerson(isAttendance, user.getName(), user.getFaceImageUri());
    }

    private List<Attendance> findAttendanceByUserList(List<User> users) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        List<Long> userIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        return attendanceRepository.findAttendanceByTeamAndDate(userIds, startOfToday);
    }
}
