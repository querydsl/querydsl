package com.querydsl.r2dbc.types;

import com.querydsl.r2dbc.binding.BindMarker;
import com.querydsl.r2dbc.binding.BindTarget;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

/**
 * JSR310OffsetDateTimeType maps {@linkplain OffsetDateTime}
 * to {@linkplain Timestamp} on the JDBC level
 */
@IgnoreJRERequirement //conditionally included
public class JSR310OffsetDateTimeType extends AbstractJSR310DateTimeType<OffsetDateTime> {


    public JSR310OffsetDateTimeType() {
        super(Types.TIMESTAMP);
    }

    public JSR310OffsetDateTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(OffsetDateTime value) {
        return dateTimeFormatter.format(value);
    }

    @Override
    public Class<OffsetDateTime> getReturnedClass() {
        return OffsetDateTime.class;
    }

    @Override
    public void setValue(BindMarker bindMarker, BindTarget bindTarget, OffsetDateTime value) {
        try {
            super.setValue(bindMarker, bindTarget, value);
        } catch (Exception e) {
            bindMarker.bind(bindTarget, new Timestamp(value.toInstant().toEpochMilli()));
        }
    }

    @Override
    protected OffsetDateTime fromDbValue(Temporal value) {
        if (LocalDateTime.class.isAssignableFrom(value.getClass())) {
            return ((LocalDateTime) value).atOffset(ZoneOffset.UTC);
        }
        if (ZonedDateTime.class.isAssignableFrom(value.getClass())) {
            return ((ZonedDateTime) value).toOffsetDateTime();
        }

        return (OffsetDateTime) value;
    }

}
