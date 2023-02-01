package com.sungam1004.register.domain.dto;

import com.sungam1004.register.domain.entity.User;
import com.sungam1004.register.domain.entity.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AddUserDto {


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "이름은 필수입니다.")
        private String name;

        @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리입니다.")
        private String password;

        @Pattern(regexp = "[0-9][0-9].[0-9][0-9].[0-9][0-9].", message = "생년월일은 YY.MM.DD. 형식으로 입력해야 합니다.")
        private String birth;

        @NotBlank(message = "팀 선택은 필수입니다.")
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
