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
package com.querydsl.core.dml;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;

/**
 * InsertClause defines a generic interface for Insert clauses
 *
 * @author tiwe
 *
 * @param <C> concrete subtype
 */
public interface InsertClause<C extends InsertClause<C>> extends StoreClause<C> {

    /**
     * Define the columns to be populated
     *
     * @param columns
     * @return
     */
    C columns(Path<?>... columns);

    /**
     * Define the populate via subquery
     *
     * @param subQuery
     * @return
     */
    C select(SubQueryExpression<?> subQuery);

    /**
     * Define the value bindings
     *
     * @param v
     * @return
     */
    C values(Object... v);

}
