package com.querydsl.sql.types;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import javax.annotation.Nullable;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

/**
 * JSR310OffsetDateTimeType maps java.time.OffsetDateTime to Date on the JDBC level
 *
 */
@IgnoreJRERequirement //conditionally included
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
        st.setTimestamp(startIndex, new Timestamp(value.toInstant().toEpochMilli()), utc());
    }
}
