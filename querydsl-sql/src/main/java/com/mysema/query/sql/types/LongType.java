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
public class LongType extends AbstractNumberType<Long> {
    
    public LongType() {
        super(Types.BIGINT);
    }
    
    public LongType(int type) {
        super(type);
    }

    @Override
    public Class<Long> getReturnedClass() {
        return Long.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Long value) throws SQLException {
        st.setLong(startIndex, value);
    }

}
