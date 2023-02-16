package com.sungam1004.register.domain.user.entity;

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

    @Column(nullable = false)
    private String name;

    private String password;
    private String birth;
    private Integer attendanceNumber;
    private String faceImageUri;

    @Enumerated(value = EnumType.STRING)
    private Team team;

    @Builder
    public User(String name, String password, String birth, Team team, String faceImageUri) {
        this.name = name;
        this.password = password;
        this.birth = birth;
        this.team = team;
        this.faceImageUri = faceImageUri;
        this.attendanceNumber = 0;
    }

    public void increaseAttendanceNumber() {
        attendanceNumber++;
    }

    public void decreaseAttendanceNumber() {
        attendanceNumber--;
    }

    public void updateFaceImageUri(String faceImageUri) {
        this.faceImageUri = faceImageUri;
    }

    public void updateUserPassword(String password) {
        this.password = password;
    }

}
