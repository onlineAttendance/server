package com.sungam1004.register.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserPasswordDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리입니다.")
        private String password;
    }


}
