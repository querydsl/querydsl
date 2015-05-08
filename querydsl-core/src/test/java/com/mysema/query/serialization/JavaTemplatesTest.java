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
package com.mysema.query.serialization;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Test;

import com.mysema.query.types.JavaTemplates;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;

public class JavaTemplatesTest {

    @Test
    public void Mappings() throws IllegalArgumentException, IllegalAccessException {
        JavaTemplates templates = new JavaTemplates();
        int matched = 0;
        for (Field field : Ops.class.getFields()) {
            if (Operator.class.isAssignableFrom(field.getType())) {
                matched++;
                Operator<?> operator = (Operator<?>) field.get(null);
                assertNotNull(field.getName() + " missing", templates.getTemplate(operator));
            }
        }
        assertTrue(matched > 0);
    }

}
