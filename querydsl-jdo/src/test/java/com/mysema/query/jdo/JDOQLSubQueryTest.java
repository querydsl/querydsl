package com.mysema.query.jdo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jdo.test.domain.QProduct;

public class JDOQLSubQueryTest {
    
    @Test
    public void Multiple_Projections(){
        JDOQLSubQuery query = new JDOQLSubQuery();
        query.from(QProduct.product);
        assertEquals(1, query.list(QProduct.product).getMetadata().getProjection().size());
        assertEquals(1, query.list(QProduct.product).getMetadata().getProjection().size());
    }

}
