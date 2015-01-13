package com.querydsl.mongodb.domain;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;

@Entity
public class Dates extends AbstractEntity {

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
