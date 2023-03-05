package com.sungam1004.register.domain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungam1004.register.domain.user.dto.ChangeUserPasswordDto;
import com.sungam1004.register.domain.user.dto.LoginUserDto;
import com.sungam1004.register.domain.user.dto.SignupUserDto;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.domain.user.application.UserLoginService;
import com.sungam1004.register.domain.user.application.UserSignupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserPatchAccountApiTest {

    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 자동으로 주입해줌

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserSignupService userSignupService;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("비밀번호 변경 성공")
    void changePassword() throws Exception {
        // given
        SignupUserDto signupUserDto = new SignupUserDto("tester", "1234", "00.12.12.", "복통", "default.png");
        userSignupService.addUser(signupUserDto);

        String accessToken = userLoginService.loginUser(new LoginUserDto.Request("tester", "1234")).getToken();
        ChangeUserPasswordDto.Request loginDto = new ChangeUserPasswordDto.Request("4321");
        String content = objectMapper.writeValueAsString(loginDto);

        System.out.println("=====================================");
        // expected
        mockMvc.perform(patch("/api/users/account/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andDo(print());
        System.out.println("=====================================");

        Optional<User> optionalUser = userRepository.findByName("tester");
        assertThat(optionalUser.isPresent()).isEqualTo(true);
        User user = optionalUser.get();
        assertThat(user.getPassword()).isEqualTo("4321");
    }


}