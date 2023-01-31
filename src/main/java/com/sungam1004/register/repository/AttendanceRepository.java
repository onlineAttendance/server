package com.sungam1004.register.repository;

import com.sungam1004.register.domain.Attendance;
import com.sungam1004.register.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUser(User user);

    Optional<Attendance> findByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

    boolean existsByUserAndCreatedAtAfter(User user, LocalDateTime date);
}
