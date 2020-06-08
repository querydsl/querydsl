package com.querydsl.r2dbc.types;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import javax.annotation.Nullable;
import java.sql.Time;
import java.sql.Types;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

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

    @Nullable
    @Override
    public OffsetTime getValue(Row row, int startIndex) {
        try {
            return super.getValue(row, startIndex);
        } catch (Exception e) {
            Time val = row.get(startIndex, Time.class);
            return val != null ? OffsetTime.ofInstant(Instant.ofEpochMilli(val.getTime()), ZoneOffset.UTC) : null;
        }
    }

    @Override
    public void setValue(Statement st, int startIndex, OffsetTime value) {
        try {
            super.setValue(st, startIndex, value);
        } catch (Exception e) {
            if (value == null) {
                st.bindNull(startIndex, getReturnedClass());
            } else {
                OffsetTime normalized = value.withOffsetSameInstant(ZoneOffset.UTC);
                st.bind(startIndex, new Time(normalized.get(ChronoField.MILLI_OF_DAY)));
            }
        }
    }

}
