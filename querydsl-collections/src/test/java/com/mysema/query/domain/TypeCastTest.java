package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.mysema.query.collections.ColQuery;
import com.mysema.query.collections.MiniApi;
import com.mysema.query.collections.impl.ColQueryImpl;
import com.mysema.query.domain.animal.Animal;
import com.mysema.query.domain.animal.Cat;
import com.mysema.query.domain.animal.QAnimal;
import com.mysema.query.domain.animal.QCat;

public class TypeCastTest {

    @Test
    public void cast() {
        QAnimal animal = QAnimal.animal;
        QCat cat = new QCat(animal.getMetadata());
        System.out.println(cat);

        ColQuery query = MiniApi.from(animal, Collections.<Animal> emptyList())
                .from(cat, Collections.<Cat> emptyList());
        assertEquals(1, ((ColQueryImpl) query).getMetadata().getJoins().size());
    }

}
