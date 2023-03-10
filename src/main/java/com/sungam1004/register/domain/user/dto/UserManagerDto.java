package com.sungam1004.register.domain.user.dto;

import com.sungam1004.register.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserManagerDto {
    private Long id;
    private String name;
    private String birth;
    private Integer attendanceNumber;
    private String team;

    public static UserManagerDto of(User user) {
        return new UserManagerDto(user.getId(), user.getName(), user.getBirth(),
                user.getAttendanceNumber(), user.getTeam().toString());
    }
}
