package com.querydsl.sql.types;

import java.sql.*;
import java.time.Instant;

import org.jetbrains.annotations.Nullable;

public class JSR310InstantType extends AbstractJSR310DateTimeType<Instant>  {

    // JDBC 4.2 does not define any support for Instant, unlike most other JSR-310 types
    // few drivers support it natively, so go through Timestamp to handle it
    
    public JSR310InstantType() {
        super(Types.TIMESTAMP);
    }

    public JSR310InstantType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Instant value) {
        return dateTimeFormatter.format(Timestamp.from(value).toLocalDateTime());
    }

    @Override
    public Class<Instant> getReturnedClass() {
        return Instant.class;
    }

    @Nullable
    @Override
    public Instant getValue(ResultSet rs, int startIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(startIndex);
        return timestamp != null ? timestamp.toInstant() : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Instant value) throws SQLException {
        st.setTimestamp(startIndex, Timestamp.from(value));
    }
}
