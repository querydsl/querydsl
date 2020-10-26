package com.querydsl.sql.types;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import org.jetbrains.annotations.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * JSR310ZonedDateTimeType maps {@linkplain java.time.ZonedDateTime}
 * to {@linkplain java.sql.Timestamp} on the JDBC level
 *
 */
@IgnoreJRERequirement //conditionally included
public class JSR310ZonedDateTimeType extends AbstractJSR310DateTimeType<ZonedDateTime> {

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
        Timestamp ts = rs.getTimestamp(startIndex, utc());
        return ts != null ? ZonedDateTime.ofInstant(ts.toInstant(), ZoneOffset.UTC) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, ZonedDateTime value) throws SQLException {
        st.setTimestamp(startIndex, Timestamp.from(value.toInstant()), utc());
    }
}
