package com.sungam1004.register.global.validation.annotation;

import com.sungam1004.register.global.validation.validator.UserPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPasswordValid {

    String message() default "비밀번호는 숫자 4자리입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String pattern() default "[0-9][0-9][0-9][0-9]";
}