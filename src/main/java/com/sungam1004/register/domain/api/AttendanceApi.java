package com.sungam1004.register.domain.api;

import com.sungam1004.register.domain.dto.AttendanceDto;
import com.sungam1004.register.domain.entity.Team;
import com.sungam1004.register.domain.service.AttendanceService;
import com.sungam1004.register.domain.service.UserAccountService;
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
