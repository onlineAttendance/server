package com.sungam1004.register.domain.post.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public static PostSummaryDto createExistPost(Long postId, String date, String title) {
        return PostSummaryDto.builder()
                .id(postId)
                .title(title)
                .date(date)
                .isExist(true)
                .build();
    }

    public static PostSummaryDto createNotExistPost(String date) {
        return PostSummaryDto.builder()
                .date(date)
                .isExist(false)
                .build();
    }
}
