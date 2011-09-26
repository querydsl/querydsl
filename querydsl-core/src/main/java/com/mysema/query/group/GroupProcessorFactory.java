/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import java.util.List;

/**
 * A factory for GroupProcessors. Required for stateful GroupProcessors while keeping 
 * GroupBy definition itself stateless. 
 * 
 * @author sasa
 *
 * @param <K> Key type of the result map
 * @param <O> Output type of the transformer
 */
public interface GroupProcessorFactory<K, O> {

    public GroupProcessor<K, O> createProcessor(List<GroupDefinition<?, ?>> columnDefinitions);
    
}
