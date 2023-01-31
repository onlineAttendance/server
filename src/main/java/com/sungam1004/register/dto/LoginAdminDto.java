package com.sungam1004.register.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LoginAdminDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @Size(min = 5, max = 20, message = "비밀번호는 {min}자리 이상, {max}자리 이하입니다.")
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String token;
    }

}
