package com.querydsl.r2dbc.types;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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

    @Nullable
    @Override
    public ZonedDateTime getValue(Row row, int startIndex) {
        try {
            return super.getValue(row, startIndex);
        } catch (Exception e) {
            Timestamp val = row.get(startIndex, Timestamp.class);
            return val != null ? ZonedDateTime.ofInstant(val.toInstant(), ZoneOffset.UTC) : null;
        }
    }

    @Override
    public void setValue(Statement st, int startIndex, ZonedDateTime value) {
        try {
            super.setValue(st, startIndex, value);
        } catch (Exception e) {
            if (value == null) {
                st.bindNull(startIndex, getReturnedClass());
            } else {
                st.bind(startIndex, new Timestamp(value.toInstant().toEpochMilli()));
            }
        }
    }

}
