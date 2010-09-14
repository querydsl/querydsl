package com.mysema.query.jpa;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.hql.domain.QCat;
import com.mysema.query.jpa.JPQLSubQuery;

public class JPQLSubQueryTest {
    
    @Test
    public void Multiple_Projections(){
        JPQLSubQuery query = new JPQLSubQuery();
        query.from(QCat.cat);
        assertEquals(1, query.list(QCat.cat).getMetadata().getProjection().size());
        assertEquals(1, query.list(QCat.cat).getMetadata().getProjection().size());
    }

}
