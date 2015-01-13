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

import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.core.alias.Alias.alias;
import static com.querydsl.core.alias.Alias.var;
import static com.querydsl.collections.CollQueryFactory.from;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.alias.Alias;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;

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
    public void AliasVariations1() {
        // 1st
        QCat cat = new QCat("cat");
        for (String name : from(cat, cats).where(cat.kittens.size().gt(0)).list(cat.name)) {
            assertNotNull(name);
            System.out.println(name);
        }

        // 2nd
        Cat c = alias(Cat.class, "cat");
        for (String name : from(c, cats).where($(c.getKittens()).size().gt(0)).list($(c.getName()))) {
            assertNotNull(name);
            System.out.println(name);
        }

        // 2nd - variation 1
//        for (String name : from(c, cats).where($(c.getKittens().size()).gt(0))
//                .list(c.getName())) {
//            System.out.println(name);
//        }

    }

    @Test
    public void AliasVariations2() {
        // 1st
        QCat cat = new QCat("cat");
        for (String name : from(cat, cats).where(cat.name.matches("fri.*")).list(cat.name)) {
            assertNotNull(name);
            System.out.println(name);
        }

        // 2nd
        Cat c = alias(Cat.class, "cat");
        for (String name : from(c, cats).where($(c.getName()).matches("fri.*")).list($(c.getName()))) {
            assertNotNull(name);
            System.out.println(name);
        }
    }

    @Test
    public void Alias3() {
        QCat cat = new QCat("cat");
        Cat other = new Cat();
        Cat c = alias(Cat.class, "cat");

        // 1
        from(c, cats).where($(c.getBirthdate()).gt(new Date())).list($(c)).iterator();

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
             .list($(c.getName())).iterator();

        // 5
        from(c, cats).where($(c).eq(other)).list($(c)).iterator();

        // 6
        from(c, cats).where($(c.getKittens()).contains(other)).list($(c))
                .iterator();

        // 7
        from(c, cats).where($(c.getKittens().isEmpty())).list($(c)).iterator();

        // 8
        from(c, cats).where($(c.getName()).startsWith("B")).list($(c)).iterator();

        // 9
        from(c, cats).where($(c.getName()).upper().eq("MOE")).list($(c)).iterator();

        // 10
        assertNotNull($(c.getKittensByName()));
        assertNotNull($(c.getKittensByName().get("Kitty")));
        from(c, cats).where($(c.getKittensByName().get("Kitty")).isNotNull()).list(cat);

        // 11
//        try {
//            from(cat, cats).where(cat.mate.alive).list(cat);
//            fail("expected RuntimeException");
//        } catch (RuntimeException e) {
//            System.out.println(e.getMessage());
//            assertEquals("null in cat.mate.alive", e.getMessage());
//        }

        // 12
        // TestQuery querydsl = querydsl().from(cat, c1, c2).from(cat, c1, c2);
        // assertEquals(1, querydsl.getMetadata().getJoins().size());

    }

    @Test
    public void Various1() {
        StringPath str = new StringPath("str");
        for (String s : from(str, "a", "ab", "cd", "de").where(str.startsWith("a")).list(str)) {
            assertTrue(s.equals("a") || s.equals("ab"));
            System.out.println(s);
        }
    }

    @Test
    public void Various2() {
        for (Object o : from(var(), 1, 2, "abc", 5, 3).where(var().ne("abc")).list(var())) {
            int i = (Integer) o;
            assertTrue(i > 0 && i < 6);
            System.out.println(o);
        }
    }

    @Test
    public void Various3() {
        NumberPath<Integer> num = new NumberPath<Integer>(Integer.class, "num");
        for (Integer i : from(num, 1, 2, 3, 4).where(num.lt(4)).list(num)) {
            System.out.println(i);
        }
    }

}
