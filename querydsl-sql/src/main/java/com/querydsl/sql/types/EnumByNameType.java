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
 * EnumByNameType maps Enum types to their String names on the JDBC level
 *
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
