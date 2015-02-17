package com.querydsl.sql.types;

import java.sql.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.annotation.Nullable;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

/**
 * JSR310ZonedDateTimeType maps java.time.ZonedDateTime to Date on the JDBC level
 *
 */
@IgnoreJRERequirement //conditionally included
public class JSR310ZonedDateTimeType extends AbstractJSR310DateTimeType<ZonedDateTime> {


    public JSR310ZonedDateTimeType() {
        super(Types.TIMESTAMP);
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
        Timestamp timestamp = rs.getTimestamp(startIndex, utc());
        return timestamp != null ? ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.UTC) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, ZonedDateTime value) throws SQLException {
        st.setTimestamp(startIndex, new Timestamp(value.toInstant().toEpochMilli()), utc());
    }
}
