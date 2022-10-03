package com.querydsl.sql.types;

import org.jetbrains.annotations.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public class JSR310ZonedDateTimeType extends AbstractJSR310DateTimeType<ZonedDateTime> {

    // JDBC 4.2 does not define any support for ZonedDateTime, unlike most other JSR-310 types
    // but many drivers support it anyway
    // if the driver does not support it, fall back to OffsetDateTime
    
    public JSR310ZonedDateTimeType() {
        super(Types.TIMESTAMP_WITH_TIMEZONE);
    }

    public JSR310ZonedDateTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(ZonedDateTime value) {
        return dateTimeFormatter.format(value);
    }

    @Override
    public Class<ZonedDateTime> getReturnedClass() {
        return ZonedDateTime.class;
    }

    @Nullable
    @Override
    public ZonedDateTime getValue(ResultSet rs, int startIndex) throws SQLException {
        try {
            return rs.getObject(startIndex, ZonedDateTime.class);
        } catch (SQLException e) {
            OffsetDateTime odt = rs.getObject(startIndex, OffsetDateTime.class);
            return odt != null ? odt.toZonedDateTime() : null;
        }
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, ZonedDateTime value) throws SQLException {
        try {
            st.setObject(startIndex, value);
        } catch (SQLException e) {
            st.setObject(startIndex, value.toOffsetDateTime());
        }
    }
}
