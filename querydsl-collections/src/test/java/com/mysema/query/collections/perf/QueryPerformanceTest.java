/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.perf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.collections.ColQuery;
import com.mysema.query.collections.ColQueryImpl;
import com.mysema.query.collections.domain.Cat;
import com.mysema.query.collections.support.JoinExpressionComparator;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * QueryPerformanceTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class QueryPerformanceTest extends AbstractPerformanceTest {

    private long testIterations = 50;

    private long results;

    private List<String> resultLog = new ArrayList<String>(30);

    @Test
    @Ignore
    public void longTest() {
        Map<Expr<?>, Iterable<?>> exprToIt = new HashMap<Expr<?>, Iterable<?>>();
        exprToIt.put(cat, Collections.emptySet());
        exprToIt.put(otherCat, Collections.emptySet());

        List<JoinExpression> joins = Arrays.<JoinExpression> asList(
                new JoinExpression(JoinType.DEFAULT, cat),
                new JoinExpression(JoinType.DEFAULT, otherCat));

        for (int i = 0; i < conditionsFor2Sources.size(); i++) {
            if (new JoinExpressionComparator(conditionsFor2Sources.get(i))
                    .comparePrioritiesOnly(joins.get(0), joins.get(1)) > 0) {
                System.out.println("#" + (i + 1) + " inverted");
            } else {
                System.out.println("#" + (i + 1) + " order preserved");
            }
        }

        System.out.println();

        runTest(100);
        runTest(500);
        runTest(1000);
        // runTest(5000);
        // runTest(10000);
        // runTest(50000);

        for (String line : resultLog) {
            System.out.println(line);
        }
    }

    private void runTest(int size) {
        List<Cat> cats1 = cats(size);
        List<Cat> cats2 = cats(size);
        resultLog.add(size + " * " + size + " items");

        // test each condition
        for (EBoolean condition : conditionsFor2Sources) {
            long level1 = 0, level2 = 0, level3 = 0, level4 = 0, level5 = 0;
            ColQueryImpl query;
            for (long j = 0; j < testIterations; j++) {
                // without wrapped iterators
                query = new ColQueryWithoutIndexing();
                query.setWrapIterators(false);
                level1 += query(query, condition, cats1, cats2);

                // without reordering
                query = new ColQueryWithoutIndexing();
                query.setSortSources(false);
                level2 += query(query, condition, cats1, cats2);

                // with reordering and iterator wrapping
                query = new ColQueryWithoutIndexing();
                level3 += query(query, condition, cats1, cats2);

                // indexed, with reordering and iterator wrapping
                query = new ColQueryImpl();
                level4 += query(query, condition, cats1, cats2);

                // indexed with sequential union
                query = new ColQueryImpl();
                query.setSequentialUnion(true);
                level5 += query(query, condition, cats1, cats2);

            }
            StringBuilder builder = new StringBuilder();
            builder.append(" #").append(
                    conditionsFor2Sources.indexOf(condition) + 1).append(
                    "          ");
            builder.append(
                    StringUtils.leftPad(
                            String.valueOf(level1 / testIterations), 10))
                    .append(" ms");
            builder.append(
                    StringUtils.leftPad(
                            String.valueOf(level2 / testIterations), 10))
                    .append(" ms");
            builder.append(
                    StringUtils.leftPad(
                            String.valueOf(level3 / testIterations), 10))
                    .append(" ms");
            builder.append(
                    StringUtils.leftPad(
                            String.valueOf(level4 / testIterations), 10))
                    .append(" ms");
            builder.append(
                    StringUtils.leftPad(
                            String.valueOf(level5 / testIterations), 10))
                    .append(" ms");
            resultLog.add(builder.toString());
        }
        resultLog.add("");
        System.out.println("finished for " + size);
    }

    private long query(ColQuery query, EBoolean condition, List<Cat> cats1,
            List<Cat> cats2) {
        long start = System.currentTimeMillis();
        Iterator<Cat> it = query.from(cat, cats1).from(otherCat, cats2).where(
                condition).iterate(cat);
        results = 0;
        while (it.hasNext()) {
            results++;
            it.next();
        }
        return System.currentTimeMillis() - start;
    }

    public static void main(String[] args) {
        new QueryPerformanceTest().longTest();
    }

}
