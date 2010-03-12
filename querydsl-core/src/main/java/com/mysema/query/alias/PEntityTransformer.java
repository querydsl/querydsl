/**
 * 
 */
package com.mysema.query.alias;

import org.apache.commons.collections15.Transformer;

import com.mysema.commons.lang.Pair;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PathMetadataFactory;

public final class PEntityTransformer implements Transformer<Pair<Class<?>, String>, PEntity<?>> {
    
    @SuppressWarnings("unchecked")
    @Override
    public PEntity<?> transform(Pair<Class<?>, String> input) {
        return new PEntity(input.getFirst(), PathMetadataFactory.forVariable(input.getSecond()));
    }
    
}