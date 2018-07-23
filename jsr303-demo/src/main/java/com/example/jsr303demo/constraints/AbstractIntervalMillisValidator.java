package com.example.jsr303demo.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public abstract class AbstractIntervalMillisValidator<T> implements ConstraintValidator<IntervalMillis, T> {

    protected long millis = -1;

    @Override
    public void initialize(IntervalMillis constraintAnnotation) {

        this.millis = constraintAnnotation.millis();

    }

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {
        if (value != null && millis >= 0) {
            return getCurrentInterval(value) <= millis;
        }
        return true;
    }

    protected abstract long getCurrentInterval(T value);
}
