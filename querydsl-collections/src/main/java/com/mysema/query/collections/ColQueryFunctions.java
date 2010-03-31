package com.mysema.query.collections;

import java.util.Calendar;
import java.util.Date;

public final class ColQueryFunctions {
    
    private ColQueryFunctions(){}
    
    public static <T> T coalesce(T... args){
        for (T arg : args){
            if (arg != null){
                return arg;
            }
        }
        return null;
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
    
    public static int getYearMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH) + 1;
    }
    
    public static int getMonth(Date date){
        return getField(date, Calendar.MONTH) + 1;
    }
    
    public static int getWeek(Date date){
        return getField(date, Calendar.WEEK_OF_YEAR);
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
