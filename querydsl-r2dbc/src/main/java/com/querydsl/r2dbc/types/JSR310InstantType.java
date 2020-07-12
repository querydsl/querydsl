package com.querydsl.r2dbc.types;

import com.querydsl.r2dbc.binding.BindMarker;
import com.querydsl.r2dbc.binding.BindTarget;
import io.r2dbc.spi.Row;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.*;
import java.time.temporal.Temporal;

/**
 * JSR310InstantType maps {@linkplain Instant} to
 * {@linkplain Timestamp} on the JDBC level
 */
@IgnoreJRERequirement //conditionally included
public class JSR310InstantType extends AbstractJSR310DateTimeType<Instant> {

    public JSR310InstantType() {
        super(Types.TIMESTAMP);
    }

    public JSR310InstantType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Instant value) {
        return dateTimeFormatter.format(LocalDateTime.ofInstant(value, ZoneId.of("Z")));
    }

    @Override
    public Class<Instant> getReturnedClass() {
        return Instant.class;
    }

    @Nullable
    @Override
    public Instant getValue(Row row, int startIndex) {
        try {
            return super.getValue(row, startIndex);
        } catch (Exception e) {
            Timestamp timestamp = row.get(startIndex, Timestamp.class);
            return timestamp != null ? timestamp.toInstant().atOffset(ZoneOffset.UTC).toInstant() : null;
        }
    }

    @Override
    public void setValue(BindMarker bindMarker, BindTarget bindTarget, Instant value) {
        try {
            super.setValue(bindMarker, bindTarget, value);
        } catch (Exception e) {
            bindMarker.bind(bindTarget, Timestamp.from(value.atOffset(ZoneOffset.UTC).toInstant()));
        }
    }

    @Override
    protected Instant fromDbValue(Temporal value) {
        if (LocalDateTime.class.isAssignableFrom(value.getClass())) {
            return ((LocalDateTime) value).toInstant(ZoneOffset.UTC);
        }
        if (OffsetDateTime.class.isAssignableFrom(value.getClass())) {
            return ((OffsetDateTime) value).toInstant();
        }
        if (ZonedDateTime.class.isAssignableFrom(value.getClass())) {
            return ((ZonedDateTime) value).toInstant();
        }

        return Instant.from(value);
    }

}
