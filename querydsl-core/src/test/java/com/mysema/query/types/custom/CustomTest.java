/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.custom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mysema.query.BooleanConstant;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.JavaTemplates;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.Templates;
import com.mysema.query.types.ToStringVisitor;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.StringPath;

public class CustomTest {

    @Test
    public void constructors(){
        Templates templates = new JavaTemplates();
        Template template = TemplateFactory.DEFAULT.create("{0}");
        List<Expression<?>> args = Arrays.<Expression<?>>asList(new StringPath("a"));
        List<TemplateExpression<?>> customs = Arrays.<TemplateExpression<?>>asList(
            new BooleanTemplate(template, args),
            new ComparableTemplate<String>(String.class, template, args),
            new DateTemplate<java.sql.Date>(java.sql.Date.class, template, args),
            new DateTimeTemplate<Date>(Date.class, template, args),
            new NumberTemplate<Integer>(Integer.class, template, args),
            new SimpleTemplate<Object>(Object.class, template, args),
            new StringTemplate(template, args),
            new TimeTemplate<Time>(Time.class, template, args)
        );
        TemplateExpression<?> prev = null;
        for (TemplateExpression<?> custom : customs){
            assertNotNull(custom);
            assertNotNull(custom.getTemplate());
            assertNotNull(custom.getType());
            assertNotNull(custom.getArgs());
            assertEquals(custom, custom);
            if (prev != null){
                assertFalse(custom.equals(prev));
            }
            assertEquals(custom.getType().hashCode(), custom.hashCode());
            custom.accept(ToStringVisitor.DEFAULT, templates);
            prev = custom;
        }
    }

    @Test
    public void factoryMethods(){
        String template = "";
        BooleanExpression arg = BooleanConstant.TRUE;

        BooleanTemplate.create(template, arg);
        ComparableTemplate.create(String.class, template, arg);
        DateTemplate.create(Date.class, template, arg);
        DateTimeTemplate.create(Date.class, template, arg);
        NumberTemplate.create(Integer.class, template, arg);
        SimpleTemplate.create(Object.class, template, arg);
        StringTemplate.create(template, arg);
        TimeTemplate.create(Time.class, template, arg);
    }

    @Test
    public void factoryMethods2(){
        Template template = TemplateFactory.DEFAULT.create("");
        BooleanExpression arg = BooleanConstant.TRUE;

        BooleanTemplate.create(template, arg);
        ComparableTemplate.create(String.class, template, arg);
        DateTemplate.create(Date.class, template, arg);
        DateTimeTemplate.create(Date.class, template, arg);
        NumberTemplate.create(Integer.class, template, arg);
        SimpleTemplate.create(Object.class, template, arg);
        StringTemplate.create(template, arg);
        TimeTemplate.create(Time.class, template, arg);
    }
}
