package com.mysema.query.collections;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExistsTest extends AbstractQueryTest{

    @Test
    public void Exists(){
        assertTrue(query().from(cat, cats).where(cat.name.eq("Bob")).exists());
    }

    @Test
    public void NotExists(){
        assertTrue(query().from(cat, cats).where(cat.name.eq("Bobby")).notExists());
    }


}
