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

public class JoinTest {

    public interface Entity{

        List<String> getNames();
    }
    
    private final Entity alias = Alias.alias(Entity.class);

    private final StringPath path = new StringPath("path");
    private final JPQLSubQuery subQuery = new JPQLSubQuery();
    private final JPQLQuery query = new HibernateQuery(new DummySessionHolder(), HQLTemplates.DEFAULT);

    
    @Test
    public void SubQuery_FullJoin(){
        subQuery.from($(alias));
        subQuery.fullJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void SubQuery_InnerJoin(){
        subQuery.from($(alias));
        subQuery.innerJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void SubQuery_Join(){
        subQuery.from($(alias));
        subQuery.join($(alias.getNames()), path);
        // TODO : assertions
    }

    @Test
    public void SubQuery_LeftJoin(){
        subQuery.from($(alias));
        subQuery.leftJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_FullJoin(){
        query.from($(alias));
        query.fullJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_InnerJoin(){
        query.from($(alias));
        query.innerJoin($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_Join(){
        query.from($(alias));
        query.join($(alias.getNames()), path);
        // TODO : assertions
    }
    
    @Test
    public void Query_LeftJoin(){
        query.from($(alias));
        query.leftJoin($(alias.getNames()), path);
        // TODO : assertions
    }

}
