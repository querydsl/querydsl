package com.querydsl.sql.types;

import javax.annotation.Nullable;
import java.sql.*;
import java.time.ZonedDateTime;

/**
 * JSR310ZonedDateTimeType maps java.time.ZonedDateTime to Date on the JDBC level
 *
 */
public class JSR310ZonedDateTimeType extends AbstractJSR310DateTimeType<ZonedDateTime> {


    public JSR310ZonedDateTimeType() {
        super(Types.TIMESTAMP);
    }

    public JSR310ZonedDateTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(ZonedDateTime value) {
        return dateFormatter.format(value);
    }

    @Override
    public Class<ZonedDateTime> getReturnedClass() {
        return ZonedDateTime.class;
    }

    @Nullable
    @Override
    public ZonedDateTime getValue(ResultSet rs, int startIndex) throws SQLException {
        Date date = rs.getDate(startIndex, utc());
        return date != null ? ZonedDateTime.from(date.toInstant()) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, ZonedDateTime value) throws SQLException {
        java.util.Date from = Date.from(value.toInstant());
        st.setDate(startIndex, new Date(from.getTime()), utc());
    }
}
