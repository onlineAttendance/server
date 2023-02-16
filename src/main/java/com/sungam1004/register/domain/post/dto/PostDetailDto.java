package com.sungam1004.register.domain.post.dto;

import com.sungam1004.register.domain.post.entity.Post;
import lombok.*;

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
    public PostDetailDto(String title, String content, String date, Long postId) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.postId = postId;
    }

    public static PostDetailDto of(Post post) {
        PostDetailDto ret = PostDetailDto.builder()
                .content(post.getContent())
                .title(post.getTitle())
                .date(post.getDate().format(DateTimeFormatter.ISO_DATE))
                .postId(post.getId())
                .build();
        post.getQuestions()
                .forEach(question -> ret.getQuestions().add(question.getContent()));
        return ret;
    }

}
