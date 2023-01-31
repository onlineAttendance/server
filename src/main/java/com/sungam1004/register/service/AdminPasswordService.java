package com.sungam1004.register.service;

import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;
import com.sungam1004.register.manager.PasswordManager;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminPasswordService {
    private final PasswordManager passwordManager;

    public void loginAdmin(String password) {
        if (!passwordManager.isCorrectAdminPassword(password)) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
    }

    public void changeUserPassword(String password) {
        passwordManager.changeUserPassword(password);
    }

    public void changeAdminPassword(String password) {
        passwordManager.changeAdminPassword(password);
    }
}
