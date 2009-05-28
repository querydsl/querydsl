/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import java.sql.Time;
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
        return factory.createComparable(Date.class, Ops.DateTimeOps.CURRENT_DATE);
    }

    public static ENumber<Integer> dayOfMonth(Expr<Date> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.DAY_OF_MONTH, d);
    }

    public static ENumber<Integer> dayOfWeek(Expr<Date> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.DAY_OF_WEEK,d);
    }

    public static ENumber<Integer> dayOfYear(Expr<Date> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.DAY_OF_YEAR,d);
    }

    public static ENumber<Integer> hour(Expr<Time> t) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.HOUR, t);
    }

    public static ENumber<Integer> minute(Expr<Time> t) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.MINUTE, t);
    }

    public static ENumber<Integer> year(Expr<Date> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.YEAR, d);
    }

    public static ENumber<Integer> week(Expr<Date> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.WEEK, d);
    }

    public static ENumber<Integer> second(Expr<Time> t) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.SECOND, t);
    }

    public static EComparable<Date> now() {
        return factory.createComparable(Date.class, Ops.DateTimeOps.CURRENT_TIME);
    }

    public static ENumber<Integer> month(Expr<Date> d) {
        return factory.createNumber(Integer.class, Ops.DateTimeOps.MONTH, d);
    }

}
