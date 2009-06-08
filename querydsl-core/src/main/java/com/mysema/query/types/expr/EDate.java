package com.mysema.query.types.expr;

import com.mysema.query.functions.DateTimeFunctions;

/**
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings("unchecked")
public abstract class EDate<D extends Comparable> extends EComparable<D> {
    
    private ENumber<Integer> dayOfMonth;
    
    private ENumber<Integer> month;
    
    private ENumber<Integer> year;

    public EDate(Class<? extends D> type) {
        super(type);
    }

    public ENumber<Integer> getDayOfMonth(){
        if (dayOfMonth == null){
            dayOfMonth = DateTimeFunctions.dayOfMonth(this);
        }
        return dayOfMonth;
    }
    
    public ENumber<Integer> getMonth(){
        if (month == null){
            month = DateTimeFunctions.month(this);
        }
        return month;
    }
    
    public ENumber<Integer> getYear(){
        if (year == null){
            year = DateTimeFunctions.year(this);
        }
        return year;
    }
}
