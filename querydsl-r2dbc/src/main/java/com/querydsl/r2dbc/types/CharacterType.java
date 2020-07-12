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

import java.sql.Types;

/**
 * {@code CharacterType} maps Character to Character on the JDBC level
 *
 * @author mc_fish
 */
public class CharacterType extends AbstractType<Character, String> {

    public CharacterType() {
        super(Types.CHAR);
    }

    public CharacterType(int type) {
        super(type);
    }

    @Override
    public Character getValue(Row row, int startIndex) {
        Object val = row.get(startIndex);
        if (val instanceof Character) {
            return (Character) val;
        }

        return val == null ? null : ((String) val).charAt(0);
    }

    @Override
    public Class<Character> getReturnedClass() {
        return Character.class;
    }

    @Override
    public Class<String> getDatabaseClass() {
        return String.class;
    }

    @Override
    protected Character fromDbValue(String value) {
        return value.charAt(0);
    }

    @Override
    protected String toDbValue(Character value) {
        return value.toString();
    }

}
