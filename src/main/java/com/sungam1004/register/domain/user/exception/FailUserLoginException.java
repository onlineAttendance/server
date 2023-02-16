package com.sungam1004.register.domain.user.exception;

import com.sungam1004.register.global.exception.AuthenticationException;
import com.sungam1004.register.global.exception.ErrorCode;

public class FailUserLoginException extends AuthenticationException {
    public FailUserLoginException() {
        super(ErrorCode.FAIL_LOGIN);
    }
}
