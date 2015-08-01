package com.querydsl.sql.types;

import java.sql.*;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

import javax.annotation.Nullable;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

/**
 * JSR310OffsetTimeType maps {@linkplain java.time.OffsetTime}
 * to {@linkplain java.sql.Time} on the JDBC level
 *
 */
@IgnoreJRERequirement //conditionally included
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
        Time time = rs.getTime(startIndex, utc());
        return time != null ? OffsetTime.ofInstant(Instant.ofEpochMilli(time.getTime()), ZoneOffset.UTC) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, OffsetTime value) throws SQLException {
        OffsetTime normalized = value.withOffsetSameInstant(ZoneOffset.UTC);
        st.setTime(startIndex, new Time(normalized.get(ChronoField.MILLI_OF_DAY)), utc());
    }
}
