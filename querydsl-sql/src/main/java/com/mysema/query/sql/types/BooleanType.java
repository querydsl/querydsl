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
public class BooleanType extends AbstractType<Boolean> {
    
    public BooleanType() {
        super(Types.BOOLEAN);
    }

    public BooleanType(int type) {
        super(type);
    }

    @Override
    public Boolean getValue(ResultSet rs, int startIndex) throws SQLException {
        Object value = rs.getObject(startIndex);
        return value instanceof Boolean ? (Boolean)value : null;
    }

    @Override
    public Class<Boolean> getReturnedClass() {
        return Boolean.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Boolean value) throws SQLException {
        st.setBoolean(startIndex, value);            
    }

}
