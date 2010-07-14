/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * @author tiwe
 *
 */
public class TimestampType implements Type<Timestamp>{
    
    @Override
    public Timestamp getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getTimestamp(startIndex);
    }

    @Override
    public Class<Timestamp> getReturnedClass() {
        return Timestamp.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Timestamp value) throws SQLException {
        st.setTimestamp(startIndex, value);
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.TIMESTAMP};
    }

}
