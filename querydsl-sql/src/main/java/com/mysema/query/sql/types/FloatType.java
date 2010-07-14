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
public class FloatType implements Type<Float>{

    @Override
    public Float getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getFloat(startIndex);
    }

    @Override
    public Class<Float> getReturnedClass() {
        return Float.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Float value) throws SQLException {
        st.setFloat(startIndex, value);
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.FLOAT};
    }

}
