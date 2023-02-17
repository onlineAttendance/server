package com.sungam1004.register.domain.attendance.dto;

import com.sungam1004.register.global.validation.annotation.AttendancePasswordValid;
import lombok.*;

public class AttendancePasswordDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Request {

        @AttendancePasswordValid
        private String password;
    }


}
