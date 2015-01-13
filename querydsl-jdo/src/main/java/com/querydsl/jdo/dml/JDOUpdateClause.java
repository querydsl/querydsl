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
package com.querydsl.jdo.dml;

import java.util.List;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;

/**
 * UpdateClause implementation for JDO
 *
 * @author tiwe
 *
 */
public class JDOUpdateClause implements UpdateClause<JDOUpdateClause> {

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    @Override
    public long execute() {
        // TODO : implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public JDOUpdateClause set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++) {
            if (values.get(i) != null) {
                metadata.addProjection(ExpressionUtils.eqConst(((Expression)paths.get(i)), values.get(i)));
            } else {
                metadata.addProjection(ExpressionUtils.eq(((Expression)paths.get(i)), 
                        new NullExpression(paths.get(i).getType())));
            }
        }
        return this;
    }

    @Override
    public <T> JDOUpdateClause set(Path<T> path, T value) {
        if (value != null) {
            metadata.addProjection(ExpressionUtils.eqConst(path, value));
        } else {
            metadata.addProjection(ExpressionUtils.eq(path, new NullExpression<T>(path.getType())));
        }
        return this;
    }


    @Override
    public <T> JDOUpdateClause set(Path<T> path, Expression<? extends T> expression) {
        metadata.addProjection(ExpressionUtils.eq(path, expression));
        return this;
    }
    
    @Override
    public <T> JDOUpdateClause setNull(Path<T> path) {
        metadata.addProjection(ExpressionUtils.eq(path, new NullExpression<T>(path.getType())));
        return this;
    }
    
    @Override
    public JDOUpdateClause where(Predicate... o) {
        for (Predicate p : o) {
            metadata.addWhere(p);    
        }        
        return this;
    }

    @Override
    public boolean isEmpty() {
        return metadata.getProjection().isEmpty();
    }


}
