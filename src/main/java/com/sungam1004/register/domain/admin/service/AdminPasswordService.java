package com.sungam1004.register.domain.admin.service;

import com.sungam1004.register.domain.attendance.exception.IncorrectPasswordException;
import com.sungam1004.register.global.manager.PasswordManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminPasswordService {
    private final PasswordManager passwordManager;

    public void loginAdmin(String password) {
        if (!passwordManager.isCorrectAdminPassword(password)) {
            throw new IncorrectPasswordException();
        }
    }

    public void changeAdminPassword(String password) {
        passwordManager.changeAdminPassword(password);
    }
}
