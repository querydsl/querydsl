/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.collections.support.DefaultSourceSortingSupport;
import com.mysema.query.collections.support.JoinExpressionComparator;
import com.mysema.query.types.expr.EBoolean;

/**
 * SourceSortingSupport enables the injection of source sorting functionality
 * into ColQuery queries
 * 
 * @see DefaultSourceSortingSupport
 * @see JoinExpressionComparator
 * 
 * @author tiwe
 * @version $Id$
 */
public interface SourceSortingSupport {

    /**
     * sort the given join sources using some optimization heuristics based on
     * the given match condition
     * 
     * @param joins
     * @param condition
     */
    void sortSources(List<JoinExpression> joins, EBoolean condition);

}
