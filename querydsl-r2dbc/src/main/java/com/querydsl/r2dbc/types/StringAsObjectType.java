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

import io.r2dbc.spi.Clob;
import io.r2dbc.spi.Row;
import reactor.core.publisher.Mono;

import java.sql.Types;

/**
 * {@code StringAsObjectType} maps String to String on the JDBC level
 *
 * @author tiwe
 */
public class StringAsObjectType extends AbstractType<CharSequence> {

    public static final StringAsObjectType DEFAULT = new StringAsObjectType();

    public StringAsObjectType() {
        super(Types.VARCHAR);
    }

    public StringAsObjectType(int type) {
        super(type);
    }

    @Override
    public String getValue(Row row, int startIndex) {
        Object o = row.get(startIndex);
        if (o instanceof String) {
            return (String) o;
//        } else if (o instanceof Clob) {
//            Clob clob = (Clob) o;
//            return clob.stream();
        } else if (o != null) {
            return o.toString();
        } else {
            return null;
        }
    }

    @Override
    public Class<CharSequence> getReturnedClass() {
        return CharSequence.class;
    }

    @Override
    protected Object toDbValue(CharSequence value) {
        return Clob.from(Mono.just(value));
    }

}
