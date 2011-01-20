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
