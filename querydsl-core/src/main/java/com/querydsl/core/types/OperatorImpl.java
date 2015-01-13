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
package com.querydsl.core.types;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

/**
 * OperatorImpl is the default implementation of the {@link Operator} interface
 */
@Immutable
public final class OperatorImpl<T> implements Operator<T> {

    static final Map<String, Operator<?>> OPS = new HashMap<String, Operator<?>>(150);

    static {
        try {
            // initialize all fields of Ops
            List<Field> fields = new ArrayList<Field>();
            fields.addAll(Arrays.asList(Ops.class.getFields()));
            for (Class<?> cl : Ops.class.getClasses()) {
                fields.addAll(Arrays.asList(cl.getFields()));
            }
            for (Field field : fields) {
                field.get(null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static final long serialVersionUID = -2435035383548549877L;

    private final String id;

    private final int hashCode;

    public OperatorImpl(String ns, String local) {
        this(ns + "#" + local);
    }

    private OperatorImpl(String id) {
        this.id = id;
        this.hashCode = id.hashCode();
        OPS.put(id, this);
    }

    /**
     * Get the unique id for this Operator
     *
     * @return
     */
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
