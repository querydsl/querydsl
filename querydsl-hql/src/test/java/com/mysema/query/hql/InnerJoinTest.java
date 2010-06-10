/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static com.mysema.query.alias.Alias.$;

import java.util.List;

import org.junit.Test;

import com.mysema.query.alias.Alias;
import com.mysema.query.hql.hibernate.HibernateQuery;
import com.mysema.query.types.path.PString;

public class InnerJoinTest {

    public interface Entity{
        
        List<String> getNames();
    }
    
    @Test
    public void subQuery(){
        Entity alias = Alias.alias(Entity.class);
        PString path = new PString("path");
        HQLSubQuery subQuery = new HQLSubQuery();
        subQuery.from($(alias));
        subQuery.fullJoin($(alias.getNames()), path);        
        subQuery.innerJoin($(alias.getNames()), path);
        subQuery.join($(alias.getNames()), path);
        subQuery.leftJoin($(alias.getNames()), path);        
    }
    
    @Test
    public void query(){
        Entity alias = Alias.alias(Entity.class);
        PString path = new PString("path");
        HQLQuery query = new HibernateQuery(new DummySessionHolder(), HQLTemplates.DEFAULT);
        query.from($(alias));
        query.fullJoin($(alias.getNames()), path);        
        query.innerJoin($(alias.getNames()), path);
        query.join($(alias.getNames()), path);
        query.leftJoin($(alias.getNames()), path);
    }
    
    
}
