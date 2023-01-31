package com.sungam1004.register.api;

import com.sungam1004.register.dto.AddUserDto;
import com.sungam1004.register.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/manager/add")
@Slf4j
public class AdminAddUserApi {

    private final AdminService adminService;

    @PostMapping
    public void addUser(@Valid @RequestBody AddUserDto.Request requestDto) {
        adminService.addUser(requestDto);
    }

}
