package com.querydsl.r2dbc.types;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * JSR310LocalDateTimeType maps {@linkplain LocalDateTime}
 * to {@linkplain Timestamp} on the JDBC level
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

    @Nullable
    @Override
    public LocalDateTime getValue(Row row, int startIndex) {
        try {
            return super.getValue(row, startIndex);
        } catch (Exception e) {
            Timestamp val = row.get(startIndex, Timestamp.class);
            return val != null ? LocalDateTime.ofInstant(val.toInstant(), ZoneOffset.UTC) : null;
        }
    }

    @Override
    public void setValue(Statement st, int startIndex, LocalDateTime value) {
        try {
            super.setValue(st, startIndex, value);
        } catch (Exception e) {
            if (value == null) {
                st.bindNull(startIndex, getReturnedClass());
            } else {
                Instant i = value.toInstant(ZoneOffset.UTC);

                st.bind(startIndex, new Timestamp(i.toEpochMilli()));
            }
        }
    }

}
