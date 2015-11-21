/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import static com.querydsl.collections.CollQueryFactory.from;
import static com.querydsl.core.alias.Alias.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.alias.Alias;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

public class AliasTest extends AbstractQueryTest {

    @Before
    public void setUp() {
        myInts.add(1);
        myInts.add(2);
        myInts.add(3);
        myInts.add(4);

        Alias.resetAlias();
    }

    @Test
    public void aliasVariations1() {
        // 1st
        QCat cat = new QCat("cat");
        assertEquals(Arrays.asList("Kitty", "Bob", "Alex", "Francis"),
                from(cat, cats).where(cat.kittens.size().gt(0)).select(cat.name).fetch());

        // 2nd
        Cat c = alias(Cat.class, "cat");
        assertEquals(Arrays.asList("Kitty", "Bob", "Alex", "Francis"),
                from(c, cats).where($(c.getKittens()).size().gt(0)).select($(c.getName())).fetch());
    }

    @Test
    public void aliasVariations2() {
        // 1st
        QCat cat = new QCat("cat");
        assertEquals(Arrays.asList(),
                from(cat, cats).where(cat.name.matches("fri.*")).select(cat.name).fetch());

        // 2nd
        Cat c = alias(Cat.class, "cat");
        assertEquals(Arrays.asList(),
                from(c, cats).where($(c.getName()).matches("fri.*")).select($(c.getName())).fetch());
    }

    @Test
    public void alias3() {
        QCat cat = new QCat("cat");
        Cat other = new Cat();
        Cat c = alias(Cat.class, "cat");

        // 1
        from(c, cats).where($(c.getBirthdate()).gt(new Date())).select($(c)).iterate();

        // 2
        try {
            from(c, cats).where($(c.getMate().getName().toUpperCase()).eq("MOE"));
            fail("expected NPE");
        } catch (NullPointerException ne) {
            // expected
        }

        // 3
        assertEquals(cat.name, $(c.getName()));

        // 4
         from(c,cats)
             .where($(c.getKittens().get(0).getBodyWeight()).gt(12))
             .select($(c.getName())).iterate();

        // 5
        from(c, cats).where($(c).eq(other)).select($(c)).iterate();

        // 6
        from(c, cats).where($(c.getKittens()).contains(other)).select($(c))
                .iterate();

        // 7
        from(c, cats).where($(c.getKittens().isEmpty())).select($(c)).iterate();

        // 8
        from(c, cats).where($(c.getName()).startsWith("B")).select($(c)).iterate();

        // 9
        from(c, cats).where($(c.getName()).upper().eq("MOE")).select($(c)).iterate();

        // 10
        assertNotNull($(c.getKittensByName()));
        assertNotNull($(c.getKittensByName().get("Kitty")));
        from(c, cats).where($(c.getKittensByName().get("Kitty")).isNotNull()).select(cat).iterate();

        // 11
//        try {
//            from(cat, cats).where(cat.mate.alive).fetch(cat);
//            fail("expected RuntimeException");
//        } catch (RuntimeException e) {
//            System.out.println(e.getMessage());
//            assertEquals("null in cat.mate.alive", e.getMessage());
//        }

        // 12
        // TestQuery query = query().from(cat, c1, c2).from(cat, c1, c2);
        // assertEquals(1, query.getMetadata().getJoins().size());

    }

    @Test
    public void various1() {
        StringPath str = Expressions.stringPath("str");
        assertEquals(Arrays.asList("a", "ab"),
                from(str, "a", "ab", "cd", "de").where(str.startsWith("a")).select(str).fetch());
    }

    @Test
    public void various2() {
        assertEquals(Arrays.asList(1, 2, 5, 3),
                from(var(), 1, 2, "abc", 5, 3).where(var().ne("abc")).select(var()).fetch());
    }

    @Test
    public void various3() {
        NumberPath<Integer> num = Expressions.numberPath(Integer.class, "num");
        assertEquals(Arrays.asList(1, 2, 3),
                from(num, 1, 2, 3, 4).where(num.lt(4)).select(num).fetch());
    }

}
