package com.querydsl.sql.types;

import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.TimeZone;

public abstract class AbstractJSR310DateTimeTypeTest<T extends Temporal> {

    protected static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    static {
        UTC.setTimeInMillis(0);
    }

    protected final AbstractJSR310DateTimeType<T> type;

    public AbstractJSR310DateTimeTypeTest(AbstractJSR310DateTimeType<T> type) {
        this.type = type;
    }
}
