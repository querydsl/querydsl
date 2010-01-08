/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Collection;

import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.Path;

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
    <P> HQLQuery innerJoin(Path<? extends Collection<P>> target);
    
    /**
     * Create an inner join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery innerJoin(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create an inner join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery innerJoin(PMap<?, P, ?> target);    
    
    /**
     * Create an inner join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery innerJoin(PMap<?, P, ?> target, Path<P> alias);

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
    <P> HQLQuery join(Path<? extends Collection<P>> target);
    
    /**
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery join(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create an join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery join(PMap<?, P, ?> target);
    
    /**
     * Create a join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery join(PMap<?, P, ?> target, Path<P> alias);

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
    <P> HQLQuery leftJoin(Path<? extends Collection<P>> target);
    
    /**
     * Create a left join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery leftJoin(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create an left join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery leftJoin(PMap<?, P, ?> target);
    
    /**
     * Create a left join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery leftJoin(PMap<?, P, ?> target, Path<P> alias);

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
    <P> HQLQuery fullJoin(Path<? extends Collection<P>> target);
    
    /**
     * Create a full join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery fullJoin(Path<? extends Collection<P>> target, Path<P> alias);

    /**
     * Create an full join with the given target.
     * Use fetch() to add the fetch parameter to this join.
     * 
     * @param <P>
     * @param target
     * @return
     */
    <P> HQLQuery fullJoin(PMap<?, P, ?> target); 
    
    /**
     * Create a full join with the given target and alias.
     * 
     * @param <P>
     * @param target
     * @param alias
     * @return
     */
    <P> HQLQuery fullJoin(PMap<?, P, ?> target, Path<P> alias);

    /**
     * Add conditions to the with clause
     * 
     * @param condition
     * @return
     */
    HQLQuery with(EBoolean... condition);

    /**
     * Add the "fetch" flag to the last defined join
     * 
     * @return
     */
    HQLQuery fetch();

    /**
      * Add the "fetch all properties" flag to the last defined join.
      * @return
       */
    HQLQuery fetchAll();
    
}
