package com.example.jsr303demo.constraints;

import java.util.Date;

public class IntervalMillisValidatorForDate extends AbstractIntervalMillisValidator<Date> {

    @Override
    protected long getCurrentInterval(Date value) {
        return System.currentTimeMillis() - value.getTime();
    }
}
