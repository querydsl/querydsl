/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import com.mysema.query.Projectable;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

/**
 * Query interface for JDOQL queries
 * 
 * @author tiwe
 *
 */
public interface JDOQLQuery extends Projectable { 
    
	/**
	 * Define the sources of the query, the first becomes the candidate, the rest variables
	 */
	JDOQLQuery from(PEntity<?>... o); 
    
	/**
	 * Define the order the projection
	 * 
	 * @param o
	 * @return
	 */
	JDOQLQuery orderBy(OrderSpecifier<?>... o); 
    
	/**
	 * Define the filter of the query
	 * 
	 * @param o
	 * @return
	 */
	JDOQLQuery where(EBoolean... o);
    
	/**
	 * Define the limit of the results
	 * 
	 * @param limit
	 * @return
	 */
	JDOQLQuery limit(long limit);
    
	/**
	 * Define the offset of the results
	 * 
	 * @param offset
	 * @return
	 */
	JDOQLQuery offset(long offset);
    
	/**
	 * Define the limit and offset of the results
	 * 
	 * @param mod
	 * @return
	 */
	JDOQLQuery restrict(QueryModifiers mod); 

}
