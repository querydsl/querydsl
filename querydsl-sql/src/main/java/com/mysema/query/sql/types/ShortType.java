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
public class ShortType implements Type<Short>{

    @Override
    public Short getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getShort(startIndex);
    }

    @Override
    public Class<Short> getReturnedClass() {
        return Short.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Short value) throws SQLException {
        st.setShort(startIndex, value);
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.SMALLINT};
    }

}
