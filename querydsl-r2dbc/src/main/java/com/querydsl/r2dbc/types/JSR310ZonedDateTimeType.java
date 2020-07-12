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
 * JSR310ZonedDateTimeType maps {@linkplain ZonedDateTime}
 * to {@linkplain Timestamp} on the JDBC level
 */
@IgnoreJRERequirement //conditionally included
public class JSR310ZonedDateTimeType extends AbstractJSR310DateTimeType<ZonedDateTime> {

    public JSR310ZonedDateTimeType() {
        super(Types.TIMESTAMP_WITH_TIMEZONE);
    }

    public JSR310ZonedDateTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(ZonedDateTime value) {
        return dateTimeFormatter.format(value);
    }

    @Override
    public Class<ZonedDateTime> getReturnedClass() {
        return ZonedDateTime.class;
    }

    @Override
    public void setValue(BindMarker bindMarker, BindTarget bindTarget, ZonedDateTime value) {
        try {
            super.setValue(bindMarker, bindTarget, value);
        } catch (Exception e) {
            bindMarker.bind(bindTarget, new Timestamp(value.toInstant().toEpochMilli()));
        }
    }

    @Override
    protected ZonedDateTime fromDbValue(Temporal value) {
        if (LocalDateTime.class.isAssignableFrom(value.getClass())) {
            return ((LocalDateTime) value).atZone(ZoneOffset.UTC);
        }
        if (OffsetDateTime.class.isAssignableFrom(value.getClass())) {
            return ((OffsetDateTime) value).toZonedDateTime();
        }

        return (ZonedDateTime) value;
    }

}
