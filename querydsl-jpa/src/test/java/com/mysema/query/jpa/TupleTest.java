package com.mysema.query.jpa;

import org.junit.Test;

import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.hibernate.HibernateSubQuery;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.SubQueryExpression;

public class TupleTest extends AbstractQueryTest {
        
    @Test
    public void test() {
        QCat cat = QCat.cat;
        
        SubQueryExpression<?> subQuery = subQuery().from(cat)
        .where(subQuery()
           .from(cat)
           .groupBy(cat.mate)
           .list(new QTuple(cat.mate, cat.birthdate.max()))
             .contains(new QTuple(cat.mate, cat.birthdate)))
        .list(new QTuple(cat.birthdate, cat.name, cat.mate));
        
        assertToString(
                "(select cat.birthdate, cat.name, cat.mate from Cat cat " +
                "where (cat.mate, cat.birthdate) in " +
                    "(select cat.mate, max(cat.birthdate) from Cat cat group by cat.mate))", subQuery);
    }

    private HibernateSubQuery subQuery() {
        return new HibernateSubQuery();
    }
    
}
