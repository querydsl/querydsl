/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

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
 * 
 * 
 * @author tiwe
 *
 */
@Contracts
public interface HQLQuery extends Projectable, Detachable{
    
    HQLQuery from(PEntity<?>... o);

    <P> HQLQuery innerJoin(PEntity<P> target, PEntity<P> alias);
    
    <P> HQLQuery innerJoin(PEntityCollection<P> target, PEntity<P> alias);
    
    <P> HQLQuery join(PEntity<P> target, PEntity<P> alias);
    
    <P> HQLQuery join(PEntityCollection<P> target, PEntity<P> alias);
    
    <P> HQLQuery leftJoin(PEntity<P> target, PEntity<P> alias);
    
    <P> HQLQuery leftJoin(PEntityCollection<P> target, PEntity<P> alias);
    
    <P> HQLQuery fullJoin(PEntity<P> target, PEntity<P> alias);
    
    <P> HQLQuery fullJoin(PEntityCollection<P> target, PEntity<P> alias);
    
    HQLQuery fetch();
    
    HQLQuery groupBy(Expr<?>... e);
    
    HQLQuery having(EBoolean... cond);
    
    HQLQuery limit(long limit);
    
    HQLQuery offset(long offset);
    
    HQLQuery on(EBoolean cond);
        
    HQLQuery orderBy(OrderSpecifier<?>... o);

    HQLQuery restrict(QueryModifiers mod);

    HQLQuery where(EBoolean... o);

}
