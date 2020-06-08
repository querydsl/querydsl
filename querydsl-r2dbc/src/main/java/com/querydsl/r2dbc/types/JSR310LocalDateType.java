package com.querydsl.r2dbc.types;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import javax.annotation.Nullable;
import java.sql.Date;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * JSR310LocalDateType maps {@linkplain LocalDate}
 * to {@linkplain Date} on the JDBC level
 */
@IgnoreJRERequirement //conditionally included
public class JSR310LocalDateType extends AbstractJSR310DateTimeType<LocalDate> {

    public JSR310LocalDateType() {
        super(Types.DATE);
    }

    public JSR310LocalDateType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(LocalDate value) {
        return dateFormatter.format(value);
    }

    @Override
    public Class<LocalDate> getReturnedClass() {
        return LocalDate.class;
    }

    @Nullable
    @Override
    public LocalDate getValue(Row row, int startIndex) {
        try {
            return super.getValue(row, startIndex);
        } catch (Exception e) {
            Date val = row.get(startIndex, Date.class);
            return val != null ? LocalDateTime.ofInstant(Instant.ofEpochMilli(val.getTime()),
                    ZoneOffset.UTC).toLocalDate() : null;
        }
    }

    @Override
    public void setValue(Statement st, int startIndex, LocalDate value) {
        try {
            super.setValue(st, startIndex, value);
        } catch (Exception e) {
            if (value == null) {
                st.bindNull(startIndex, getReturnedClass());
            } else {
                Instant i = value.atStartOfDay(ZoneOffset.UTC).toInstant();
                st.bind(startIndex, new Date(i.toEpochMilli()));
            }
        }
    }

}
