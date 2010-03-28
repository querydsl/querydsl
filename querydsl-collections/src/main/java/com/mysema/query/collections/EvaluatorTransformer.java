/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import org.apache.commons.collections15.Transformer;

/**
 * @author tiwe
 *
 * @param <S>
 * @param <T>
 */
public final class EvaluatorTransformer<S, T> implements Transformer<S, T> {
    
    private final Evaluator<T> ev;

    EvaluatorTransformer(Evaluator<T> ev) {
        this.ev = ev;
    }

    public T transform(S input) {
        return ev.evaluate((Object[]) input);
    }
}