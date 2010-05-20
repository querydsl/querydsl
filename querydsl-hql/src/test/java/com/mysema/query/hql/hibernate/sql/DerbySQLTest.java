/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.SearchResults;
import com.mysema.query.hql.domain.Cat;
import com.mysema.query.hql.domain.QCat;
import com.mysema.query.hql.domain.sql.SAnimal;
import com.mysema.query.sql.DerbyTemplates;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.EConstructor;
import com.mysema.testutil.HibernateConfig;
import com.mysema.testutil.HibernateTestRunner;

@RunWith(HibernateTestRunner.class)
@HibernateConfig("derby.properties")
public class DerbySQLTest {
    
    private static final SQLTemplates derbyTemplates = new DerbyTemplates();
    
    private Session session;
    
    protected HibernateSQLQuery query(){
        return new HibernateSQLQuery(session, derbyTemplates);
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
    @Before
    public void setUp(){
        session.save(new Cat("Beck",1));
        session.save(new Cat("Kate",2));
        session.save(new Cat("Kitty",3));
        session.save(new Cat("Bobby",4));
        session.save(new Cat("Harold",5));
        session.save(new Cat("Tim",6));
        session.flush();   
    }
    
    @Test
    public void scalarQueries(){
        SAnimal cat = new SAnimal("cat");

        // count
        assertEquals(6l, query().from(cat).where(cat.dtype.eq("C")).count());
        
        // countDistinct
        assertEquals(6l, query().from(cat).where(cat.dtype.eq("C")).countDistinct());
        
        // list
        assertEquals(6, query().from(cat).where(cat.dtype.eq("C")).list(cat.id).size());
        
        // list with limit
        assertEquals(3, query().from(cat).limit(3).list(cat.id).size());
        
        // list with offset
        assertEquals(3, query().from(cat).offset(3).list(cat.id).size());
        
        // list with limit and offset
        assertEquals(3, query().from(cat).offset(3).limit(3).list(cat.id).size());
        
        // list multiple
        print(query().from(cat).where(cat.dtype.eq("C")).list(cat.id, cat.name, cat.bodyweight));
        
        // listResults
        SearchResults<String> results = query().from(cat).limit(3).orderBy(cat.name.asc()).listResults(cat.name);
        assertEquals(Arrays.asList("Beck","Bobby","Harold"), results.getResults());
        assertEquals(6l, results.getTotal());
        
    }
    
    @Test
    public void entityQueries(){
        SAnimal cat = new SAnimal("cat");
        SAnimal mate = new SAnimal("mate");
        QCat catEntity = QCat.cat;
    
        // 1
        List<Cat> cats = query().from(cat).orderBy(cat.name.asc()).list(catEntity);
        assertEquals(6, cats.size());
        for (Cat c : cats) System.out.println(c.getName());        
        
        // 2
        cats = query().from(cat)
            .innerJoin(mate).on(cat.mateId.eq(mate.id))
            .where(cat.dtype.eq("C"), mate.dtype.eq("C"))
            .list(catEntity);
        assertTrue(cats.isEmpty());
    }
    
    
    @Test
    public void entityProjections(){
        SAnimal cat = new SAnimal("cat");
        
        List<Cat> cats = query().from(cat).orderBy(cat.name.asc())
            .list(EConstructor.create(Cat.class, cat.name, cat.id));
        assertEquals(6, cats.size());
        for (Cat c : cats) System.out.println(c.getName());
    }
    
    @Test
    public void wildcard(){
        SAnimal cat = new SAnimal("cat");
        
        List<Object[]> rows = query().from(cat).list(cat.all());
        assertEquals(6, rows.size());
        print(rows);
        
        rows = query().from(cat).list(cat.id, cat.all());
        assertEquals(6, rows.size());
        print(rows);
    }
    
    private void print(Iterable<Object[]> rows){
        for (Object[] row : rows){
            System.out.println(Arrays.asList(row));
        }
    }

}
