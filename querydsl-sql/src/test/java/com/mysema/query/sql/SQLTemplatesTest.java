package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.ENumberConst;

public class SQLTemplatesTest {

    @Test
    public void test(){
        Template template = TemplateFactory.DEFAULT.create("fetch first {0s} rows only");
        assertTrue(template.getElements().get(1).isAsString());
        
        SQLSerializer serializer = new SQLSerializer(new DerbyTemplates());
        serializer.handle(CSimple.create(Object.class, template, ENumberConst.create(5)));
        assertEquals("fetch first 5 rows only", serializer.toString());
    }
}
