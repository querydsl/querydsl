package com.mysema.query.apt;

import java.sql.Date;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.path.BooleanPath;

public class DateExtensions {
        
    @QueryDelegate(Date.class)
    public static Predicate extension(DateExpression<Date> date){
        return new BooleanPath("b");
    }

}
