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
package com.querydsl.jpa;

import java.util.Date;

import org.junit.Test;

import com.querydsl.core.types.expr.DateExpression;
import com.querydsl.core.types.expr.DateTimeExpression;
import com.querydsl.core.types.expr.TimeExpression;
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.DateTimePath;

public class DateTimeTest extends AbstractQueryTest {

    @Test
    public void CurrentDate() {
        assertToString("current_date", DateExpression.currentDate());
    }
    
    @Test
    public void CurrentDate2() {
        assertToString("current_date", DateTimeExpression.currentDate());
    }
    
    @Test
    public void CurrentTime() {
        assertToString("current_time", TimeExpression.currentTime());   
    }
    
    @Test
    public void CurrentTimestamp() {
        assertToString("current_timestamp", DateTimeExpression.currentTimestamp());
    }
    
    @Test
    public void DayOfMonth() {
        assertToString("day(date)", new DatePath<Date>(Date.class, "date").dayOfMonth());
    }
    
    @Test
    public void DayOfMonth2() {
        assertToString("day(date)", new DateTimePath<Date>(Date.class, "date").dayOfMonth());
    }
    
    @Test
    public void DateOperations2() {
//        catalog.effectiveDate.second();
//        catalog.effectiveDate.minute();
//        catalog.effectiveDate.hour();
        catalog.effectiveDate.dayOfMonth();
        catalog.effectiveDate.month();
        catalog.effectiveDate.year();
    }

}
