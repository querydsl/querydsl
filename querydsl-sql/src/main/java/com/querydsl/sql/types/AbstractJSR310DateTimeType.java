package com.querydsl.sql.types;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

/**
 * Common abstract superclass for Type implementations for Java Time API (JSR310)
 *
 * @param <T>
 */
@IgnoreJRERequirement //conditionally included
public abstract  class AbstractJSR310DateTimeType<T> extends AbstractType<T> {

    private static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    static {
        UTC.setTimeInMillis(0);
    }

    protected static Calendar utc() {
        return (Calendar) UTC.clone();
    }

    protected static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public AbstractJSR310DateTimeType(int type) {
        super(type);
    }
}
