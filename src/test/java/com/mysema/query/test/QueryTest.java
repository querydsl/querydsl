package com.mysema.query.test;

import static com.mysema.query.grammar.Grammar.*;
import static com.mysema.query.test.domain.Domain.*;

import org.junit.Test;



/**
 * QueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QueryTest extends QueryBase{
    
    @Test
    public void testSimple(){
        from(cat);
        from(cat,cust).where(gt(cat.name,cust.name().firstName));
        select(lower(cat.name)).from(cat).where(eq(substr(cat.name,0,2),"Mi"));
        select(upper(cat.name)).from(cat);
        select(concat(lower(cat.name),cat.mate().name)).from(cat);
    }
    
    // cats
    
    @Test
    public void testQueryCat1(){
//      from Cat as cat left join cat.kittens as kitten 
//          with kitten.bodyWeight > 10.0
        from(cat).leftJoin(cat.kittens().as(kitten))
            .with(gt(kitten.bodyWeight,10));            
    }
    
    @Test
    public void testQueryCat2(){
//      from Cat as cat inner join fetch cat.mate
//          left join fetch cat.kittens child left join fetch child.kittens
        from(cat).innerJoin(cat.mate())
            .leftJoin(cat.kittens().as(child)).leftJoin(child.kittens());
    }
   
    @Test
    public void testQueryCat3(){
//      from Cat as cat where cat.mate.name like '%s%'
        from(cat).where(like(cat.mate().name,"%s%"));
    }
    
    @Test
    public void testDomesticCat1(){
//      select cat.name from DomesticCat cat where cat.name like 'fri%'
        select(cat).from(cat).where(like(cat.name, "%fri%"));
    }
       
    // docs
   
    @Test
    public void testQueryDoc1(){
//    from Document doc fetch all properties where lower(doc.name) like '%cats%'
      from(doc).where(like(lower(doc.name),"%cats%"));
  }
    
    // customers
    
    @Test
    public void testCustomers(){
//        select cust.name.firstName from Customer as cust
        select(cust.name().firstName).from(cust);
    }
    
    

}
