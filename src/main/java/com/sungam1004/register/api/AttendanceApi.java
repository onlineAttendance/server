package com.sungam1004.register.api;

import com.sungam1004.register.dto.AttendanceDto;
import com.sungam1004.register.service.AttendanceService;
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
    public AttendanceDto.Response saveAttendance(@Valid @RequestBody AttendanceDto.Request requestDto) {
        String team = attendanceService.saveAttendance(requestDto.getName(), requestDto.getPassword());
        return attendanceService.findTodayAttendanceByTeam(team);
    }

}
