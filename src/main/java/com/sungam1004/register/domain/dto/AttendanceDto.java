package com.sungam1004.register.domain.dto;

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

        @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리입니다.")
        private String password;
    }

    @Data
    public static class Response {

        private String team;
        private List<Person> attendance = new ArrayList<>();
        private List<Person> notAttendance = new ArrayList<>();

        public Response(String team) {
            this.team = team;
        }

        public void addPerson(boolean isAttendance, String name, String imageFileName) {
            if (isAttendance) attendance.add(new Person(name, imageFileName));
            else notAttendance.add(new Person(name, imageFileName));
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        private static class Person {
            private String name;
            private String imageFileName;
        }
    }
}
