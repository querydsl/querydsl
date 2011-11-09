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
import com.mysema.query.types.Operation;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.template.SimpleTemplate;

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
    public void Quote(){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_\\-]+");
        assertTrue(pattern.matcher("a1").matches());
        assertTrue(pattern.matcher("a").matches());
    }
    
    @Test
    public void NextVal() {
        Operation<String> nextval = new OperationImpl<String>(String.class, SQLTemplates.NEXTVAL, ConstantImpl.create("myseq"));
        assertEquals("nextval('myseq')", new SQLSerializer(SQLTemplates.DEFAULT).handle(nextval).toString());
        // Derby OK
        // H2 OK
        // HSQLDB OK
        // MSSQL OK
        // MySQL
        // Oracle OK
        // Postgres OK
        
    }
    
    
}
