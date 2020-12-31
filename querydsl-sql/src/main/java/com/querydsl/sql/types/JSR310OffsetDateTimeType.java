package com.querydsl.sql.types;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * JSR310OffsetDateTimeType maps {@linkplain java.time.OffsetDateTime}
 * to {@linkplain java.sql.Timestamp} on the JDBC level
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
        return dateTimeFormatter.format(value);
    }

    @Override
    public Class<OffsetDateTime> getReturnedClass() {
        return OffsetDateTime.class;
    }

    @Nullable
    @Override
    public OffsetDateTime getValue(ResultSet rs, int startIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(startIndex, utc());
        return timestamp != null ? OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.UTC) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, OffsetDateTime value) throws SQLException {
        st.setTimestamp(startIndex, Timestamp.from(value.toInstant()), utc());
    }
}
