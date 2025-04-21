package budhioct.dev.security.role;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidRole constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equals(value)) {
                return true;
            }
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Invalid role. Valid values: " + Arrays.toString(enumClass.getEnumConstants())
        ).addConstraintViolation();

        return false;
    }

}
