package com.sungam1004.register.domain.user.exception;

import com.sungam1004.register.global.exception.ApplicationException;
import com.sungam1004.register.global.exception.ErrorCode;

public class DuplicateUserNameException extends ApplicationException {

    public DuplicateUserNameException() {
        super(ErrorCode.DUPLICATE_USER_NAME);
    }
}
