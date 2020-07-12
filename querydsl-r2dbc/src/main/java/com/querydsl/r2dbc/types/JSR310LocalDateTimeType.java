package com.querydsl.r2dbc.types;

import com.querydsl.r2dbc.binding.BindMarker;
import com.querydsl.r2dbc.binding.BindTarget;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import java.sql.Types;
import java.time.*;
import java.time.temporal.Temporal;

/**
 * JSR310LocalDateTimeType maps {@linkplain LocalDateTime}
 * to {@linkplain LocalDateTime} on the R2DBC level
 */
@IgnoreJRERequirement //conditionally included
public class JSR310LocalDateTimeType extends AbstractJSR310DateTimeType<LocalDateTime> {

    public JSR310LocalDateTimeType() {
        super(Types.TIMESTAMP);
    }

    public JSR310LocalDateTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(LocalDateTime value) {
        return dateTimeFormatter.format(value);
    }

    @Override
    public Class<LocalDateTime> getReturnedClass() {
        return LocalDateTime.class;
    }

    @Override
    public void setValue(BindMarker bindMarker, BindTarget bindTarget, LocalDateTime value) {
        try {
            super.setValue(bindMarker, bindTarget, value);
        } catch (Exception e) {
            Instant i = value.toInstant(ZoneOffset.UTC);

            bindMarker.bind(bindTarget, i);
        }
    }

    @Override
    protected LocalDateTime fromDbValue(Temporal value) {
        if (OffsetDateTime.class.isAssignableFrom(value.getClass())) {
            return ((OffsetDateTime) value).toLocalDateTime();
        }
        if (ZonedDateTime.class.isAssignableFrom(value.getClass())) {
            return ((ZonedDateTime) value).toLocalDateTime();
        }
        if (LocalDate.class.isAssignableFrom(value.getClass())) {
            return ((LocalDate) value).atStartOfDay();
        }
        if (Instant.class.isAssignableFrom(value.getClass())) {
            return LocalDateTime.ofInstant((Instant) value, ZoneOffset.UTC);
        }

        return (LocalDateTime) value;
    }

}
