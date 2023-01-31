package com.sungam1004.register.api;

import com.sungam1004.register.dto.ChangeAttendanceDto;
import com.sungam1004.register.dto.UserDetailDto;
import com.sungam1004.register.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/manager")
@Slf4j
public class AdminUserManageApi {
    private final AdminService adminService;

    @GetMapping("detail/{userId}")
    public UserDetailDto getUserDetail(@PathVariable Long userId) {
        return adminService.userDetail(userId);
    }

    @PatchMapping("attendance/{userId}")
    public void changeAttendance(@Valid @RequestBody ChangeAttendanceDto.Request dto, @PathVariable Long userId) {
        adminService.changeAttendance(userId, dto);
    }
}
