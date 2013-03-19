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
package com.mysema.query.types;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OpsTest {

    @Test
    public void test() {
        Map<String,Field> fields = new HashMap<String,Field>();
        for (Class<?> cl : Arrays.<Class<?>>asList(Ops.class,
                Ops.DateTimeOps.class,
                Ops.MathOps.class,
                Ops.StringOps.class)) {
            for (Field field : cl.getDeclaredFields()) {
                Field old = fields.put(field.getName(), field);
                if (old != null) {
                    fail("Duplicate field name " + field.getName() +
                            " in " + field.getDeclaringClass().getSimpleName() +
                            " and " +
                            old.getDeclaringClass().getSimpleName());
                }
            }
        }
    }

}
