package com.querydsl.collections;

import java.util.Date;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class StateHistory {

    private Date changedAt;

    protected final Date getChangedAtTime() {
        return changedAt;
    }

}