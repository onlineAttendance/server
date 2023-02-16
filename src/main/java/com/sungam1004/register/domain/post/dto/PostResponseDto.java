package com.sungam1004.register.domain.post.dto;

import com.sungam1004.register.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    String title;
    String content;
    String date;
    List<String> questions = new ArrayList<>();

    public PostResponseDto(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public void addQuestion(String question) {
        this.questions.add(question);
    }

    public static PostResponseDto of(Post post) {
        PostResponseDto ret = new PostResponseDto(post.getTitle(), post.getContent(), post.getDate().format(DateTimeFormatter.ISO_DATE));
        post.getQuestions().forEach(e -> ret.addQuestion(e.getContent()));
        return ret;
    }
}
