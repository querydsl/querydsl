package com.mysema.query;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import com.mysema.query.animal.Cat;
import com.mysema.query.animal.QCat;
import com.mysema.query.collections.impl.ColQueryImpl;

public class QueryMutabilityTest {
    
    @Test
    public void test(){
        QCat cat = QCat.cat;
        ColQueryImpl query = new ColQueryImpl();
        query.from(cat, Collections.<Cat>emptyList());
        
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

    private void assertProjectionEmpty(ColQueryImpl query) {
        assertTrue(query.getMetadata().getProjection().isEmpty());        
    }

}
