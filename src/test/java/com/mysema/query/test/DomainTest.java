package com.mysema.query.test;

import static com.mysema.query.Grammar.as;
import static com.mysema.query.Grammar.gt;
import static com.mysema.query.test.Domain.cat;
import static com.mysema.query.test.Domain.kitten;

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
        .leftJoin(as(cat.kittens,kitten))
            .with(gt(kitten.bodyWeight,10));
            
    }

}
