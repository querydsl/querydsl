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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * BigDecimalAsDoubleType maps BigDecimal to Double on the JDBC level
 *
 * @author tiwe
 *
 */
public class BigDecimalAsDoubleType extends AbstractType<BigDecimal> {

    public static final BigDecimalAsDoubleType DEFAULT = new BigDecimalAsDoubleType();

    public BigDecimalAsDoubleType() {
        super(Types.DOUBLE);
    }

    public BigDecimalAsDoubleType(int type) {
        super(type);
    }

    @Override
    public BigDecimal getValue(ResultSet rs, int startIndex) throws SQLException {
        Number num = (Number)rs.getObject(startIndex);
        return num != null ? BigDecimal.valueOf(num.doubleValue()) : null;
    }

    @Override
    public Class<BigDecimal> getReturnedClass() {
        return BigDecimal.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, BigDecimal value)
            throws SQLException {
        st.setDouble(startIndex, value.doubleValue());
    }

}
