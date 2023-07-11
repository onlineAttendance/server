package com.sungam1004.register.domain.admin.controller;

import com.sungam1004.register.domain.admin.dto.AdminPasswordDto;
import com.sungam1004.register.global.exception.NotFormatMatchAdminPasswordException;
import com.sungam1004.register.global.manager.PasswordManager;
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

    private final PasswordManager passwordManager;

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
            passwordManager.changeAdminPassword(requestDto.getPassword());
        } catch (NotFormatMatchAdminPasswordException e) {
            bindingResult.rejectValue("password", null, e.getMessage());
            return "admin/password/changeAdminPassword";
        }
        return "admin/password/completeChangePassword";
    }

}

