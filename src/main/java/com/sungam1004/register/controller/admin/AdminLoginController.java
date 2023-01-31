package com.sungam1004.register.controller.admin;

import com.sungam1004.register.dto.LoginAdminDto;
import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;
import com.sungam1004.register.service.AdminPasswordService;
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
    private final AdminPasswordService adminPasswordService;

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

        try {
            adminPasswordService.loginAdmin(requestDto.getPassword());
        } catch (CustomException e) {
            if (e.getError() == ErrorCode.INCORRECT_PASSWORD) {
                bindingResult.rejectValue("password", "0", e.getMessage());
            }
            return "admin/loginAdmin";
        }

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
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

