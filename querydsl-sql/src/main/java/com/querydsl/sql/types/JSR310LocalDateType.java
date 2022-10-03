package com.querydsl.sql.types;

import java.sql.*;
import java.time.LocalDate;

import org.jetbrains.annotations.Nullable;

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
        return rs.getObject(startIndex, LocalDate.class);
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, LocalDate value) throws SQLException {
        st.setObject(startIndex, value);
    }
}
