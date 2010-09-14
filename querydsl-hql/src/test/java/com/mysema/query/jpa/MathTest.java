/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import org.junit.Test;

import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.types.path.NumberPath;

public class MathTest extends AbstractQueryTest{

    @Test
    public void test(){
        NumberPath<Double> path = QCat.cat.bodyWeight;
        assertToString("(cat.bodyWeight - sum(cat.bodyWeight)) * cat.bodyWeight", path.subtract(path.sum()).multiply(path));
    }

    @Test
    public void testArithmeticOperationsInFunctionalWay() {
        assertToString("cat.bodyWeight + :a1", cat.bodyWeight.add(10));
        assertToString("cat.bodyWeight - :a1", cat.bodyWeight.subtract(10));
        assertToString("cat.bodyWeight * :a1", cat.bodyWeight.multiply(10));
        assertToString("cat.bodyWeight / :a1", cat.bodyWeight.divide(10));

        assertToString("cat.bodyWeight + :a1 < :a1", cat.bodyWeight.add(10.0).lt(10.0));
        assertToString("cat.bodyWeight - :a1 < :a1", cat.bodyWeight.subtract(10.0).lt(10.0));
        assertToString("cat.bodyWeight * :a1 < :a1", cat.bodyWeight.multiply(10.0).lt(10.0));
        assertToString("cat.bodyWeight / :a1 < :a2", cat.bodyWeight.divide(10.0).lt(20.0));

        assertToString("(cat.bodyWeight + :a1) * :a2", cat.bodyWeight.add(10).multiply(20));
        assertToString("(cat.bodyWeight - :a1) * :a2", cat.bodyWeight.subtract(10).multiply(20));
        assertToString("cat.bodyWeight * :a1 + :a2", cat.bodyWeight.multiply(10).add(20));
        assertToString("cat.bodyWeight * :a1 - :a2", cat.bodyWeight.multiply(10).subtract(20));

        QCat c1 = new QCat("c1");
        QCat c2 = new QCat("c2");
        QCat c3 = new QCat("c3");
        assertToString("c1.id + c2.id * c3.id", c1.id.add(c2.id.multiply(c3.id)));
        assertToString("c1.id * (c2.id + c3.id)", c1.id.multiply(c2.id.add(c3.id)));
        assertToString("(c1.id + c2.id) * c3.id", c1.id.add(c2.id).multiply(c3.id));
    }

    @Test
    public void testMathematicalOperations() {
        // mathematical operators +, -, *, /
        cat.bodyWeight.add(kitten.bodyWeight);
        cat.bodyWeight.subtract(kitten.bodyWeight);
        cat.bodyWeight.multiply(kitten.bodyWeight);
        cat.bodyWeight.divide(kitten.bodyWeight);
    }

}
