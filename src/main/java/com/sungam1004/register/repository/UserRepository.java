package com.sungam1004.register.repository;

import com.sungam1004.register.domain.Team;
import com.sungam1004.register.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    boolean existsByName(String name);

    List<User> findByTeam(Team team);
}
