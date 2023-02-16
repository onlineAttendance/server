package com.sungam1004.register.domain.user.dto;

import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.user.entity.User;
import com.sungam1004.register.global.validation.annotation.DateValid;
import com.sungam1004.register.global.validation.annotation.TeamValid;
import com.sungam1004.register.global.validation.annotation.UserPasswordValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupUserDto {

    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 10, message = "이름은 최대 {max}자리 이하입니다.")
    private String name;

    @UserPasswordValid
    private String password;

    @DateValid(pattern = "yy.MM.dd.", message = "생년월일은 YY.MM.DD. ㅁ형식으로 입력해야 합니다.")
    private String birth;

    @TeamValid
    private String team;

    @NotBlank(message = "faceImageFile은 필수입니다.")
    private String faceImageFile;

    public User toEntity() {
        return User.builder()
                .name(name)
                .birth(birth)
                .password(password)
                .faceImageUri(faceImageFile)
                .team(Team.convertTeamByString(team))
                .build();
    }
}
