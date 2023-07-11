package com.sungam1004.register.global.exception;

public class NotFormatMatchAdminPasswordException extends ApplicationException {

    public NotFormatMatchAdminPasswordException() {
        super(ErrorCode.NOT_FORMAT_MATCH_ADMIN_PASSWORD);
    }
}
