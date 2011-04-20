package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.joda.time.DateTime;

public class DateTimeType implements Type<DateTime> {

    @Override
    public Class<DateTime> getReturnedClass() {
        return DateTime.class;
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.TIMESTAMP};
    }

    @Override
    public DateTime getValue(ResultSet rs, int startIndex) throws SQLException {
        Timestamp ts = rs.getTimestamp(startIndex);
        return ts != null ? new DateTime(ts.getTime()) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, DateTime value) throws SQLException {
        st.setTimestamp(startIndex, new Timestamp(value.getMillis()));        
    }

}
