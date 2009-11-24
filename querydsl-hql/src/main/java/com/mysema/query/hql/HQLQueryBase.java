/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.QueryBaseWithProjection;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.path.PathMetadata;

/**
 * HQLQueryBase is a base Query class for HQL
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class HQLQueryBase<SubType extends HQLQueryBase<SubType>> extends QueryBaseWithProjection<SubType> {

    private Map<Object,String> constants;

    @Nullable
    private String countRowsString, queryString;

    private final HQLTemplates templates;
    
    public HQLQueryBase(QueryMetadata md, HQLTemplates templates) {
        super(md);
        this.templates = templates;
    }

    private String buildQueryString(boolean forCountRow) {
        if (getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("No joins given");
        }
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serialize(getMetadata(), forCountRow);
        constants = serializer.getConstantToLabel();
        return serializer.toString();
    }

    @SuppressWarnings("unchecked")
    private <D> Expr<D> createAlias(PEntity<?> target, PEntity<D> alias){
        return OSimple.create((Class<D>)alias.getType(), Ops.ALIAS, target, alias);
    }
    
    @SuppressWarnings("unchecked")
    private <D> Expr<D> createAlias(Path<? extends Collection<D>> target, PEntity<D> alias){
        return OSimple.create((Class<D>)alias.getType(), Ops.ALIAS, target.asExpr(), alias);
    }
    
    @SuppressWarnings("unchecked")
    private <D> Expr<D> createAlias(PMap<?,D,?> target, PEntity<D> alias){
        return OSimple.create((Class<D>)alias.getType(), Ops.ALIAS, target, alias);
    }

    protected EBoolean createQBECondition(PEntity<?> entity,
            Map<String, Object> map) {
        BooleanBuilder expr = new BooleanBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            PathMetadata<String> md = PathMetadata.forProperty(entity, entry.getKey());
            PSimple<Object> path = new PSimple<Object>(Object.class, md);
            if (entry.getValue() != null) {
                expr.and(path.eq(entry.getValue()));
            } else {
                expr.and(path.isNull());
            }
        }
        return expr;
    }

    public SubType from(PEntity<?>... args) {        
        getMetadata().addFrom(args);
        return _this;
    }
    
    public SubType fetch(){
        List<JoinExpression> joins = getMetadata().getJoins();
        joins.get(joins.size()-1).setFetch(true);
        return _this;
    }
    
    public <P> SubType fullJoin(PEntity<P> target) {
        getMetadata().addJoin(JoinType.FULLJOIN, target);
        return _this;
    }
    
    public <P> SubType fullJoin(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType fullJoin(Path<? extends Collection<P>> target) {
        getMetadata().addJoin(JoinType.FULLJOIN, target.asExpr());
        return _this;
    }
    
    public <P> SubType fullJoin(Path<? extends Collection<P>> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType fullJoin(PMap<?,P,?> target) {
        getMetadata().addJoin(JoinType.FULLJOIN, target);
        return _this;
    }
    
    public <P> SubType fullJoin(PMap<?,P,?> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return _this;
    }
    
    protected Map<Object,String> getConstants() {
        return constants;
    }
    
    public <P> SubType innerJoin(PEntity<P> target) {
        getMetadata().addJoin(JoinType.INNERJOIN, target);
        return _this;
    }
    
    public <P> SubType innerJoin(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return _this;
    }

    public <P> SubType innerJoin(Path<? extends Collection<P>> target) {
        getMetadata().addJoin(JoinType.INNERJOIN, target.asExpr());
        return _this;
    }
    
    public <P> SubType innerJoin(Path<? extends Collection<P>>target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType innerJoin(PMap<?,P,?> target) {
        getMetadata().addJoin(JoinType.INNERJOIN, target);
        return _this;
    }
    
    public <P> SubType innerJoin(PMap<?,P,?> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType join(PEntity<P> target) {
        getMetadata().addJoin(JoinType.JOIN, target);
        return _this;
    }
    
    public <P> SubType join(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.JOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType join(Path<? extends Collection<P>> target) {
        getMetadata().addJoin(JoinType.JOIN, target.asExpr());
        return _this;
    }
    
    public <P> SubType join(Path<? extends Collection<P>> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.JOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType join(PMap<?,P,?> target) {
        getMetadata().addJoin(JoinType.JOIN, target);
        return _this;
    }
    
    public <P> SubType join(PMap<?,P,?> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.JOIN, createAlias(target, alias));
        return _this;
    }

    public <P> SubType leftJoin(PEntity<P> target) {
        getMetadata().addJoin(JoinType.LEFTJOIN, target);
        return _this;
    }
    
    public <P> SubType leftJoin(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType leftJoin(Path<? extends Collection<P>> target) {
        getMetadata().addJoin(JoinType.LEFTJOIN, target.asExpr());
        return _this;
    }
    
    public <P> SubType leftJoin(Path<? extends Collection<P>> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType leftJoin(PMap<?,P,?> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType leftJoin(PMap<?,P,?> target) {
        getMetadata().addJoin(JoinType.LEFTJOIN, target);
        return _this;
    }
    
    
    
    public SubType with(EBoolean... conditions){
        for (EBoolean condition : conditions){
            getMetadata().addJoinCondition(condition);    
        }        
        return _this;
    }

    protected String toCountRowsString() {
        if (countRowsString == null) {
            countRowsString = buildQueryString(true);
        }
        return countRowsString;
    }
    
    protected String toQueryString(){
      if (queryString == null) {
          queryString = buildQueryString(false);
      }
      return queryString;
    }

    @Override
    public String toString() {
        return buildQueryString(false).trim();
    }

}
