package com.sungam1004.register.domain.repository;

import com.sungam1004.register.domain.entity.Post;
import com.sungam1004.register.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByPost(Post post);
}
