package com.mysema.query;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.mysema.query.animal.Cat;
import com.mysema.query.animal.QCat;
import com.mysema.query.collections.EvaluatorFactory;
import com.mysema.query.collections.MiniApi;

public class LoadTest {
    
    private QCat cat = QCat.cat;
    
    @Test
    public void creation(){
        System.out.println("Evaluator creation #1");
        for (int i = 0; i < 5; i++){
            long s = System.currentTimeMillis();
            EvaluatorFactory.DEFAULT.create(Collections.singletonList(cat), cat.name.startsWith("Bob"));
            long e = System.currentTimeMillis();
            System.out.println(" " + (e-s)+"ms");    
        }        
        System.out.println();
        
        System.out.println("Evaluator creation #2");
        for (int i = 0; i < 5; i++){
            long s = System.currentTimeMillis();
            EvaluatorFactory.DEFAULT.create(Collections.singletonList(cat), cat.name.startsWith("Bob" + i));
            long e = System.currentTimeMillis();
            System.out.println(" " + (e-s)+"ms");    
        }        
        System.out.println();
    }
    
    @Test
    public void test(){
        List<Cat> data = new ArrayList<Cat>(5000);
        for (int i = 0; i < 1000; i++){
            data.addAll(Arrays.asList(
                    new Cat("Bob" + i),
                    new Cat("Ruth" + i),
                    new Cat("Felix" + i),
                    new Cat("Allen" + i),
                    new Cat("Mary" + i)
            ));
        }
        
        // #1
        System.out.println("Querydsl iteration");
        for (int i = 0; i < 5; i++){
            long s1 = System.currentTimeMillis();
            List<Cat> bobs1 = MiniApi.from(cat, data).where(cat.name.startsWith("Bob")).list(cat);
            assertEquals(1000, bobs1.size());
            long e1 = System.currentTimeMillis();
            System.out.println(" " + (e1-s1)+"ms");
        }
        System.out.println();
        
        // #2
        System.out.println("Normal iteration");
        for (int i = 0; i < 5; i++){
            long s2 = System.currentTimeMillis();
            List<Cat> bobs2 = new ArrayList<Cat>();
            for (Cat c : data){
                if (c.getName().startsWith("Bob")) bobs2.add(c);
            }
            assertEquals(1000, bobs2.size());
            long e2 = System.currentTimeMillis();
            System.out.println(" " + (e2-s2)+"ms");    
        }        
        System.out.println();
    }

}
