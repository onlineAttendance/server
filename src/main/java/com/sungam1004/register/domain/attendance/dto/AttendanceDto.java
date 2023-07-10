package com.sungam1004.register.domain.attendance.dto;

import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.global.validation.annotation.AttendancePasswordValid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class AttendanceDto {

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Request {

        @AttendancePasswordValid
        private String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Response {

        private String team;
        private List<Person> attendance = new ArrayList<>();
        private List<Person> notAttendance = new ArrayList<>();

        private Response(String team) {
            this.team = team;
        }

        public static Response FromTeam(Team team) {
            return new Response(team.name());
        }

        public void addPerson(boolean isAttendance, String name, String imageFileName) {
            if (isAttendance) {
                attendance.add(new Person(name, imageFileName));
            }
            else {
                notAttendance.add(new Person(name, imageFileName));
            }
        }

        @AllArgsConstructor
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        @Getter
        private static class Person {
            private String name;
            private String imageFileName;
        }
    }
}
