package com.sungam1004.register.domain.attendance.exception;

import com.sungam1004.register.global.exception.ApplicationException;
import com.sungam1004.register.global.exception.ErrorCode;

public class DuplicateAttendanceException extends ApplicationException {

    public DuplicateAttendanceException() {
        super(ErrorCode.DUPLICATE_ATTENDANCE);
    }
}
