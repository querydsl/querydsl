package com.mysema.query.test;

import static com.mysema.query.grammar.Grammar.*;
import static com.mysema.query.test.Domain.*;
import org.junit.Test;



/**
 * DomainTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class DomainTest extends QueryBase{
    
    @Test
    public void testQuery1(){
//        from Cat as cat 
//        left join cat.kittens as kitten 
//            with kitten.bodyWeight > 10.0
        from(cat)
        .leftJoin(cat.kittens.as(kitten))
            .with(gt(kitten.bodyWeight,10));
            
    }
    
    @Test
    public void testQuery2(){
//        from Cat as cat 
//            inner join fetch cat.mate
//            left join fetch cat.kittens child
//            left join fetch child.kittens
        from(cat)
            .innerJoin(cat.mate)
            .leftJoin()
    }

}
