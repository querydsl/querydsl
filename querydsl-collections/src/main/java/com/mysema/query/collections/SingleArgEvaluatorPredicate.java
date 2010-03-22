/**
 * 
 */
package com.mysema.query.collections;

import org.apache.commons.collections15.Predicate;

public final class SingleArgEvaluatorPredicate<S> implements Predicate<S> {
    
    private final Evaluator<Boolean> ev;

    SingleArgEvaluatorPredicate(Evaluator<Boolean> ev) {
        this.ev = ev;
    }

    public boolean evaluate(S object) {
        return ev.evaluate(object);
    }
}