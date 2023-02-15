package com.sungam1004.register.domain.dto;

import com.sungam1004.register.global.validation.annotation.UserPasswordValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserPasswordDto {
    @UserPasswordValid
    private String password;
}
