package com.sungam1004.register.dto;

import com.sungam1004.register.validation.annotation.DateValid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ChangeAttendanceDto {


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
        private String date;

        @NotNull
        private Boolean attendance;
    }
}
