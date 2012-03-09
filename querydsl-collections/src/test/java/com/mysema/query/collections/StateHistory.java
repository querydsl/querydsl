package com.mysema.query.collections;

import java.util.Date;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class StateHistory {

    private Date changedAt;

    protected final Date getChangedAtTime() {
        return changedAt;
    }

}