package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jdoql.testdomain.QProduct;

public class JDOQLSubQueryTest {
    
    @Test
    public void Multiple_Projections(){
        JDOQLSubQuery query = new JDOQLSubQuery();
        query.from(QProduct.product);
        assertEquals(1, query.list(QProduct.product).getMetadata().getProjection().size());
        assertEquals(1, query.list(QProduct.product).getMetadata().getProjection().size());
    }

}
