package com.querydsl.sql.types;

import java.time.temporal.Temporal;

/**
 * Common abstract superclass for Type implementations for Java Time API (JSR310)
 *
 * @param <T>
 */
public abstract class AbstractJSR310DateTimeType<T extends Temporal> extends AbstractDateTimeType<T> {

    public AbstractJSR310DateTimeType(int type) {
        super(type);
    }

}
