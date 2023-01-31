package com.sungam1004.register.dto;

import com.sungam1004.register.manager.TodayPostManager;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class AttendanceDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "이름이 공백일 수 없습니다.")
        private String name;

        @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리입니다.")
        private String password;
    }

    @Data
    public static class Response {

        private String team;
        private Integer ratio;
        private List<String> attendanceNames = new ArrayList<>();
        private List<String> notAttendanceNames = new ArrayList<>();
        private String todayPost;
        private List<String> questions;

        public Response(String team) {
            this.team = team;
            this.todayPost = TodayPostManager.post;
            this.questions = TodayPostManager.questions;
        }
    }
}
