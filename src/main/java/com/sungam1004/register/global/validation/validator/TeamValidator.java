package com.sungam1004.register.global.validation.validator;

import com.sungam1004.register.domain.entity.Team;
import com.sungam1004.register.global.validation.annotation.TeamValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class TeamValidator implements ConstraintValidator<TeamValid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            Team.valueOf(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
