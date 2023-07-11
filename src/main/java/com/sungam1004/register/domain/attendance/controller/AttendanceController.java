package com.sungam1004.register.domain.attendance.controller;

import com.sungam1004.register.domain.attendance.application.AttendanceSaveService;
import com.sungam1004.register.domain.attendance.dto.AttendanceControllerDto;
import com.sungam1004.register.domain.attendance.exception.DuplicateAttendanceException;
import com.sungam1004.register.domain.attendance.exception.IncorrectPasswordException;
import com.sungam1004.register.domain.attendance.exception.InvalidDayOfWeekException;
import com.sungam1004.register.domain.user.exception.UserNotFoundException;
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
            return "attendance/attendance";
        }

        try {
            attendanceSaveService.saveAttendanceForController(requestDto.getName(), requestDto.getPassword());
            return "redirect:/attendance/completeAttendance";
        } catch (UserNotFoundException | IncorrectPasswordException |
                 InvalidDayOfWeekException | DuplicateAttendanceException e) {
            if (e instanceof UserNotFoundException) {
                bindingResult.rejectValue("name", null, e.getMessage());
            }
            if (e instanceof IncorrectPasswordException) {
                bindingResult.rejectValue("password", null, e.getMessage());
            }
            if (e instanceof InvalidDayOfWeekException || e instanceof DuplicateAttendanceException) {
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