package com.mysema.query;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.collections.MiniApi;
import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;

public class ColQueryStandardTest {
    
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
    public void stringProjections(){               
        for (EString str : TestExprs.getProjectionsForString(cat.name, otherCat.name, "Bob")){
            System.out.println(str);
            MiniApi.from(cat, data).from(otherCat, data).list(str);
        }
    }
    
    @Test
    public void numericPojections(){
        for (ENumber<?> num : TestExprs.getProjectionsForNumber(cat.id, otherCat.id, 1)){
            System.out.println(num);
            MiniApi.from(cat, data).from(otherCat, data).list(num);
        }
    }
    
    @Test
    public void stringFilters(){
        for (EBoolean f : TestFilters.getFiltersForString(cat.name, otherCat.name, "Bob")){
            System.out.println(f);
            MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat.name);
        }
    }
    
    @Test
    public void matchingStringFilters(){
        for (EBoolean f : TestFilters.getMatchingFilters(cat.name, otherCat.name, "Bob")){
            System.out.println(f);
            assertTrue(!MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat.name).isEmpty());
        }
    }
    
    @Test
    public void booleanFilters(){
        for (EBoolean f : TestFilters.getFiltersForBoolean(cat.name.isNull(), otherCat.kittens.isEmpty())){
            System.out.println(f);
            MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat.name);
        }
    }
    
    @Test
    public void numericFilters(){
        for (EBoolean f : TestFilters.getFiltersForNumber(cat.id, otherCat.id, 1)){
            System.out.println(f);
            MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat.name);
        }
    }
    
    @Test
    public void matchingNumericFilters(){
        for (EBoolean f : TestFilters.getMatchingFilters(cat.id, otherCat.id, 1)){
            System.out.println(f);
            assertTrue(!MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat.name).isEmpty());
        }
    }

}
