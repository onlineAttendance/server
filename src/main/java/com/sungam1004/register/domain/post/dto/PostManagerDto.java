package com.sungam1004.register.domain.post.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostManagerDto {

    private Long id;
    private String title;

    private String date;
    private Boolean isExist;

    @Builder
    public PostManagerDto(Long id, String title, String date, Boolean isExist) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.isExist = isExist;
    }
}
