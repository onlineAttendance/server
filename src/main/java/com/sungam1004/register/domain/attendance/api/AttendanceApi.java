package com.sungam1004.register.domain.attendance.api;

import com.sungam1004.register.domain.attendance.dto.AttendanceDto;
import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.attendance.application.AttendanceService;
import com.sungam1004.register.domain.user.application.UserAccountService;
import com.sungam1004.register.global.resolver.UserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/users/attendances")
public class AttendanceApi {

    private final AttendanceService attendanceService;
    private final UserAccountService userAccountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AttendanceDto.Response saveAttendance(@UserId Long userId, @Valid @RequestBody AttendanceDto.Request requestDto) {
        Team team = attendanceService.saveAttendance(userId, requestDto.getPassword());
        return attendanceService.findTodayAttendanceByTeam(team);
    }

    @GetMapping
    public AttendanceDto.Response findTeamAttendance(@UserId Long userId) {
        Team team = userAccountService.findTeam(userId);
        return attendanceService.findTodayAttendanceByTeam(team);
    }
}
