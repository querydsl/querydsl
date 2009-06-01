/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.io.Closeable;

import com.mysema.query.Projectable;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;

/**
 * Query interface for JDOQL queries
 * 
 * @author tiwe
 * 
 */
public interface JDOQLQuery extends Projectable, Closeable {

    JDOQLQuery from(PEntity<?>... o);

    JDOQLQuery orderBy(OrderSpecifier<?>... o);

    JDOQLQuery where(EBoolean... o);

    JDOQLQuery groupBy(Expr<?>... e);
    
    JDOQLQuery having(EBoolean... cond);
        
    JDOQLQuery limit(long limit);

    JDOQLQuery offset(long offset);

    JDOQLQuery restrict(QueryModifiers mod);

}
