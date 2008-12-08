package com.mysema.query.collections;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.grammar.types.Expr;


/**
 * ColQueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class ColQueryTest {
    Cat c1 = new Cat("Kitty");
    Cat c2 = new Cat("Bob");
    Cat c3 = new Cat("Alex");
    Cat c4 = new Cat("Francis");
    
    QCat cat = new QCat("cat");
    
    TestQuery last;
            
    @Test
    public void test(){
        query().select(cat.name);
        assertTrue(last.res.size() == 4);
        
        query().select(cat.kittens);
        assertTrue(last.res.size() == 4);
        
        query().where(cat.kittens.size().gt(0)).select(cat.name);
        assertTrue(last.res.size() == 4);
        
        query().where(cat.name.eq("Kitty")).select(cat.name);
        assertTrue(last.res.size() == 1);
        
        query().where(cat.name.like("Kitt%")).select(cat.name);
        assertTrue(last.res.size() == 1);
    }
    
    private TestQuery query(){
        last = new TestQuery().from(cat, c1, c2, c3, c4);
        return last;
    }
    
    private static class TestQuery extends ColQuery<TestQuery>{
        List<Object> res = new ArrayList<Object>();
        <RT> void select(final Expr<RT> projection){
            for (Object o : iterate(projection)){
                System.out.println(o);
                res.add(o);
            }
            System.out.println();
        }
    }
    

}
