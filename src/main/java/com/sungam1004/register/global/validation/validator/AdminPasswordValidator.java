package com.sungam1004.register.global.validation.validator;

import com.sungam1004.register.global.validation.annotation.AdminPasswordValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminPasswordValidator implements ConstraintValidator<AdminPasswordValid, String> {

    private int min;
    private int max;

    @Override
    public void initialize(AdminPasswordValid constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String message = "비밀번호는 " + this.min + "자리 이상, " + this.max + " 자리 이하입니다.";
        if (value == null || value.length() < this.min || value.length() > this.max) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}