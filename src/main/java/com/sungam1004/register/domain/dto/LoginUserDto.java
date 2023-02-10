package com.sungam1004.register.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LoginUserDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 10, message = "이름은 최대 {max}자리 이하입니다.")
        private String name;

        @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리입니다.")
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String token;
        private String grantType;

        public static Response of(TokenDto tokenDto) {
            return new LoginUserDto.Response(tokenDto.getAccessToken(), tokenDto.getGrantType());
        }
    }

}
