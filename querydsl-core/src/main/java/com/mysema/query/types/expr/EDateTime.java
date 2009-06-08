package com.mysema.query.types.expr;

import com.mysema.query.functions.DateTimeFunctions;

/**
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings("unchecked")
public abstract class EDateTime<D extends Comparable> extends EComparable<D> {

    private ENumber<Integer> dayOfMonth;
    
    private ENumber<Integer> month;
    
    private ENumber<Integer> year;
    
    private ENumber<Integer> hour;
    
    private ENumber<Integer> minute;
    
    private ENumber<Integer> second;
    
    public EDateTime(Class<? extends D> type) {
        super(type);
    }
    
    public ENumber<Integer> getDayOfMonth(){
        if (dayOfMonth == null){
            dayOfMonth = DateTimeFunctions.dayOfMonth(this);
        }
        return dayOfMonth;
    }
    
    public ENumber<Integer> getHour(){
        if (hour == null){
            hour = DateTimeFunctions.hour(this);
        }
        return hour;
    }
    
    public ENumber<Integer> getMinute(){
        if (minute == null){
            minute = DateTimeFunctions.minute(this);
        }
        return minute;
    }

    public ENumber<Integer> getMonth(){
        if (month == null){
            month = DateTimeFunctions.month(this);
        }
        return month;
    }
    
    public ENumber<Integer> getSecond(){
        if (second == null){
            second = DateTimeFunctions.second(this);
        }
        return second;
    }
    
    public ENumber<Integer> getYear(){
        if (year == null){
            year = DateTimeFunctions.year(this);
        }
        return year;
    }

}
