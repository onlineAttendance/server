package com.sungam1004.register.domain.post.repository;

import com.sungam1004.register.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"questions"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Post> findWithQuestionsById(Long id);

    Slice<Post> findSliceBy(Pageable pageable);

}
