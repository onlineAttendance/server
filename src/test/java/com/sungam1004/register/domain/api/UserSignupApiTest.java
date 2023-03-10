package com.sungam1004.register.domain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungam1004.register.domain.user.application.UserSignupService;
import com.sungam1004.register.domain.user.dto.SignupUserDto;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.repository.UserRepository;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserSignupApiTest {

    @Autowired
    private ObjectMapper objectMapper; // 스프링에서 자동으로 주입해줌

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSignupService userSignupService;

    @Test
    @DisplayName("유저 회원가입")
    void signupUser() throws Exception {
        // given
        SignupUserDto requestDto = new SignupUserDto("tester", "1234", "02.04.26.", "복통", "default.png");
        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andDo(print());

        Optional<User> tester = userRepository.findByName("tester");
        assertThat(tester.isPresent()).isEqualTo(true);
        User user = tester.get();
        assertThat(user.getName()).isEqualTo("tester");
        assertThat(user.getPassword()).isEqualTo("1234");
        assertThat(user.getBirth()).isEqualTo("02.04.26.");
        assertThat(user.getTeam().name()).isEqualTo("복통");
        assertThat(user.getFaceImageUri()).isEqualTo("default.png");
    }

    @Test
    @DisplayName("유저 회원가입 시, 동일한 이름을 가지면 안됨")
    void signupUserDuplicateName() throws Exception {
        // given
        SignupUserDto requestDto = new SignupUserDto("tester", "1234", "00.12.12.", "복통", "default.png");
        userSignupService.addUser(requestDto.toEntity());
        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_USER_NAME.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_USER_NAME.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호는 숫자 4자리")
    void errorOnPassword() throws Exception {
        // given
        SignupUserDto requestDto = new SignupUserDto("tester", "123", "00.12.12.", "복통", "default.png");
        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_VALUE.getCode()))
                .andExpect(jsonPath("$.message").value("비밀번호는 숫자 4자리입니다."))
                .andDo(print());


        requestDto = new SignupUserDto("tester", "12345", "00.12.12.", "복통", "default.png");
        content = objectMapper.writeValueAsString(requestDto);
        // expected
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_VALUE.getCode()))
                .andExpect(jsonPath("$.message").value("비밀번호는 숫자 4자리입니다."))
                .andDo(print());


        requestDto = new SignupUserDto("tester", "abcd", "00.12.12.", "복통", "default.png");
        content = objectMapper.writeValueAsString(requestDto);
        // expected
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_VALUE.getCode()))
                .andExpect(jsonPath("$.message").value("비밀번호는 숫자 4자리입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("생년월일 포맷은 yy.MM.dd")
    void errorOnBirth() throws Exception {
        // given
        SignupUserDto requestDto = new SignupUserDto("tester", "1234", "00.12.12", "복통", "default.png");
        String content = objectMapper.writeValueAsString(requestDto);

        // expected
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_VALUE.getCode()))
                .andExpect(jsonPath("$.message").value("생년월일은 YY.MM.DD. 형식으로 입력해야 합니다."))
                .andDo(print());
    }
}