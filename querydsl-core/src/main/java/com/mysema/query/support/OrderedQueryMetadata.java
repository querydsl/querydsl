/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * OrderedQueryMetadata performs no metadata validation and ensures that FROM elements are before JOIN elements
 * 
 * @author tiwe
 *
 */
public class OrderedQueryMetadata extends DefaultQueryMetadata{
    
    private static final long serialVersionUID = 6326236143414219377L;

    private final List<JoinExpression> joins = new ArrayList<JoinExpression>();
    
    @Nullable
    private JoinExpression last;
    
    public OrderedQueryMetadata(){
        super(false);
    }
    
    @Override
    public void addJoin(JoinExpression... j) {
        for (JoinExpression join : j) {
            if (joins.contains(join)) {
                continue;
            }        
            if (join.getType() == JoinType.DEFAULT) {
                int index = joins.size();
                while (index > 0 && joins.get(index-1).getType() != JoinType.DEFAULT) {
                    index--;
                }            
                joins.add(index, join);
            } else {
                joins.add(join);
            }
            last = join;    
        }        
    }
    
    @Override
    public void addJoin(JoinType joinType, Expression<?> expr) {
        addJoin(new JoinExpression(joinType, expr));
    }

    @Override
    public void addJoinCondition(Predicate o) {
        if (last != null) {
            last.addCondition(o);
        }
    }
    
    @Override
    public QueryMetadata clone(){
        QueryMetadata md = super.clone();
        for (JoinExpression join : joins) {
            md.addJoin(join);
        }
        return md;
    }
    
    @Override
    public List<JoinExpression> getJoins() {
        return joins;
    }
}
