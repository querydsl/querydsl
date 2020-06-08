package com.querydsl.r2dbc.types;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

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
    public void setValue(Statement st, int startIndex, Instant value) {
        try {
            super.setValue(st, startIndex, value);
        } catch (Exception e) {
            if (value == null) {
                st.bindNull(startIndex, getReturnedClass());
            } else {
                st.bind(startIndex, Timestamp.from(value.atOffset(ZoneOffset.UTC).toInstant()));
            }
        }
    }

}
