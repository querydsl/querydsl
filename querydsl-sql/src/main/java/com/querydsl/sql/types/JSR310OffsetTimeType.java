package com.querydsl.sql.types;

import java.sql.*;
import java.time.OffsetTime;

import org.jetbrains.annotations.Nullable;

public class JSR310OffsetTimeType extends AbstractJSR310DateTimeType<OffsetTime> {

    public JSR310OffsetTimeType() {
        super(Types.TIME);
    }

    public JSR310OffsetTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(OffsetTime value) {
        return timeFormatter.format(value);
    }

    @Override
    public Class<OffsetTime> getReturnedClass() {
        return OffsetTime.class;
    }

    @Nullable
    @Override
    public OffsetTime getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getObject(startIndex, OffsetTime.class);
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, OffsetTime value) throws SQLException {
        st.setObject(startIndex, value);
    }
}
