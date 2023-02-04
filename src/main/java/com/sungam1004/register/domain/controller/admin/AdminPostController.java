package com.sungam1004.register.domain.controller.admin;

import com.sungam1004.register.domain.dto.*;
import com.sungam1004.register.domain.service.AdminPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/post")
@Slf4j
public class AdminPostController {
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

    @GetMapping("add")
    public String savePostForm(Model model) {
        SavePostDto.Request request = new SavePostDto.Request();
        request.getQuestions().add(new SavePostDto.Request.Question(1, ""));
        request.getQuestions().add(new SavePostDto.Request.Question(2, ""));
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

    @GetMapping("edit/{postId}")
    public String editPostFrom(@PathVariable Long postId, Model model) {
        EditPostDto editPostDto = adminPostService.editPostById(postId);
        model.addAttribute("editPostDto", editPostDto);
        log.info("editPostDto={}", editPostDto);
        return "admin/post/editPostForm";
    }

    /*
    @PostMapping
    public String savePost(@Valid @ModelAttribute("savePostDto") SavePostDto.Request requestDto,
                          BindingResult bindingResult, MultipartFile faceImageFile, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teams", Team.getTeamNameList());
            return "admin/userManage/addUser";
        }
        try {
            String faceImageUri = imageService.registryImage(faceImageFile);
            adminService.addUser(requestDto, faceImageUri);
        } catch (CustomException e) {
            if (e.getError() == ErrorCode.DUPLICATE_USER_NAME) {
                bindingResult.rejectValue("name", "0", e.getMessage());
            }
            model.addAttribute("teams", Team.getTeamNameList());
            return "admin/userManage/addUser";
        }

        return "admin/userManage/completeAddUser";
    }
     */
}
