package com.sungam1004.register.domain.attendance.controller;

import com.sungam1004.register.domain.attendance.application.AttendanceService;
import com.sungam1004.register.domain.attendance.dto.AttendancePasswordDto;
import com.sungam1004.register.global.exception.ApplicationException;
import com.sungam1004.register.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin")
@Slf4j
public class AdminAttendancePasswordController {

    private final AttendanceService attendanceService;

    @GetMapping("/userPassword")
    public String changeUserPasswordForm(Model model) {
        model.addAttribute("userPasswordDto", new AttendancePasswordDto.Request());
        return "admin/password/changeUserPassword";
    }

    @PostMapping("/userPassword")
    public String changeUserPassword(@Valid @ModelAttribute("userPasswordDto") AttendancePasswordDto.Request requestDto,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/password/changeUserPassword";
        }
        try {
            attendanceService.changeAttendancePassword(requestDto.getPassword());
        } catch (ApplicationException e) {
            if (e.getError() == ErrorCode.NOT_FORMAT_MATCH_USER_PASSWORD) {
                bindingResult.rejectValue("password", "0", e.getMessage());
            }
            return "admin/password/changeUserPassword";
        }
        return "admin/password/completeChangePassword";
    }
}
