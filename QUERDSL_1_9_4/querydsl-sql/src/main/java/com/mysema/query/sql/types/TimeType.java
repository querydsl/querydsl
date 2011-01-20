/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;

/**
 * @author tiwe
 *
 */
public class TimeType implements Type<Time>{
    
    @Override
    public Time getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getTime(startIndex);
    }

    @Override
    public Class<Time> getReturnedClass() {
        return Time.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Time value) throws SQLException {
        st.setTime(startIndex, value);
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.TIME};
    }

}
