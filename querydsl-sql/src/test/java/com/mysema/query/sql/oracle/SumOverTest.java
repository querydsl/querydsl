/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.oracle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.NumberPath;

public class SumOverTest {

    @Test
    public void ToString(){
        NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "intPath");
        SumOver<Integer> sumOver = new SumOver<Integer>(intPath);
        sumOver.order(intPath);
        sumOver.partition(intPath);

        assertEquals("sum(intPath) over (partition by intPath order by intPath)", sumOver.toString());
    }

    @Test
    public void Equals(){
        NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "intPath");
        SumOver<Integer> sumOver1 = new SumOver<Integer>(intPath).order(intPath).partition(intPath);
        SumOver<Integer> sumOver2 = new SumOver<Integer>(intPath).order(intPath).partition(intPath);
        assertEquals(sumOver1, sumOver2);
    }

}
