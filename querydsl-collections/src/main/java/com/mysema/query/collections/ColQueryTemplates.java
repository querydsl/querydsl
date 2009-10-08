/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Calendar;
import java.util.Date;

import com.mysema.query.serialization.JavaTemplates;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PathType;

/**
 * JavaOps extends OperationPatterns to add Java syntax specific operation
 * templates.
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColQueryTemplates extends JavaTemplates {

    public ColQueryTemplates() {
        String functions = ColQueryTemplates.class.getName();
        add(Ops.EQ_OBJECT, "{0}.equals({1})");
        add(Ops.NE_OBJECT, "!{0}.equals({1})");
        add(Ops.INSTANCE_OF, "{1}.isInstance({0})");
        
        // Comparable
        add(Ops.AFTER, "{0}.compareTo({1}) > 0");
        add(Ops.BEFORE, "{0}.compareTo({1}) < 0");
        add(Ops.AOE, "{0}.compareTo({1}) >= 0");
        add(Ops.BOE, "{0}.compareTo({1}) <= 0");
        add(Ops.BETWEEN, functions + ".between({0},{1},{2})");
        add(Ops.STRING_CAST, "String.valueOf({0})");
        
        // Date and Time
        add(Ops.DateTimeOps.YEAR,         functions + ".getYear({0})");
        add(Ops.DateTimeOps.MONTH,        functions + ".getMonth({0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK,  functions + ".getDayOfWeek({0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, functions + ".getDayOfMonth({0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR,  functions + ".getDayOfYear({0})");
        add(Ops.DateTimeOps.HOUR,         functions + ".getHour({0})");
        add(Ops.DateTimeOps.MINUTE,       functions + ".getMinute({0})");
        add(Ops.DateTimeOps.SECOND,       functions + ".getSecond({0})");
        add(Ops.DateTimeOps.MILLISECOND,  functions + ".getMilliSecond({0})");
        
        // String
        add(Ops.LIKE, functions + ".like({0},{1})");
        
        // Path types
        for (PathType type : new PathType[] { 
                PathType.LISTVALUE,
                PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "{0}.get({1})");
        }
        add(PathType.LISTVALUE_CONSTANT, "{0}.get({1s})");
        add(PathType.ARRAYVALUE, "{0}[{1}]");
        add(PathType.ARRAYVALUE_CONSTANT, "{0}[{1s}]");
        
        // TEMPORARY FIXES
        
        add(Ops.DIV, "((double){0}) / ((double){1})");
    }
    
    public static boolean like(String str, String like){
        // TODO : better escaping
        return str.matches(like.replace("%", ".*").replace('_', '.'));
    }

    public static <A extends Comparable<? super A>> boolean between(A a, A b, A c) {
        return a.compareTo(b) > 0 && a.compareTo(c) < 0;
    }
    
    public static int getYear(Date date){
        return getField(date, Calendar.YEAR);
    }
    
    public static int getMonth(Date date){
        return getField(date, Calendar.MONTH) + 1;
    }
    
    public static int getDayOfMonth(Date date){
        return getField(date, Calendar.DAY_OF_MONTH);
    }
    
    public static int getDayOfWeek(Date date){
        return getField(date, Calendar.DAY_OF_WEEK);
    }
    
    public static int getDayOfYear(Date date){
        return getField(date, Calendar.DAY_OF_YEAR);
    }
    
    public static int getHour(Date date){
        return getField(date, Calendar.HOUR_OF_DAY);
    }
    
    public static int getMinute(Date date){
        return getField(date, Calendar.MINUTE);
    }
    
    public static int getSecond(Date date){
        return getField(date, Calendar.SECOND);
    }
    
    public static int getMilliSecond(Date date){
        return getField(date, Calendar.MILLISECOND);
    }
    
    private static int getField(Date date, int field){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

}
