package cn.edu.buaa.patpat.boot.extensions.validation;

import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

public class Validator {
    private Validator() {}

    public static void validate(Object object) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            jakarta.validation.Validator validator = factory.getValidator();
            var violations = validator.validate(object);
            if (!violations.isEmpty()) {
                var message = String.join("\n", violations.stream().map(ConstraintViolation::getMessage).toArray(String[]::new));
                throw new BadRequestException(message);
            }
        }
    }
}
