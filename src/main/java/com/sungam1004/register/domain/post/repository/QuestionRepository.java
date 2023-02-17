package com.sungam1004.register.domain.post.repository;

import com.sungam1004.register.domain.post.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
