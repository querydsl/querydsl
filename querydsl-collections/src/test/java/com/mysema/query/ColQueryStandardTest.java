/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mysema.commons.lang.Pair;
import com.mysema.query.animal.Cat;
import com.mysema.query.animal.QCat;
import com.mysema.query.collections.MiniApi;
import com.mysema.query.types.EBoolean;
import com.mysema.query.types.Expr;

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
    
    private QueryExecution standardTest = new QueryExecution(Module.COLLECTIONS, Target.MEM){        
        @Override
        protected Pair<Projectable,List<Expr<?>>> createQuery() {
            return Pair.of(
                (Projectable)MiniApi.from(cat, data).from(otherCat, data),
                Collections.<Expr<?>>emptyList());
        }
        @Override
        protected Pair<Projectable,List<Expr<?>>> createQuery(EBoolean filter) {
            return Pair.of(
                    (Projectable)MiniApi.from(cat, data).from(otherCat, data).where(filter),
                Collections.<Expr<?>>singletonList(cat.name));
        }              
    };
    
    @Test
    public void test(){        
        Cat kitten = data.get(0).getKittens().get(0);  
        standardTest.runArrayTests(cat.kittenArray, otherCat.kittenArray, kitten, new Cat());
        standardTest.runBooleanTests(cat.name.isNull(), otherCat.kittens.isEmpty());
        standardTest.runCollectionTests(cat.kittens, otherCat.kittens, kitten, new Cat());
        standardTest.runDateTests(cat.dateField, otherCat.dateField, date);
        standardTest.runDateTimeTests(cat.birthdate, otherCat.birthdate, birthDate);
        standardTest.runListTests(cat.kittens, otherCat.kittens, kitten, new Cat());
        standardTest.runMapTests(cat.kittensByName, otherCat.kittensByName, "Kitty", kitten, "NoName", new Cat());
        
        // int
        standardTest.runNumericCasts(cat.id, otherCat.id, 1);
        standardTest.runNumericTests(cat.id, otherCat.id, 1);
        
        standardTest.runStringTests(cat.name, otherCat.name, "Bob");
        standardTest.runTimeTests(cat.timeField, otherCat.timeField, time);
        standardTest.report();        
    }
        
}
