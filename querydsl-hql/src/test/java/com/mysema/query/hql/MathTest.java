package com.mysema.query.hql;

import static com.mysema.query.functions.MathFunctions.add;
import static com.mysema.query.functions.MathFunctions.div;
import static com.mysema.query.functions.MathFunctions.mult;
import static com.mysema.query.functions.MathFunctions.sub;

import org.junit.Test;

import com.mysema.query.hql.domain.QCat;

public class MathTest extends AbstractQueryTest{

    @Test
    public void testArithmeticOperationsInFunctionalWay() {
        toString("cat.bodyWeight + :a1", add(cat.bodyWeight, 10));
        toString("cat.bodyWeight - :a1", sub(cat.bodyWeight, 10));
        toString("cat.bodyWeight * :a1", mult(cat.bodyWeight, 10));
        toString("cat.bodyWeight / :a1", div(cat.bodyWeight, 10));

        toString("cat.bodyWeight + :a1 < :a1", add(cat.bodyWeight, 10).lt(10));
        toString("cat.bodyWeight - :a1 < :a1", sub(cat.bodyWeight, 10).lt(10));
        toString("cat.bodyWeight * :a1 < :a1", mult(cat.bodyWeight, 10).lt(10));
        toString("cat.bodyWeight / :a1 < :a2", div(cat.bodyWeight, 10).lt(10d));

        toString("(cat.bodyWeight + :a1) * :a2", mult(add(cat.bodyWeight, 10),
                20));
        toString("(cat.bodyWeight - :a1) * :a2", mult(sub(cat.bodyWeight, 10),
                20));
        toString("cat.bodyWeight * :a1 + :a2",
                add(mult(cat.bodyWeight, 10), 20));
        toString("cat.bodyWeight * :a1 - :a2",
                sub(mult(cat.bodyWeight, 10), 20));

        QCat c1 = new QCat("c1");
        QCat c2 = new QCat("c2");
        QCat c3 = new QCat("c3");
        toString("c1.id + c2.id * c3.id", add(c1.id, mult(c2.id, c3.id)));
        toString("c1.id * (c2.id + c3.id)", mult(c1.id, add(c2.id, c3.id)));
        toString("(c1.id + c2.id) * c3.id", mult(add(c1.id, c2.id), c3.id));
    }
    

    @Test
    public void testMathematicalOperations() {
        // mathematical operators +, -, *, /
        add(cat.bodyWeight, kitten.bodyWeight);
        sub(cat.bodyWeight, kitten.bodyWeight);
        mult(cat.bodyWeight, kitten.bodyWeight);
        div(cat.bodyWeight, kitten.bodyWeight);
    }
    
}
