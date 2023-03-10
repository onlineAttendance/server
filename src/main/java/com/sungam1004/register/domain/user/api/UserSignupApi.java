package com.sungam1004.register.domain.user.api;

import com.sungam1004.register.domain.user.application.UserSignupService;
import com.sungam1004.register.domain.user.dto.SignupUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@Slf4j
public class UserSignupApi {
    private final UserSignupService userSignupService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signupUser(@Valid @RequestBody SignupUserDto requestDto) {
        userSignupService.addUser(requestDto.toEntity());
    }
}

