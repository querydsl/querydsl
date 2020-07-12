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

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Types;

/**
 * {@code BooleanType} maps Boolean to Boolean on the JDBC level
 *
 * @author mc_fish
 */
public class BooleanType extends AbstractType<Boolean, Object> {

    public BooleanType() {
        super(Types.BOOLEAN);
    }

    public BooleanType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Boolean value) {
        return value ? "1" : "0";
    }

    @Override
    public Class<Boolean> getReturnedClass() {
        return Boolean.class;
    }

    @Override
    public Class<Object> getDatabaseClass() {
        return Object.class;
    }

    @Override
    protected Boolean fromDbValue(Object value) {
        if (Byte.class.isAssignableFrom(value.getClass())) {
            return (Byte) value == 1;
        }
        if (Integer.class.isAssignableFrom(value.getClass())) {
            return (Integer) value == 1;
        }
        if (Long.class.isAssignableFrom(value.getClass())) {
            return (Long) value == 1;
        }
        if (BigInteger.class.isAssignableFrom(value.getClass())) {
            return ((BigInteger) value).longValue() == 1;
        }
        //mysql
        if (ByteBuffer.class.isAssignableFrom(value.getClass())) {
            return ((ByteBuffer) value).get() == 1;
        }

        return (Boolean) value;
    }

}
