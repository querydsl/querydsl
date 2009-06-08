package com.mysema.query.types.expr;

import com.mysema.query.functions.DateTimeFunctions;

/**
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings("unchecked")
public abstract class ETime<D extends Comparable> extends EComparable<D> {
    
    private ENumber<Integer> hour;
    
    private ENumber<Integer> minute;
    
    private ENumber<Integer> second;

    public ETime(Class<? extends D> type) {
        super(type);
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
    
    public ENumber<Integer> getSecond(){
        if (second == null){
            second = DateTimeFunctions.second(this);
        }
        return second;
    }

}
