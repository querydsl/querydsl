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
package com.querydsl.jpa;

import com.querydsl.core.Projectable;

/**
 * Query interface for JPQL queries
 *
 * @author tiwe
 *
 */
public interface JPQLQuery extends JPACommonQuery<JPQLQuery>, Projectable {

    /**
     * Add the "fetch" flag to the last defined join
     *
     * Mind that collection joins might result in duplicate rows and that "inner join fetch"
     * will restrict your result set.
     *
     * @return
     */
    JPQLQuery fetch();

    /**
      * Add the "fetch all properties" flag to the last defined join.
      * @return
       */
    JPQLQuery fetchAll();

}
