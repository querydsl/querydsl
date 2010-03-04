package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.mysema.query.QueryMutability;
import com.mysema.query.jdoql.testdomain.QProduct;

public class QueryMutabilityTest extends AbstractJDOTest{
    
    @Test
    public void test() throws IOException, SecurityException,
            IllegalArgumentException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        QProduct product = QProduct.product;
        JDOQLQueryImpl query = (JDOQLQueryImpl) query().from(product);
        new QueryMutability(query).test(product.name, product.description);
    }
    
    @Test
    public void testClone(){
        QProduct product = QProduct.product;
        JDOQLQueryImpl query = new JDOQLQueryImpl().from(product).where(product.name.isNotNull());        
        JDOQLQueryImpl query2 = query.clone(pm);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(product);
    }

}
