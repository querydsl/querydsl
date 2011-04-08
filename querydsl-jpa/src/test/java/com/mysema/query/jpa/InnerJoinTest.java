/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import static com.mysema.query.alias.Alias.$;

import java.util.List;

import org.junit.Test;

import com.mysema.query.alias.Alias;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.types.path.StringPath;

public class InnerJoinTest {

    public interface Entity{

        List<String> getNames();
    }

    @Test
    public void SubQuery_FullJoin(){
        Entity alias = Alias.alias(Entity.class);
        StringPath path = new StringPath("path");
        JPQLSubQuery subQuery = new JPQLSubQuery();
        subQuery.from($(alias));
        subQuery.fullJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void SubQuery_InnerJoin(){
        Entity alias = Alias.alias(Entity.class);
        StringPath path = new StringPath("path");
        JPQLSubQuery subQuery = new JPQLSubQuery();
        subQuery.from($(alias));
        subQuery.innerJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void SubQuery_Join(){
        Entity alias = Alias.alias(Entity.class);
        StringPath path = new StringPath("path");
        JPQLSubQuery subQuery = new JPQLSubQuery();
        subQuery.from($(alias));
        subQuery.join($(alias.getNames()), path);
        // TODO : assertions
    }

    @Test
    public void SubQuery_LeftJoin(){
        Entity alias = Alias.alias(Entity.class);
        StringPath path = new StringPath("path");
        JPQLSubQuery subQuery = new JPQLSubQuery();
        subQuery.from($(alias));
        subQuery.leftJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_FullJoin(){
        Entity alias = Alias.alias(Entity.class);
        StringPath path = new StringPath("path");
        JPQLQuery query = new HibernateQuery(new DummySessionHolder(), HQLTemplates.DEFAULT);
        query.from($(alias));
        query.fullJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_InnerJoin(){
        Entity alias = Alias.alias(Entity.class);
        StringPath path = new StringPath("path");
        JPQLQuery query = new HibernateQuery(new DummySessionHolder(), HQLTemplates.DEFAULT);
        query.from($(alias));
        query.innerJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_Join(){
        Entity alias = Alias.alias(Entity.class);
        StringPath path = new StringPath("path");
        JPQLQuery query = new HibernateQuery(new DummySessionHolder(), HQLTemplates.DEFAULT);
        query.from($(alias));
        query.join($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_LeftJoin(){
        Entity alias = Alias.alias(Entity.class);
        StringPath path = new StringPath("path");
        JPQLQuery query = new HibernateQuery(new DummySessionHolder(), HQLTemplates.DEFAULT);
        query.from($(alias));
        query.leftJoin($(alias.getNames()), path);
        // TODO : assertions
    }

}
