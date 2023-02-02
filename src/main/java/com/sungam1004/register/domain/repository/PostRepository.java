package com.sungam1004.register.domain.repository;

import com.sungam1004.register.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    //List<Post> findAllOrderByDateAsc();
}
