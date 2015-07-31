package com.querydsl.sql.types;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.annotation.Nullable;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

/**
 * JSR310LocalDateTimeType maps {@linkplain java.time.LocalDateTime}
 * to {@linkplain java.sql.Timestamp} on the JDBC level
 *
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
    public LocalDateTime getValue(ResultSet rs, int startIndex) throws SQLException {
        Timestamp ts = rs.getTimestamp(startIndex, utc());
        return ts != null ? LocalDateTime.ofInstant(ts.toInstant(), ZoneOffset.UTC) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, LocalDateTime value) throws SQLException {
        Instant i = value.toInstant(ZoneOffset.UTC);
        st.setTimestamp(startIndex, new Timestamp(i.toEpochMilli()), utc());
    }
}
