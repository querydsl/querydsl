/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author tiwe
 *
 */
public class ByteType extends AbstractNumberType<Byte> {
    
    public ByteType() {
        super(Types.TINYINT);
    }

    public ByteType(int type) {
        super(type);
    }

    @Override
    public Class<Byte> getReturnedClass() {
        return Byte.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Byte value) throws SQLException {
        st.setByte(startIndex, value);
    }

}
