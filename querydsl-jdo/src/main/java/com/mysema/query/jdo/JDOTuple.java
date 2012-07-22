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
package com.mysema.query.jdo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;

/**
 * JDOTuple is a {@link Tuple} implementation for JDOQuery instances
 * 
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public class JDOTuple implements Tuple{

    private final List<Object> indexed = new ArrayList<Object>();

    private final Map<String,Object> mapped = new LinkedHashMap<String,Object>();

    public void put(Object name, Object value) {
        indexed.add(value);
        mapped.put(name.toString(), value);
    }

    @Override
    public <T> T get(int index, Class<T> type) {
        return (T) indexed.get(index);
    }

    @Override
    public <T> T get(Expression<T> expr) {
        if (expr instanceof Path) {
            return (T) mapped.get(((Path)expr).getMetadata().getExpression().toString());
        } else {
            return (T) mapped.get(expr.toString());
        }
    }

    @Override
    public Object[] toArray() {
        return indexed.toArray();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Tuple) {
            return Arrays.equals(toArray(), ((Tuple) obj).toArray());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

}
