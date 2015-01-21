/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * EnumByOrdinalType maps Enum types to their Integer ordinals on the JDBC level
 *
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
