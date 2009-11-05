/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PEntityMap;

/**
 * Query interface for HQL queries
 * 
 * @author tiwe
 * 
 */
public interface HQLQuery extends Query<HQLQuery>, Projectable {

    /**
     * Set the sources of this query
     * 
     * @param sources
     * @return
     */
    HQLQuery from(PEntity<?>... sources);

    /**
     * Create an inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery innerJoin(PEntity<P> target);
    
    /**
     * Create an inner join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery innerJoin(PEntity<P> target, PEntity<P> alias);

    /**
     * Create an inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery innerJoin(PEntityCollection<P> target);
    
    /**
     * Create an inner join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery innerJoin(PEntityCollection<P> target, PEntity<P> alias);

    /**
     * Create an inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery innerJoin(PEntityMap<?, P, ?> target);    
    
    /**
     * Create an inner join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery innerJoin(PEntityMap<?, P, ?> target, PEntity<P> alias);

    /**
     * Create an join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery join(PEntity<P> target);    
    
    /**
     * Create a join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery join(PEntity<P> target, PEntity<P> alias);

    /**
     * Create an join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery join(PEntityCollection<P> target);
    
    /**
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery join(PEntityCollection<P> target, PEntity<P> alias);

    /**
     * Create an join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery join(PEntityMap<?, P, ?> target);
    
    /**
     * Create a join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery join(PEntityMap<?, P, ?> target, PEntity<P> alias);

    /**
     * Create an left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery leftJoin(PEntity<P> target);
    
    /**
     * Create a left join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery leftJoin(PEntity<P> target, PEntity<P> alias);

    /**
     * Create an left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery leftJoin(PEntityCollection<P> target);
    
    /**
     * Create a left join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery leftJoin(PEntityCollection<P> target, PEntity<P> alias);

    /**
     * Create an left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery leftJoin(PEntityMap<?, P, ?> target);
    
    /**
     * Create a left join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery leftJoin(PEntityMap<?, P, ?> target, PEntity<P> alias);

    /**
     * Create an full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery fullJoin(PEntity<P> target);
    
    /**
     * Create a full join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery fullJoin(PEntity<P> target, PEntity<P> alias);

    /**
     * Create an full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery fullJoin(PEntityCollection<P> target);
    
    /**
     * Create a full join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery fullJoin(PEntityCollection<P> target, PEntity<P> alias);

    /**
     * Create an full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery fullJoin(PEntityMap<?, P, ?> target); 
    
    /**
     * Create a full join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery fullJoin(PEntityMap<?, P, ?> target, PEntity<P> alias);

    /**
     * Add conditions to the with clause
     * 
     * @param condition
     * @return
     */
    HQLQuery with(EBoolean... condition);

    /**
     * Set the fetch parameter to the last defined join
     * 
     * @return
     */
    HQLQuery fetch();

}
