package com.sungam1004.register.domain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungam1004.register.domain.user.dto.LoginUserDto;
import com.sungam1004.register.domain.user.dto.SignupUserDto;
import com.sungam1004.register.domain.user.application.UserSignupService;
import com.sungam1004.register.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserLoginApiTest {

    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 자동으로 주입해줌

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserSignupService userSignupService;

    @Test
    @DisplayName("로그인 성공")
    void login() throws Exception {
        // given
        SignupUserDto signupUserDto = new SignupUserDto("tester", "1234", "00.12.12.", "복통", "default.png");
        userSignupService.addUser(signupUserDto);

        LoginUserDto.Request loginDto = new LoginUserDto.Request("tester", "1234");
        String content = objectMapper.writeValueAsString(loginDto);

        // expected
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grantType").value("Bearer"))
                .andExpect(jsonPath("$.token").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호가 다름")
    void failsLoginByPassword() throws Exception {
        // given
        SignupUserDto signupUserDto = new SignupUserDto("tester", "1234", "00.12.12.", "복통", "default.png");
        userSignupService.addUser(signupUserDto);

        LoginUserDto.Request loginDto = new LoginUserDto.Request("tester", "1231");
        String content = objectMapper.writeValueAsString(loginDto);

        // expected
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.FAIL_LOGIN.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.FAIL_LOGIN.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 - 이름이 다름")
    void failsLoginByName() throws Exception {
        // given
        SignupUserDto signupUserDto = new SignupUserDto("tester", "1234", "00.12.12.", "복통", "default.png");
        userSignupService.addUser(signupUserDto);

        LoginUserDto.Request loginDto = new LoginUserDto.Request("tester1", "1234");
        String content = objectMapper.writeValueAsString(loginDto);

        // expected
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.FAIL_LOGIN.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.FAIL_LOGIN.getMessage()))
                .andDo(print());
    }
}