package com.sungam1004.register.domain.attendance.dto;

import com.sungam1004.register.global.validation.annotation.AttendancePasswordValid;
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

        @AttendancePasswordValid
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
