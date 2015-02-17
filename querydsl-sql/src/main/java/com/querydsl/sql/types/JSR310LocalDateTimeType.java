package com.querydsl.sql.types;

import javax.annotation.Nullable;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * JSR310LocalDateTimeType maps java.time.LocalDateTime to Date on the JDBC level
 *
 * @author Artur Chyży <artur.chyzy@gmail.com>
 */
public class JSR310LocalDateTimeType extends AbstractJSR310DateTimeType<LocalDateTime> {


    public JSR310LocalDateTimeType() {
        super(Types.TIMESTAMP);
    }

    public JSR310LocalDateTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(LocalDateTime value) {
        return dateFormatter.format(value);
    }

    @Override
    public Class<LocalDateTime> getReturnedClass() {
        return LocalDateTime.class;
    }

    @Nullable
    @Override
    public LocalDateTime getValue(ResultSet rs, int startIndex) throws SQLException {
        Date date = rs.getDate(startIndex, utc());
        return date != null ? LocalDateTime.from(date.toInstant()) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, LocalDateTime value) throws SQLException {
        java.util.Date from = Date.from(value.toInstant(ZoneOffset.UTC));
        st.setDate(startIndex, new Date(from.getTime()), utc());
    }
}
