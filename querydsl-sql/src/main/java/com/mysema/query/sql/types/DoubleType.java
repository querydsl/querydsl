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
public class DoubleType extends AbstractNumberType<Double>{

    @Override
    public Class<Double> getReturnedClass() {
        return Double.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Double value) throws SQLException {
        st.setDouble(startIndex, value);
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.DOUBLE};
    }

}
