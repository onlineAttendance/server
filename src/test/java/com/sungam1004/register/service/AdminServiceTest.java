package com.sungam1004.register.service;

import com.sungam1004.register.domain.Team;
import com.sungam1004.register.domain.User;
import com.sungam1004.register.dto.AddUserDto;
import com.sungam1004.register.dto.UserManagerDto;
import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;
import com.sungam1004.register.repository.AttendanceRepository;
import com.sungam1004.register.repository.UserRepository;
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
        String phone = "01012345678";
        String birth = "99.10.11.";
        String team = "복덕복덕";

        // given
        Mockito.when(userRepository.existsByName(any()))
                .thenReturn(false);

        // when
        adminService.addUser(new AddUserDto.Request(name, phone, birth, team));
        // then
    }

    @Test
    @DisplayName("유저 저장하기 - 실패")
    void addUserFail() {
        // given
        String name = "tester1";
        String phone = "01012345678";
        String birth = "99.10.11.";
        String team = "복덕복덕";
        Mockito.when(userRepository.existsByName(any()))
                .thenReturn(true);

        CustomException customException =
                assertThrows(CustomException.class, () -> adminService.addUser(new AddUserDto.Request(name, phone, birth, team)));

        //then
        assertEquals(customException.getError(), ErrorCode.DUPLICATE_USER_NAME);
    }

    @Test
    @DisplayName("모든 유저 찾기")
    void findUserAll() {
        // given
        User user1 = User.builder()
                .name("tester1")
                .phone("01012345678")
                .birth("99.10.11.")
                .team(Team.복덕복덕)
                .build();

        User user2 = User.builder()
                .name("tester2")
                .phone("01012345679")
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
        assertEquals(userAll.get(0).getPhone(), "01012345678");
        assertEquals(userAll.get(0).getTeam(), "복덕복덕");
        assertEquals(userAll.get(0).getBirth(), "99.10.11.");

        assertEquals(userAll.get(1).getName(), "tester2");
        assertEquals(userAll.get(1).getPhone(), "01012345679");
        assertEquals(userAll.get(1).getTeam(), "복통");
        assertEquals(userAll.get(1).getBirth(), "00.10.11.");
    }

    @Test
    @DisplayName("모든 유저 찾기")
    void userDetail() {
        User user1 = User.builder()
                .name("tester1")
                .phone("01012345678")
                .birth("99.10.11.")
                .team(Team.복덕복덕)
                .build();

        Mockito.when(userRepository.findById(any()))
                .thenReturn(Optional.ofNullable(user1));

    }
}