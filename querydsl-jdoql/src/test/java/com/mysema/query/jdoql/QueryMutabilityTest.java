package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.mysema.query.jdoql.testdomain.QProduct;

public class QueryMutabilityTest extends AbstractJDOTest{
    
    @Test
    public void test() throws IOException{
        QProduct product = QProduct.product;
        JDOQLQueryImpl query = (JDOQLQueryImpl) query().from(product);
        
        query.count();
        assertProjectionEmpty(query);
        query.countDistinct();
        assertProjectionEmpty(query);
        
        query.iterate(product);
        assertProjectionEmpty(query);
        query.iterate(product,product);
        assertProjectionEmpty(query);
        query.iterateDistinct(product);
        assertProjectionEmpty(query);
        query.iterateDistinct(product,product);
        assertProjectionEmpty(query);
        
        query.list(product);
        assertProjectionEmpty(query);
        query.list(product,product);
        assertProjectionEmpty(query);
        query.listDistinct(product);
        assertProjectionEmpty(query);
        query.listDistinct(product,product);
        assertProjectionEmpty(query);
        
        query.listResults(product);
        assertProjectionEmpty(query);
        query.listDistinctResults(product);
        assertProjectionEmpty(query);
        
        query.map(product.name, product);
        assertProjectionEmpty(query);
        
        query.uniqueResult(product);
        assertProjectionEmpty(query);
        query.uniqueResult(product,product);
        assertProjectionEmpty(query);
        
    }
    
    @Test
    public void testClone(){
        QProduct product = QProduct.product;
        JDOQLQueryImpl query = (JDOQLQueryImpl) query().from(product).where(product.name.isNotNull());        
        JDOQLQueryImpl query2 = query.clone(pm);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(product);
    }

    private void assertProjectionEmpty(JDOQLQueryImpl query) throws IOException {
        assertTrue(query.getMetadata().getProjection().isEmpty());        
        query.close();
    }

}
