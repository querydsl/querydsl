/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import java.util.Map;

import org.apache.commons.collections15.Transformer;

/**
 * A result processor for GroupBy that gets to analyze (and optionally reject) 
 * the result set rows in the first place and then gets to transform the 
 * Map-based results into something else. 
 * 
 * @author sasa
 *
 * @param <K> Key type of the input map
 * @param <O> Output type of this transformation
 */
public interface GroupProcessor<K, O> extends Transformer<Map<K, Group>, O> {

    /**
     * Analyze the given row. 
     * 
     * @param row
     * @return true if row is accepted, false if rejected
     */
    boolean accept(Object[] row);

}
