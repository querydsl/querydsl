/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hsqldb.Types;

/**
 * @author tiwe
 *
 */
public class CharacterType implements Type<Character>{

    @Override
    public Character getValue(ResultSet rs, int startIndex) throws SQLException {
        String str = rs.getString(startIndex);
        return str != null ? str.charAt(0) : null;
    }

    @Override
    public Class<Character> getReturnedClass() {
        return Character.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Character value)
            throws SQLException {
        st.setString(startIndex, value.toString());
        
    }

    @Override
    public int[] getSQLTypes() {
        return new int[]{Types.CHAR};
    }

}
