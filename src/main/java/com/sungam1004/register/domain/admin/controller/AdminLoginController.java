package com.sungam1004.register.domain.admin.controller;

import com.sungam1004.register.domain.admin.dto.LoginAdminDto;
import com.sungam1004.register.global.exception.ErrorCode;
import com.sungam1004.register.global.manager.PasswordManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin")
@Slf4j
public class AdminLoginController {
    private final PasswordManager passwordManager;

    @GetMapping("/login")
    public String loginAdminForm(Model model) {
        model.addAttribute("LoginAdminDto", new LoginAdminDto.Request());
        return "admin/loginAdmin";
    }

    @PostMapping("/login")
    public String loginAdmin(@Valid @ModelAttribute("LoginAdminDto") LoginAdminDto.Request requestDto,
                             BindingResult bindingResult, @RequestParam(defaultValue = "/admin") String redirectURL,
                             HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "admin/loginAdmin";
        }

        if (!passwordManager.isCorrectAdminPassword(requestDto.getPassword())) {
            String message = ErrorCode.INCORRECT_PASSWORD.getMessage();
            bindingResult.rejectValue("password", null, message);
            return "admin/loginAdmin";
        }

        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        // 세션에 로그인 회원 정보 보관
        session.setAttribute("Admin", "successLogin");
        return "redirect:" + redirectURL;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // getSession(false) 를 사용해야 함 (세션이 없더라도 새로 생성하면 안되기 때문, 없으면 null 반환)
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/admin/login";
    }
}

