package com.sungam1004.register.domain.user.application;

import com.sungam1004.register.domain.user.dto.LoginUserDto;
import com.sungam1004.register.domain.user.dto.TokenDto;
import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.global.manager.PasswordEncoder;
import com.sungam1004.register.global.manager.TokenManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLoginServiceTest {

    @InjectMocks
    private UserLoginService userLoginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenManager tokenManager;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인 성공")
    void loginUser() {
        // given
        PasswordEncoder passwordEncoder = new PasswordEncoder();

        final String accessToken = "token";
        final String grantType = "Bearer";
        final String userName = "tester1";
        final String password = "password1";
        final String encodedPassword = passwordEncoder.encrypt(password);
        final Long userId = 1L;

        User user = new User(userName, encodedPassword, "00.03.21", Team.또복, "default");
        ReflectionTestUtils.setField(user, "id", userId);

        doReturn(Optional.of(user)).when(userRepository)
                .findByName(userName);
        doReturn(new TokenDto(grantType, accessToken)).when(tokenManager)
                .createTokenDto(userId);

        // when
        LoginUserDto.Request request = new LoginUserDto.Request(userName, password);
        LoginUserDto.Response response = userLoginService.loginUser(request);

        // then
        assertThat(response.getGrantType()).isEqualTo(grantType);
        assertThat(response.getToken()).isEqualTo(accessToken);

        // verify
        verify(userRepository, times(1)).findByName(userName);
        verify(tokenManager, times(1)).createTokenDto(userId);
    }
}