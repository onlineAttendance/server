package com.sungam1004.register.domain.admin.controller;

import com.sungam1004.register.domain.attendance.application.AttendanceSaveService;
import com.sungam1004.register.domain.attendance.application.AttendanceToggleService;
import com.sungam1004.register.domain.user.application.UserManageService;
import com.sungam1004.register.domain.user.dto.UserDetailDto;
import com.sungam1004.register.domain.user.dto.UserManagerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/manager")
@Slf4j
public class AdminUserManageController {
    private final UserManageService userManageService;
    private final AttendanceSaveService attendanceSaveService;
    private final AttendanceToggleService attendanceToggleService;

    @GetMapping
    public String userManagerHome(Model model) {
        List<UserManagerDto> users = userManageService.findUserAll();
        model.addAttribute("users", users);
        return "admin/userManage/home";
    }

    @GetMapping("detail/{userId}")
    public String userDetailForm(@PathVariable Long userId, Model model) {
        UserDetailDto userDetailDto = userManageService.userDetail(userId);
        model.addAttribute("userDetailDto", userDetailDto);
        return "admin/userManage/userDetail";
    }

    @GetMapping("change")
    public String changeAttendance(@RequestParam Long userId, @RequestParam String date) {
        attendanceToggleService.toggleAttendance(userId, date);
        return "redirect:/admin/manager/detail/" + userId;
    }

    @GetMapping("password/{userId}")
    @ResponseBody
    public void resetPassword(@PathVariable Long userId) {
        userManageService.resetPassword(userId);
    }
}
