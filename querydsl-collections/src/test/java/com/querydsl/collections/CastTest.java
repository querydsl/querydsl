package com.querydsl.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class CastTest extends AbstractQueryTest {

    @Test
    public void parents() {
        QCat cat = QAnimal.Constants.animal.as(QCat.class);
        assertEquals(QAnimal.Constants.animal, cat.getMetadata().getParent());
    }

    @Test
    public void cast() {
        assertEquals(Arrays.asList(c1, c2, c3, c4),
            query().from(QAnimal.Constants.animal, cats)
                .where(QAnimal.Constants.animal.as(QCat.class).breed.eq(0))
                .select(QAnimal.Constants.animal).fetch());
    }

    @Test
    public void property_dereference() {
         Cat cat = new Cat();
         cat.setEyecolor(Color.TABBY);
         assertEquals(Color.TABBY,
             CollQueryFactory.from(QAnimal.Constants.animal, cat)
                 .where(QAnimal.Constants.animal.instanceOf(Cat.class))
                 .select(QAnimal.Constants.animal.as(QCat.class).eyecolor).fetchFirst());
    }

}
