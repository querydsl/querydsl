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

import static com.querydsl.core.alias.Alias.$;

import java.util.List;

import org.junit.Test;

import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.core.types.path.StringPath;

public class JoinTest {

    public interface Entity{

        List<String> getNames();
    }
    
    private final Entity alias = Alias.alias(Entity.class);

    private final StringPath path = new StringPath("path");
    private final JPASubQuery subQuery = new JPASubQuery();
    private final HibernateQuery query = new HibernateQuery(new DummySessionHolder(), HQLTemplates.DEFAULT);

    
    @Test
    public void SubQuery_FullJoin() {
        subQuery.from($(alias));
        subQuery.fullJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void SubQuery_InnerJoin() {
        subQuery.from($(alias));
        subQuery.innerJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void SubQuery_Join() {
        subQuery.from($(alias));
        subQuery.join($(alias.getNames()), path);
        // TODO : assertions
    }

    @Test
    public void SubQuery_LeftJoin() {
        subQuery.from($(alias));
        subQuery.leftJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_FullJoin() {
        query.from($(alias));
        query.fullJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_InnerJoin() {
        query.from($(alias));
        query.innerJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_Join() {
        query.from($(alias));
        query.join($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_LeftJoin() {
        query.from($(alias));
        query.leftJoin($(alias.getNames()), path);
        // TODO : assertions
    }

}
