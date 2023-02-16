package com.sungam1004.register.domain.attendance.entity;

import com.sungam1004.register.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;

    public Attendance(User user) {
        this.user = user;
    }

    public Attendance(User user, LocalDateTime createdAt) {
        this.user = user;
        this.createdAt = createdAt;
    }

    @PrePersist
    void createdAt() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }
}
