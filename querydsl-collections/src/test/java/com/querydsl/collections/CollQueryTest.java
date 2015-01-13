/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.collections;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import org.junit.Test;
import static com.querydsl.collections.CollQueryFactory.from;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CollQueryTest extends AbstractQueryTest {

    @Test
    public void CustomTemplates() {
        CollQueryTemplates templates = new CollQueryTemplates() {{
            add(Ops.DateTimeOps.MONTH, "{0}.getMonthOfYear()");
            add(Ops.DateTimeOps.YEAR, "{0}.getYear()");
        }};
        new CollQuery(templates);
    }
    
    @Test
    public void InstanceOf() {
        assertEquals(
                Arrays.asList(c1, c2),
                query().from(cat, Arrays.asList(c1, c2)).where(cat.instanceOf(Cat.class)).list(cat));
    }

    @Test
    public void After_And_Before() {
        query().from(cat, Arrays.asList(c1, c2))
            .where(
                cat.birthdate.lt(new Date()),
                cat.birthdate.loe(new Date()), 
                cat.birthdate.gt(new Date()),
                cat.birthdate.goe(new Date()))
            .list(cat);
    }

    @Test
    public void ArrayProjection() {
        // select pairs of cats with different names
        query().from(cat, cats).from(otherCat, cats).where(cat.name.ne(otherCat.name)).list(cat.name, otherCat.name);
        assertEquals(4*3, last.res.size());
    }

    @Test
    public void Cast() {
        NumberExpression<?> num = cat.id;
        Expression<?>[] expr = new Expression[] { num.byteValue(), num.doubleValue(),
                num.floatValue(), num.intValue(), num.longValue(),
                num.shortValue(), num.stringValue() };

        for (Expression<?> e : expr) {
            query().from(cat, Arrays.asList(c1, c2)).list(e);
        }

    }
    
    @Test
    public void Clone() {
        CollQuery query = new CollQuery().from(cat, Collections.<Cat>emptyList()).where(cat.isNotNull()).clone();
        assertEquals("cat is not null", query.getMetadata().getWhere().toString());
    }

    @Test
    public void Primitives() {
        // select cats with kittens
        query().from(cat, cats).where(cat.kittens.size().ne(0)).list(cat.name);
        assertTrue(last.res.size() == 4);

        // select cats without kittens
        query().from(cat, cats).where(cat.kittens.size().eq(0)).list(cat.name);
        assertTrue(last.res.size() == 0);
    }

    @Test
    public void SimpleCases() {
        // select all cat names
        query().from(cat, cats).list(cat.name);
        assertTrue(last.res.size() == 4);

        // select all kittens
        query().from(cat, cats).list(cat.kittens);
        assertTrue(last.res.size() == 4);

        // select cats with kittens
        query().from(cat, cats).where(cat.kittens.size().gt(0)).list(cat.name);
        assertTrue(last.res.size() == 4);

        // select cats named Kitty
        query().from(cat, cats).where(cat.name.eq("Kitty")).list(cat.name);
        assertTrue(last.res.size() == 1);

        // select cats named Kitt%
        query().from(cat, cats).where(cat.name.matches("Kitt.*")).list(cat.name);
        assertTrue(last.res.size() == 1);

        query().from(cat, cats).list(cat.bodyWeight.add(cat.weight));
    }

    @Test
    public void Various() {
        StringPath a = new StringPath("a");
        StringPath b = new StringPath("b");
        for (Tuple strs : from(a, "aa", "bb", "cc")
                .from(b, Arrays.asList("a","b"))
                .where(a.startsWith(b)).list(a, b)) {
            System.out.println(strs);
        }

        query().from(cat, cats).list(cat.mate);

        query().from(cat, cats).list(cat.kittens);

        query().from(cat, cats).where(cat.kittens.isEmpty()).list(cat);

        query().from(cat, cats).where(cat.kittens.isNotEmpty()).list(cat);

        query().from(cat, cats).where(cat.name.matches("fri.*")).list(cat.name);

    }

    @Test
    public void BigDecimals() {
        NumberPath<BigDecimal> a = new NumberPath<BigDecimal>(BigDecimal.class, "cost");
        List<BigDecimal> nums = from(a, new BigDecimal("2.1"), new BigDecimal("20.21"),
                new BigDecimal("44.4")).where(a.lt(new BigDecimal("35.1"))).list(a);

        for (BigDecimal num : nums) {
            System.out.println(num);
        }
        assertEquals(2, nums.size());
        for (BigDecimal num : nums) {
            assertEquals(-1, num.compareTo(new BigDecimal("35")));
        }
    }

    @Test(expected=UnsupportedOperationException.class)
    public void GroupBy() {
        query().from(cat, cats).groupBy(cat.name);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void Having() {
        query().from(cat, cats).having(cat.name.isNull());
    }

}
