package com.sungam1004.register.domain.dto;

import com.sungam1004.register.domain.entity.Post;
import com.sungam1004.register.global.validation.annotation.DateValid;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditPostDto {

    private Long postId;

    @NotBlank
    @Size(max = 30, message = "제목은 최대 {max}자리 이하입니다.")
    private String title;

    private String content;

    @DateValid(message = "yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
    private String date;

    public static EditPostDto of(Post post) {
        return EditPostDto.builder()
                .content(post.getContent())
                .title(post.getTitle())
                .date(post.getDate().format(DateTimeFormatter.ISO_DATE))
                .postId(post.getId())
                .questions(new ArrayList<>())
                .build();
    }

    @Valid
    private List<Question> questions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {

        @NotNull
        private Integer order;

        @NotBlank
        @Size(max = 100, message = "각 질문은 최대 {max}자리 이하입니다.")
        private String content;
    }

}
