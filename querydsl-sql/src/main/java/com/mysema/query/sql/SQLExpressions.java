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

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateOperation;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.DateTimeOperation;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleOperation;
import com.mysema.query.types.expr.Wildcard;

/**
 * Common SQL expressions
 * 
 * @author tiwe
 *
 */
@SuppressWarnings("rawtypes")
public final class SQLExpressions {
    
    private static final Map<DatePart, Operator> DATE_ADD_OPS = new HashMap<DatePart, Operator>();
    
    private static final Map<DatePart, Operator> DATE_DIFF_OPS = new HashMap<DatePart, Operator>();
    
    static {
        DATE_ADD_OPS.put(DatePart.year, Ops.DateTimeOps.ADD_YEARS);
        DATE_ADD_OPS.put(DatePart.month, Ops.DateTimeOps.ADD_MONTHS);
        DATE_ADD_OPS.put(DatePart.week, Ops.DateTimeOps.ADD_WEEKS);
        DATE_ADD_OPS.put(DatePart.day, Ops.DateTimeOps.ADD_DAYS);
        DATE_ADD_OPS.put(DatePart.hour, Ops.DateTimeOps.ADD_HOURS);
        DATE_ADD_OPS.put(DatePart.minute, Ops.DateTimeOps.ADD_MINUTES);
        DATE_ADD_OPS.put(DatePart.second, Ops.DateTimeOps.ADD_SECONDS);
        DATE_ADD_OPS.put(DatePart.millisecond, null); // TODO
        
        DATE_DIFF_OPS.put(DatePart.year, Ops.DateTimeOps.DIFF_YEARS);
        DATE_DIFF_OPS.put(DatePart.month, Ops.DateTimeOps.DIFF_MONTHS);
        DATE_DIFF_OPS.put(DatePart.week, Ops.DateTimeOps.DIFF_WEEKS);
        DATE_DIFF_OPS.put(DatePart.day, Ops.DateTimeOps.DIFF_DAYS);
        DATE_DIFF_OPS.put(DatePart.hour, Ops.DateTimeOps.DIFF_HOURS);
        DATE_DIFF_OPS.put(DatePart.minute, Ops.DateTimeOps.DIFF_MINUTES);
        DATE_DIFF_OPS.put(DatePart.second, Ops.DateTimeOps.DIFF_SECONDS);
        DATE_DIFF_OPS.put(DatePart.millisecond, null); // TODO
    }
    
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
     * Get a nextval(sequence) expression
     * 
     * @param sequence
     * @return
     */
    public static final SimpleExpression<Long> nextval(String sequence) {
        return nextval(Long.class, sequence);
    }    
    
    /**
     * Get a nextval(sequence) expression of the given type
     * 
     * @param type
     * @param sequence
     * @return
     */
    public static final <T extends Number> SimpleExpression<T> nextval(Class<T> type, String sequence) {
        return SimpleOperation.create(type, SQLTemplates.NEXTVAL, ConstantImpl.create(sequence));
    }
    
    /**
     * Get a dateadd(unit, date, amount) expression
     * 
     * @param unit
     * @param date
     * @param amount
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> dateadd(DatePart unit, DateTimeExpression<D> date, int amount) {
        return DateTimeOperation.create((Class)date.getType(), DATE_ADD_OPS.get(unit), date, ConstantImpl.create(amount));
    }
    
    /**
     * Get a dateadd(unit, date, amount) expression
     * 
     * @param unit
     * @param date
     * @param amount
     * @return
     */
    public static <D extends Comparable> DateExpression<D> dateadd(DatePart unit, DateExpression<D> date, int amount) {
        return DateOperation.create((Class)date.getType(), DATE_ADD_OPS.get(unit), date, ConstantImpl.create(amount));
    }
    
    /**
     * Get a datediff(unit, start, end) expression
     * 
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit, 
            DateExpression<D> start, DateExpression<D> end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), start, end);
    }
    
    /**
     * Get a datediff(unit, start, end) expression
     * 
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit, 
            D start, DateExpression<D> end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), new ConstantImpl<D>(start), end);
    }
    
    /**
     * Get a datediff(unit, start, end) expression
     * 
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit, 
            DateExpression<D> start, D end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), start, new ConstantImpl<D>(end));
    }
    
    /**
     * Get a datediff(unit, start, end) expression
     * 
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit, 
            DateTimeExpression<D> start, DateTimeExpression<D> end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), start, end);
    }
    
    /**
     * Get a datediff(unit, start, end) expression
     * 
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit, 
            D start, DateTimeExpression<D> end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), new ConstantImpl<D>(start), end);
    }
    
    /**
     * Get a datediff(unit, start, end) expression
     * 
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit, 
            DateTimeExpression<D> start, D end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), start, new ConstantImpl<D>(end));
    }
    
    /**
     * Add the given amount of years to the date
     * 
     * @param date
     * @param years
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addYears(DateTimeExpression<D> date, int years) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_YEARS, date, ConstantImpl.create(years));
    }
    
    /**
     * Add the given amount of months to the date
     * 
     * @param date
     * @param months
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addMonths(DateTimeExpression<D> date, int months) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_MONTHS, date, ConstantImpl.create(months));
    }
    
    /**
     * Add the given amount of weeks to the date
     * 
     * @param date
     * @param weeks
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addWeeks(DateTimeExpression<D> date, int weeks) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_WEEKS, date, ConstantImpl.create(weeks));
    }
    
    /**
     * Add the given amount of days to the date
     * 
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addDays(DateTimeExpression<D> date, int days) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_DAYS, date, ConstantImpl.create(days));
    }
    
    /**
     * Add the given amount of hours to the date
     * 
     * @param date
     * @param hours
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addHours(DateTimeExpression<D> date, int hours) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_HOURS, date, ConstantImpl.create(hours));
    }
    
    /**
     * Add the given amount of minutes to the date
     * 
     * @param date
     * @param minutes
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addMinutes(DateTimeExpression<D> date, int minutes) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_MINUTES, date, ConstantImpl.create(minutes));
    }
    
    /**
     * Add the given amount of seconds to the date
     * 
     * @param date
     * @param seconds
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addSeconds(DateTimeExpression<D> date, int seconds) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_SECONDS, date, ConstantImpl.create(seconds));
    }
    
    /**
     * Add the given amount of years to the date
     * 
     * @param date
     * @param years
     * @return
     */    
    public static <D extends Comparable> DateExpression<D> addYears(DateExpression<D> date, int years) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_YEARS, date, ConstantImpl.create(years));
    }
    
    /**
     * Add the given amount of months to the date
     * 
     * @param date
     * @param months
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addMonths(DateExpression<D> date, int months) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_MONTHS, date, ConstantImpl.create(months));
    }
    
    /**
     * Add the given amount of weeks to the date
     * 
     * @param date
     * @param weeks
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addWeeks(DateExpression<D> date, int weeks) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_WEEKS, date, ConstantImpl.create(weeks));
    }
    
    /**
     * Add the given amount of days to the date
     * 
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addDays(DateExpression<D> date, int days) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_DAYS, date, ConstantImpl.create(days));
    }
    
    private SQLExpressions() {}

}
