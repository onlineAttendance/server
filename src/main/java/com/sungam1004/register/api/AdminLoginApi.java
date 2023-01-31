package com.sungam1004.register.api;

import com.sungam1004.register.dto.LoginAdminDto;
import com.sungam1004.register.service.AdminPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
@Slf4j
public class AdminLoginApi {
    private final AdminPasswordService adminPasswordService;

    @PostMapping("/login")
    public LoginAdminDto.Response loginAdmin(@Valid @RequestBody LoginAdminDto.Request requestDto, HttpServletRequest request) {
        adminPasswordService.loginAdmin(requestDto.getPassword());

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute("Admin", "successLogin");
        return new LoginAdminDto.Response(session.getId());
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        // getSession(false) 를 사용해야 함 (세션이 없더라도 새로 생성하면 안되기 때문, 없으면 null 반환)
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}

