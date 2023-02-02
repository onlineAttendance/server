package com.sungam1004.register.domain.controller.admin;

import com.sungam1004.register.domain.dto.PostManagerDto;
import com.sungam1004.register.domain.service.AdminPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
