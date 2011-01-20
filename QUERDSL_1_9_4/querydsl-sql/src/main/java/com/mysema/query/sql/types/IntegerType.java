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
public class IntegerType extends AbstractNumberType<Integer>{

    @Override
    public Class<Integer> getReturnedClass() {
        return Integer.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Integer value) throws SQLException {
        st.setInt(startIndex, value);
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.INTEGER};
    }

}
