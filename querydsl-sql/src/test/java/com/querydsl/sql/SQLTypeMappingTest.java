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
package com.querydsl.sql;

import java.lang.reflect.Field;

import org.junit.Test;
import static org.junit.Assert.fail;

public class SQLTypeMappingTest {

    @Test
    public void Get() throws IllegalArgumentException, IllegalAccessException {
        JDBCTypeMapping mapping = new JDBCTypeMapping();
        for (Field field : java.sql.Types.class.getFields()) {
            if (field.getType().equals(int.class)) {
                int val = field.getInt(null);
                if (mapping.get(val,0,0) == null) {
                    fail("Got no value for " + field.getName() + " (" + val + ")");
                }
            }
        }
    }

}
