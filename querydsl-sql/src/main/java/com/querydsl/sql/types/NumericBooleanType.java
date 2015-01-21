/*
 * Copyright 2013, Mysema Ltd
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

import javax.annotation.Nullable;

/**
 * NumericBooleanType maps Boolean to 1/0 (Integer) on the JDBC level
 *
 * @author tiwe
 *
 */
public class NumericBooleanType extends AbstractType<Boolean> {

    public static NumericBooleanType DEFAULT = new NumericBooleanType();

    public NumericBooleanType() {
        super(Types.INTEGER);
    }

    public NumericBooleanType(int type) {
        super(type);
    }

    @Override
    public Class<Boolean> getReturnedClass() {
        return Boolean.class;
    }

    @Override
    @Nullable
    public Boolean getValue(ResultSet rs, int startIndex) throws SQLException {
        Number num = (Number) rs.getObject(startIndex);
        return num != null ? Boolean.valueOf(num.intValue() == 1) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Boolean value) throws SQLException {
        st.setInt(startIndex, value.booleanValue() ? 1 : 0);
    }

}
