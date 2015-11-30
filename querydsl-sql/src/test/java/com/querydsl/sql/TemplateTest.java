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
package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;

public class TemplateTest {

    @Test
    public void toDate() {
        StringExpression str = Expressions.stringPath("str");
        assertEquals("to_date(str,'DD-MON-YYYY')", to_date(str, "DD-MON-YYYY").toString());
    }

    @Test
    public void toChar() {
        DateExpression<Date> date = Expressions.datePath(Date.class,"date");
        assertEquals("to_char(date,'DD-MON-YYYY')", to_char(date, "DD-MON-YYYY").toString());
    }

    private DateExpression<Date> to_date(Expression<String> expr, String pattern) {
        return Expressions.dateTemplate(Date.class, "to_date({0},'{1s}')", expr, ConstantImpl.create(pattern));
    }

    private StringExpression to_char(Expression<?> expr, String pattern) {
        return Expressions.stringTemplate("to_char({0},'{1s}')", expr, ConstantImpl.create(pattern));
    }

}
