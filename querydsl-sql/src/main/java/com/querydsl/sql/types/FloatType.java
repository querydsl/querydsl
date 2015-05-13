/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
 * {@code FloatType} maps Float to Float on the JDBC level
 *
 * @author tiwe
 *
 */
public class FloatType extends AbstractType<Float> {

    public FloatType() {
        super(Types.FLOAT);
    }

    public FloatType(int type) {
        super(type);
    }

    @Override
    public Class<Float> getReturnedClass() {
        return Float.class;
    }

    @Override
    public Float getValue(ResultSet rs, int startIndex) throws SQLException {
        float val = rs.getFloat(startIndex);
        return rs.wasNull() ? null : val;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Float value) throws SQLException {
        st.setFloat(startIndex, value);
    }

}
