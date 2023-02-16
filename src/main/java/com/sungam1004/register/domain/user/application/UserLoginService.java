package com.sungam1004.register.domain.user.application;

import com.sungam1004.register.domain.user.dto.LoginUserDto;
import com.sungam1004.register.domain.user.dto.TokenDto;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.global.exception.CustomException;
import com.sungam1004.register.global.exception.ErrorCode;
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

    @Transactional(readOnly = true)
    public LoginUserDto.Response loginUser(LoginUserDto.Request requestDto) {
        User user = userRepository.findByName(requestDto.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.FAIL_LOGIN));

        String userPassword = user.getPassword();
        if (requestDto.getPassword().equals(userPassword)) {
            TokenDto tokenDto = tokenManager.createTokenDto(user.getId());
            return LoginUserDto.Response.of(tokenDto);
        }
        throw new CustomException(ErrorCode.FAIL_LOGIN);
    }
}
