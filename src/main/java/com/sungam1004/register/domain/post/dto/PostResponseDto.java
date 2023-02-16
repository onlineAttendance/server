package com.sungam1004.register.domain.post.dto;

import com.sungam1004.register.domain.post.entity.Post;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostResponseDto {
    String title;
    String content;
    String date;
    List<String> questions = new ArrayList<>();

    @Builder
    public PostResponseDto(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public static PostResponseDto of(Post post) {
        PostResponseDto ret = PostResponseDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .date(post.getDate().format(DateTimeFormatter.ISO_DATE))
                .build();
        post.getQuestions().forEach(e -> ret.questions.add(e.getContent()));
        return ret;
    }
}
