package com.sungam1004.register.api;

import com.sungam1004.register.dto.AdminPasswordDto;
import com.sungam1004.register.dto.UserPasswordDto;
import com.sungam1004.register.service.AdminPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
@Slf4j
public class AdminChangePasswordApi {

    private final AdminPasswordService adminPasswordService;

    @PatchMapping("/userpassword")
    public void changeUserPassword(@Valid @RequestBody UserPasswordDto.Request requestDto) {
        adminPasswordService.changeUserPassword(requestDto.getPassword());
    }

    @PatchMapping("/adminpassword")
    public void changeAdminPassword(@Valid @RequestBody AdminPasswordDto.Request requestDto) {
        adminPasswordService.changeAdminPassword(requestDto.getPassword());
    }

}

