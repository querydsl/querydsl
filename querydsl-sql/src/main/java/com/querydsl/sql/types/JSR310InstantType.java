package com.querydsl.sql.types;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.jetbrains.annotations.Nullable;

public class JSR310InstantType extends AbstractJSR310DateTimeType<Instant>  {

    // JDBC 4.2 does not define any support for Instant, unlike most other JSR-310 types
    // but many drivers support it anyway
    // if the driver does not support it, fall back to Timestamp
    
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
    public Instant getValue(ResultSet rs, int startIndex) throws SQLException {
        try {
            return rs.getObject(startIndex, Instant.class);
        } catch (SQLException e) {
            Timestamp timestamp = rs.getTimestamp(startIndex);
            return timestamp != null ? timestamp.toInstant() : null;
        }
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Instant value) throws SQLException {
        try {
            st.setObject(startIndex, value);
        } catch (SQLException e) {
            st.setTimestamp(startIndex, Timestamp.from(value));
        }
    }
}
