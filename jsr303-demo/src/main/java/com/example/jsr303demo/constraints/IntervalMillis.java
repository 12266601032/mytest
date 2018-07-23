package com.example.jsr303demo.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {IntervalMillisValidatorForDate.class, IntervalMillisValidatorForLongTimestamp.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface IntervalMillis {

    long millis() default 0;

    String message() default "{com.example.jsr303demo.constraints.IntervalMillis}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
