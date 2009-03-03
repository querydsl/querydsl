/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * SourceSortingSupport provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface SourceSortingSupport {
    
    void sortSources(List<JoinExpression<Object>> joins, EBoolean condition);

}
