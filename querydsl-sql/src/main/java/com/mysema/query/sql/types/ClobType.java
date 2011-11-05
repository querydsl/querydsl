/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.types;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author tiwe
 *
 */
public class ClobType extends AbstractType<Clob> {
    
    public ClobType() {
        super(Types.CLOB);
    }

    public ClobType(int type) {
        super(type);
    }

    @Override
    public Clob getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getClob(startIndex);
    }

    @Override
    public Class<Clob> getReturnedClass() {
        return Clob.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Clob value) throws SQLException {
        st.setClob(startIndex, value);
    }

}
