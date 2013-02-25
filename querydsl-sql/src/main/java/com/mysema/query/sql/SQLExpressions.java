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
package com.mysema.query.sql;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateOperation;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.DateTimeOperation;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleOperation;
import com.mysema.query.types.expr.Wildcard;

/**
 * Common SQL expressions
 * 
 * @author tiwe
 *
 */
public final class SQLExpressions {
    
    public static final Expression<Object[]> all = Wildcard.all;
    
    public static final Expression<Long> countAll = Wildcard.count;
    
    /**
     * Get an aggregate any expression for the given boolean expression
     */
    public static BooleanExpression any(BooleanExpression expr) {
        return BooleanOperation.create(Ops.AggOps.BOOLEAN_ANY, expr);
    }
    
    /**
     * Get an aggregate all expression for the given boolean expression
     */
    public static BooleanExpression all(BooleanExpression expr) {
        return BooleanOperation.create(Ops.AggOps.BOOLEAN_ALL, expr);
    }
    
    /**
     * @param sequence
     * @return
     */
    public static final SimpleExpression<Long> nextval(String sequence) {
        return nextval(Long.class, sequence);
    }    
    
    /**
     * @param type
     * @param sequence
     * @return
     */
    public static final <T extends Number> SimpleExpression<T> nextval(Class<T> type, String sequence) {
        return SimpleOperation.create(type, SQLTemplates.NEXTVAL, ConstantImpl.create(sequence));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addYears(DateTimeExpression<D> date, int days) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_YEARS, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addMonths(DateTimeExpression<D> date, int days) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_MONTHS, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addWeeks(DateTimeExpression<D> date, int days) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_WEEKS, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addDays(DateTimeExpression<D> date, int days) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_DAYS, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addHours(DateTimeExpression<D> date, int days) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_HOURS, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addMinutes(DateTimeExpression<D> date, int days) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_MINUTES, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addSeconds(DateTimeExpression<D> date, int days) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_SECONDS, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addYears(DateExpression<D> date, int days) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_YEARS, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addMonths(DateExpression<D> date, int days) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_MONTHS, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addWeeks(DateExpression<D> date, int days) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_WEEKS, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addDays(DateExpression<D> date, int days) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_DAYS, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addHours(DateExpression<D> date, int days) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_HOURS, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addMinutes(DateExpression<D> date, int days) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_MINUTES, date, ConstantImpl.create(days));
    }
    
    /**
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addSeconds(DateExpression<D> date, int days) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_SECONDS, date, ConstantImpl.create(days));
    }
    
    private SQLExpressions() {}

}
