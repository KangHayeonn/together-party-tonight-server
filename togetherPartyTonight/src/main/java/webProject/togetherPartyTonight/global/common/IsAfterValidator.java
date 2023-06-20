package webProject.togetherPartyTonight.global.common;

import org.springframework.http.ResponseEntity;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class IsAfterValidator implements ConstraintValidator<IsAfter, String> {

    private IsAfter meetingDate;

    @Override
    public void initialize(IsAfter constraintAnnotation) {
        this.meetingDate= constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        LocalDate date = LocalDate.parse(value);

        if (date.isBefore(LocalDate.now())) {
            return false;
        }

        return true;
    }
}
