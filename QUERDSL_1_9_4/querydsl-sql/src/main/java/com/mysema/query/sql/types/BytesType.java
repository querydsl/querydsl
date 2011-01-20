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
public class BytesType implements Type<byte[]>{

    @Override
    public byte[] getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getBytes(startIndex);
    }

    @Override
    public Class<byte[]> getReturnedClass() {
        return byte[].class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, byte[] value) throws SQLException {
        st.setBytes(startIndex, value);
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.BLOB};
    }
    
}
