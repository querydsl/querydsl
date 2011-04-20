package com.mysema.query.sql.types;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.joda.time.LocalDate;

public class LocalDateType implements Type<LocalDate> {

    @Override
    public Class<LocalDate> getReturnedClass() {
        return LocalDate.class;
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.DATE};
    }

    @Override
    public LocalDate getValue(ResultSet rs, int startIndex) throws SQLException {
        Date date = rs.getDate(startIndex);
        return date != null ? new LocalDate(date.getTime()) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, LocalDate value) throws SQLException {
        st.setDate(startIndex, new Date(value.toDateTimeAtStartOfDay().getMillis()));
    }

}
