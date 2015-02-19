package com.querydsl.sql.types;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.annotation.Nullable;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

/**
 * JSR310LocalDateTimeType maps {@linkplain java.time.LocalDateTime}
 * to {@linkplain java.sql.Timestamp} on the JDBC level
 */
@IgnoreJRERequirement //conditionally included
public class JSR310LocalDateTimeType extends AbstractJSR310DateTimeType<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public JSR310LocalDateTimeType() {
        super(Types.TIMESTAMP);
    }

    public JSR310LocalDateTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(LocalDateTime value) {
        return formatter.format(value);
    }

    @Override
    public Class<LocalDateTime> getReturnedClass() {
        return LocalDateTime.class;
    }

    @Nullable
    @Override
    public LocalDateTime getValue(ResultSet rs, int startIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(startIndex, utc());
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, LocalDateTime value) throws SQLException {
        st.setTimestamp(startIndex, Timestamp.from(value.toInstant(ZoneOffset.UTC)), utc());
    }
}
