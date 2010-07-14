/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hsqldb.Types;

/**
 * @author tiwe
 *
 */
public class LongType implements Type<Long>{

    @Override
    public Long getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getLong(startIndex);
    }

    @Override
    public Class<Long> getReturnedClass() {
        return Long.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Long value) throws SQLException {
        st.setLong(startIndex, value);
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.BIGINT};
    }

}
