/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.animal.Cat;
import com.mysema.query.animal.QCat;

public class ColDeleteClauseTest {

    @Test
    public void testExecute() {
        QCat cat = QCat.cat;
        List<Cat> cats = new ArrayList<Cat>(Arrays.asList(new Cat("Ann"), new Cat("Bob"), new Cat("John"), new Cat("Carl")));
        
        ColDeleteClause<Cat> deleteClause = new ColDeleteClause<Cat>(cat, cats);
        deleteClause.where(cat.name.eq("Bob"));
        assertEquals(1, deleteClause.execute());
        
        assertEquals(3, cats.size());
        assertEquals("Ann", cats.get(0).getName());
        assertEquals("John", cats.get(1).getName());
        assertEquals("Carl", cats.get(2).getName());
    }

}
