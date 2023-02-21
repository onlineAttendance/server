package com.sungam1004.register.domain.attendance.repository;

import com.sungam1004.register.domain.attendance.entity.Attendance;
import com.sungam1004.register.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUser(User user);

    Optional<Attendance> findByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

    boolean existsByUserAndCreatedAtAfter(User user, LocalDateTime date);

    @Query("select a from Attendance a where a.user.id in :userIds and a.createdAt > :date")
    List<Attendance> findByUsersAndCreatedAtAfter(@Param("userIds") List<Long> userIds, @Param("date") LocalDateTime date);

}
