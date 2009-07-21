/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.io.Closeable;

import com.mysema.contracts.Contracts;
import com.mysema.query.Detachable;
import com.mysema.query.Projectable;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;

/**
 * Query interface for JDOQL queries
 * 
 * @author tiwe
 * 
 */
@Contracts
public interface JDOQLQuery extends Projectable, Closeable, Detachable {

    JDOQLQuery from(PEntity<?>... o);
    
    // only sub query
    <P> JDOQLQuery from(PEntityCollection<P> target, PEntity<P> alias);
    
    JDOQLQuery orderBy(OrderSpecifier<?>... o);

    JDOQLQuery where(EBoolean... o);

    JDOQLQuery groupBy(Expr<?>... e);
    
    JDOQLQuery having(EBoolean... cond);
        
    // not in subquery
    JDOQLQuery limit(long limit);

    // not in subquery
    JDOQLQuery offset(long offset);

    // not in subquery
    JDOQLQuery restrict(QueryModifiers mod);

}
