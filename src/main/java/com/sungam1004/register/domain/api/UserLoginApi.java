package com.sungam1004.register.domain.api;

import com.sungam1004.register.domain.dto.LoginUserDto;
import com.sungam1004.register.domain.service.UserLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
@Slf4j
public class UserLoginApi {
    private final UserLoginService userLoginService;

    @PostMapping("/login")
    public LoginUserDto.Response loginUser(@Valid @RequestBody LoginUserDto.Request requestDto) {
        return userLoginService.loginUser(requestDto);
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

