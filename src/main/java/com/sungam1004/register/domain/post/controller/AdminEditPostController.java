package com.sungam1004.register.domain.post.controller;

import com.sungam1004.register.domain.post.application.AdminEditPostService;
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

    private final AdminEditPostService adminEditPostService;

    @GetMapping("edit/{postId}")
    public String editPostFrom(@PathVariable Long postId, Model model) {
        EditPostDto editPostDto = adminEditPostService.getEditPostFormById(postId);
        model.addAttribute("editPostDto", editPostDto);
        return "admin/post/editPostForm";
    }

    @PostMapping("edit/{postId}")
    public String editPost(@PathVariable Long postId,
                           @Valid @ModelAttribute("editPostDto") EditPostDto requestDto,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("post 수정 실패");
            return "admin/post/editPostForm";
        }
        adminEditPostService.editPost(postId, requestDto);
        log.info("Complete edit Post = {}", requestDto);
        return "admin/post/completeSavePost";
    }
}
