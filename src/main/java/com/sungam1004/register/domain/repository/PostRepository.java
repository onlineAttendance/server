package com.sungam1004.register.domain.repository;

import com.sungam1004.register.domain.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"questions"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Post> findWithQuestionsById(Long id);
}
