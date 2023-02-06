package com.sungam1004.register.domain.api;

import com.sungam1004.register.domain.dto.PostResponseDto;
import com.sungam1004.register.domain.service.AdminPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users/posts")
@Slf4j
public class PostApi {

    private final AdminPostService adminPostService;

    @GetMapping("/{page}")
    public List<PostResponseDto> findPostUsingPage(@PathVariable int page) {
        return adminPostService.findPostUsingPage(page);
    }
}
