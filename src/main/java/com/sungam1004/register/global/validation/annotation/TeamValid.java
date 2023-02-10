package com.sungam1004.register.global.validation.annotation;

import com.sungam1004.register.global.validation.validator.TeamValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TeamValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TeamValid {

    String message() default "Team에 없는 값입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
