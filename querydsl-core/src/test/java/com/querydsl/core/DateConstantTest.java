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
package com.querydsl.core;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.Calendar;

import org.junit.Test;

import com.querydsl.core.types.expr.DateExpression;

public class DateConstantTest {

    @Test
    public void test() {
        // 1.1.2000
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR,  2000);
        System.out.println(cal.getTime());

        DateExpression<Date> date = DateConstant.create(new Date(cal.getTimeInMillis()));
        assertEquals("1",   date.dayOfMonth().toString());
        assertEquals("1",   date.month().toString());
        assertEquals("2000",date.year().toString());
        assertEquals("7", date.dayOfWeek().toString());
        assertEquals("1", date.dayOfYear().toString());
    }

}
