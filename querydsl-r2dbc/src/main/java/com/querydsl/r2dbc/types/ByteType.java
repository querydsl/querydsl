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
import java.sql.Types;

/**
 * {@code ByteType} maps Byte to Byte on the JDBC level
 *
 * @author mc_fish
 */
public class ByteType extends AbstractType<Byte, Object> {

    public ByteType() {
        super(Types.TINYINT);
    }

    public ByteType(int type) {
        super(type);
    }

    @Override
    public Class<Byte> getReturnedClass() {
        return Byte.class;
    }

    @Override
    public Class<Object> getDatabaseClass() {
        return Object.class;
    }

    @Override
    protected Byte fromDbValue(Object value) {
        if (Boolean.class.isAssignableFrom(value.getClass())) {
            return (Boolean) value ? (byte) 1 : (byte) 0;
        }
        if (Integer.class.isAssignableFrom(value.getClass())) {
            return (Integer) value == 1 ? (byte) 1 : (byte) 0;
        }
        if (Long.class.isAssignableFrom(value.getClass())) {
            return (Long) value == 1 ? (byte) 1 : (byte) 0;
        }
        if (BigInteger.class.isAssignableFrom(value.getClass())) {
            return ((BigInteger) value).longValue() == 1 ? (byte) 1 : (byte) 0;
        }

        return (Byte) value;
    }

}
