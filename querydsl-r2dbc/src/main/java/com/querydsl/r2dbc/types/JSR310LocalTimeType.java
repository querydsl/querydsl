package com.querydsl.r2dbc.types;

import com.querydsl.r2dbc.binding.BindMarker;
import com.querydsl.r2dbc.binding.BindTarget;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import java.sql.Time;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;

/**
 * JSR310LocalTimeType maps {@linkplain LocalTime}
 * to {@linkplain Time} on the JDBC level
 */
@IgnoreJRERequirement //conditionally included
public class JSR310LocalTimeType extends AbstractJSR310DateTimeType<LocalTime> {

    public JSR310LocalTimeType() {
        super(Types.TIME);
    }

    public JSR310LocalTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(LocalTime value) {
        return timeFormatter.format(value);
    }

    @Override
    public Class<LocalTime> getReturnedClass() {
        return LocalTime.class;
    }

    @Override
    public void setValue(BindMarker bindMarker, BindTarget bindTarget, LocalTime value) {
        try {
            super.setValue(bindMarker, bindTarget, value);
        } catch (Exception e) {
            bindMarker.bind(bindTarget, new Time(value.get(ChronoField.MILLI_OF_DAY)));
        }
    }

    @Override
    protected LocalTime fromDbValue(Temporal value) {
        if (LocalDateTime.class.isAssignableFrom(value.getClass())) {
            return ((LocalDateTime) value).toLocalTime();
        }

        return super.fromDbValue(value);
    }

}
