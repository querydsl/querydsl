/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author tiwe
 *
 */
public class ByteType implements Type<Byte>{

    @Override
    public Byte getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getByte(startIndex);
    }

    @Override
    public Class<Byte> getReturnedClass() {
        return Byte.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Byte value) throws SQLException {
        st.setByte(startIndex, value);
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.TINYINT};
    }

}
