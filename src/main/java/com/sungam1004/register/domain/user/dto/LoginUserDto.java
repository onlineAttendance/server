package com.sungam1004.register.domain.user.dto;

import com.sungam1004.register.global.validation.annotation.UserPasswordValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

public class LoginUserDto {

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Request {

        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 10, message = "이름은 최대 {max}자리 이하입니다.")
        private String name;

        @UserPasswordValid
        private String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Response {

        private String token;
        private String grantType;

        public static Response of(TokenDto tokenDto) {
            return new LoginUserDto.Response(tokenDto.getAccessToken(), tokenDto.getGrantType());
        }
    }

}
