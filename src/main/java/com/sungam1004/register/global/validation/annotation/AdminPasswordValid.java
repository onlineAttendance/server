package com.sungam1004.register.global.validation.annotation;

import com.sungam1004.register.global.validation.validator.AdminPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AdminPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminPasswordValid {

    String message() default "비밀번호는 5자리 이상, 20자리 이하입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default 5;

    int max() default 20;
}