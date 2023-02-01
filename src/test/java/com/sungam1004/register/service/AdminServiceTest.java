package com.sungam1004.register.service;

import com.sungam1004.register.domain.entity.Team;
import com.sungam1004.register.domain.entity.User;
import com.sungam1004.register.domain.dto.AddUserDto;
import com.sungam1004.register.domain.dto.UserManagerDto;
import com.sungam1004.register.domain.service.AdminService;
import com.sungam1004.register.global.exception.CustomException;
import com.sungam1004.register.global.exception.ErrorCode;
import com.sungam1004.register.domain.repository.AttendanceRepository;
import com.sungam1004.register.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class AdminServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AttendanceRepository attendanceRepository;

    @InjectMocks
    AdminService adminService;

    @Test
    @DisplayName("유저 저장하기")
    void addUserSuccess() {
        String name = "tester1";
        String password = "4321";
        String birth = "99.10.11.";
        String team = "복덕복덕";

        // given
        Mockito.when(userRepository.existsByName(any()))
                .thenReturn(false);

        // when
        adminService.addUser(new AddUserDto.Request(name, password, birth, team), "default.png");
        // then
    }

    @Test
    @DisplayName("유저 저장하기 - 실패")
    void addUserFail() {
        // given
        String name = "tester1";
        String password = "4321";
        String birth = "99.10.11.";
        String team = "복덕복덕";
        Mockito.when(userRepository.existsByName(any()))
                .thenReturn(true);

        CustomException customException =
                assertThrows(CustomException.class, () -> adminService.addUser(new AddUserDto.Request(name, password, birth, team), "default.png"));

        //then
        assertEquals(customException.getError(), ErrorCode.DUPLICATE_USER_NAME);
    }

    @Test
    @DisplayName("모든 유저 찾기")
    void findUserAll() {
        // given
        User user1 = User.builder()
                .name("tester1")
                .password("4321")
                .birth("99.10.11.")
                .team(Team.복덕복덕)
                .build();

        User user2 = User.builder()
                .name("tester2")
                .password("1234")
                .birth("00.10.11.")
                .team(Team.복통)
                .build();
        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        // when
        List<UserManagerDto> userAll = adminService.findUserAll();

        // then
        assertEquals(userAll.size(), 2);
        assertEquals(userAll.get(0).getName(), "tester1");
        assertEquals(userAll.get(0).getPassword(), "4321");
        assertEquals(userAll.get(0).getTeam(), "복덕복덕");
        assertEquals(userAll.get(0).getBirth(), "99.10.11.");

        assertEquals(userAll.get(1).getName(), "tester2");
        assertEquals(userAll.get(1).getPassword(), "1234");
        assertEquals(userAll.get(1).getTeam(), "복통");
        assertEquals(userAll.get(1).getBirth(), "00.10.11.");
    }

    @Test
    @DisplayName("모든 유저 찾기")
    void userDetail() {
        User user1 = User.builder()
                .name("tester1")
                .password("4321")
                .birth("99.10.11.")
                .team(Team.복덕복덕)
                .build();

        Mockito.when(userRepository.findById(any()))
                .thenReturn(Optional.ofNullable(user1));

    }
}