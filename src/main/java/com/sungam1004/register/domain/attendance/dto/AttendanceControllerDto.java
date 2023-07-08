package com.sungam1004.register.domain.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttendanceControllerDto {

    @NotBlank(message = "이름이 공백일 수 없습니다.")
    private String name;

    @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리입니다.")
    private String password;
}
