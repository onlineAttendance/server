package com.sungam1004.register.domain.user.api;

import com.sungam1004.register.domain.user.dto.LoginUserDto;
import com.sungam1004.register.domain.user.application.UserLoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@Slf4j
public class UserLoginApi {
    private final UserLoginService userLoginService;

    @PostMapping("login")
    public LoginUserDto.Response loginUser(@Valid @RequestBody LoginUserDto.Request requestDto) {
        return userLoginService.loginUser(requestDto);
    }
}

