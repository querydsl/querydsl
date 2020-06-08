package com.querydsl.r2dbc.types;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import javax.annotation.Nullable;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

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

    @Nullable
    @Override
    public LocalTime getValue(Row row, int startIndex) {
        try {
            return super.getValue(row, startIndex);
        } catch (Exception e) {
            Time val = row.get(startIndex, Time.class);
            return val != null ? LocalTime.ofNanoOfDay(val.getTime() * 1000000) : null;
        }
    }

    @Override
    public void setValue(Statement st, int startIndex, LocalTime value) {
        try {
            super.setValue(st, startIndex, value);
        } catch (Exception e) {
            if (value == null) {
                st.bindNull(startIndex, getReturnedClass());
            } else {
                st.bind(startIndex, new Time(value.get(ChronoField.MILLI_OF_DAY)));
            }
        }
    }

}
