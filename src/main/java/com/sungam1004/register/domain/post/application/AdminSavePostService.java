package com.sungam1004.register.domain.post.application;

import com.sungam1004.register.domain.post.dto.SavePostDto;
import com.sungam1004.register.domain.post.entity.Post;
import com.sungam1004.register.domain.post.entity.Question;
import com.sungam1004.register.domain.post.repository.PostRepository;
import com.sungam1004.register.domain.post.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminSavePostService {
    private final PostRepository postRepository;
    private final QuestionRepository questionRepository;

    public void savePost(SavePostDto requestDto) {
        Post post = requestDto.toEntity();
        int questionOrderNumber = 1;
        List<SavePostDto.Question> questions = requestDto.getQuestions();
        for (SavePostDto.Question questionDto : questions) {
            if (isSuccessSaveQuestion(post, questionDto, questionOrderNumber)) {
                questionOrderNumber++;
            }
        }
        postRepository.save(post);
    }

    private boolean isSuccessSaveQuestion(Post post, SavePostDto.Question questionDto, int orderNum) {
        if (!questionDto.isContentBlank()) {
            Question question = Question.builder()
                    .content(questionDto.getContent())
                    .order(orderNum)
                    .build();
            post.addQuestion(question);
            questionRepository.save(question);
            return true;
        }
        return false;
    }

}
