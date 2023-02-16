package com.sungam1004.register.domain.user.api;

import com.sungam1004.register.domain.user.dto.ChangeUserPasswordDto;
import com.sungam1004.register.domain.image.application.ImageService;
import com.sungam1004.register.domain.user.application.UserAccountService;
import com.sungam1004.register.global.resolver.UserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users/account")
@Slf4j
public class UserAccountApi {
    private final UserAccountService userAccountService;
    private final ImageService imageService;

    @PatchMapping("password")
    public void changeUserPassword(@UserId Long userId, @Valid @RequestBody ChangeUserPasswordDto requestDto) {
        userAccountService.changePassword(userId, requestDto.getPassword());
    }

    @PatchMapping("images")
    public void loginUser(@UserId Long userId, MultipartFile faceImageFile) {
        String faceImageUri = imageService.registryImage(faceImageFile);
        userAccountService.changeFaceImage(userId, faceImageUri);
    }
}

