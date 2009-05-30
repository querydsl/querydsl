/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import com.mysema.query.Projectable;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;

/**
 * 
 * 
 * @author tiwe
 *
 */
public interface HQLQuery extends Projectable{
    
    HQLQuery from(PEntity<?>... o);

    HQLQuery innerJoin(EEntity<?> target, PEntity<?> alias);
    
    HQLQuery join(EEntity<?> target, PEntity<?> alias);
    
    HQLQuery fullJoin(EEntity<?> target, PEntity<?> alias);
    
    HQLQuery leftJoin(EEntity<?> target, PEntity<?> alias);
    
    HQLQuery on(EBoolean cond);
    
    HQLQuery groupBy(Expr<?>... e);
    
    HQLQuery having(EBoolean... cond);
    
    HQLQuery where(EBoolean... o);
    
    HQLQuery orderBy(OrderSpecifier<?>... o);
        
    HQLQuery limit(long limit);

    HQLQuery offset(long offset);

    HQLQuery restrict(QueryModifiers mod);

}
