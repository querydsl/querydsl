package com.querydsl.sql.types;

import java.sql.*;
import java.time.LocalTime;

import org.jetbrains.annotations.Nullable;

public class JSR310LocalTimeType extends AbstractJSR310DateTimeType<LocalTime> {

    public JSR310LocalTimeType() {
        super(Types.TIME);
    }

    public JSR310LocalTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(LocalTime value) {
        return timeFormatter.format(value);
    }

    @Override
    public Class<LocalTime> getReturnedClass() {
        return LocalTime.class;
    }

    @Nullable
    @Override
    public LocalTime getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getObject(startIndex, LocalTime.class);
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, LocalTime value) throws SQLException {
        st.setObject(startIndex, value);
    }
}
