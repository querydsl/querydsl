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
 * BooleanType maps Boolean to Boolean on the JDBC level
 *
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
        final boolean value = rs.getBoolean(startIndex);
        return rs.wasNull() ? null : value;
    }

    @Override
    public Class<Boolean> getReturnedClass() {
        return Boolean.class;
    }

    @Override
    public String getLiteral(Boolean value) {
        return value ? "1" : "0";
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Boolean value) throws SQLException {
        st.setBoolean(startIndex, value);
    }

}
