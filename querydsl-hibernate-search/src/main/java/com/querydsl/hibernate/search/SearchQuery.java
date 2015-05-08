/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.hibernate.search;

import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.querydsl.core.types.EntityPath;

/**
 * {@code SearchQuery} is a Query implementation for Hibernate Search
 *
 * @author tiwe
 *
 * @param <T>
 */
public class SearchQuery<T> extends AbstractSearchQuery<T, SearchQuery<T>> {

    /**
     * Create a new SearchQuery instance
     * 
     * @param session session
     * @param path query source
     */
    public SearchQuery(FullTextSession session, EntityPath<T> path) {
        super(session, path);
    }

    /**
     * Create a new SearchQuery instance
     *
     * @param session session
     * @param path query source
     */
    public SearchQuery(Session session, EntityPath<T> path) {
        this(Search.getFullTextSession(session), path);
    }

}
