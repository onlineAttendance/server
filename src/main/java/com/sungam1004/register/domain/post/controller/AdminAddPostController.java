package com.sungam1004.register.domain.post.controller;

import com.sungam1004.register.domain.post.application.AdminPostService;
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
    private final AdminPostService adminPostService;

    @GetMapping("add")
    public String savePostForm(Model model) {
        SavePostDto.Request request = new SavePostDto.Request();
        request.getQuestions().add(new SavePostDto.Request.Question(1, ""));
        request.getQuestions().add(new SavePostDto.Request.Question(2, ""));
        request.getQuestions().add(new SavePostDto.Request.Question(3, ""));
        model.addAttribute("savePostDto", request);
        return "admin/post/savePostForm";
    }

    @PostMapping("add")
    public String savePost(@Valid @ModelAttribute("savePostDto") SavePostDto.Request requestDto,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("post 저장 실패");
            return "admin/post/savePostForm";
        }
        adminPostService.savePost(requestDto);
        return "admin/post/completeSavePost";
    }
}
