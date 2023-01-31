package com.sungam1004.register.domain.repository;

import com.sungam1004.register.domain.entity.Team;
import com.sungam1004.register.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    boolean existsByName(String name);

    List<User> findByTeam(Team team);
}
