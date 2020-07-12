package com.querydsl.r2dbc.types;

import com.querydsl.r2dbc.binding.BindMarker;
import com.querydsl.r2dbc.binding.BindTarget;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import java.sql.Time;
import java.sql.Types;
import java.time.*;
import java.time.temporal.Temporal;

/**
 * JSR310OffsetTimeType maps {@linkplain OffsetTime}
 * to {@linkplain Time} on the JDBC level
 */
@IgnoreJRERequirement //conditionally included
public class JSR310OffsetTimeType extends AbstractJSR310DateTimeType<OffsetTime> {

    public JSR310OffsetTimeType() {
        super(Types.TIME);
    }

    public JSR310OffsetTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(OffsetTime value) {
        return timeFormatter.format(value);
    }

    @Override
    public Class<OffsetTime> getReturnedClass() {
        return OffsetTime.class;
    }

    @Override
    public void setValue(BindMarker bindMarker, BindTarget bindTarget, OffsetTime value) {
        try {
            super.setValue(bindMarker, bindTarget, value);
        } catch (Exception e) {
            bindMarker.bind(bindTarget, value);
        }
    }

    @Override
    protected Temporal toDbValue(OffsetTime value) {
        return value.toLocalTime();
    }

    @Override
    protected OffsetTime fromDbValue(Temporal value) {
        if (LocalDateTime.class.isAssignableFrom(value.getClass())) {
            return ((LocalDateTime) value).toLocalTime().atOffset(ZoneOffset.UTC);
        }
        if (ZonedDateTime.class.isAssignableFrom(value.getClass())) {
            return ((ZonedDateTime) value).toOffsetDateTime().toOffsetTime();
        }
        if (LocalTime.class.isAssignableFrom(value.getClass())) {
            return ((LocalTime) value).atOffset(ZoneOffset.UTC);
        }

        return super.fromDbValue(value);
    }
}
