package com.querydsl.sql.types;

import javax.annotation.Nullable;
import java.sql.*;
import java.time.LocalTime;

/**
 * JSR310LocalTimeType maps java.time.LocalTime to Date on the JDBC level
 *
 */
public class JSR310LocalTimeType extends AbstractJSR310DateTimeType<LocalTime> {


    public JSR310LocalTimeType() {
        super(Types.TIME);
    }

    public JSR310LocalTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(LocalTime value) {
        return dateFormatter.format(value);
    }

    @Override
    public Class<LocalTime> getReturnedClass() {
        return LocalTime.class;
    }

    @Nullable
    @Override
    public LocalTime getValue(ResultSet rs, int startIndex) throws SQLException {
        Time time = rs.getTime(startIndex, utc());
        return time != null ? LocalTime.from(time.toInstant()) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, LocalTime value) throws SQLException {
        st.setTime(startIndex, new Time(value.toNanoOfDay() / 1000000), utc());
    }
}
