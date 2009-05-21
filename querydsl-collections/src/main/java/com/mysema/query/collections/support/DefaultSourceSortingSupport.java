/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.support;

import java.util.Collections;
import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.collections.SourceSortingSupport;
import com.mysema.query.types.expr.EBoolean;

/**
 * DefaultSourceSortingSupport provides
 *
 * @see SourceSortingSupport
 *
 * @author tiwe
 * @version $Id$
 */
public class DefaultSourceSortingSupport implements SourceSortingSupport{
    
    public void sortSources(List<JoinExpression<Object>> joins, EBoolean condition){
        JoinExpressionComparator comp = new JoinExpressionComparator(condition);
        if (joins.size() == 2){
            if (comp.comparePrioritiesOnly(joins.get(0), joins.get(1)) > 0){
                JoinExpression<Object> je = joins.set(0, joins.get(1));
                joins.set(1, je);
            }                         
        }else{
            Collections.sort(joins, comp);
        }  
    }
    
}
