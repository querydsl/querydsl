package com.mysema.query.collections;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.animal.Cat;
import com.mysema.query.animal.QCat;

public class ColUpdateClauseTest {

    @Test
    public void testExecute() {
        QCat cat = QCat.cat;
        List<Cat> cats = Arrays.asList(new Cat("Ann"), new Cat("Bob"), new Cat("John"), new Cat("Carl"));
        
        ColUpdateClause<Cat> updateClause = new ColUpdateClause<Cat>(cat, cats);
        updateClause.where(cat.name.eq("Bob"));
        updateClause.set(cat.name, "Bobby");
        assertEquals(1, updateClause.execute());
        
        assertEquals("Bobby", cats.get(1).getName());
    }

}
