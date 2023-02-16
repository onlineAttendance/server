package com.sungam1004.register.domain.post.exception;


import com.sungam1004.register.global.exception.ErrorCode;
import com.sungam1004.register.global.exception.NotFoundException;

public class PostNotFoundException extends NotFoundException {

    public PostNotFoundException() {
        super(ErrorCode.NOT_FOUND_POST);
    }
}
