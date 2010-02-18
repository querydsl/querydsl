package com.mysema.query;

import static org.junit.Assert.assertTrue;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.hql.domain.QCat;
import com.mysema.query.hql.hibernate.HibernateQuery;
import com.mysema.testutil.HibernateConfig;
import com.mysema.testutil.HibernateTestRunner;

@RunWith(HibernateTestRunner.class)
@HibernateConfig("derby.properties")
public class HibernateQueryMutabilityTest{
    
    private Session session;
    
    protected HibernateQuery query(){
        return new HibernateQuery(session);
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
    @Test
    public void test(){
        QCat cat = QCat.cat;
        HibernateQuery query = query().from(cat);
        
        query.count();
        assertProjectionEmpty(query);
        query.countDistinct();
        assertProjectionEmpty(query);
        
        query.iterate(cat);
        assertProjectionEmpty(query);
        query.iterate(cat,cat);
        assertProjectionEmpty(query);
        query.iterateDistinct(cat);
        assertProjectionEmpty(query);
        query.iterateDistinct(cat,cat);
        assertProjectionEmpty(query);
        
        query.list(cat);
        assertProjectionEmpty(query);
        query.list(cat,cat);
        assertProjectionEmpty(query);
        query.listDistinct(cat);
        assertProjectionEmpty(query);
        query.listDistinct(cat,cat);
        assertProjectionEmpty(query);
        
        query.listResults(cat);
        assertProjectionEmpty(query);
        query.listDistinctResults(cat);
        assertProjectionEmpty(query);
        
        query.map(cat.name, cat);
        assertProjectionEmpty(query);
        
        query.uniqueResult(cat);
        assertProjectionEmpty(query);
        query.uniqueResult(cat,cat);
        assertProjectionEmpty(query);
        
    }

    private void assertProjectionEmpty(HibernateQuery query) {
        assertTrue(query.getMetadata().getProjection().isEmpty());        
    }

}
