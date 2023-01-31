package com.sungam1004.register.domain.api;

import com.sungam1004.register.domain.dto.ChangeAttendanceDto;
import com.sungam1004.register.domain.dto.UserDetailDto;
import com.sungam1004.register.domain.service.AdminService;
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
