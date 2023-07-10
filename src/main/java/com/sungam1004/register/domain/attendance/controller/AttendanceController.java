package com.sungam1004.register.domain.attendance.controller;

import com.sungam1004.register.domain.attendance.application.AttendanceSaveService;
import com.sungam1004.register.domain.attendance.dto.AttendanceControllerDto;
import com.sungam1004.register.global.exception.ApplicationException;
import com.sungam1004.register.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("attendance")
public class AttendanceController {

    private final AttendanceSaveService attendanceSaveService;

    @GetMapping
    public String attendancePage(Model model) {
        model.addAttribute("AttendanceRequestDto", new AttendanceControllerDto());
        return "attendance/attendance";
    }

    @PostMapping
    public String saveAttendance(@Valid @ModelAttribute("AttendanceRequestDto") AttendanceControllerDto requestDto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("bindingResult = " + bindingResult);
            return "attendance/attendance";
        }

        try {
            attendanceSaveService.saveAttendanceForController(requestDto.getName(), requestDto.getPassword());
            //redirectAttributes.addAttribute("team", team);
            return "redirect:/attendance/completeAttendance";
        } catch (ApplicationException e) {
            if (e.getError() == ErrorCode.NOT_FOUND_USER) {
                bindingResult.rejectValue("name", "0", e.getMessage());
            }
            if (e.getError() == ErrorCode.INCORRECT_PASSWORD) {
                bindingResult.rejectValue("password", "0", e.getMessage());
            }
            if (e.getError() == ErrorCode.INVALID_DAY_OF_WEEK || e.getError() == ErrorCode.DUPLICATE_ATTENDANCE) {
                bindingResult.addError(new ObjectError(e.getError().name(), e.getMessage())); // 글로벌 오류
            }
            return "attendance/attendance";
        }
    }

    @GetMapping("completeAttendance")
    public String attendanceCompletePage() {
        return "attendance/completeAttendance";
    }

}