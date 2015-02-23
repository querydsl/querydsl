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
package com.querydsl.sql;

import java.util.List;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.*;

/**
 * Union defines an interface for Union queries
 *
 * @author tiwe
 *
 * @param <RT> return type of projection
 */
public interface Union<RT> extends SubQueryExpression<RT> {
    
    /**
     * Get the projection as a typed List
     *
     * @return
     */
    List<RT> list();
    
    
    /**
     * Get the projection as a typed Iterator
     *
     * @return
     */
    CloseableIterator<RT> iterate();
    
    /**
     * Defines the grouping/aggregation expressions
     *
     * @param o
     * @return
     */
    Union<RT> groupBy(Expression<?>... o);

    /**
     * Defines the filters for aggregation
     *
     * @param o
     * @return
     */
    Union<RT> having(Predicate... o);
    

    /**
     * Define the ordering of the query results
     *
     * @param o
     * @return
     */
    Union<RT> orderBy(OrderSpecifier<?>... o);

    /**
     *
     * @param alias
     * @return
     */
    Expression<RT> as(String alias);

    /**
     *
     * @param alias
     * @return
     */
    Expression<RT> as(Path<RT> alias);
}
