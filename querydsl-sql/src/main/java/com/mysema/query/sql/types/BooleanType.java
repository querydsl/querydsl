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
public class BooleanType implements Type<Boolean>{

    @Override
    public Boolean getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getBoolean(startIndex);
    }

    @Override
    public Class<Boolean> getReturnedClass() {
        return Boolean.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Boolean value) throws SQLException {
        st.setBoolean(startIndex, value);            
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.BOOLEAN};
    }

}
