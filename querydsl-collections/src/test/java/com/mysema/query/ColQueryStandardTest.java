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

import com.mysema.query.collections.MiniApi;
import com.mysema.query.collections.domain.Cat;
import com.mysema.query.collections.domain.QCat;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

public class ColQueryStandardTest {
    
    private final QCat cat = new QCat("cat");
    
    private final QCat otherCat = new QCat("otherCat");
    
    private final List<Cat> data = Arrays.asList(
            new Cat("Bob", 1),
            new Cat("Ruth", 2),
            new Cat("Felix", 3),
            new Cat("Allen", 4),
            new Cat("Mary", 5)
    );
    
    private StandardTest standardTest = new StandardTest(){
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
        standardTest.booleanTests(cat.name.isNull(), otherCat.kittens.isEmpty());
        standardTest.collectionTests(cat.kittens, otherCat.kittens, kitten, new Cat());
//        testData.dateTests(null, null, null);
        standardTest.dateTimeTests(cat.birthdate, otherCat.birthdate, new Date());
        standardTest.listTests(cat.kittens, otherCat.kittens, kitten, new Cat());
        standardTest.mapTests(cat.kittensByName, otherCat.kittensByName, "Kitty", kitten, "NoName", new Cat());
        standardTest.numericCasts(cat.id, otherCat.id, 1);
        standardTest.numericTests(cat.id, otherCat.id, 1);
        standardTest.stringTests(cat.name, otherCat.name, "Bob");
//        testData.timeTests(null, null, null);
        standardTest.report();        
    }
        

}
