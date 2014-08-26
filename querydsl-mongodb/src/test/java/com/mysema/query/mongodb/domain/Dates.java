package com.mysema.query.mongodb.domain;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;

@Entity
public class Dates extends AbstractEntity {

    private Date date;
    
    private Integer day;
    private Integer month;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
    
    

}
