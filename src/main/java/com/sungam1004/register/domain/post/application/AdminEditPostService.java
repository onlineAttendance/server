package com.sungam1004.register.domain.post.application;

import com.sungam1004.register.domain.post.dto.EditPostDto;
import com.sungam1004.register.domain.post.entity.Post;
import com.sungam1004.register.domain.post.entity.Question;
import com.sungam1004.register.domain.post.exception.PostNotFoundException;
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
public class AdminEditPostService {
    private final PostRepository postRepository;
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public EditPostDto getEditPostFormById(Long postId) {
        Post post = postRepository.findWithQuestionsById(postId)
                .orElseThrow(PostNotFoundException::new);
        return EditPostDto.fromPost(post);
    }

    public void editPost(Long postId, EditPostDto requestDto) {
        postRepository.deleteById(postId);
        Post post = requestDto.toEntity();

        int questionOrderNumber = 1;
        List<EditPostDto.QuestionDto> questions = requestDto.getQuestionDtoList();
        for (EditPostDto.QuestionDto questionDto : questions) {
            if (isSuccessSaveQuestion(post, questionDto, questionOrderNumber)) {
                questionOrderNumber++;
            }
        }
        postRepository.save(post);
    }

    private boolean isSuccessSaveQuestion(Post post, EditPostDto.QuestionDto questionDto, int orderNum) {
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
