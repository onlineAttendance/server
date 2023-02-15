package com.sungam1004.register.global.validation.validator;

import com.sungam1004.register.global.validation.annotation.AttendancePasswordValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AttendancePasswordValidator implements ConstraintValidator<AttendancePasswordValid, String> {

    private String pattern;

    @Override
    public void initialize(AttendancePasswordValid constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.matches(pattern);
    }
}