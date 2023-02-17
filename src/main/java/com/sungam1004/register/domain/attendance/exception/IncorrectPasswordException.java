package com.sungam1004.register.domain.attendance.exception;

import com.sungam1004.register.global.exception.ApplicationException;
import com.sungam1004.register.global.exception.ErrorCode;

public class IncorrectPasswordException extends ApplicationException {

    public IncorrectPasswordException() {
        super(ErrorCode.INCORRECT_PASSWORD);
    }
}
