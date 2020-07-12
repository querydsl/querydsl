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

import java.sql.Types;
import java.util.UUID;

/**
 * {@code UtilUUIDType} maps UUID to String on the JDBC level
 *
 * @author mc_fish
 */
public class UtilUUIDType extends AbstractType<UUID, Object> {

    public UtilUUIDType() {
        this(Types.VARCHAR);
    }

    public UtilUUIDType(int type) {
        super(type);
    }

    @Override
    public Class<UUID> getReturnedClass() {
        return UUID.class;
    }

    @Override
    public Class<Object> getDatabaseClass() {
        return Object.class;
    }

    @Override
    protected String toDbValue(UUID value) {
        return value.toString();
    }

    @Override
    protected UUID fromDbValue(Object value) {
        if (String.class.isAssignableFrom(value.getClass())) {
            return UUID.fromString((String) value);
        }

        return (UUID) value;
    }

}