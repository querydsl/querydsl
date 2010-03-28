/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import org.apache.commons.collections15.Predicate;

/**
 * @author tiwe
 *
 * @param <S>
 */
public final class EvaluatorPredicate<S> implements Predicate<S> {
    
    private final Evaluator<Boolean> ev;

    EvaluatorPredicate(Evaluator<Boolean> ev) {
        this.ev = ev;
    }

    public boolean evaluate(S object) {
        return ev.evaluate((Object[]) object).booleanValue();
    }
}