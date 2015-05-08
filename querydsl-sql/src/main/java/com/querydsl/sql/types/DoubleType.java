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
 * {@code DoubleType} maps Double to Double on the JDBC level
 *
 * @author tiwe
 *
 */
public class DoubleType extends AbstractType<Double> {

    public DoubleType() {
        super(Types.DOUBLE);
    }

    public DoubleType(int type) {
        super(type);
    }

    @Override
    public Class<Double> getReturnedClass() {
        return Double.class;
    }

    @Override
    public Double getValue(ResultSet rs, int startIndex) throws SQLException {
        double val = rs.getDouble(startIndex);
        return rs.wasNull() ? null : Double.valueOf(val);
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Double value) throws SQLException {
        st.setDouble(startIndex, value);
    }

}
