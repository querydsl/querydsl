package com.querydsl.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class CastTest extends AbstractQueryTest {

    @Test
    public void parents() {
        QCat cat = QAnimal.animal.as(QCat.class);
        assertEquals(QAnimal.animal, cat.getMetadata().getParent());
    }

    @Test
    public void cast() {
        assertEquals(Arrays.asList(c1, c2, c3, c4),
                query().from(QAnimal.animal, cats)
                .where(QAnimal.animal.as(QCat.class).breed.eq(0))
                .select(QAnimal.animal).fetch());
    }

    @Test
    public void property_dereference() {
        Cat cat = new Cat();
        cat.setEyecolor(Color.TABBY);
        assertEquals(Color.TABBY,
                CollQueryFactory.from(QAnimal.animal, cat)
                .where(QAnimal.animal.instanceOf(Cat.class))
                .select(QAnimal.animal.as(QCat.class).eyecolor).fetchFirst());
    }

}
