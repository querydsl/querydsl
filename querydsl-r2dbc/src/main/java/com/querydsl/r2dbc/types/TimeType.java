package com.querydsl.r2dbc.types;

import java.sql.Time;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;

/**
 * {@code TimeType} maps Time to Time on the JDBC level
 *
 * @author mc_fish
 */
public class TimeType extends AbstractDateTimeType<Time, Temporal> {

    public TimeType() {
        super(Types.TIME);
    }

    public TimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Time value) {
        return timeFormatter.format(value);
    }

    @Override
    public Class<Time> getReturnedClass() {
        return Time.class;
    }

    @Override
    public Class<Temporal> getDatabaseClass() {
        return Temporal.class;
    }

    @Override
    protected LocalTime toDbValue(Time value) {
        return value.toLocalTime();
    }

    @Override
    protected Time fromDbValue(Temporal value) {
        if (LocalDateTime.class.isAssignableFrom(value.getClass())) {
            return Time.valueOf(((LocalDateTime) value).toLocalTime());
        }

        return Time.valueOf((LocalTime) value);
    }

}
