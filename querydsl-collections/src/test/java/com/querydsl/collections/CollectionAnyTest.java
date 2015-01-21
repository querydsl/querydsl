package com.querydsl.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CollectionAnyTest extends AbstractQueryTest {

    @Test
    public void Any_In_Projection() {
        Cat a = new Cat("a");
        Cat aa = new Cat("aa");
        Cat ab = new Cat("ab");
        Cat ac = new Cat("ac");
        a.setKittens(Arrays.asList(aa,ab,ac));

        Cat b = new Cat("b");
        Cat ba = new Cat("ba");
        Cat bb = new Cat("bb");
        b.setKittens(Arrays.asList(ba, bb));

        QCat cat = QCat.cat;
        List<Cat> kittens = CollQueryFactory.from(cat, Arrays.asList(a,b)).list(cat.kittens.any());
        assertEquals(Arrays.asList(aa,ab,ac,ba,bb), kittens);
    }

    @Test
    public void Any_In_Projection2() {
        Cat a = new Cat("a");
        Cat aa = new Cat("aa");
        Cat ab = new Cat("ab");
        Cat ac = new Cat("ac");
        a.setKittens(Arrays.asList(aa,ab,ac));

        Cat b = new Cat("b");
        Cat ba = new Cat("ba");
        Cat bb = new Cat("bb");
        b.setKittens(Arrays.asList(ba, bb));

        QCat cat = QCat.cat;
        List<String> kittens = CollQueryFactory.from(cat, Arrays.asList(a,b))
                .list(cat.kittens.any().name);
        assertEquals(Arrays.asList("aa","ab","ac","ba","bb"), kittens);
    }

    @Test
    public void Any_In_Where_And_Projection() {
        Cat a = new Cat("a");
        Cat aa = new Cat("aa");
        Cat ab = new Cat("ab");
        Cat ac = new Cat("ac");
        a.setKittens(Arrays.asList(aa,ab,ac));

        Cat b = new Cat("b");
        Cat ba = new Cat("ba");
        Cat bb = new Cat("bb");
        b.setKittens(Arrays.asList(ba, bb));

        QCat cat = QCat.cat;
        List<Cat> kittens = CollQueryFactory.from(cat, Arrays.asList(a,b))
                .where(cat.kittens.any().name.startsWith("a"))
                .list(cat.kittens.any());

        assertEquals(Arrays.asList(aa,ab,ac), kittens);
    }

    @Test
    public void Any_In_Where_And_Projection2() {
        Cat a = new Cat("a");
        Cat aa = new Cat("aa");
        Cat ab = new Cat("ab");
        Cat ac = new Cat("ac");
        a.setKittens(Arrays.asList(aa,ab,ac));

        Cat b = new Cat("b");
        Cat ba = new Cat("ba");
        Cat bb = new Cat("bb");
        b.setKittens(Arrays.asList(ba, bb));

        QCat cat = QCat.cat;
        List<String> kittens = CollQueryFactory.from(cat, Arrays.asList(a,b))
                .where(cat.kittens.any().name.startsWith("a"))
                .list(cat.kittens.any().name);

        assertEquals(Arrays.asList("aa","ab","ac"), kittens);
    }

}
