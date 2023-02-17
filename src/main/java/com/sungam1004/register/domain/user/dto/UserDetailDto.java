package com.sungam1004.register.domain.user.dto;

import com.sungam1004.register.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserDetailDto {

    private Long id;
    private String name;
    private String password;
    private String birth;
    private Integer attendanceNumber;
    private String team;

    private List<AttendanceDate> attendanceDates;

    @Builder
    public UserDetailDto(Long id, String name, String password, String birth,
                         Integer attendanceNumber, String team, List<AttendanceDate> attendanceDates) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.birth = birth;
        this.attendanceNumber = attendanceNumber;
        this.team = team;
        this.attendanceDates = attendanceDates;
    }

    public static UserDetailDto of(User user, List<AttendanceDate> AttendanceDates) {
        return UserDetailDto.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .birth(user.getBirth())
                .attendanceNumber(user.getAttendanceNumber())
                .team(user.getTeam().name())
                .attendanceDates(AttendanceDates)
                .build();
    }

    @Data
    public static class AttendanceDate {
        String date;
        String time;
        boolean isAttendance;

        public AttendanceDate(String date, LocalDateTime time) {
            this.date = date;
            if (time != null) {
                isAttendance = true;
                this.time = time.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
            }
            else isAttendance = false;
        }
    }
}
