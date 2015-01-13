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
package com.querydsl.jpa.hibernate;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 * DefaultSessionHolder is the default implementation of the {@link SessionHolder} interface
 * 
 * @author tiwe
 *
 */
public class DefaultSessionHolder implements SessionHolder{

    private final Session session;

    public DefaultSessionHolder(Session session) {
        this.session = session;
    }

    @Override
    public Query createQuery(String queryString) {
        return session.createQuery(queryString);
    }

    @Override
    public SQLQuery createSQLQuery(String queryString) {
        return session.createSQLQuery(queryString);
    }

}
