package com.sungam1004.register.domain.image.exception;

import com.sungam1004.register.global.exception.ApplicationException;
import com.sungam1004.register.global.exception.ErrorCode;

public class FailStoreImageException extends ApplicationException {

    public FailStoreImageException() {
        super(ErrorCode.FAIL_STORE_IMAGE);
    }
}
