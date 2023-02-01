package com.sungam1004.register.domain.dto;

import com.sungam1004.register.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDto {

    private Long id;
    private String name;
    private String password;
    private String birth;
    private Integer attendanceNumber;
    private String team;

    private List<AttendanceDate> attendanceDates;

    public static UserDetailDto of(User user, List<AttendanceDate> AttendanceDates) {
        return new UserDetailDto(user.getId(), user.getName(), user.getPassword(), user.getBirth(),
                user.getAttendanceNumber(), user.getTeam().toString(), AttendanceDates);
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
