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
    
    private static QCat cat = new QCat("cat");
    
    private static final List<Cat> data = Arrays.asList(
            new Cat("Bob", 1),
            new Cat("Ruth", 2),
            new Cat("Felix", 3),
            new Cat("Allen", 4),
            new Cat("Mary", 5)
    );
    
    private static QCat otherCat = new QCat("otherCat");
    
    private StandardTest testData = new StandardTest(){
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
        testData.booleanTests(cat.name.isNull(), otherCat.kittens.isEmpty());
        testData.collectionTests(cat.kittens, otherCat.kittens, kitten);
//        testData.dateTests(null, null, null);
        testData.dateTimeTests(cat.birthdate, otherCat.birthdate, new Date());
        testData.listTests(cat.kittens, otherCat.kittens, kitten);
        testData.mapTests(cat.kittensByName, otherCat.kittensByName, "Kitty", kitten);
        testData.numericCasts(cat.id, otherCat.id, 1);
        testData.numericTests(cat.id, otherCat.id, 1);
        testData.stringTests(cat.name, otherCat.name, "Bob");
//        testData.timeTests(null, null, null);
        testData.report();        
    }
        

}
