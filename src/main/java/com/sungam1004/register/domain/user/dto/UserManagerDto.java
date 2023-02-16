package com.sungam1004.register.domain.user.dto;

import com.sungam1004.register.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserManagerDto {
    private Long id;
    private String name;
    private String password;
    private String birth;
    private Integer attendanceNumber;
    private String team;

    public static UserManagerDto of(User user) {
        return new UserManagerDto(user.getId(), user.getName(), user.getPassword(), user.getBirth(),
                user.getAttendanceNumber(), user.getTeam().toString());
    }
}
