/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.mysema.query.types.expr.BooleanExpression;

public class CompilationOverheadTest {

    private static final QCat cat = QCat.cat;

    @Test
    public void test(){
        List<BooleanExpression> conditions = Arrays.asList(
            cat.mate.isNull(),
            cat.mate.isNotNull(),
            cat.mate.name.eq("Kitty"),
            cat.mate.name.ne("Kitty"),
            cat.mate.isNotNull().and(cat.mate.name.eq("Kitty")),
            cat.mate.isNotNull().and(cat.mate.name.eq("Kitty")).and(cat.kittens.isEmpty())
        );

        // 1st
        for (BooleanExpression condition : conditions){
            query(condition);
        }
        System.err.println();

        // 2nd
        for (BooleanExpression condition : conditions){
            query(condition);
        }
    }

    private void query(BooleanExpression condition){
        long start = System.currentTimeMillis();
        MiniApi.from(cat, Collections.<Cat>emptyList()).where(condition).list(cat);
        long duration = System.currentTimeMillis() - start;
        System.out.println(condition + " : " + duration + "ms");
    }

}
