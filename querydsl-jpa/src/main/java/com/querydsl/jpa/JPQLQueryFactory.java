/*
 * Copyright 2013, Mysema Ltd
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
package com.querydsl.jpa;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.EntityPath;

/**
 * Common interface for JPA related QueryFactory implementations
 * 
 * @author tiwe
 *
 */
public interface JPQLQueryFactory extends QueryFactory<JPQLQuery, JPQLSubQuery> {

    /**
     * Create a new DELETE clause
     * 
     * @param path
     * @return
     */
    DeleteClause<?> delete(EntityPath<?> path);

    /**
     * Create a new Query with the given source
     * 
     * @param from
     * @return
     */
    JPQLQuery from(EntityPath<?> from);
    
    /**
     * Create a new UPDATE clause
     * 
     * @param path
     * @return
     */
    UpdateClause<?> update(EntityPath<?> path);

}