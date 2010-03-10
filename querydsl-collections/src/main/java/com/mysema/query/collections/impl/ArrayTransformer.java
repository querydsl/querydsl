/**
 * 
 */
package com.mysema.query.collections.impl;

import org.apache.commons.collections15.Transformer;

public final class ArrayTransformer<S> implements Transformer<S, S[]> {
    
    @SuppressWarnings("unchecked")
    public S[] transform(S input) {
        return (S[])new Object[]{input};
    }
}