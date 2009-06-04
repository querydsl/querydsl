/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.perf;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.mysema.query.collections.ColQueryImpl;
import com.mysema.query.collections.ColQueryPatterns;
import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.eval.JavaSerializer;
import com.mysema.query.types.expr.EBoolean;

/**
 * QueryValidationTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class QueryValidationTest extends AbstractPerformanceTest {

    @Test
    public void validateResults1Source() {
        int size = 50;
        int max = size * size;
        long count, expected;
        List<Cat> cats1 = cats(size);
        ColQueryImpl query;
        StringBuilder res = new StringBuilder();
        for (EBoolean condition : conditionsFor1Source) {
            // without wrapped iterators
            query = new ColQueryWithoutIndexing();
            query.setWrapIterators(false);
            count = query.from(cat, cats1).where(condition).count();
            expected = count;
            res.append(StringUtils.leftPad(String.valueOf(count), 7));

            // without reordering
            query = new ColQueryWithoutIndexing();
            query.setSortSources(false);
            count = query.from(cat, cats1).where(condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append((expected != count || count > max) ? " X" : "  ");

            // with reordering and iterator wrapping
            query = new ColQueryWithoutIndexing();
            count = query.from(cat, cats1).where(condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append((expected != count || count > max) ? " X" : "  ");

            // indexed, without reordering
            query = new ColQueryImpl();
            query.setSortSources(false);
            count = query.from(cat, cats1).where(condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append((expected != count || count > max) ? " X" : "  ");

            // indexed, with reordering and iterator wrapping
            query = new ColQueryImpl();
            count = query.from(cat, cats1).where(condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append((expected != count || count > max) ? " X" : "  ");

            // indexed, with reordering and iterator wrapping and sequential
            // union handling
            query = new ColQueryImpl();
            query.setSequentialUnion(true);
            count = query.from(cat, cats1).where(condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append((expected != count || count > max) ? " X" : "  ");

            res.append("   ");
            res.append(new JavaSerializer(ColQueryPatterns.DEFAULT).handle(condition)
                    .toString());
            res.append("\n");
        }
        System.out.println(res);
        if (res.toString().contains("X")) {
            fail(res.toString().replaceAll("[^X]", "").length()
                    + " errors occurred. See log for details.");
        }
    }

    @Test
    public void validateResults2Sources() {
        int size = 50;
        int max = size * size;
        long count, expected;
        List<Cat> cats1 = cats(size);
        List<Cat> cats2 = cats(size);
        ColQueryImpl query;
        StringBuilder res = new StringBuilder();
        for (EBoolean condition : conditionsFor2Sources) {
            // without wrapped iterators
            query = new ColQueryWithoutIndexing();
            query.setWrapIterators(false);
            count = query.from(cat, cats1).from(otherCat, cats2).where(
                    condition).count();
            expected = count;
            res.append(StringUtils.leftPad(String.valueOf(count), 7));

            // without reordering
            query = new ColQueryWithoutIndexing();
            query.setSortSources(false);
            count = query.from(cat, cats1).from(otherCat, cats2).where(
                    condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append((expected != count || count > max) ? " X" : "  ");

            // with reordering and iterator wrapping
            query = new ColQueryWithoutIndexing();
            count = query.from(cat, cats1).from(otherCat, cats2).where(
                    condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append((expected != count || count > max) ? " X" : "  ");

            // indexed, without reordering
            query = new ColQueryImpl();
            query.setSortSources(false);
            count = query.from(cat, cats1).from(otherCat, cats2).where(
                    condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append((expected != count || count > max) ? " X" : "  ");

            // indexed, with reordering and iterator wrapping
            query = new ColQueryImpl();
            count = query.from(cat, cats1).from(otherCat, cats2).where(
                    condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append((expected != count || count > max) ? " X" : "  ");

            // indexed, with reordering and iterator wrapping and sequential
            // union handling
            query = new ColQueryImpl();
            query.setSequentialUnion(true);
            count = query.from(cat, cats1).from(otherCat, cats2).where(
                    condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append((expected != count || count > max) ? " X" : "  ");

            res.append("   ");
            res.append(new JavaSerializer(ColQueryPatterns.DEFAULT).handle(condition)
                    .toString());
            res.append("\n");
        }
        System.out.println(res);
        if (res.toString().contains("X")) {
            fail(res.toString().replaceAll("[^X]", "").length()
                    + " errors occurred. See log for details.");
        }
    }

    @Test
    public void testQueryResults() {
        EBoolean condition = cat.bodyWeight.eq(0)
                .and(otherCat.name.eq("Kate5"));
        List<Cat> cats1 = cats(10);
        List<Cat> cats2 = cats(10);

        ColQueryImpl query = new ColQueryWithoutIndexing();
        query.setSortSources(false);
        for (Object[] cats : query.from(cat, cats1).from(otherCat, cats2)
                .where(condition).list(cat, otherCat)) {
            System.out.println(Arrays.asList(cats));
        }
    }
}
