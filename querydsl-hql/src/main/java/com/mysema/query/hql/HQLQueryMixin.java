package com.mysema.query.hql;

import java.util.Collection;
import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryMixin;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.Path;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class HQLQueryMixin<T> extends QueryMixin<T> {

    public HQLQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }
    
    public HQLQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
    }
    

    @SuppressWarnings("unchecked")
    private <D> Expr<D> createAlias(Path<? extends Collection<D>> target, PEntity<D> alias){
        return OSimple.create((Class<D>)alias.getType(), Ops.ALIAS, target.asExpr(), alias);
    }
    
    @SuppressWarnings("unchecked")
    private <D> Expr<D> createAlias(PEntity<?> target, PEntity<D> alias){
        return OSimple.create((Class<D>)alias.getType(), Ops.ALIAS, target, alias);
    }
    
    @SuppressWarnings("unchecked")
    private <D> Expr<D> createAlias(PMap<?,D,?> target, PEntity<D> alias){
        return OSimple.create((Class<D>)alias.getType(), Ops.ALIAS, target, alias);
    }
    
    public T fetch(){
        List<JoinExpression> joins = metadata.getJoins();
        joins.get(joins.size()-1).setFlag(HQLFlags.FETCH);
        return self;
    }
    
    public T fetchAll(){
        List<JoinExpression> joins = metadata.getJoins();
        joins.get(joins.size()-1).setFlag(HQLFlags.FETCH_ALL);
        return self;
    }
    
    public <P> T fullJoin(Path<? extends Collection<P>> target) {
        metadata.addJoin(JoinType.FULLJOIN, target.asExpr());
        return self;
    }
    

    public <P> T fullJoin(Path<? extends Collection<P>> target, PEntity<P> alias) {
        metadata.addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return self;
    }
    
    public <P> T fullJoin(PEntity<P> target, PEntity<P> alias) {
        metadata.addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return self;
    }
    
    public <P> T fullJoin(PMap<?,P,?> target) {
        metadata.addJoin(JoinType.FULLJOIN, target);
        return self;
    }
    
    public <P> T fullJoin(PMap<?,P,?> target, PEntity<P> alias) {
        metadata.addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return self;
    }
    
    public <P> T innerJoin(Path<? extends Collection<P>> target) {
        metadata.addJoin(JoinType.INNERJOIN, target.asExpr());
        return self;
    }
    
    public <P> T innerJoin(Path<? extends Collection<P>>target, PEntity<P> alias) {
        metadata.addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return self;
    }
    
    public <P> T innerJoin(PEntity<P> target, PEntity<P> alias) {
        metadata.addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return self;
    }
    
    public <P> T innerJoin(PMap<?,P,?> target) {
        metadata.addJoin(JoinType.INNERJOIN, target);
        return self;
    }
    
    public <P> T innerJoin(PMap<?,P,?> target, PEntity<P> alias) {
        metadata.addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return self;
    }
    
    public <P> T join(Path<? extends Collection<P>> target) {
        metadata.addJoin(JoinType.JOIN, target.asExpr());
        return self;
    }

    public <P> T join(Path<? extends Collection<P>> target, PEntity<P> alias) {
        metadata.addJoin(JoinType.JOIN, createAlias(target, alias));
        return self;
    }
    
    public <P> T join(PEntity<P> target, PEntity<P> alias) {
        metadata.addJoin(JoinType.JOIN, createAlias(target, alias));
        return self;
    }
    
    public <P> T join(PMap<?,P,?> target) {
        metadata.addJoin(JoinType.JOIN, target);
        return self;
    }
    
    public <P> T join(PMap<?,P,?> target, PEntity<P> alias) {
        metadata.addJoin(JoinType.JOIN, createAlias(target, alias));
        return self;
    }
    
    public <P> T leftJoin(Path<? extends Collection<P>> target) {
        metadata.addJoin(JoinType.LEFTJOIN, target.asExpr());
        return self;
    }
    
    public <P> T leftJoin(Path<? extends Collection<P>> target, PEntity<P> alias) {
        metadata.addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return self;
    }

    public <P> T leftJoin(PEntity<P> target, PEntity<P> alias) {
        metadata.addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return self;
    }

    public <P> T leftJoin(PMap<?,P,?> target) {
        metadata.addJoin(JoinType.LEFTJOIN, target);
        return self;
    }
    
    public <P> T leftJoin(PMap<?,P,?> target, PEntity<P> alias) {
        metadata.addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return self;
    }
    
    public T with(EBoolean... conditions){
        for (EBoolean condition : conditions){
            metadata.addJoinCondition(condition);    
        }        
        return self;
    }

}
