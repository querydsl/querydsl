/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import org.junit.Test;

import com.mysema.query.hql.domain.QCat;

public class MathTest extends AbstractQueryTest{

    @Test
    public void testArithmeticOperationsInFunctionalWay() {
        toString("cat.bodyWeight + :a1", cat.bodyWeight.add(10));
        toString("cat.bodyWeight - :a1", cat.bodyWeight.sub(10));
        toString("cat.bodyWeight * :a1", cat.bodyWeight.mult(10));
        toString("cat.bodyWeight / :a1", cat.bodyWeight.div(10));

        toString("cat.bodyWeight + :a1 < :a1", cat.bodyWeight.add(10).lt(10));
        toString("cat.bodyWeight - :a1 < :a1", cat.bodyWeight.sub(10).lt(10));
        toString("cat.bodyWeight * :a1 < :a1", cat.bodyWeight.mult(10).lt(10));
        toString("cat.bodyWeight / :a1 < :a2", cat.bodyWeight.div(10).lt(10d));

        toString("(cat.bodyWeight + :a1) * :a2", cat.bodyWeight.add(10).mult(20));
        toString("(cat.bodyWeight - :a1) * :a2", cat.bodyWeight.sub(10).mult(20));
        toString("cat.bodyWeight * :a1 + :a2", cat.bodyWeight.mult(10).add(20));
        toString("cat.bodyWeight * :a1 - :a2", cat.bodyWeight.mult(10).sub(20));

        QCat c1 = new QCat("c1");
        QCat c2 = new QCat("c2");
        QCat c3 = new QCat("c3");
        toString("c1.id + c2.id * c3.id", c1.id.add(c2.id.mult(c3.id)));
        toString("c1.id * (c2.id + c3.id)", c1.id.mult(c2.id.add(c3.id)));
        toString("(c1.id + c2.id) * c3.id", c1.id.add(c2.id).mult(c3.id));
    }
    

    @Test
    public void testMathematicalOperations() {
        // mathematical operators +, -, *, /
        cat.bodyWeight.add(kitten.bodyWeight);
        cat.bodyWeight.sub(kitten.bodyWeight);
        cat.bodyWeight.mult(kitten.bodyWeight);
        cat.bodyWeight.div(kitten.bodyWeight);
    }
    
}
