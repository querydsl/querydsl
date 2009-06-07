package com.mysema.query;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.collections.MiniApi;
import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

public class ColQueryStandardTest implements StandardTest {
    
    private static final List<Cat> data = Arrays.asList(
            new Cat("Bob", 1),
            new Cat("Ruth", 2),
            new Cat("Felix", 3),
            new Cat("Allen", 4),
            new Cat("Mary", 5)
    );
    
    private static QCat cat = new QCat("cat");
    
    private static QCat otherCat = new QCat("otherCat");
    
    @Test
    public void booleanFilters(){
        for (EBoolean f : StandardTestData.booleanFilters(cat.name.isNull(), otherCat.kittens.isEmpty())){
            System.out.println(f);
            MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat.name);
        }
    }

    @Test
    public void numericFilters(){
        for (EBoolean f : StandardTestData.numericFilters(cat.id, otherCat.id, 1)){
            System.out.println(f);
            MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat.name);
        }
    }

    @Test
    public void numericMatchingFilters(){
        for (EBoolean f : StandardTestData.numericMatchingFilters(cat.id, otherCat.id, 1)){
            System.out.println(f);
            assertTrue(f + " failed", !MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat.name).isEmpty());
        }
    }

    @Test
    public void numericProjections(){
        for (ENumber<?> num : StandardTestData.numericProjections(cat.id, otherCat.id, 1)){
            System.out.println(num);
            MiniApi.from(cat, data).from(otherCat, data).list(num);
        }
    }
    
    @Test
    public void numericCasts(){
        for (ENumber<?> num : StandardTestData.numericCasts(cat.id, otherCat.id, 1)){
            System.out.println(num);
            MiniApi.from(cat, data).from(otherCat, data).list(num);
        }
    }

    @Test
    public void stringFilters(){
        for (EBoolean f : StandardTestData.stringFilters(cat.name, otherCat.name, "Bob")){
            System.out.println(f);
            MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat.name);
        }
    }

    @Test
    public void stringMatchingFilters(){
        for (EBoolean f : StandardTestData.stringMatchingFilters(cat.name, otherCat.name, "Bob")){
            System.out.println(f);
            assertTrue(f + " failed", !MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat.name).isEmpty());
        }
    }

    @Test
    public void stringProjections(){               
        for (EString str : StandardTestData.stringProjections(cat.name, otherCat.name, "Bob")){
            System.out.println(str);
            MiniApi.from(cat, data).from(otherCat, data).list(str);
        }
    }

    @Test
    @Ignore
    public void listProjections() {
        // FIXME : requires replacement of Janino
        for (Expr<?> pr : StandardTestData.listProjections(cat.kittens, otherCat.kittens, new Cat())){
            System.out.println(pr);
            MiniApi.from(cat, data).from(otherCat, data).list(pr);
        }        
    }
    
    @Test
    public void listFilters() {
        for (EBoolean filter : StandardTestData.listFilters(cat.kittens, otherCat.kittens, new Cat())){
            System.out.println(filter);
            MiniApi.from(cat, data).from(otherCat, data).where(filter).list(cat, otherCat);
        }        
    }

    @Test
    @Ignore
    public void mapProjections() {
        // FIXME : requires replacement of Janino
        for (Expr<?> pr : StandardTestData.mapProjections(cat.kittensByName, otherCat.kittensByName, "Bob", new Cat())){
            System.out.println(pr);
            MiniApi.from(cat, data).from(otherCat, data).list(pr);
        }           
    }
    
    @Test
    public void mapFilters() {
        for (EBoolean f : StandardTestData.mapFilters(cat.kittensByName, otherCat.kittensByName, "Bob", new Cat())){
            System.out.println(f);
            MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat, otherCat);
        }           
    }

}
