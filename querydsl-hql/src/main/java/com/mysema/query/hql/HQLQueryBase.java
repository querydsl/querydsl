/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.query.CascadingBoolean;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.QueryBaseWithProjectionAndDetach;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PathMetadata;

/**
 * HQLQueryBase is a base Query class for HQL
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class HQLQueryBase<SubType extends HQLQueryBase<SubType>> extends QueryBaseWithProjectionAndDetach<SubType> {

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
    private <D> Expr<D> createAlias(EEntity<?> target, PEntity<D> alias){
        return OSimple.create((Class<D>)alias.getType(), Ops.ALIAS, target, alias);
    }

    protected EBoolean createQBECondition(PEntity<?> entity,
            Map<String, Object> map) {
        CascadingBoolean expr = new CascadingBoolean();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            PathMetadata<String> md = PathMetadata.forProperty(entity, entry
                    .getKey());
            PSimple<Object> path = new PSimple<Object>(Object.class, md);
            if (entry.getValue() != null) {
                expr.and(path.eq(entry.getValue()));
            } else {
                expr.and(path.isNull());
            }
        }
        return expr.create();
    }

    public SubType from(PEntity<?>... o) {
        super.from(o);
        return _this;
    }
    
    public SubType fetch(){
        List<JoinExpression> joins = getMetadata().getJoins();
        joins.get(joins.size()-1).setFetch(true);
        return _this;
    }
    
    public <P> SubType fullJoin(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType fullJoin(PEntityCollection<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return _this;
    }
    
    protected Map<Object,String> getConstants() {
        return constants;
    }
    
    public <P> SubType innerJoin(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return _this;
    }

    public <P> SubType innerJoin(PEntityCollection<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType join(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.JOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType join(PEntityCollection<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.JOIN, createAlias(target, alias));
        return _this;
    }

    public <P> SubType leftJoin(PEntity<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return _this;
    }
    
    public <P> SubType leftJoin(PEntityCollection<P> target, PEntity<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return _this;
    }
    
    public SubType on(EBoolean condition){
        getMetadata().addJoinCondition(condition);
        return _this;
    }

    public String toCountRowsString() {
        if (countRowsString == null) {
            countRowsString = buildQueryString(true);
        }
        return countRowsString;
    }

    @Override
    public String toString() {
        if (queryString == null) {
            queryString = buildQueryString(false);
        }
        return queryString;
    }

}
