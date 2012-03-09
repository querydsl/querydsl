package com.mysema.query.collections;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class StateHistoryOwner {

    private StateHistory stateHistory;

    protected final StateHistory getStateHistory() {
        return stateHistory;
    }

}
