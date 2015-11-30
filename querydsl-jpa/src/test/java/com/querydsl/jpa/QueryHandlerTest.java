package com.querydsl.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QueryHandlerTest {

    @Test
    public void types() {
        assertEquals(EclipseLinkHandler.class, EclipseLinkTemplates.DEFAULT.getQueryHandler().getClass());
        assertEquals(HibernateHandler.class, HQLTemplates.DEFAULT.getQueryHandler().getClass());
        assertEquals(DefaultQueryHandler.class, JPQLTemplates.DEFAULT.getQueryHandler().getClass());
    }

}
