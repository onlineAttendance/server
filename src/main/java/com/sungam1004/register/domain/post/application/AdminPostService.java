package com.sungam1004.register.domain.post.application;

import com.sungam1004.register.domain.post.dto.*;
import com.sungam1004.register.domain.post.entity.Post;
import com.sungam1004.register.domain.post.exception.PostNotFoundException;
import com.sungam1004.register.domain.post.repository.PostRepository;
import com.sungam1004.register.domain.post.entity.Question;
import com.sungam1004.register.domain.post.repository.QuestionRepository;
import com.sungam1004.register.global.manager.SundayDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> dates = SundayDate.dates;

        int listPoint = 0;
        for (String date : dates) {
            if (posts.size() > listPoint) {
                Post post = posts.get(listPoint);
                if (post.getDate().equals(LocalDate.parse(date, DateTimeFormatter.ISO_DATE))) {
                    ret.add(PostManagerDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .date(date)
                            .isExist(true)
                            .build());
                    listPoint++;
                    continue;
                }
            }
            ret.add(PostManagerDto.builder()
                    .date(date)
                    .isExist(false)
                    .build());
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

    @Transactional(readOnly = true)
    public PostDetailDto postDetail(Long postId) {
        Post post = postRepository.findWithQuestionsById(postId).orElseThrow(PostNotFoundException::new);
        return PostDetailDto.of(post);
    }

    @Transactional(readOnly = true)
    public EditPostDto editPostFormById(Long postId) {
        Post post = postRepository.findWithQuestionsById(postId).orElseThrow(PostNotFoundException::new);
        EditPostDto ret = EditPostDto.of(post);

        List<Question> questions = post.getQuestions();
        for (Question question : questions) {
            ret.getQuestions().add(new EditPostDto.Question(question.getOrder(), question.getContent()));
        }
        if (questions.size() == 0)
            ret.getQuestions().add(new EditPostDto.Question(1, ""));
        if (questions.size() == 1)
            ret.getQuestions().add(new EditPostDto.Question(2, ""));
        return ret;
    }

    public void editPost(Long postId, EditPostDto requestDto) {
        postRepository.deleteById(postId);
        Post post = requestDto.toEntity();

        List<EditPostDto.Question> questions = requestDto.getQuestions();
        int orderNum = 0;
        for (EditPostDto.Question questionDto : questions) {
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

    public List<PostResponseDto> findPostUsingPage(int page) {
        // 한 페이지당 넘어올 데이터 개수 = 5
        // 페이지는 0부터 계산이 됨
        // Sort.by(정렬방식, 정렬기준)
        // Sort.by(정렬기준): 기본적으로 오름차순으로 정렬
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "date"));
        return postRepository.findAll(pageable).stream()
                .map(PostResponseDto::of)
                .collect(Collectors.toList());
    }
}
