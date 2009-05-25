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
 * 
 * @author tiwe
 *
 */
public interface JDOQLQuery extends Projectable { // --> projections go into result
    
	JDOQLQuery from(PEntity<?>... o); // first is candidate, rest are variables
    
	JDOQLQuery orderBy(OrderSpecifier<?>... o); // -> ordering
    
	JDOQLQuery where(EBoolean... o); // limit
    
	JDOQLQuery limit(long limit); // --> range
    
	JDOQLQuery offset(long offset); // --> range
    
	JDOQLQuery restrict(QueryModifiers mod); // --> range

}
