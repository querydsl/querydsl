/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.type.DoubleType;

@SuppressWarnings("serial")
public class ExtDoubleType extends DoubleType{

    @Override
    public void set(PreparedStatement st, Object value, int index) throws SQLException {
        st.setDouble( index, ( (Number) value ).doubleValue() );
    }

}
