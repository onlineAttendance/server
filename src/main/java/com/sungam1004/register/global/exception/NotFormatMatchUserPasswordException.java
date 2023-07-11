package com.sungam1004.register.global.exception;

import com.sungam1004.register.global.exception.ApplicationException;
import com.sungam1004.register.global.exception.ErrorCode;

public class NotFormatMatchUserPasswordException extends ApplicationException {

    public NotFormatMatchUserPasswordException() {
        super(ErrorCode.NOT_FORMAT_MATCH_USER_PASSWORD);
    }
}
