package com.sungam1004.register.domain.post.controller;

import com.sungam1004.register.domain.post.application.AdminPostService;
import com.sungam1004.register.domain.post.dto.PostDetailDto;
import com.sungam1004.register.domain.post.dto.PostManagerDto;
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
    private final AdminPostService adminPostService;

    @GetMapping("list")
    public String adminPostHome(Model model) {
        List<PostManagerDto> ret = adminPostService.findPostList();
        model.addAttribute("postManagerDto", ret);
        return "admin/post/postList";
    }

    @GetMapping("detail/{postId}")
    public String postDetailForm(@PathVariable Long postId, Model model) {
        PostDetailDto response = adminPostService.postDetail(postId);
        model.addAttribute("postDetailDto", response);
        return "admin/post/postDetail";
    }
}
