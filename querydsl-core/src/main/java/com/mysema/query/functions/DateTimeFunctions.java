/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import java.util.Date;

import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OComparable;
import com.mysema.query.types.operation.ONumber;
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

    public static EComparable<Date> currentDate() {
        return OComparable.create(Date.class, Ops.DateTimeOps.CURRENT_DATE);
    }

    public static EComparable<Date> currentTime() {
        return OComparable.create(Date.class, Ops.DateTimeOps.CURRENT_TIME);
    }
    
    public static EComparable<Date> currentTimestamp() {
        return OComparable.create(Date.class, Ops.DateTimeOps.CURRENT_TIMESTAMP);
    }

    public static ENumber<Integer> dayOfWeek(Expr<?> d) {
        return ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_WEEK,d);
    }

    public static ENumber<Integer> dayOfYear(Expr<?> d) {
        return ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_YEAR,d);
    }

    public static ENumber<Integer> week(Expr<?> d) {
        return ONumber.create(Integer.class, Ops.DateTimeOps.WEEK, d);
    }


}
