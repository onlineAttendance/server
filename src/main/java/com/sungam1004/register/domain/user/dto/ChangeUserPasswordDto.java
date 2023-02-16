package com.sungam1004.register.domain.user.dto;

import com.sungam1004.register.global.validation.annotation.UserPasswordValid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ChangeUserPasswordDto {

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Request {

        @UserPasswordValid
        private String password;
    }
}
