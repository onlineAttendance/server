package com.sungam1004.register.domain.post.dto;

import com.sungam1004.register.domain.post.entity.Post;
import com.sungam1004.register.global.validation.annotation.DateValid;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavePostDto {

    @NotBlank
    @Size(max = 30, message = "제목은 최대 {max}자리 이하입니다.")
    private String title;

    private String content;

    @DateValid(message = "yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
    private String date;

    @Valid
    private List<Question> questions = new ArrayList<>();

    public static SavePostDto initialSetThreeQuestions() {
        SavePostDto request = new SavePostDto();
        for (int i = 1; i <= 3; i++) {
            request.getQuestions().add(new SavePostDto.Question(i, ""));
        }
        return request;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {

        @NotNull
        private Integer order;

        private String content;

        public boolean isContentBlank() {
            return content.trim().length() == 0;
        }
    }

    public Post toEntity() {
        return Post.builder()
                .content(content)
                .title(title)
                .date(LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
                .build();
    }
}

