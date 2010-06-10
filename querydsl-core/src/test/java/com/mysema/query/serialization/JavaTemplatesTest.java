/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Test;

import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.path.PString;

public class JavaTemplatesTest {

    @Test
    public void testMappings() throws IllegalArgumentException, IllegalAccessException {
        JavaTemplates templates = new JavaTemplates();
        int matched = 0;
        for (Field field : Ops.class.getFields()){            
            if (Operator.class.isAssignableFrom(field.getType())){
                matched++;
                Operator<?> operator = (Operator<?>) field.get(null);
                assertNotNull(field.getName() + " missing", templates.getTemplate(operator));
            }
        }
        assertTrue(matched > 0);
    }

}
