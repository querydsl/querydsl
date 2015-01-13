package com.querydsl.collections;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class StateHistoryOwner {

    private StateHistory stateHistory;

    protected final StateHistory getStateHistory() {
        return stateHistory;
    }

}
