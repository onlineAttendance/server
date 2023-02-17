package com.sungam1004.register.domain.attendance.exception;

import com.sungam1004.register.global.exception.ApplicationException;
import com.sungam1004.register.global.exception.ErrorCode;

public class InvalidDayOfWeekException extends ApplicationException {

    public InvalidDayOfWeekException() {
        super(ErrorCode.INVALID_DAY_OF_WEEK);
    }
}
