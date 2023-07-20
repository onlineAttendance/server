package com.sungam1004.register.global.manager;

import com.sungam1004.register.global.exception.NotFormatMatchAdminPasswordException;
import com.sungam1004.register.global.exception.NotFormatMatchUserPasswordException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordManager {

    @Value("${password.attendance}")
    private String attendancePassword;

    @Value("${password.admin}")
    private String adminPassword;

    private final String initUserPassword = "1234";

    private static final String attendancePasswordRegex = "^[0-9]{4}$";
    private static final int adminPasswordMinLength = 5;
    private static final int adminPasswordMaxLength = 20;

    public void changeAttendancePassword(String nPassword) {
        if (isMatchFormatToChangeAttendancePassword(nPassword)) {
            attendancePassword = nPassword;
        }
        else {
            throw new NotFormatMatchUserPasswordException();
        }
    }

    public void changeAdminPassword(String nPassword) {
        if (isMatchFormatToChangeAdminPassword(nPassword)) {
            adminPassword = nPassword;
        }
        else {
            throw new NotFormatMatchAdminPasswordException();
        }
    }

    public Boolean isCorrectAttendancePassword(String nPassword) {
        return attendancePassword.equals(nPassword);
    }

    public Boolean isCorrectAdminPassword(String nPassword) {
        return adminPassword.equals(nPassword);
    }

    private boolean isMatchFormatToChangeAdminPassword(String password) {
        return adminPasswordMinLength <= password.length() && password.length() <= adminPasswordMaxLength;
    }

    private boolean isMatchFormatToChangeAttendancePassword(String password) {
        return Pattern.matches(attendancePasswordRegex, password);
    }

    public String getInitUserPassword() {
        return initUserPassword;
    }
}
