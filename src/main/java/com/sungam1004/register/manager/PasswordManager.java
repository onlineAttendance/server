package com.sungam1004.register.manager;

import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class PasswordManager {

    @Value("${password.user}")
    public String userPassword;

    @Value("${password.admin}")
    public String adminPassword;

    public void changeUserPassword(String nPassword) {
        if (Pattern.matches("^[0-9]{4}$", nPassword)) {
            userPassword = nPassword;
        }
        else throw new CustomException(ErrorCode.NOT_FORMAT_MATCH_USER_PASSWORD);
    }

    public Boolean isCorrectUserPassword(String nPassword) {
        return Objects.equals(userPassword, nPassword);
    }

    public void changeAdminPassword(String nPassword) {
        if (nPassword.length() >= 5 && nPassword.length() <= 20) {
            adminPassword = nPassword;
        }
        else throw new CustomException(ErrorCode.NOT_FORMAT_MATCH_ADMIN_PASSWORD);
    }

    public Boolean isCorrectAdminPassword(String nPassword) {
        return Objects.equals(adminPassword, nPassword);
    }
}
