package com.sungam1004.register.controller.admin;

import com.sungam1004.register.dto.AdminPasswordDto;
import com.sungam1004.register.dto.UserPasswordDto;
import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;
import com.sungam1004.register.service.AdminPasswordService;
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
public class AdminChangePasswordController {

    private final AdminPasswordService adminPasswordService;

    @GetMapping("/userPassword")
    public String changeUserPasswordForm(Model model) {
        model.addAttribute("userPasswordDto", new UserPasswordDto.Request());
        return "admin/password/changeUserPassword";
    }

    @PostMapping("/userPassword")
    public String changeUserPassword(@Valid @ModelAttribute("userPasswordDto") UserPasswordDto.Request requestDto,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/password/changeUserPassword";
        }
        try {
            adminPasswordService.changeUserPassword(requestDto.getPassword());
        } catch (CustomException e) {
            if (e.getError() == ErrorCode.NOT_FORMAT_MATCH_USER_PASSWORD) {
                bindingResult.rejectValue("password", "0", e.getMessage());
            }
            return "admin/password/changeUserPassword";
        }
        return "admin/password/completeChangePassword";
    }

    @GetMapping("/adminPassword")
    public String changeAdminPasswordForm(Model model) {
        model.addAttribute("adminPasswordDto", new AdminPasswordDto.Request());
        return "admin/password/changeAdminPassword";
    }

    @PostMapping("/adminPassword")
    public String changeAdminPassword(@Valid @ModelAttribute("adminPasswordDto") AdminPasswordDto.Request requestDto,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/password/changeAdminPassword";
        }
        try {
            adminPasswordService.changeAdminPassword(requestDto.getPassword());
        } catch (CustomException e) {
            if (e.getError() == ErrorCode.NOT_FORMAT_MATCH_USER_PASSWORD) {
                bindingResult.rejectValue("password", "0", e.getMessage());
            }
            return "admin/password/changeAdminPassword";
        }
        return "admin/password/completeChangePassword";
    }

}

