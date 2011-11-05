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
import java.sql.Types;

/**
 * @author tiwe
 *
 */
public class BigDecimalType extends AbstractType<BigDecimal> {
    
    public BigDecimalType() {
        super(Types.DECIMAL);
    }
    
    public BigDecimalType(int type) {
        super(type);
    }

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

}
