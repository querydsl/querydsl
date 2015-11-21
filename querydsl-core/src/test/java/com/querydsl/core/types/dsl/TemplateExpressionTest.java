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
package com.querydsl.core.types.dsl;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.types.*;

public class TemplateExpressionTest {

    @Test
    public void constructors() {
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
    public void factoryMethods() {
        String template = "";
        Expression<Boolean> arg = ConstantImpl.create(true);

        Expressions.booleanTemplate(template, arg);
        Expressions.comparableTemplate(String.class, template, arg);
        Expressions.dateTemplate(Date.class, template, arg);
        Expressions.dateTimeTemplate(Date.class, template, arg);
        Expressions.enumTemplate(PropertyType.class, template, arg);
        Expressions.numberTemplate(Integer.class, template, arg);
        Expressions.template(Object.class, template, arg);
        Expressions.stringTemplate(template, arg);
        Expressions.timeTemplate(Time.class, template, arg);
    }

    @Test
    public void factoryMethods2() {
        Template template = TemplateFactory.DEFAULT.create("");
        Expression<Boolean> arg = ConstantImpl.create(true);

        Expressions.booleanTemplate(template, arg);
        Expressions.comparableTemplate(String.class, template, arg);
        Expressions.dateTemplate(Date.class, template, arg);
        Expressions.dateTimeTemplate(Date.class, template, arg);
        Expressions.enumTemplate(PropertyType.class, template, arg);
        Expressions.numberTemplate(Integer.class, template, arg);
        Expressions.template(Object.class, template, arg);
        Expressions.stringTemplate(template, arg);
        Expressions.timeTemplate(Time.class, template, arg);
    }
}
