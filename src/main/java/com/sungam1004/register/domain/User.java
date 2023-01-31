package com.sungam1004.register.domain;

import com.sungam1004.register.dto.StatisticsDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String phone;
    private String birth;
    private Integer absenceNumber;
    private Integer attendanceNumber;

    @Enumerated(value = EnumType.STRING)
    private Team team;

    @Builder
    public User(String name, String phone, String birth, Team team) {
        this.name = name;
        this.phone = phone;
        this.birth = birth;
        this.team = team;
        this.attendanceNumber = 0;
        this.absenceNumber = StatisticsDto.date.size();
    }

    public void increaseAttendanceNumber() {
        attendanceNumber++;
    }

    public void increaseAbsenceNumber() {
        absenceNumber++;
    }

    public void decreaseAttendanceNumber() {
        attendanceNumber--;
    }

    public void decreaseAbsenceNumber() {
        absenceNumber--;
    }
}
