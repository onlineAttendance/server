package com.sungam1004.register.domain.image.exception;


import com.sungam1004.register.global.exception.ErrorCode;
import com.sungam1004.register.global.exception.NotFoundException;

public class ImageNotFoundException extends NotFoundException {

    public ImageNotFoundException() {
        super(ErrorCode.FAIL_CALL_IMAGE);
    }
}
