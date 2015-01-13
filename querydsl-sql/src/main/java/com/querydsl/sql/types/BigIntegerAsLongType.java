/*
 * Copyright 2015, Timo Westkämper
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

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * BigIntegerType maps BigInteger to Long on the JDBC level
 *
 * @author tiwe
 *
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
    public BigInteger getValue(ResultSet rs, int startIndex) throws SQLException {
        Number num = (Number) rs.getObject(startIndex);
        return num != null ? BigInteger.valueOf(num.longValue()) : null;
    }

    @Override
    public Class<BigInteger> getReturnedClass() {
        return BigInteger.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, BigInteger value)
            throws SQLException {
        st.setLong(startIndex, value.longValue());
    }

}
