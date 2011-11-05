package com.mysema.query.sql.types;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.joda.time.LocalDate;

public class LocalDateType extends AbstractType<LocalDate> {
    
    public LocalDateType() {
        super(Types.DATE);
    }
    
    public LocalDateType(int type) {
        super(type);
    }

    @Override
    public Class<LocalDate> getReturnedClass() {
        return LocalDate.class;
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
