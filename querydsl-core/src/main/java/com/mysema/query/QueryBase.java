/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * QueryBase provides a basic implementation of the Query interface.
 * 
 * @author tiwe
 * @version $Id$
 */
public class QueryBase<JoinMeta,SubType extends QueryBase<JoinMeta,SubType>> implements Query<SubType> {
    protected final List<Expr<?>> groupBy = new ArrayList<Expr<?>>();
    
    protected final CascadingBoolean having = new CascadingBoolean();
    
    protected final Set<Expr<?>> exprInJoins = new HashSet<Expr<?>>();
    protected final List<JoinExpression<JoinMeta>> joins = new ArrayList<JoinExpression<JoinMeta>>();
    protected final List<OrderSpecifier<?>> orderBy = new ArrayList<OrderSpecifier<?>>();
    protected final List<Expr<?>> select = new ArrayList<Expr<?>>();
    protected final CascadingBoolean where = new CascadingBoolean();
    protected void clear(){
        exprInJoins.clear();
        joins.clear();
        groupBy.clear();
        having.clear();
        orderBy.clear();
        select.clear();
        where.clear();
    }

    @SuppressWarnings("unchecked")
    private SubType _this = (SubType)this;
    
    protected String toString;
    
    private final Metadata metadata = new Metadata();
    
    public SubType from(Expr<?>... o) {        
        for (Expr<?> expr : o){
            if (!exprInJoins.contains(expr)){
                joins.add(new JoinExpression<JoinMeta>(JoinType.DEFAULT,expr));
                exprInJoins.add(expr);
            }            
        }
        return _this;
    }
    
    public SubType groupBy(Expr<?>... o) {
        groupBy.addAll(Arrays.asList(o));
        return _this;
    }
        
    public SubType having(EBoolean... o) {
        for (EBoolean b : o) having.and(normalize(b));
        return _this;
    }
    
    public SubType innerJoin(Expr<?> o) {
        joins.add(new JoinExpression<JoinMeta>(JoinType.INNERJOIN,o));
        return _this;
    }
    
    public SubType fullJoin(Expr<?> o) {
        joins.add(new JoinExpression<JoinMeta>(JoinType.FULLJOIN,o));
        return _this;
    }
 
    public SubType join(Expr<?> o) {
        joins.add(new JoinExpression<JoinMeta>(JoinType.JOIN,o));
        return _this;
    }
 
    public SubType leftJoin(Expr<?> o) {
        joins.add(new JoinExpression<JoinMeta>(JoinType.LEFTJOIN,o));
        return _this;
    }
    
    public SubType on(EBoolean o) {
        if (!joins.isEmpty()){
            joins.get(joins.size()-1).setCondition(normalize(o));
        }
        return _this;
    }
    
    public SubType orderBy(OrderSpecifier<?>... o) {
        orderBy.addAll(Arrays.asList(o));
        return _this;
    }

    protected SubType select(Expr<?>... o) {
        select.addAll(Arrays.asList(o));
        return _this;
    }

    public SubType where(EBoolean... o) {
        for (EBoolean b : o) where.and(normalize(b));
        return _this;
    }
        
    public Metadata getMetadata(){
        return metadata;
    }
    
    protected EBoolean normalize(EBoolean e){
        return e;
    }
    
    /**
     * The Class Metadata.
     */
    public class Metadata{
        public List<Expr<?>> getGroupBy() {
            return groupBy;
        }
        public EBoolean getHaving() {
            return having.create();
        }
        public List<JoinExpression<JoinMeta>> getJoins() {
            return joins;
        }
        public List<OrderSpecifier<?>> getOrderBy() {
            return orderBy;
        }
        public List<Expr<?>> getSelect() {
            return select;
        }
        public EBoolean getWhere() {
            return where.create();
        }
    }

}
