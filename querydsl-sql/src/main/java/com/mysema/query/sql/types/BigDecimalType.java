/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.types;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hsqldb.Types;

/**
 * @author tiwe
 *
 */
public class BigDecimalType implements Type<BigDecimal>{

    @Override
    public BigDecimal getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getBigDecimal(startIndex);
    }

    @Override
    public Class<BigDecimal> getReturnedClass() {
        return BigDecimal.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, BigDecimal value)
            throws SQLException {
        st.setBigDecimal(startIndex, value);
        
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.DECIMAL};
    }

}
