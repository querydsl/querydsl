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
package com.mysema.query.collections;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;

import com.mysema.util.ReflectionUtils;

/**
 * CollQueryFunctions defines function implementation for use in ColQueryTemplates
 *
 * @author tiwe
 *
 */
public final class CollQueryFunctions {
    
    public static <A extends Comparable<? super A>> boolean between(A a, A b, A c) {
        return a.compareTo(b) >= 0 && a.compareTo(c) <= 0;
    }
    
    public static double cot(double x) {
        return Math.cos(x) / Math.sin(x);
    }
    
    public static double coth(double x) {
        return Math.cosh(x) / Math.sinh(x);
    }
    
    public static double degrees(double x) {
        return x * 180.0 / Math.PI;
    }
    
    public static double radians(double x) {
        return x * Math.PI / 180.0;
    }
    
    public static double log(double x, int y) {
        return Math.log(x) / Math.log(y);
    }

    @Nullable
    public static <T> T coalesce(T... args) {
        for (T arg : args) { 
            if (arg != null) {
                return arg;
            }
        }
        return null;
    }
    
    public static <T> T nullif(T first, T second) {
        if (first.equals(second)) {
            return null;
        } else {
            return first;
        }
    }

    public static int getDayOfMonth(Date date) {
        return getField(date, Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfWeek(Date date) {
        return getField(date, Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfYear(Date date) {
        return getField(date, Calendar.DAY_OF_YEAR);
    }

    private static int getField(Date date, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    public static int getHour(Date date) {
        return getField(date, Calendar.HOUR_OF_DAY);
    }

    public static int getMilliSecond(Date date) {
        return getField(date, Calendar.MILLISECOND);
    }

    public static int getMinute(Date date) {
        return getField(date, Calendar.MINUTE);
    }

    public static int getMonth(Date date) {
        return getField(date, Calendar.MONTH) + 1;
    }

    public static int getSecond(Date date) {
        return getField(date, Calendar.SECOND);
    }

    public static int getWeek(Date date) {
        return getField(date, Calendar.WEEK_OF_YEAR);
    }

    public static int getYear(Date date) {
        return getField(date, Calendar.YEAR);
    }

    public static int getYearMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH) + 1;
    }

    public static boolean like(final String str, String like) {
        final StringBuilder pattern = new StringBuilder(like.length() + 4);
        for (int i = 0; i < like.length(); i++) {
            final char ch = like.charAt(i);
            if (ch == '%') {
                pattern.append(".*");
                continue;
            } else if (ch == '_') {
                pattern.append('.');
                continue;
            } else if (ch == '.' || ch == '$' || ch == '^') {
                pattern.append('\\');
            }
            pattern.append(ch);
        }
        if (pattern.toString().equals(like)) {
            return str.equals(like);
        } else {
            return str.matches(pattern.toString());    
        }                  
    }
    
    public static boolean like(String str, String like, char escape) {
        return like(str, like);
    }

    public static <T> T get(Object parent, String f) {
        try {
            Field field = ReflectionUtils.getFieldOrNull(parent.getClass(), f);
            if (field != null) {
                field.setAccessible(true);
                return (T)field.get(parent);    
            } else {
                throw new IllegalArgumentException("No field " + f + " for " + parent.getClass());
            }            
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }        
    }
    
    private CollQueryFunctions() {}

}
