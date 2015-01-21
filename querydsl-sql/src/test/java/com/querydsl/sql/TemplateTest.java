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
package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.expr.DateExpression;
import com.querydsl.core.types.expr.StringExpression;
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.StringPath;
import com.querydsl.core.types.template.DateTemplate;
import com.querydsl.core.types.template.StringTemplate;

public class TemplateTest {

    @Test
    public void ToDate() {
        StringExpression str = new StringPath("str");
        assertEquals("to_date(str,'DD-MON-YYYY')", to_date(str, "DD-MON-YYYY").toString());
    }

    @Test
    public void ToChar() {
        DateExpression<Date> date = new DatePath<Date>(Date.class,"date");
        assertEquals("to_char(date,'DD-MON-YYYY')", to_char(date, "DD-MON-YYYY").toString());
    }

    private DateExpression<Date> to_date(Expression<String> expr, String pattern) {
        return DateTemplate.create(Date.class, "to_date({0},'{1s}')", expr, ConstantImpl.create(pattern));
    }

    private StringExpression to_char(Expression<?> expr, String pattern) {
        return StringTemplate.create("to_char({0},'{1s}')", expr, ConstantImpl.create(pattern));
    }
        
}
