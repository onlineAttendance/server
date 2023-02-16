package com.sungam1004.register.global.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorCode error;

    public ApplicationException(ErrorCode e) {
        super(e.getMessage());
        this.error = e;
    }
}