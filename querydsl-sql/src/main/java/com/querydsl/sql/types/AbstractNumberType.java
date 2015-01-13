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

import java.sql.ResultSet;
import java.sql.SQLException;

import com.querydsl.core.util.MathUtils;

/**
 * @author tiwe
 *
 * @param <T>
 */
public abstract class AbstractNumberType<T extends Number & Comparable<T>> extends AbstractType<T> {

    public AbstractNumberType(int type) {
        super(type);
    }

    @Override
    public T getValue(ResultSet rs, int startIndex) throws SQLException {
        Object obj = rs.getObject(startIndex);
        if (obj instanceof Number) {
            return MathUtils.cast((Number) obj, getReturnedClass());
        } else if (obj instanceof Boolean) {
            return MathUtils.cast(Boolean.TRUE.equals(obj) ? 1 : 0, getReturnedClass());
        } else if (obj != null) {
            throw new IllegalArgumentException(obj.toString());
        } else {
            return null;
        }
    }
    
}
