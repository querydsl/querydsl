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

import java.math.BigInteger;
import java.sql.Types;

/**
 * {@code BigIntegerType} maps BigInteger to Long on the JDBC level
 *
 * @author tiwe
 */
public class BigIntegerAsLongType extends AbstractType<BigInteger> {

    public static final BigIntegerAsLongType DEFAULT = new BigIntegerAsLongType();

    public BigIntegerAsLongType() {
        super(Types.NUMERIC);
    }

    public BigIntegerAsLongType(int type) {
        super(type);
    }

    @Override
    public BigInteger getValue(Row row, int startIndex) {
        Integer val = row.get(startIndex, Integer.class);
        return val == null ? null : BigInteger.valueOf(val);
    }

    @Override
    public Class<BigInteger> getReturnedClass() {
        return BigInteger.class;
    }

    @Override
    public void setValue(Statement st, int startIndex, BigInteger value) {
        if (value == null) {
            st.bindNull(startIndex, Integer.class);
        } else {
            st.bind(startIndex, value.longValue());
        }
    }

}
