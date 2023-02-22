package com.sungam1004.register.domain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sungam1004.register.domain.attendance.dto.AttendanceDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AttendanceApiTest {

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
    @DisplayName("출석")
    void attendanceSuccess() throws Exception {
        // given
        SignupUserDto signupUserDto = new SignupUserDto("tester", "1234", "00.12.12.", "복통", "default.png");
        userSignupService.addUser(signupUserDto);
        String accessToken = userLoginService.loginUser(new LoginUserDto.Request("tester", "1234")).getToken();

        AttendanceDto.Request attendanceDto = new AttendanceDto.Request("1234");
        String content = objectMapper.writeValueAsString(attendanceDto);

        //System.out.println("=====================================");
        // expected
        mockMvc.perform(post("/api/users/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(content)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.team").value("복통"))
                .andExpect(jsonPath("$.attendance.length()").value(1))
                .andExpect(jsonPath("$.attendance[0].name").value("tester"))
                .andExpect(jsonPath("$.attendance[0].imageFileName").value("default.png"))
                .andExpect(jsonPath("$.notAttendance").exists())
                .andDo(print());
        //System.out.println("=====================================");
        Optional<User> optionalUser = userRepository.findByName("tester");
        assertThat(optionalUser.isPresent()).isEqualTo(true);
        User user = optionalUser.get();
        assertThat(user.getAttendanceNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("팀의 출석 현황 확인")
    void findTeamAttendance() throws Exception {
        // given
        SignupUserDto signupUserDto = new SignupUserDto("tester", "1234", "00.12.12.", "복통", "default.png");
        userSignupService.addUser(signupUserDto);
        String accessToken = userLoginService.loginUser(new LoginUserDto.Request("tester", "1234")).getToken();

        // expected
        mockMvc.perform(get("/api/users/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.team").value("복통"))
                .andExpect(jsonPath("$.notAttendance").exists())
                .andExpect(jsonPath("$.attendance").isEmpty())
                .andDo(print());
    }
}