package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.joda.time.LocalDateTime;

public class LocalDateTimeType extends AbstractType<LocalDateTime> {
    
    public LocalDateTimeType() {
        super(Types.TIMESTAMP);
    }
    
    public LocalDateTimeType(int type) {
        super(type);
    }
    
    @Override
    public Class<LocalDateTime> getReturnedClass() {
        return LocalDateTime.class;
    }

    @Override
    public LocalDateTime getValue(ResultSet rs, int startIndex) throws SQLException {
        Timestamp ts = rs.getTimestamp(startIndex);
        return ts != null ? new LocalDateTime(ts.getTime()) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, LocalDateTime value) throws SQLException {
        st.setTimestamp(startIndex, new Timestamp(value.toDateTime().getMillis()));
    }

}
