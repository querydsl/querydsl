/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.Query;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * 
 * SubQuery is a sub query
 * 
 * @author tiwe
 * @version $Id$
 * 
 * @param <A>
 */
public class SubQuery<JM, A> extends Expr<A> implements Query<SubQuery<JM, A>>, CollectionType<A> {
    
    private QueryMetadata<JM> md = new DefaultQueryMetadata<JM>();

    public SubQuery(Expr<A> select) {
        super(null);
        md.addProjection(select);
    }

    public SubQuery() {
        super(null);
    }
    
    @Override
    public SubQuery<JM, A> from(Expr<?>... o) {
        md.addFrom(o);
        return this;
    }

    @Override
    public SubQuery<JM, A> fullJoin(Expr<?> o) {
        md.addJoin(JoinType.FULLJOIN, o);
        return this;
    }

    public QueryMetadata<JM> getMetadata() {
        return md;
    }

    @Override
    public SubQuery<JM, A> groupBy(Expr<?>... o) {
        md.addGroupBy(o);
        return this;
    }

    @Override
    public SubQuery<JM, A> having(EBoolean... o) {
        md.addHaving(o);
        return this;
    }

    @Override
    public SubQuery<JM, A> innerJoin(Expr<?> o) {
        md.addJoin(JoinType.INNERJOIN, o);
        return this;
    }

    @Override
    public SubQuery<JM, A> join(Expr<?> o) {
        md.addJoin(JoinType.JOIN, o);
        return this;
    }

    @Override
    public SubQuery<JM, A> leftJoin(Expr<?> o) {
        md.addJoin(JoinType.LEFTJOIN, o);
        return this;
    }

    @Override
    public SubQuery<JM, A> on(EBoolean o) {
        md.addJoinCondition(o);
        return this;
    }

    @Override
    public SubQuery<JM, A> orderBy(OrderSpecifier<?>... o) {
        md.addOrderBy(o);
        return this;
    }

    public SubQuery<JM, A> select(Expr<?>... o) {
        md.addProjection(o);
        return this;
    }

    @Override
    public SubQuery<JM, A> where(EBoolean... o) {
        md.addWhere(o);
        return this;
    }

    public Class<A> getElementType() {
        return null;
    }
}