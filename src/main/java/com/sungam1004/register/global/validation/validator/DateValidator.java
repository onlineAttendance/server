package com.sungam1004.register.global.validation.validator;

import com.sungam1004.register.global.validation.annotation.DateValid;
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
            String tempPattern = pattern;
            if (tempPattern.equals("yy.MM.dd.")) {
                tempPattern = "yyyy.MM.dd.";
                if (value != null && value.length() > 0) {
                    if (value.charAt(0) != '0') value = "19" + value;
                    else value = "20" + value;
                }
                else return false;
            }

            LocalDate.from(LocalDate.parse(value, DateTimeFormatter.ofPattern(tempPattern)));
        } catch (DateTimeParseException e) {
            log.error("DateValidator :", e);
            return false;
        }
        return true;
    }
}