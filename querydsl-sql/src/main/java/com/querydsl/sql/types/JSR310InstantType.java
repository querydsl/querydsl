package com.querydsl.sql.types;

import javax.annotation.Nullable;
import java.sql.*;
import java.time.Instant;

/**
 * JSR310InstantType maps java.time.Instant to Date on the JDBC level
 *
 */
public class JSR310InstantType extends AbstractJSR310DateTimeType<Instant>  {

    public JSR310InstantType() {
        super(Types.TIMESTAMP);
    }

    public JSR310InstantType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Instant value) {
        return dateFormatter.format(value);
    }

    @Override
    public Class<Instant> getReturnedClass() {
        return Instant.class;
    }

    @Nullable
    @Override
    public Instant getValue(ResultSet rs, int startIndex) throws SQLException {
        Date date = rs.getDate(startIndex, utc());
        return date != null ? Instant.from(date.toInstant()) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Instant value) throws SQLException {
        java.util.Date from = Date.from(value);
        st.setDate(startIndex, new Date(from.getTime()), utc());
    }
}
