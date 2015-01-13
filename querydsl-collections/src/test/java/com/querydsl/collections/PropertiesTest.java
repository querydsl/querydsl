package com.querydsl.collections;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PropertiesTest {
    
    @Test
    public void Hidden() {
        QStateHistory history = QStateHistory.stateHistory;
        List<StateHistory> histories = Collections.singletonList(new StateHistory());
        assertEquals(1, CollQueryFactory.from(history, histories)
                .where(history.changedAt.isNull()).list(history).size());
    }
    
    @Test
    public void Hidden2() {
        QStateHistoryOwner historyOwner = QStateHistoryOwner.stateHistoryOwner;
        List<StateHistoryOwner> historyOwners = Collections.singletonList(new StateHistoryOwner());
        assertEquals(1, CollQueryFactory.from(historyOwner, historyOwners)
                .where(historyOwner.stateHistory.isNull()).list(historyOwner).size());
    }

}
