package com.sungam1004.register.domain.post.controller;

import com.sungam1004.register.domain.post.application.AdminSavePostService;
import com.sungam1004.register.domain.post.dto.SavePostDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/post")
@Slf4j
public class AdminAddPostController {
    private final AdminSavePostService adminSavePostService;

    @GetMapping("add")
    public String savePostForm(Model model) {
        SavePostDto request = SavePostDto.initialSetThreeQuestions();
        model.addAttribute("savePostDto", request);
        return "admin/post/savePostForm";
    }

    @PostMapping("add")
    public String savePost(@Valid @ModelAttribute("savePostDto") SavePostDto requestDto,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("post 저장 실패");
            return "admin/post/savePostForm";
        }
        adminSavePostService.savePost(requestDto);
        return "admin/post/completeSavePost";
    }
}
