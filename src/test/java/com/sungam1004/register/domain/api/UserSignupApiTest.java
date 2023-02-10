package com.sungam1004.register.domain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungam1004.register.domain.dto.SignupUserDto;
import com.sungam1004.register.domain.entity.User;
import com.sungam1004.register.domain.repository.UserRepository;
import com.sungam1004.register.domain.service.UserSignupService;
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
    private UserRepository userRepository;

    @Autowired
    private UserSignupService userSignupService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("유저 회원가입")
    void signupUser() throws Exception {
        // given
        SignupUserDto requestDto = new SignupUserDto("tester", "1234", "00.12.12.", "복통", "default.png");

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
        assertThat(user.getBirth()).isEqualTo("00.12.12.");
        assertThat(user.getTeam().name()).isEqualTo("복통");
        assertThat(user.getFaceImageUri()).isEqualTo("default.png");
    }

    @Test
    @DisplayName("유저 회원가입 시, 동일한 이름을 가지면 안됨")
    void signupUserDuplicateName() throws Exception {
        // given
        SignupUserDto requestDto = new SignupUserDto("tester", "1234", "00.12.12.", "복통", "default.png");
        userSignupService.addUser(requestDto);
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
}