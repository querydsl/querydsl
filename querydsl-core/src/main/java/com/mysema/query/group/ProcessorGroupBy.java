/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import java.util.List;

import com.mysema.query.Projectable;
import com.mysema.query.ResultTransformer;

/**
 * A wrapper for GroupBy that binds the output type of given GroupProcessor(Factory). 
 * 
 * @author sasa
 *
 * @param <K> Key type of the result Map
 * @param <O> Output type of given transformer
 */
public class ProcessorGroupBy<K, O> implements ResultTransformer<O> {

    private final GroupBy<K> groupBy; 

    private final GroupProcessorFactory<K, O> processorFactory;
    
    public static <K, O> ProcessorGroupBy<K, O> create(GroupBy<K> groupBy, GroupProcessor<K, O> processor) {
        return new ProcessorGroupBy<K, O>(groupBy, processor);
    }
    
    public static <K, O> ProcessorGroupBy<K, O> create(GroupBy<K> groupBy, GroupProcessorFactory<K, O> processorFactory) {
        return new ProcessorGroupBy<K, O>(groupBy, processorFactory);
    }
    
    public ProcessorGroupBy(GroupBy<K> groupBy, final GroupProcessor<K, O> processor) {
        this(groupBy, new GroupProcessorFactory<K, O>() {

            @Override
            public GroupProcessor<K, O> createProcessor(List<GroupColumnDefinition<?, ?>> columnDefinitions) {
                return processor;
            }
            
        });
    }
    
    public ProcessorGroupBy(GroupBy<K> groupBy, GroupProcessorFactory<K, O> processorFactory) {
        this.groupBy = groupBy;
        this.processorFactory = processorFactory;
    }

    @Override
    public O transform(Projectable projectable) {
        return groupBy.process(projectable, processorFactory.createProcessor(groupBy.getColumnDefinitions()));
    }

}
