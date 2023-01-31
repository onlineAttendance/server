package com.sungam1004.register.dto;

import com.sungam1004.register.domain.Team;
import com.sungam1004.register.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatisticsDto {

    public static final List<String> date =
            List.of("2023-01-01",
                    "2023-01-08",
                    "2023-01-15",
                    "2023-01-22",
                    "2023-01-29",
                    "2023-02-05",
                    "2023-02-12",
                    "2023-02-19",
                    "2023-02-26",
                    "2023-03-05",
                    "2023-03-12",
                    "2023-03-19",
                    "2023-03-26",
                    "2023-04-02",
                    "2023-04-09",
                    "2023-04-16",
                    "2023-04-23",
                    "2023-04-30",
                    "2023-05-07",
                    "2023-05-14",
                    "2023-05-21",
                    "2023-05-28",
                    "2023-06-04",
                    "2023-06-11",
                    "2023-06-18",
                    "2023-06-25",
                    "2023-07-02",
                    "2023-07-09",
                    "2023-07-16",
                    "2023-07-23",
                    "2023-07-30",
                    "2023-08-06",
                    "2023-08-13",
                    "2023-08-20",
                    "2023-08-27",
                    "2023-09-03",
                    "2023-09-10",
                    "2023-09-17",
                    "2023-09-24",
                    "2023-10-01",
                    "2023-10-08",
                    "2023-10-15",
                    "2023-10-22",
                    "2023-10-29",
                    "2023-11-05",
                    "2023-11-12",
                    "2023-11-19",
                    "2023-11-26",
                    "2023-12-03",
                    "2023-12-10",
                    "2023-12-17",
                    "2023-12-24",
                    "2023-12-31");

    @Getter
    public static class NameAndAttendance {
        String name;
        Team team;
        List<LocalDateTime> dateTimes;

        public NameAndAttendance(String name, Team team) {
            this.name = name;
            this.team = team;
            this.dateTimes = new ArrayList<>(Collections.nCopies(date.size(), null));
        }
    }

    private final Map<String, Integer> mapNameIndex = new HashMap<>();
    private final List<NameAndAttendance> nameAndAttendances = new ArrayList<>();

    public void setName(List<User> users) {
        for (User user : users) {
            mapNameIndex.put(user.getName(), nameAndAttendances.size());
            nameAndAttendances.add(new NameAndAttendance(user.getName(), user.getTeam()));
        }
    }

    public void addAttendance(String name, LocalDateTime dateTime) {
        int index = mapNameIndex.get(name);
        NameAndAttendance nameAndAttendance = nameAndAttendances.get(index);
        String attendanceDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateTime);

        for (int i = 0; i < date.size(); i++) {
            if (Objects.equals(date.get(i), attendanceDate)) {
                nameAndAttendance.dateTimes.set(i, dateTime);
                return;
            }
        }
    }

    public List<NameAndAttendance> getNameAndAttendances() {
        return nameAndAttendances;
    }
}
