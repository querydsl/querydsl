/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Types;

/**
 * @author tiwe
 *
 */
public class UtilDateType extends AbstractType<Date> {
    
    public UtilDateType() {
        super(Types.TIMESTAMP);
    }
    
    public UtilDateType(int type) {
        super(type);
    }
    
    @Override
    public Date getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getTimestamp(startIndex);
    }

    @Override
    public Class<Date> getReturnedClass() {
        return Date.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Date value) throws SQLException {
        st.setTimestamp(startIndex, new java.sql.Timestamp(value.getTime()));
    }

}
