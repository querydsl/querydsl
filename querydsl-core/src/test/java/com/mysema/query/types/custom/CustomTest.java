/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.sql.Time;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EBooleanConst;
import com.mysema.query.types.expr.Expr;

public class CustomTest {
    
    @Test
    public void constructors(){
        Template template = TemplateFactory.DEFAULT.create("");
        List<Expr<?>> args = Collections.<Expr<?>>emptyList();
        
        new CBoolean(template, args);
        new CComparable<String>(String.class, template, args);
        new CDate<Date>(Date.class, template, args);
        new CDateTime<Date>(Date.class, template, args);
        new CNumber<Integer>(Integer.class, template, args);
        new CSimple<Object>(Object.class, template, args);
        new CString(template, args);
        new CTime<Time>(Time.class, template, args);
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
}
