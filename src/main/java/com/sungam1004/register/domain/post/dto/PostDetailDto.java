package com.sungam1004.register.domain.post.dto;

import com.sungam1004.register.domain.post.entity.Post;
import com.sungam1004.register.domain.post.entity.Question;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostDetailDto {

    private Long postId;
    private String title;

    private String content;

    private String date;
    private List<String> questions = new ArrayList<>();


    @Builder
    public PostDetailDto(String title, String content, String date, Long postId, List<String> questions) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.postId = postId;
        this.questions = questions;
    }

    public static PostDetailDto createFromPost(Post post) {
        List<String> questions = getQuestions(post.getQuestions());

        return PostDetailDto.builder()
                .content(post.getContent())
                .title(post.getTitle())
                .date(post.getDate().format(DateTimeFormatter.ISO_DATE))
                .postId(post.getId())
                .questions(questions)
                .build();
    }

    private static List<String> getQuestions(List<Question> questions) {
        return questions.stream()
                .map(Question::getContent)
                .toList();
    }
}
