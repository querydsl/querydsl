/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.types.operation.Ops;

/**
 * SQLServerTemplates is an SQL dialect for Microsoft SQL Server
 * 
 * tested with MS SQL Server 2008 Express
 * 
 * @author tiwe
 *
 */
public class SQLServerTemplates extends SQLTemplates{
    {
        addClass2TypeMappings("decimal", Double.class);
        
        // String        
        add(Ops.CHAR_AT, "cast(substring({0},{1}+1,1) as char)");
        add(Ops.INDEX_OF, "charindex({1},{0})-1");
        add(Ops.INDEX_OF_2ARGS, "charindex({1},{0},{2})-1");
        add(Ops.MATCHES, "{0} like {1}"); // NOTE : needs to be replaced with real regular expression
        add(Ops.STRING_IS_EMPTY, "len({0}) = 0");
        add(Ops.STRING_LENGTH, "len({0})");
        add(Ops.SUBSTR_1ARG, "substring({0},{1}+1,255)");
        add(Ops.SUBSTR_2ARGS, "substring({0},{1}+1,{2})");
        add(Ops.TRIM, "ltrim(rtrim({0}))");
        
        // Date / time
        add(Ops.DateTimeOps.YEAR, "datepart(year, {0})");
        add(Ops.DateTimeOps.MONTH, "datepart(month, {0})");
        add(Ops.DateTimeOps.WEEK, "datepart(week, {0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "datepart(day, {0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "datepart(weekday, {0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "datepart(dayofyear, {0})");
        add(Ops.DateTimeOps.HOUR, "datepart(hour, {0})");
        add(Ops.DateTimeOps.MINUTE, "datepart(minute, {0})");
        add(Ops.DateTimeOps.SECOND, "datepart(second, {0})");
        add(Ops.DateTimeOps.MILLISECOND, "datepart(millisecond, {0})");
    }
}
