/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Collection;
import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EBoolean;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PMap;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class HQLQueryMixin<T> extends QueryMixin<T> {

    public HQLQueryMixin() {}
    
    public HQLQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }
    
    public HQLQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
    }
    

    @SuppressWarnings("unchecked")
    private <D> Expr<D> createAlias(Path<? extends Collection<D>> target, Path<D> alias){
        return OSimple.create((Class<D>)alias.getType(), Ops.ALIAS, target.asExpr(), alias.asExpr());
    }
    
    @SuppressWarnings("unchecked")
    private <D> Expr<D> createAlias(PEntity<?> target, PEntity<D> alias){
        return OSimple.create((Class<D>)alias.getType(), Ops.ALIAS, target, alias);
    }
    
    @SuppressWarnings("unchecked")
    private <D> Expr<D> createAlias(PMap<?,D,?> target, Path<D> alias){
        return OSimple.create((Class<D>)alias.getType(), Ops.ALIAS, target, alias.asExpr());
    }
    
    public T fetch(){
        List<JoinExpression> joins = getMetadata().getJoins();
        joins.get(joins.size()-1).setFlag(HQLFlags.FETCH);
        return getSelf();
    }
    
    public T fetchAll(){
        List<JoinExpression> joins = getMetadata().getJoins();
        joins.get(joins.size()-1).setFlag(HQLFlags.FETCH_ALL);
        return getSelf();
    }
    
    public <P> T fullJoin(Path<? extends Collection<P>> target) {
        getMetadata().addJoin(JoinType.FULLJOIN, target.asExpr());
        return getSelf();
    }
    

    public <P> T fullJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T fullJoin(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T fullJoin(PMap<?,P,?> target) {
        getMetadata().addJoin(JoinType.FULLJOIN, target);
        return getSelf();
    }
    
    public <P> T fullJoin(PMap<?,P,?> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T innerJoin(Path<? extends Collection<P>> target) {
        getMetadata().addJoin(JoinType.INNERJOIN, target.asExpr());
        return getSelf();
    }
    
    public <P> T innerJoin(Path<? extends Collection<P>>target, Path<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T innerJoin(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T innerJoin(PMap<?,P,?> target) {
        getMetadata().addJoin(JoinType.INNERJOIN, target);
        return getSelf();
    }
    
    public <P> T innerJoin(PMap<?,P,?> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T join(Path<? extends Collection<P>> target) {
        getMetadata().addJoin(JoinType.JOIN, target.asExpr());
        return getSelf();
    }

    public <P> T join(Path<? extends Collection<P>> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.JOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T join(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.JOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T join(PMap<?,P,?> target) {
        getMetadata().addJoin(JoinType.JOIN, target);
        return getSelf();
    }
    
    public <P> T join(PMap<?,P,?> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.JOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T leftJoin(Path<? extends Collection<P>> target) {
        getMetadata().addJoin(JoinType.LEFTJOIN, target.asExpr());
        return getSelf();
    }
    
    public <P> T leftJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public <P> T leftJoin(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public <P> T leftJoin(PMap<?,P,?> target) {
        getMetadata().addJoin(JoinType.LEFTJOIN, target);
        return getSelf();
    }
    
    public <P> T leftJoin(PMap<?,P,?> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public T with(EBoolean... conditions){
        for (EBoolean condition : conditions){
            getMetadata().addJoinCondition(condition);    
        }        
        return getSelf();
    }

}
