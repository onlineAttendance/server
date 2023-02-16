package com.sungam1004.register.domain.post.controller;

import com.sungam1004.register.domain.post.application.AdminPostService;
import com.sungam1004.register.domain.post.dto.EditPostDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/post")
@Slf4j
public class AdminEditPostController {
    private final AdminPostService adminPostService;

    @GetMapping("edit/{postId}")
    public String editPostFrom(@PathVariable Long postId, Model model) {
        EditPostDto editPostDto = adminPostService.editPostFormById(postId);
        model.addAttribute("editPostDto", editPostDto);
        log.info("editPostDto={}", editPostDto);
        return "admin/post/editPostForm";
    }

    @PostMapping("edit/{postId}")
    public String editPost(@PathVariable Long postId,
                           @Valid @ModelAttribute("editPostDto") EditPostDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("post 수정 실패");
            return "admin/post/editPostForm";
        }
        adminPostService.editPost(postId, requestDto);
        log.info("editPostCompleDto={}", requestDto);
        return "admin/post/completeSavePost";
    }
}
