package com.sungam1004.register.domain.api;

import com.sungam1004.register.domain.dto.AttendanceDto;
import com.sungam1004.register.domain.service.AttendanceService;
import com.sungam1004.register.global.resolver.UserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/attendance")
public class AttendanceApi {

    private final AttendanceService attendanceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AttendanceDto.Response saveAttendance(@UserId Long userId, @Valid @RequestBody AttendanceDto.Request requestDto) {
        String team = attendanceService.saveAttendance(userId, requestDto.getPassword());
        return attendanceService.findTodayAttendanceByTeam(team);
    }

}
