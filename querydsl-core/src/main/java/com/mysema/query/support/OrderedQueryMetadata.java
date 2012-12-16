/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * OrderedQueryMetadata performs no metadata validation and ensures that FROM elements are before 
 * JOIN elements
 * 
 * @author tiwe
 *
 */
public class OrderedQueryMetadata extends DefaultQueryMetadata {
    
    private static final long serialVersionUID = 6326236143414219377L;

    private final List<JoinExpression> joins = new ArrayList<JoinExpression>();
    
    @Nullable
    private JoinExpression last;
    
    public OrderedQueryMetadata(){
        super();
        noValidate();
    }
    
//    @Override
//    public void addJoin(JoinExpression join) {
//        if (joins.contains(join)) {
//            return;
//        }        
//        if (join.getType() == JoinType.DEFAULT) {
//            int index = joins.size();
//            while (index > 0 && joins.get(index-1).getType() != JoinType.DEFAULT) {
//                index--;
//            }            
//            joins.add(index, join);
//        } else {
//            joins.add(join);
//        }
//        last = join;
//    }
//    
//    @Override
//    public void addJoin(JoinType joinType, Expression<?> expr) {
//        addJoin(new JoinExpression(joinType, expr));
//    }
//
//    @Override
//    public void addJoinCondition(Predicate o) {
//        if (last != null) {
//            last.addCondition(o);
//        }
//    }
//    
//    @Override
//    public QueryMetadata clone(){
//        QueryMetadata md = super.clone();
//        for (JoinExpression join : joins) {
//            md.addJoin(join);
//        }
//        return md;
//    }
//    
//    @Override
//    public List<JoinExpression> getJoins() {
//        return joins;
//    }
}
