package com.sungam1004.register.validation.validator;

import com.sungam1004.register.validation.annotation.DateValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class DateValidator implements ConstraintValidator<DateValid, String> {

    private String pattern;

    @Override
    public void initialize(DateValid constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        try {
            LocalDate.from(LocalDate.parse(value, DateTimeFormatter.ofPattern(this.pattern)));
        } catch (DateTimeParseException e) {
            log.error("DateValidator : {}", e);
            return false;
        }
        return true;
    }
}