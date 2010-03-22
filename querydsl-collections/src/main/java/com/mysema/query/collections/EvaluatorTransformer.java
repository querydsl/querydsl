/**
 * 
 */
package com.mysema.query.collections;

import org.apache.commons.collections15.Transformer;

public final class EvaluatorTransformer<S, T> implements Transformer<S, T> {
    
    private final Evaluator<T> ev;

    EvaluatorTransformer(Evaluator<T> ev) {
        this.ev = ev;
    }

    public T transform(S input) {
        return ev.evaluate((Object[]) input);
    }
}