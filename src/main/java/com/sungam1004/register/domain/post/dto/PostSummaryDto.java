package com.sungam1004.register.domain.post.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostSummaryDto {

    private Long id;
    private String title;

    private String date;
    private Boolean isExist;

    @Builder
    public PostSummaryDto(Long id, String title, String date, Boolean isExist) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.isExist = isExist;
    }

    public static PostSummaryDto createFromExistPost(Long postId, String title, LocalDate date) {
        return PostSummaryDto.builder()
                .id(postId)
                .title(title)
                .date(date.format(DateTimeFormatter.ISO_DATE))
                .isExist(true)
                .build();
    }

    public static PostSummaryDto createFromNotExistPost(LocalDate date) {
        return PostSummaryDto.builder()
                .date(date.format(DateTimeFormatter.ISO_DATE))
                .isExist(false)
                .build();
    }
}
