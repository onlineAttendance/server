package com.sungam1004.register.domain.user.exception;


import com.sungam1004.register.global.exception.ErrorCode;
import com.sungam1004.register.global.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}
