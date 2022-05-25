package com.example.electricbillsystem.validation;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomValidator {

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    public List<String> validate(Object o) {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<Object>> violations = validator.validate(o);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Object> violation : violations) {
                errors.add(violation.getMessage());
            }
        }
        return errors;
    }

}
