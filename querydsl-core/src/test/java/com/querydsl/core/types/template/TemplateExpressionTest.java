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
package com.querydsl.core.types.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.JavaTemplates;
import com.querydsl.core.types.Template;
import com.querydsl.core.types.TemplateExpression;
import com.querydsl.core.types.TemplateFactory;
import com.querydsl.core.types.Templates;
import com.querydsl.core.types.ToStringVisitor;
import com.querydsl.core.types.path.StringPath;

public class TemplateExpressionTest {

    @Test
    public void Constructors() {
        Templates templates = new JavaTemplates();
        Template template = TemplateFactory.DEFAULT.create("{0}");
        ImmutableList<Expression<?>> args = ImmutableList.<Expression<?>>of(new StringPath("a"));
        List<TemplateExpression<?>> customs = Arrays.<TemplateExpression<?>>asList(
            new BooleanTemplate(template, args),
            new ComparableTemplate<String>(String.class, template, args),
            new DateTemplate<java.sql.Date>(java.sql.Date.class, template, args),
            new DateTimeTemplate<Date>(Date.class, template, args),
            new EnumTemplate<PropertyType>(PropertyType.class, template, args),
            new NumberTemplate<Integer>(Integer.class, template, args),
            new SimpleTemplate<Object>(Object.class, template, args),
            new StringTemplate(template, args),
            new TimeTemplate<Time>(Time.class, template, args)
        );
        TemplateExpression<?> prev = null;
        for (TemplateExpression<?> custom : customs) {
            assertNotNull(custom);
            assertNotNull(custom.getTemplate());
            assertNotNull(custom.getType());
            assertNotNull(custom.getArgs());
            assertEquals(custom, custom);
            if (prev != null) {
                assertFalse(custom.equals(prev));
            }
            //assertEquals(custom.getType().hashCode(), custom.hashCode());
            custom.accept(ToStringVisitor.DEFAULT, templates);
            prev = custom;
        }
    }

    @Test
    public void FactoryMethods() {
        String template = "";
        Expression<Boolean> arg = ConstantImpl.create(true);

        BooleanTemplate.create(template, arg);
        ComparableTemplate.create(String.class, template, arg);
        DateTemplate.create(Date.class, template, arg);
        DateTimeTemplate.create(Date.class, template, arg);
        EnumTemplate.create(PropertyType.class, template, arg);
        NumberTemplate.create(Integer.class, template, arg);
        SimpleTemplate.create(Object.class, template, arg);
        StringTemplate.create(template, arg);
        TimeTemplate.create(Time.class, template, arg);
    }

    @Test
    public void FactoryMethods2() {
        Template template = TemplateFactory.DEFAULT.create("");
        Expression<Boolean> arg = ConstantImpl.create(true);

        BooleanTemplate.create(template, arg);
        ComparableTemplate.create(String.class, template, arg);
        DateTemplate.create(Date.class, template, arg);
        DateTimeTemplate.create(Date.class, template, arg);
        EnumTemplate.create(PropertyType.class, template, arg);
        NumberTemplate.create(Integer.class, template, arg);
        SimpleTemplate.create(Object.class, template, arg);
        StringTemplate.create(template, arg);
        TimeTemplate.create(Time.class, template, arg);
    }
}
