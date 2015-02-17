package com.querydsl.sql.types;

import javax.annotation.Nullable;
import java.sql.*;
import java.time.LocalDate;

/**
 * JSR310LocalDateType maps java.time.LocalDate to Date on the JDBC level
 *
 */
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
    public LocalDate getValue(ResultSet rs, int startIndex) throws SQLException {
        Date date = rs.getDate(startIndex, utc());
        return date != null ? date.toLocalDate() : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, LocalDate value) throws SQLException {
        st.setDate(startIndex, Date.valueOf(value), utc());
    }
}
