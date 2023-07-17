package com.sungam1004.register.domain.attendance.application;

import com.sungam1004.register.domain.attendance.entity.Attendance;
import com.sungam1004.register.domain.attendance.exception.InvalidDayOfWeekException;
import com.sungam1004.register.domain.attendance.repository.AttendanceRepository;
import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AttendanceToggleServiceTest {
    @InjectMocks
    private AttendanceToggleService attendanceToggleService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("출석 토글 성공(결석 -> 출석)")
    void toggleFromAbsenceToAttendanceSuccess() {
        final String userName = "tester1";
        final Long userId = 1L;

        User user = new User(userName, "password1", "00.03.21", Team.또복, "default");

        doReturn(Optional.of(user)).when(userRepository)
                .findById(userId);
        doReturn(Optional.empty()).when(attendanceRepository)
                .findByUserAndCreatedAtBetween(eq(user), any(LocalDateTime.class), any(LocalDateTime.class));

        // when
        attendanceToggleService.toggleAttendance(userId, "2023-07-16");

        // then
        assertThat(user.getAttendanceNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("출석 토글 성공(출석 -> 결석)")
    void toggleFromAttendanceToAbsenceSuccess() {
        final String userName = "tester1";
        final Long userId = 1L;

        User user = new User(userName, "password1", "00.03.21", Team.또복, "default");
        user.increaseAttendanceNumber();

        Attendance attendance = new Attendance(user, LocalDateTime.of(2023, 7, 16, 0, 0, 0));

        doReturn(Optional.of(user)).when(userRepository)
                .findById(userId);
        doReturn(Optional.of(attendance)).when(attendanceRepository)
                .findByUserAndCreatedAtBetween(eq(user), any(LocalDateTime.class), any(LocalDateTime.class));

        // when
        attendanceToggleService.toggleAttendance(userId, "2023-07-16");

        // then
        assertThat(user.getAttendanceNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("출석 토글 실패 by 일요일")
    void toggleFromAttendanceToAbsenceFailByValidSunday() {
        final String userName = "tester1";
        final Long userId = 1L;

        User user = new User(userName, "password1", "00.03.21", Team.또복, "default");

        doReturn(Optional.of(user)).when(userRepository)
                .findById(userId);

        // when
        assertThrows(InvalidDayOfWeekException.class,
                () -> attendanceToggleService.toggleAttendance(userId, "2023-07-17"));

    }
}