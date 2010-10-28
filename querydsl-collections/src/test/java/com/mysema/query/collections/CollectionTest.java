package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CollectionTest {
    
    private final QCat cat = new QCat("cat");
    
    private final QCat other = new QCat("other");
    
    private List<Cat> cats;
    
    @Before
    public void setUp(){
        Cat cat1 = new Cat("1");
        cat1.setKittens(Arrays.asList(cat1));
        Cat cat2 = new Cat("2");
        cat2.setKittens(Arrays.asList(cat1, cat2));
        Cat cat3 = new Cat("3");
        cat3.setKittens(Arrays.asList(cat1, cat2, cat3));
        Cat cat4 = new Cat("4");
        cat4.setKittens(Arrays.asList(cat1, cat2, cat3, cat4));
        
        cats = Arrays.asList(cat1, cat2, cat3, cat4);
    }
    
    @Test
    public void Join(){
        assertEquals("4", MiniApi.from(cat, cats).innerJoin(cat.kittens, other).where(other.name.eq("4")).uniqueResult(cat.name));
    }
    
    @Test
    public void Any(){
        assertEquals("4", MiniApi.from(cat, cats).where(cat.kittens.any().name.eq("4")).uniqueResult(cat.name));
    }
    
    @Test
    public void Any2(){
        assertEquals(4, MiniApi.from(cat, cats).where(cat.kittens.any().name.isNotNull()).count());
    }
    
    @Test
    @Ignore
    public void Any3(){
        // TODO : support multiple levels of any usage
        assertEquals(4, MiniApi.from(cat, cats).where(cat.kittens.any().name.isNotNull(), cat.kittens.any().kittens.any().isNotNull()).count());
    }
    
}
