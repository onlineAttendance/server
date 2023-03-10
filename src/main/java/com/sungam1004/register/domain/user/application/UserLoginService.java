package com.sungam1004.register.domain.user.application;

import com.sungam1004.register.domain.user.dto.LoginUserDto;
import com.sungam1004.register.domain.user.dto.TokenDto;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.exception.FailUserLoginException;
import com.sungam1004.register.domain.user.exception.UserNotFoundException;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.global.manager.PasswordEncoder;
import com.sungam1004.register.global.manager.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserLoginService {
    private final UserRepository userRepository;
    private final TokenManager tokenManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public LoginUserDto.Response loginUser(LoginUserDto.Request requestDto) {
        User user = userRepository.findByName(requestDto.getName()).orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            TokenDto tokenDto = tokenManager.createTokenDto(user.getId());
            return LoginUserDto.Response.of(tokenDto);
        }
        throw new FailUserLoginException();
    }
}
