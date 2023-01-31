package com.sungam1004.register.controller.admin;

import com.sungam1004.register.dto.UserDetailDto;
import com.sungam1004.register.dto.UserManagerDto;
import com.sungam1004.register.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/manager")
@Slf4j
public class AdminUserManageController {
    private final AdminService adminService;

    @GetMapping
    public String userManagerHome(Model model) {
        List<UserManagerDto> users = adminService.findUserAll();
        model.addAttribute("users", users);
        return "admin/userManage/home";
    }

    @GetMapping("detail/{userId}")
    public String userDetailForm(@PathVariable Long userId, Model model) {
        UserDetailDto userDetailDto = adminService.userDetail(userId);
        model.addAttribute("userDetailDto", userDetailDto);
        return "admin/userManage/userDetail";
    }

    @GetMapping("change")
    public String changeAttendance(@RequestParam Long userId, @RequestParam String date) {
        adminService.toggleAttendance(userId, date);
        return "redirect:/admin/manager/detail/" + userId;
    }
}
