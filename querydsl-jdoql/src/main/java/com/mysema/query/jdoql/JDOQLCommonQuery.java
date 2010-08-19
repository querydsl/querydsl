package com.mysema.query.jdoql;

import com.mysema.query.Query;
import com.mysema.query.types.EntityPath;

/**
 * JDOQLCommonQuery is a parent interface for JDOQLQuery and JDOQLSubQuery
 * 
 * @author tiwe
 *
 * @param <Q>
 */
public interface JDOQLCommonQuery<Q extends JDOQLCommonQuery<Q>> extends Query<Q> {
    
    /**
     * Add query sources
     *
     * @param sources
     * @return
     */
    Q from(EntityPath<?>... sources);

}
