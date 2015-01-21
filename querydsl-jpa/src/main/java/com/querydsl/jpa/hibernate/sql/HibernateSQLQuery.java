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
package com.querydsl.jpa.hibernate.sql;

import com.querydsl.core.QueryMetadata;
import com.querydsl.jpa.hibernate.SessionHolder;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

/**
 * HibernateSQLQuery is an SQLQuery implementation that uses Hibernate's Native SQL functionality
 * to execute queries
 *
 * @author tiwe
 *
 */
public class HibernateSQLQuery extends AbstractHibernateSQLQuery<HibernateSQLQuery> {

    public HibernateSQLQuery(Session session, SQLTemplates sqlTemplates) {
        super(session, new Configuration(sqlTemplates));
    }
    
    public HibernateSQLQuery(Session session, Configuration conf) {
        super(session, conf);
    }

    public HibernateSQLQuery(StatelessSession session, SQLTemplates sqlTemplates) {
        super(session, new Configuration(sqlTemplates));
    }
    
    public HibernateSQLQuery(StatelessSession session, Configuration conf) {
        super(session, conf);
    }
    
    public HibernateSQLQuery(SessionHolder session, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(session, new Configuration(sqlTemplates), metadata);
    }
    
    public HibernateSQLQuery(SessionHolder session, Configuration conf, QueryMetadata metadata) {
        super(session, conf, metadata);
    }

    @Override
    protected HibernateSQLQuery clone(SessionHolder sessionHolder) {
        HibernateSQLQuery q = new HibernateSQLQuery(sessionHolder, configuration, getMetadata().clone());
        q.clone(this);
        return q;
    }

}
