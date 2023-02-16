package com.sungam1004.register.global.exception;

public class AuthenticationException extends ApplicationException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthenticationException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
