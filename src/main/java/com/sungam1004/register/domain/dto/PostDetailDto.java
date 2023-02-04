package com.sungam1004.register.domain.dto;

import com.sungam1004.register.domain.entity.Post;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PostDetailDto {

    private String title;

    private String content;

    private String date;
    private List<String> questions = new ArrayList<>();


    @Builder
    public PostDetailDto(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public static PostDetailDto of(Post post) {
        return PostDetailDto.builder()
                .content(post.getContent())
                .title(post.getTitle())
                .date(post.getDate().format(DateTimeFormatter.ISO_DATE))
                .build();
    }

}
