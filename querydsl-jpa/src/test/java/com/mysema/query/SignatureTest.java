package com.mysema.query;

import org.junit.Test;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.impl.JPAQuery;

public class SignatureTest {
    
    @Test
    public void test() {
        meet((JPAQuery)null);
        meet((HibernateQuery)null);
        meet((JPQLQuery)null);
    }
    
    public static <T extends FilteredClause<? super T>> T meet(T query) {
        return null;
    }

}
