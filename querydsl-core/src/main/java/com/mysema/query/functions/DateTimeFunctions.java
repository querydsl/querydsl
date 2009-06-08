/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import java.util.Date;

import com.mysema.query.types.OperationFactory;
import com.mysema.query.types.SimpleOperationFactory;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops;

/**
 * DateTimeFunctions provides date and time functions
 * 
 * @author tiwe
 * @version $Id$
 */
public final class DateTimeFunctions {

    private DateTimeFunctions() {
    }

    private static final OperationFactory factory = SimpleOperationFactory.getInstance();

    public static EComparable<Date> currentDate() {
        return factory.createComparable(Date.class, Ops.DateTimeOps.CURRENT_DATE);
    }

    public static EComparable<Date> currentTime() {
        return factory.createComparable(Date.class, Ops.DateTimeOps.CURRENT_TIME);
    }
    
    public static EComparable<Date> currentTimestamp() {
        return factory.createComparable(Date.class, Ops.DateTimeOps.CURRENT_TIMESTAMP);
    }
    

    public static ENumber<Integer> dayOfMonth(Expr<?> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.DAY_OF_MONTH, d);
    }

    public static ENumber<Integer> dayOfWeek(Expr<?> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.DAY_OF_WEEK,d);
    }

    public static ENumber<Integer> dayOfYear(Expr<?> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.DAY_OF_YEAR,d);
    }

    public static ENumber<Integer> hour(Expr<?> t) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.HOUR, t);
    }

    public static ENumber<Integer> minute(Expr<?> t) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.MINUTE, t);
    }

    public static ENumber<Integer> year(Expr<?> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.YEAR, d);
    }

    public static ENumber<Integer> week(Expr<?> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.WEEK, d);
    }

    public static ENumber<Integer> second(Expr<?> t) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.SECOND, t);
    }

    public static ENumber<Integer> month(Expr<?> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.MONTH, d);
    }

}
