package com.sungam1004.register.domain.service;

import com.sungam1004.register.domain.dto.PostManagerDto;
import com.sungam1004.register.domain.entity.Post;
import com.sungam1004.register.domain.repository.PostRepository;
import com.sungam1004.register.global.manager.SundayDateManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminPostService {
    private final PostRepository postRepository;

    public List<PostManagerDto> findPostList() {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
        List<PostManagerDto> ret = new ArrayList<>();
        List<String> dates = SundayDateManager.date;

        int listPoint = 0;
        for (String date : dates) {
            if (posts.size() > listPoint) {
                Post post = posts.get(listPoint);
                if (post.getDate().equals(LocalDate.parse(date, DateTimeFormatter.ISO_DATE))) {
                    ret.add(new PostManagerDto(post.getId(), post.getTitle(), date, true));
                    listPoint++;
                    continue;
                }
            }
            ret.add(new PostManagerDto(0L, "", date, false));
        }

        return ret;
    }
}
