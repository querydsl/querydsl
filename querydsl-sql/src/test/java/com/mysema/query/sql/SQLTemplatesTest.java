/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.custom.SimpleTemplate;

public class SQLTemplatesTest {

    @Test
    public void test(){
        Template template = TemplateFactory.DEFAULT.create("fetch first {0s} rows only");
        assertTrue(template.getElements().get(1).isAsString());

        SQLSerializer serializer = new SQLSerializer(new DerbyTemplates());
        serializer.handle(SimpleTemplate.create(Object.class, template, ConstantImpl.create(5)));
        assertEquals("fetch first 5 rows only", serializer.toString());
    }
    
    @Test
    public void quote(){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_\\-]+");
        assertTrue(pattern.matcher("a1").matches());
        assertTrue(pattern.matcher("a").matches());
    }
    
}
