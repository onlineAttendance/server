package com.sungam1004.register.domain.attendance.dto;

import com.sungam1004.register.global.validation.annotation.AttendancePasswordValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AttendancePasswordDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @AttendancePasswordValid
        private String password;
    }


}
