package com.querydsl.sql.types;

import javax.annotation.Nullable;
import java.sql.*;
import java.time.OffsetDateTime;

/**
 * JSR310OffsetDateTimeType maps java.time.OffsetDateTime to Date on the JDBC level
 *
 */
public class JSR310OffsetDateTimeType extends AbstractJSR310DateTimeType<OffsetDateTime> {


    public JSR310OffsetDateTimeType() {
        super(Types.TIMESTAMP);
    }

    public JSR310OffsetDateTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(OffsetDateTime value) {
        return dateFormatter.format(value);
    }

    @Override
    public Class<OffsetDateTime> getReturnedClass() {
        return OffsetDateTime.class;
    }

    @Nullable
    @Override
    public OffsetDateTime getValue(ResultSet rs, int startIndex) throws SQLException {
        Date date = rs.getDate(startIndex, utc());
        return date != null ? OffsetDateTime.from(date.toInstant()) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, OffsetDateTime value) throws SQLException {
        java.util.Date from = Date.from(value.toInstant());
        st.setDate(startIndex, new Date(from.getTime()), utc());
    }
}
