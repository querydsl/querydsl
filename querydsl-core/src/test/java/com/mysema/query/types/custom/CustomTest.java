/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mysema.query.types.Custom;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EBooleanConst;

public class CustomTest {
    
    @Test
    public void constructors(){
        Template template = TemplateFactory.DEFAULT.create("");
        List<Expr<?>> args = Collections.<Expr<?>>emptyList();        
        List<Custom<?>> customs = Arrays.<Custom<?>>asList(
            new CBoolean(template, args),
            new CComparable<String>(String.class, template, args),
            new CDate<Date>(Date.class, template, args),
            new CDateTime<Date>(Date.class, template, args),
            new CNumber<Integer>(Integer.class, template, args),
            new CSimple<Object>(Object.class, template, args),
            new CString(template, args),
            new CTime<Time>(Time.class, template, args)
        );
        for (Custom<?> custom : customs){
            assertNotNull(custom.asExpr());
            assertNotNull(custom.getTemplate());
            assertNotNull(custom.getType());
            assertNotNull(custom.getArgs());
        }
    }

    @Test
    public void factoryMethods(){
        String template = "";
        EBoolean arg = EBooleanConst.TRUE;
        
        CBoolean.create(template, arg);
        CComparable.create(String.class, template, arg);
        CDate.create(Date.class, template, arg);
        CDateTime.create(Date.class, template, arg);
        CNumber.create(Integer.class, template, arg);
        CSimple.create(Object.class, template, arg);
        CString.create(template, arg);
        CTime.create(Time.class, template, arg);
    }
    
    @Test
    public void factoryMethods2(){
        Template template = TemplateFactory.DEFAULT.create("");
        EBoolean arg = EBooleanConst.TRUE;
        
        CBoolean.create(template, arg);
        CComparable.create(String.class, template, arg);
        CDate.create(Date.class, template, arg);
        CDateTime.create(Date.class, template, arg);
        CNumber.create(Integer.class, template, arg);
        CSimple.create(Object.class, template, arg);
        CString.create(template, arg);
        CTime.create(Time.class, template, arg);
    }
}
