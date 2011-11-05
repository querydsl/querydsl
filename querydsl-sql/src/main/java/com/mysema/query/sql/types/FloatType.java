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
public class FloatType extends AbstractNumberType<Float>{

    public FloatType() {
        super(Types.FLOAT);
    }
    
    public FloatType(int type) {
        super(type);
    }

    @Override
    public Class<Float> getReturnedClass() {
        return Float.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Float value) throws SQLException {
        st.setFloat(startIndex, value);
    }

}
