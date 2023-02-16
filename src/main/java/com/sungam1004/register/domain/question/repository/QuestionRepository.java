package com.sungam1004.register.domain.question.repository;

import com.sungam1004.register.domain.post.entity.Post;
import com.sungam1004.register.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
