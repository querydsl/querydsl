package com.querydsl.r2dbc.types;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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

    @Nullable
    @Override
    public OffsetDateTime getValue(Row row, int startIndex) {
        try {
            return super.getValue(row, startIndex);
        } catch (Exception e) {
            Timestamp val = row.get(startIndex, Timestamp.class);
            return val != null ? OffsetDateTime.ofInstant(val.toInstant(), ZoneOffset.UTC) : null;
        }
    }

    @Override
    public void setValue(Statement st, int startIndex, OffsetDateTime value) {
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
