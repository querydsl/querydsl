/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mysema.query.animal.Cat;
import com.mysema.query.animal.QCat;
import com.mysema.query.collections.MiniApi;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

public class ColQueryStandardTest {
    
    private final Date birthDate = new Date();
    
    private final java.sql.Date date = new java.sql.Date(birthDate.getTime());
    
    private final java.sql.Time time = new java.sql.Time(birthDate.getTime());
    
    private final QCat cat = new QCat("cat");
    
    private final QCat otherCat = new QCat("otherCat");
    
    private final List<Cat> data = Arrays.asList(
            new Cat("Bob", 1, birthDate),
            new Cat("Ruth", 2, birthDate),
            new Cat("Felix", 3, birthDate),
            new Cat("Allen", 4, birthDate),
            new Cat("Mary", 5, birthDate)
    );
    
    private StandardTest standardTest = new StandardTest(Module.COLLECTIONS, Target.MEM){
        @Override
        public int executeFilter(EBoolean f){
            return MiniApi.from(cat, data).from(otherCat, data).where(f).list(cat.name).size();
        }
        @Override
        public int executeProjection(Expr<?> pr){
            return MiniApi.from(cat, data).from(otherCat, data).list(pr).size();
        }              
    };
    
    @Test
    public void test(){        
        Cat kitten = data.get(0).getKittens().get(0);  
        standardTest.arrayTests(cat.kittenArray, otherCat.kittenArray, kitten, new Cat());
        standardTest.booleanTests(cat.name.isNull(), otherCat.kittens.isEmpty());
        standardTest.collectionTests(cat.kittens, otherCat.kittens, kitten, new Cat());
        standardTest.dateTests(cat.dateField, otherCat.dateField, date);
        standardTest.dateTimeTests(cat.birthdate, otherCat.birthdate, birthDate);
        standardTest.listTests(cat.kittens, otherCat.kittens, kitten, new Cat());
        standardTest.mapTests(cat.kittensByName, otherCat.kittensByName, "Kitty", kitten, "NoName", new Cat());
        
        // int
        standardTest.numericCasts(cat.id, otherCat.id, 1);
        standardTest.numericTests(cat.id, otherCat.id, 1);
        
        standardTest.stringTests(cat.name, otherCat.name, "Bob");
        standardTest.timeTests(cat.timeField, otherCat.timeField, time);
        standardTest.report();        
    }
        
}
