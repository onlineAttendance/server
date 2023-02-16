package com.sungam1004.register.domain.user.dto;

import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.global.validation.annotation.DateValid;
import com.sungam1004.register.global.validation.annotation.TeamValid;
import com.sungam1004.register.global.validation.annotation.UserPasswordValid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class AddUserDto {


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Request {
        @NotBlank(message = "이름은 필수입니다.")
        private String name;

        @UserPasswordValid
        private String password;

        @DateValid(pattern = "[0-9][0-9].[0-9][0-9].[0-9][0-9].", message = "생년월일은 YY.MM.DD. 형식으로 입력해야 합니다.")
        private String birth;

        @TeamValid
        private String team;


        public User toEntity(String faceImageUri) {
            return User.builder()
                    .name(name)
                    .birth(birth)
                    .password(password)
                    .faceImageUri(faceImageUri)
                    .team(Team.convertTeamByString(team))
                    .build();
        }
    }
}
