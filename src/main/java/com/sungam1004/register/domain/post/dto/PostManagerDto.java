package com.sungam1004.register.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostManagerDto {

    private Long id;
    private String title;

    private String date;
    private Boolean isExist;
}
