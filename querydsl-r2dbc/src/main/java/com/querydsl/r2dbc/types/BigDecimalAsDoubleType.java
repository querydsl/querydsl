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
package com.querydsl.r2dbc.types;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;

import java.math.BigDecimal;
import java.sql.Types;

/**
 * {@code BigDecimalAsDoubleType} maps BigDecimal to Double on the JDBC level
 *
 * @author tiwe
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
    public BigDecimal getValue(Row row, int startIndex) {
        Double val = row.get(startIndex, Double.class);
        return val == null ? null : BigDecimal.valueOf(val);
    }

    @Override
    public Class<BigDecimal> getReturnedClass() {
        return BigDecimal.class;
    }

    @Override
    public void setValue(Statement st, int startIndex, BigDecimal value) {
        if (value == null) {
            st.bindNull(startIndex, Double.class);
        } else {
            st.bind(startIndex, value.doubleValue());
        }
    }

}
