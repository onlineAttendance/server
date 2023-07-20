package com.sungam1004.register.domain.post.controller;

import com.sungam1004.register.domain.post.application.AdminFindPostService;
import com.sungam1004.register.domain.post.dto.PostDetailDto;
import com.sungam1004.register.domain.post.dto.PostSummaryDto;
import com.sungam1004.register.domain.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/post")
@Slf4j
public class AdminFindPostController {
    private final AdminFindPostService adminFindPostService;

    @GetMapping("list")
    public String adminPostHome(Model model) {
        List<PostSummaryDto> postSummaryDtoList = adminFindPostService.findPostSummaryDtoList();
        model.addAttribute("postManagerDto", postSummaryDtoList);
        return "admin/post/postList";
    }

    @GetMapping("detail/{postId}")
    public String postDetailForm(@PathVariable Long postId, Model model) {
        try {
            PostDetailDto response = adminFindPostService.postDetail(postId);
            model.addAttribute("postDetailDto", response);
        } catch (PostNotFoundException e) {
            log.error("PostNotFoundException: {}", e.getMessage());
            return "error/404";
        }
        return "admin/post/postDetail";
    }
}
