package com.sungam1004.register.global.exception;

public class BadRequestException extends ApplicationException {

    public BadRequestException() {
        super(ErrorCode.BAD_REQUEST);
    }

    public BadRequestException(ErrorCode e) {
        super(e);
    }
}
