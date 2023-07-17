package com.sungam1004.register.domain.attendance.application;

import com.sungam1004.register.domain.attendance.exception.DuplicateAttendanceException;
import com.sungam1004.register.domain.attendance.exception.IncorrectPasswordException;
import com.sungam1004.register.domain.attendance.exception.InvalidDayOfWeekException;
import com.sungam1004.register.domain.attendance.repository.AttendanceRepository;
import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.domain.user.repository.UserRepository;
import com.sungam1004.register.global.manager.PasswordManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AttendanceSaveServiceTest {

    @InjectMocks
    private AttendanceSaveService attendanceSaveService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Clock clock;

    @Spy
    private PasswordManager passwordManager;

    @Test
    @DisplayName("출석하기 성공 by userId")
    void saveAttendanceSuccessByUserId() {
        final String userName = "tester1";
        final Long userId = 1L;
        final String attendancePassword = "1234";

        User user = new User(userName, "password1", "00.03.21", Team.또복, "default");
        ReflectionTestUtils.setField(user, "id", userId);

        doReturn(Optional.of(user)).when(userRepository)
                .findById(userId);
        doReturn(false).when(attendanceRepository)
                .existsByUserAndCreatedAtAfter(eq(user), any(LocalDateTime.class));
        doReturn(true).when(passwordManager)
                .isCorrectAttendancePassword(attendancePassword);

        doReturn(Instant.parse("2023-07-16T13:00:00Z")).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();

        // when
        attendanceSaveService.saveAttendanceByUserId(userId, attendancePassword);

        // then
        assertThat(user.getAttendanceNumber()).isEqualTo(1);

        // verify
        verify(userRepository, times(1))
                .findById(userId);
        verify(attendanceRepository, times(1))
                .existsByUserAndCreatedAtAfter(eq(user), any(LocalDateTime.class));
        verify(passwordManager, times(1))
                .isCorrectAttendancePassword(attendancePassword);
    }

    @Test
    @DisplayName("출석하기 성공 by name")
    void saveAttendanceSuccessByName() {
        final String userName = "tester1";
        final Long userId = 1L;
        final String attendancePassword = "1234";

        User user = new User(userName, "password1", "00.03.21", Team.또복, "default");
        ReflectionTestUtils.setField(user, "id", userId);

        doReturn(Optional.of(user)).when(userRepository)
                .findByName(userName);
        doReturn(false).when(attendanceRepository)
                .existsByUserAndCreatedAtAfter(eq(user), any(LocalDateTime.class));
        doReturn(true).when(passwordManager)
                .isCorrectAttendancePassword(attendancePassword);

        doReturn(Instant.parse("2023-07-16T13:00:00Z")).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();

        // when
        attendanceSaveService.saveAttendanceByName(userName, attendancePassword);

        // then
        assertThat(user.getAttendanceNumber()).isEqualTo(1);

        // verify
        verify(userRepository, times(1))
                .findByName(userName);
        verify(attendanceRepository, times(1))
                .existsByUserAndCreatedAtAfter(eq(user), any(LocalDateTime.class));
        verify(passwordManager, times(1))
                .isCorrectAttendancePassword(attendancePassword);
    }

    @Test
    @DisplayName("출석하기 실패 - 일요일 검증")
    void saveAttendanceFailBySunday() {
        final String userName = "tester1";
        final Long userId = 1L;
        final String attendancePassword = "1234";

        User user = new User(userName, "password1", "00.03.21", Team.또복, "default");
        ReflectionTestUtils.setField(user, "id", userId);

        doReturn(Optional.of(user)).when(userRepository)
                .findByName(userName);
        doReturn(true).when(passwordManager)
                .isCorrectAttendancePassword(attendancePassword);

        doReturn(Instant.parse("2023-07-17T13:00:00Z")).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();

        // when
        assertThrows(InvalidDayOfWeekException.class,
                () -> attendanceSaveService.saveAttendanceByName(userName, attendancePassword));

        // then
        assertThat(user.getAttendanceNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("출석하기 실패 - 출석 비밀번호 검증")
    void saveAttendanceFailByPassword() {
        final String userName = "tester1";
        final Long userId = 1L;
        final String attendancePassword = "1234";

        User user = new User(userName, "password1", "00.03.21", Team.또복, "default");
        ReflectionTestUtils.setField(user, "id", userId);

        doReturn(Optional.of(user)).when(userRepository)
                .findByName(userName);
        doReturn(false).when(passwordManager)
                .isCorrectAttendancePassword(attendancePassword);

        // when
        assertThrows(IncorrectPasswordException.class,
                () -> attendanceSaveService.saveAttendanceByName(userName, attendancePassword));

        // then
        assertThat(user.getAttendanceNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("출석하기 실패 - 중복 출석 검증")
    void saveAttendanceFailByDuplicationAttendance() {
        final String userName = "tester1";
        final Long userId = 1L;
        final String attendancePassword = "1234";

        User user = new User(userName, "password1", "00.03.21", Team.또복, "default");
        ReflectionTestUtils.setField(user, "id", userId);

        doReturn(Optional.of(user)).when(userRepository)
                .findByName(userName);
        doReturn(true).when(passwordManager)
                .isCorrectAttendancePassword(attendancePassword);
        doReturn(true).when(attendanceRepository)
                .existsByUserAndCreatedAtAfter(eq(user), any(LocalDateTime.class));
        doReturn(Instant.parse("2023-07-16T13:00:00Z")).when(clock).instant();
        doReturn(ZoneId.systemDefault()).when(clock).getZone();

        // when
        assertThrows(DuplicateAttendanceException.class,
                () -> attendanceSaveService.saveAttendanceByName(userName, attendancePassword));

        // then
        assertThat(user.getAttendanceNumber()).isEqualTo(0);
    }
}