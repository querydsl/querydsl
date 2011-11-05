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
 * @param <T>
 */
public class EnumByNameType<T extends Enum<T>> extends AbstractType<T> {

    private final Class<T> type;
    
    public EnumByNameType(Class<T> type) {
        this(Types.VARCHAR, type);
    }
    
    public EnumByNameType(int jdbcType, Class<T> type) {
        super(jdbcType);
        this.type = type;
    }
    
    @Override
    public Class<T> getReturnedClass() {
        return type;
    }

    @Override
    public T getValue(ResultSet rs, int startIndex) throws SQLException {
        String name = rs.getString(startIndex);
        return name != null ? Enum.valueOf(type, name) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, T value) throws SQLException {
        st.setString(startIndex, value.name());
    }

}
