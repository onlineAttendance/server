package com.sungam1004.register.dto;

import com.sungam1004.register.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserManagerDto {
    private Long id;
    private String name;
    private String phone;
    private String birth;
    private Integer absenceNumber;
    private Integer attendanceNumber;
    private String team;

    public static UserManagerDto of(User user) {
        return new UserManagerDto(user.getId(), user.getName(), user.getPhone(), user.getBirth(), user.getAbsenceNumber(),
                user.getAttendanceNumber(), user.getTeam().toString());
    }
}
