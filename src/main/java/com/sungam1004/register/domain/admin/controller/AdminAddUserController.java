package com.sungam1004.register.domain.admin.controller;

import com.sungam1004.register.domain.image.application.ImageService;
import com.sungam1004.register.domain.user.application.UserSignupService;
import com.sungam1004.register.domain.user.dto.AddUserDto;
import com.sungam1004.register.domain.user.entity.Team;
import com.sungam1004.register.domain.user.exception.DuplicateUserNameException;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/manager/add")
@Slf4j
public class AdminAddUserController {

    private final UserSignupService userSignupService;
    private final ImageService imageService;

    @GetMapping
    public String addUserForm(Model model) {
        model.addAttribute("addUserDto", new AddUserDto.Request());
        model.addAttribute("teams", Team.getTeamNameList());
        return "admin/userManage/addUser";
    }

    @PostMapping
    public String addUser(@Valid @ModelAttribute("addUserDto") AddUserDto.Request requestDto,
                          BindingResult bindingResult, MultipartFile faceImageFile, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teams", Team.getTeamNameList());
            return "admin/userManage/addUser";
        }
        try {
            String faceImageUri = imageService.registryImage(faceImageFile);
            userSignupService.addUser(requestDto.toEntity(faceImageUri));
        } catch (DuplicateUserNameException e) {
            bindingResult.rejectValue("name", null, e.getMessage());
            model.addAttribute("teams", Team.getTeamNameList());
            return "admin/userManage/addUser";
        }
        return "admin/userManage/completeAddUser";
    }

}
