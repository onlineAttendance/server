package com.sungam1004.register.domain.admin;

import com.sungam1004.register.global.exception.CustomException;
import com.sungam1004.register.global.exception.ErrorCode;
import com.sungam1004.register.global.manager.PasswordManager;
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

    public void changeAdminPassword(String password) {
        passwordManager.changeAdminPassword(password);
    }
}
