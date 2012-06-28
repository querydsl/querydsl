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
package com.mysema.query.collections;

import java.util.Collection;

import com.mysema.query.Projectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.types.MapExpression;
import com.mysema.query.types.Path;

/**
 * Query interface for Collection queries
 *
 * @author tiwe
 */
public interface ColQuery extends SimpleQuery<ColQuery>, Projectable {

    /**
     * Clone this ColQuery instance and return the clone
     *
     * @return
     */
    ColQuery clone();

    /**
     * Bind the given collection to an already existing query source
     *
     * @param <A>
     * @param entity Path for the source
     * @param col content of the source
     * @return
     */
    <A> ColQuery bind(Path<A> entity, Iterable<? extends A> col);
    
    /**
     * Add a query source
     *
     * @param <A>
     * @param entity Path for the source
     * @param col content of the source
     * @return
     */
    <A> ColQuery from(Path<A> entity, Iterable<? extends A> col);

    /**
     * Define an inner join from the Collection typed path to the alias
     *
     * @param <P>
     * @param collectionPath
     * @param alias
     * @return
     */
    <P> ColQuery innerJoin(Path<? extends Collection<P>> collectionPath, Path<P> alias);

    /**
     * Define an inner join from the Map typed path to the alias
     *
     * @param <P>
     * @param mapPath
     * @param alias
     * @return
     */
    <P> ColQuery innerJoin(MapExpression<?,P> mapPath, Path<P> alias);

}
