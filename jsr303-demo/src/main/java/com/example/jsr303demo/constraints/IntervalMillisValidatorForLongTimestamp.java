package com.example.jsr303demo.constraints;

public class IntervalMillisValidatorForLongTimestamp extends AbstractIntervalMillisValidator<Long> {

    @Override
    protected long getCurrentInterval(Long value) {
        return System.currentTimeMillis() - value;
    }
}
