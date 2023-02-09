package com.sungam1004.register.domain.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserPasswordDto {
    @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리입니다.")
    private String password;
}
