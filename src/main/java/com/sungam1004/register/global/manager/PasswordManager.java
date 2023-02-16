package com.sungam1004.register.global.manager;

import com.sungam1004.register.global.exception.ApplicationException;
import com.sungam1004.register.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class PasswordManager {

    @Value("${password.attendance}")
    public String attendancePassword;

    @Value("${password.admin}")
    public String adminPassword;

    public void changeAttendancePassword(String nPassword) {
        if (Pattern.matches("^[0-9]{4}$", nPassword)) {
            attendancePassword = nPassword;
        }
        else throw new ApplicationException(ErrorCode.NOT_FORMAT_MATCH_USER_PASSWORD);
    }

    public Boolean isCorrectAttendancePassword(String nPassword) {
        return Objects.equals(attendancePassword, nPassword);
    }

    public void changeAdminPassword(String nPassword) {
        if (nPassword.length() >= 5 && nPassword.length() <= 20) {
            adminPassword = nPassword;
        }
        else throw new ApplicationException(ErrorCode.NOT_FORMAT_MATCH_ADMIN_PASSWORD);
    }

    public Boolean isCorrectAdminPassword(String nPassword) {
        return Objects.equals(adminPassword, nPassword);
    }
}
