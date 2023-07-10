package com.sungam1004.register.domain.admin.dto;

import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatisticsDto {

    private final Map<String, Integer> mapNameAndIndex = new HashMap<>();
    private final List<NameAndAttendance> nameAndAttendanceList = new ArrayList<>();

    @Getter
    public static class NameAndAttendance {
        String name;
        Team team;
        List<LocalDateTime> dateTimeList;

        public NameAndAttendance(String name, Team team) {
            this.name = name;
            this.team = team;
            this.dateTimeList = new ArrayList<>(Collections.nCopies(date.size(), null));
        }
    }

    private StatisticsDto(List<User> users) {
        for (User user : users) {
            mapNameAndIndex.put(user.getName(), nameAndAttendanceList.size());
            nameAndAttendanceList.add(new NameAndAttendance(user.getName(), user.getTeam()));
        }
    }

    public static StatisticsDto FromUsers(List<User> users) {
        return new StatisticsDto(users);
    }

    public static StatisticsDto FromUser(User user) {
        return new StatisticsDto(List.of(user));
    }

    public void addAttendance(String name, LocalDateTime dateTime) {
        int index = mapNameAndIndex.get(name);
        NameAndAttendance nameAndAttendance = nameAndAttendanceList.get(index);
        String attendanceDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateTime);

        for (int i = 0; i < date.size(); i++) {
            if (attendanceDate.equals(date.get(i))) {
                nameAndAttendance.dateTimeList.set(i, dateTime);
                return;
            }
        }
    }

    public List<NameAndAttendance> getNameAndAttendanceList() {
        return nameAndAttendanceList;
    }


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
}
