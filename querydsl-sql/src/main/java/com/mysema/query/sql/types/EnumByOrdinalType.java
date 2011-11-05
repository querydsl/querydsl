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
public class EnumByOrdinalType<T extends Enum<T>> extends AbstractType<T> {

    private final Class<T> type;
    
    public EnumByOrdinalType(Class<T> type) {
        this(Types.INTEGER, type);
    }
    
    public EnumByOrdinalType(int jdbcType, Class<T> type) {
        super(jdbcType);
        this.type = type;
    }
    
    @Override
    public Class<T> getReturnedClass() {
        return type;
    }

    @Override
    public T getValue(ResultSet rs, int startIndex) throws SQLException {
        Integer ordinal = (Integer)rs.getObject(startIndex);
        return ordinal != null ? type.getEnumConstants()[ordinal.intValue()] : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, T value) throws SQLException {
        st.setInt(startIndex, value.ordinal());
    }

}
