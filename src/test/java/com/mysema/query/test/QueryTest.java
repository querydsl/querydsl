package com.mysema.query.test;

import static com.mysema.query.grammar.Grammar.*;
import static com.mysema.query.test.domain.Instances.*;

import java.util.Date;

import org.junit.Test;

import com.mysema.query.grammar.Types.BooleanExpr;
import com.mysema.query.test.domain.DomesticCat;
import com.mysema.query.test.domain.Payment;



/**
 * QueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QueryTest extends QueryBase{
    
    @Test
    public void testPath(){
        assert cat.mate().name.toString().equals("cat.mate.name");
        assert cust.name().firstName.toString().equals("cust.name.firstName");
    }
       
    @Test
    public void testVarious(){
        from(cat);
        BooleanExpr be = eq(cat.name,cust.name().firstName); 
        where(be);
        with(be);
//        select(cat.name.as("cat_name")); // not allowed
        from(cat,cust).where(gt(cat.name,cust.name().firstName));
        select(lower(cat.name)).from(cat).where(eq(substr(cat.name,0,2),"Mi"));
        select(upper(cat.name)).from(cat);
        select(concat(lower(cat.name),cat.mate().name)).from(cat);
        cat.as(kitten);
//        cat.as(company); // not allowed
//        asc(cust.name()); // not allowed
        asc(cust.name().firstName);
        desc(cust.name().firstName);
//        gt(cat, cat.mate()); // not allowed
//        lt(cat, cat.mate()); // not allowed
//        goe(cat, cat.mate()); // not allowed
//        loe(cat, cat.mate()); // not allowed
        
        
    }
    
    @Test
    public void testOperations(){
        gt(kitten.bodyWeight, 10);
        lt(kitten.bodyWeight, 10);
        goe(kitten.bodyWeight, 10);
        loe(kitten.bodyWeight, 10);
        
        gt(cat.name, "ABC");        
        lt(cust.name().firstName, "Albert");
        lower(cust.name().firstName);
        upper(cust.name().firstName);
    }
    
    @Test
    public void testCat1(){
//      from Cat as cat left join cat.kittens as kitten 
//          with kitten.bodyWeight > 10.0
        from(cat).leftJoin(cat.kittens().as(kitten))
            .with(gt(kitten.bodyWeight,10));            
    }
    
    @Test
    public void testCat2(){
//      from Cat as cat inner join fetch cat.mate
//          left join fetch cat.kittens child left join fetch child.kittens
        from(cat).innerJoin(cat.mate())
            .leftJoin(cat.kittens().as(child)).leftJoin(child.kittens());
    }
   
    @Test
    public void testCat3(){
//      from Cat as cat where cat.mate.name like '%s%'
        from(cat).where(like(cat.mate().name,"%s%"));
    }
    
    @Test
    public void testCat4(){
//      from Cat cat where cat.alive = true
        from(cat).where(cat.alive);
        from(cat).where(eq(cat.alive,true));
                
//        from Cat cat where cat.kittens.size > 0
        // TODO
        
//        from Cat cat where size(cat.kittens) > 0
        // TODO
    }
    
    @Test
    public void testCat5(){
//        select mother from Cat as mother, Cat as kit
//        where kit in elements(foo.kittens)
        // TODO
    }
    
    @Test
    public void testDomesticCat1(){
//      select cat.name from DomesticCat cat where cat.name like 'fri%'
        select(cat).from(cat).where(like(cat.name, "%fri%"));
    }
    
    @Test
    public void testDomesticCat2(){
//      from Cat cat where cat.class = DomesticCat        
        from(cat).where(typeOf(cat,DomesticCat.class));
    }
    
    @Test
    public void testDomesticCat3(){
//      from DomesticCat cat where cat.name between 'A' and 'B'
        from(cat).where(between(cat.name, "A","B"));
        
//      from DomesticCat cat where cat.name in ( 'Foo', 'Bar', 'Baz' )
        from(cat).where(in(cat.name, "Foo","Bar","Baz"));
        
//      from DomesticCat cat where cat.name not between 'A' and 'B'
        from(cat).where(not(between(cat.name,"A","B")));
        from(doc).where(between(doc.validTo,new Date(), new Date()));
        
//      from DomesticCat cat where cat.name not in ( 'Foo', 'Bar', 'Baz' )
        from(cat).where(not(in(cat.name, "Foo","Bar","Baz")));
    }
       
    @Test
    public void testQueryDoc1(){
//      from Document doc fetch all properties where lower(doc.name) like '%cats%'
        from(doc).where(like(lower(doc.name),"%cats%"));
      
        from(doc).where(after(doc.validTo, new Date()));
        from(doc).where(before(doc.validTo, new Date()));
  }
    
    @Test
    public void testCustomers(){
//      select cust.name.firstName from Customer as cust
        select(cust.name().firstName).from(cust);
    }
    
    @Test
    public void testAuditLog1(){
//      from AuditLog log, Payment payment 
//          where log.item.class = 'Payment' and log.item.id = payment.id
        from(log,payment)
            .where(typeOf(log.item(),Payment.class),eq(log.item().id,payment.id));
    }
    
    @Test
    public void testOrder1(){
//        from Order order where maxindex(order.items) > 100
        // TODO
    }
    
    @Test
    public void testOrder2(){
//        from Order order where minelement(order.items) > 10000
        // TODO
    }
    
    

}
