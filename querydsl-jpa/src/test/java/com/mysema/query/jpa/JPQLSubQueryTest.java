package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jpa.domain.QCat;

public class JPQLSubQueryTest {
    
    @Test
    public void Multiple_Projections(){
        JPQLSubQuery query = new JPQLSubQuery();
        query.from(QCat.cat);
        assertEquals(1, query.list(QCat.cat).getMetadata().getProjection().size());
        assertEquals(1, query.list(QCat.cat).getMetadata().getProjection().size());
    }

}
