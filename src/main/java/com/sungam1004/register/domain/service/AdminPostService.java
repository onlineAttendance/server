package com.sungam1004.register.domain.service;

import com.sungam1004.register.domain.dto.PostDetailDto;
import com.sungam1004.register.domain.dto.PostManagerDto;
import com.sungam1004.register.domain.dto.SavePostDto;
import com.sungam1004.register.domain.entity.Post;
import com.sungam1004.register.domain.entity.Question;
import com.sungam1004.register.domain.repository.PostRepository;
import com.sungam1004.register.domain.repository.QuestionRepository;
import com.sungam1004.register.global.exception.CustomException;
import com.sungam1004.register.global.exception.ErrorCode;
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
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
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

    public void savePost(SavePostDto.Request requestDto) {
        Post post = requestDto.toEntity();
        List<SavePostDto.Request.Question> questions = requestDto.getQuestions();
        int orderNum = 0;
        for (SavePostDto.Request.Question questionDto : questions) {
            if (questionDto.getContent().trim().length() != 0) {
                Question question = Question.builder()
                        .order(orderNum++)
                        .content(questionDto.getContent())
                        .build();
                post.addQuestion(question);

                questionRepository.save(question);
            }
        }
        postRepository.save(post);
    }

    public PostDetailDto postDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
        PostDetailDto ret = PostDetailDto.of(post);

        List<Question> questions = questionRepository.findByPost(post);
        for (Question question : questions) {
            ret.getQuestions().add(question.getContent());
        }
        return ret;
    }
}
