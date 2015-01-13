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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * BigIntegerType maps BigInteger to BigDecimal on the JDBC level
 *
 * @author tiwe
 *
 */
public class BigIntegerType extends AbstractType<BigInteger> {

    public BigIntegerType() {
        super(Types.NUMERIC);
    }

    public BigIntegerType(int type) {
        super(type);
    }

    @Override
    public BigInteger getValue(ResultSet rs, int startIndex) throws SQLException {
        BigDecimal bd = rs.getBigDecimal(startIndex);
        return bd != null ? bd.toBigInteger() : null;
    }

    @Override
    public Class<BigInteger> getReturnedClass() {
        return BigInteger.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, BigInteger value)
            throws SQLException {
        st.setBigDecimal(startIndex, new BigDecimal(value));
    }

}
