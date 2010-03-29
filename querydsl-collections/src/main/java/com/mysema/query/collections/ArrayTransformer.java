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
 */
public final class ArrayTransformer<S> implements Transformer<S, S[]> {
    
    @SuppressWarnings("unchecked")
    public S[] transform(S input) {
        return (S[])new Object[]{input};
    }
}