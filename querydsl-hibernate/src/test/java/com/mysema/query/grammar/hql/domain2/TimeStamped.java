package com.mysema.query.grammar.hql.domain2;

import javax.persistence.MappedSuperclass;

/**
 * Each object has a timeStamp that is equivalent to the time the object has
 * been created. (see thinglink-2.0 datamodel)
 * 
 */
@MappedSuperclass
public class TimeStamped extends BaseEntity {

    private Long _timeStamp;

    /**
     * When the constructor is called, the timeStamp is created.
     */
    public TimeStamped() {
        _timeStamp = new Long(System.currentTimeMillis());
    }

    /**
     * @return timeStamp that is equivalent to the time the object has been
     *         created.
     */
    public Long getTimeStamp() {
        return _timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        _timeStamp = new Long(timeStamp);
    }

}
