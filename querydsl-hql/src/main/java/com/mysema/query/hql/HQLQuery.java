/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityMap;

/**
 * Query interface for HQL queries
 * 
 * @author tiwe
 *
 */
public interface HQLQuery extends Query<HQLQuery>, Projectable {
    
    HQLQuery from(PEntity<?>... sources);
    
    <P> HQLQuery innerJoin(PEntity<P> target, PEntity<P> alias);
    
    <P> HQLQuery innerJoin(PCollection<P> target, PEntity<P> alias);
    
    <P> HQLQuery innerJoin(PEntityMap<?,P,?> target, PEntity<P> alias);
        
    <P> HQLQuery join(PEntity<P> target, PEntity<P> alias);
    
    <P> HQLQuery join(PCollection<P> target, PEntity<P> alias);
    
    <P> HQLQuery join(PEntityMap<?,P,?> target, PEntity<P> alias);
        
    <P> HQLQuery leftJoin(PEntity<P> target, PEntity<P> alias);
    
    <P> HQLQuery leftJoin(PCollection<P> target, PEntity<P> alias);
    
    <P> HQLQuery leftJoin(PEntityMap<?,P,?> target, PEntity<P> alias);
    
    <P> HQLQuery fullJoin(PEntity<P> target, PEntity<P> alias);
    
    <P> HQLQuery fullJoin(PCollection<P> target, PEntity<P> alias);
    
    <P> HQLQuery fullJoin(PEntityMap<?,P,?> target, PEntity<P> alias);
    
    HQLQuery with(EBoolean condition);
    
    HQLQuery fetch();
            
}
