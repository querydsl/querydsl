package com.mysema.query.grammar.hql;

import static com.mysema.query.grammar.HqlGrammar.*;
import static com.mysema.query.grammar.hql.domain.Domain.*;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.Types.ExprBoolean;
import com.mysema.query.grammar.hql.domain.DomesticCat;
import com.mysema.query.grammar.hql.domain.Payment;


/**
 * QueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class SimpleQueriesTest extends HqlQueryBase<SimpleQueriesTest>{
    
    private void expect(String expected){
        String result = toString();
        clear();
        assertEquals(expected, result);
    }
    
    @Test
    public void testPath(){
        assert cat.mate().name.toString().equals("cat.mate.name");
        assert cust.name().firstName.toString().equals("cust.name.firstName");
    }
       
    @Test
    public void testVarious(){
        from(cat);
        ExprBoolean be = cat.name.eq(cust.name().firstName); 
        where(be);
        with(be);
//        select(cat.name.as("cat_name")); // not allowed
        from(cat,cust).where(gt(cat.name,cust.name().firstName));
        select(cat.name.lower()).from(cat).where(cat.name.substring(0,2).eq("Mi"));
        select(cat.name.upper()).from(cat);
        select(concat(cat.name.lower(),cat.mate().name)).from(cat);
//        cat.as(company); // not allowed
//        asc(cust.name()); // not allowed
        cust.name().firstName.asc();
        cust.name().firstName.desc();
//        gt(cat, cat.mate()); // not allowed
//        lt(cat, cat.mate()); // not allowed
//        goe(cat, cat.mate()); // not allowed
//        loe(cat, cat.mate()); // not allowed             
    }
    
    @Test
    public void testOperations(){
        gt(kitten.bodyWeight, 10);
        kitten.bodyWeight.lt(10);
        goe(kitten.bodyWeight, 10);
        kitten.bodyWeight.loe(10);
        
        gt(cat.name, "ABC");        
        cust.name().firstName.lt("Albert");
        cust.name().firstName.lower();
        cust.name().firstName.upper();
        
        cat.name.isnull();
        cat.name.isnotnull();
    }
    
    @Test
    public void testCat1(){
//      from Cat as cat left join cat.kittens as kitten 
//          with kitten.bodyWeight > 10.0
        from(cat).leftJoin(cat.kittens.as(kitten))
            .with(gt(kitten.bodyWeight,10));     
        expect("from Cat cat\nleft join cat.kittens as kitten\n"+
           "with kitten.bodyWeight > :a1");
    }
    
    @Test
    public void testCat2(){
//      from Cat as cat inner join fetch cat.mate
//          left join fetch cat.kittens child left join fetch child.kittens
        from(cat).innerJoin(cat.mate())
            .leftJoin(cat.kittens.as(child)).leftJoin(child.kittens);
        expect("from Cat cat\ninner join cat.mate\n"+
           "left join cat.kittens as child\nleft join child.kittens");
    }
   
    @Test
    public void testCat3(){
//      from Cat as cat where cat.mate.name like '%s%'
        from(cat).where(cat.mate().name.like("%s%"));
        expect("from Cat cat\nwhere cat.mate.name like :a1");
    }
    
    @Test
    public void testCat4(){
//      from Cat cat where cat.alive = true
        from(cat).where(cat.alive);
        expect("from Cat cat\nwhere cat.alive");          
    }
    
    @Test
    public void testCat5(){
        from(cat).where(cat.alive.eq(true));
        expect("from Cat cat\nwhere cat.alive = :a1");        
                
//        from Cat cat where cat.kittens.size > 0
        // TODO
        
//        from Cat cat where size(cat.kittens) > 0
        // TODO
    }
    
    @Test
    public void testCat6(){
//        select mother from Cat as mother, Cat as kit
//        where kit in elements(foo.kittens)
        // TODO
    }
    
    @Test
    public void testDomesticCat1(){
//      select cat.name from DomesticCat cat where cat.name like 'fri%'
        select(cat.name).from(cat).where(cat.name.like("%fri%"));
        expect("select cat.name\nfrom Cat cat\nwhere cat.name like :a1");
    }
    
    @Test
    public void testDomesticCat2(){
//      from Cat cat where cat.class = DomesticCat        
        from(cat).where(cat.typeOf(DomesticCat.class));
        expect("from Cat cat\nwhere cat.class = :a1");
    }
    
    @Test
    public void testDomesticCat3(){
//      from DomesticCat cat where cat.name between 'A' and 'B'
        from(cat).where(cat.name.between("A","B"));
        expect("from Cat cat\nwhere cat.name between :a1 and :a2");        
    }    

    @Test
    public void testDomesticCat4(){
//      from DomesticCat cat where cat.name in ( 'Foo', 'Bar', 'Baz' )
        from(cat).where(cat.name.in("Foo","Bar","Baz"));
        expect("from Cat cat\nwhere cat.name in (:a1)");
    }    

    @Test
    public void testDomesticCat5(){
//      from DomesticCat cat where cat.name not between 'A' and 'B'
        from(cat).where(not(cat.name.between("A","B")));
        expect("from Cat cat\nwhere not cat.name between :a1 and :a2");           
    }    

    @Test
    public void testDomesticCat6(){
        from(doc).where(doc.validTo.between(new Date(), new Date()));
    }    

    @Test
    public void testDomesticCat7(){
    //  from DomesticCat cat where cat.name not in ( 'Foo', 'Bar', 'Baz' )
        from(cat).where(not(cat.name.in("Foo","Bar","Baz")));
        expect("from Cat cat\nwhere not cat.name in (:a1)");        
    }
       
    @Test
    public void testQueryDoc1(){
//      from Document doc fetch all properties where lower(doc.name) like '%cats%'
        from(doc).where(doc.name.lower().like("%cats%"));
        expect("from Document doc\nwhere lower(doc.name) like :a1");
        
        from(doc).where(doc.validTo.after(new Date()));
        from(doc).where(doc.validTo.before(new Date()));
  }
    
    @Test
    public void testCustomers(){
//      select cust.name.firstName from Customer cust
        select(cust.name().firstName).from(cust);
        expect("select cust.name.firstName\nfrom Customer cust");
    }
    
    @Test
    public void testAuditLog1(){
//      from AuditLog log, Payment payment 
//          where log.item.class = 'Payment' and log.item.id = payment.id
        from(log,payment)
            .where(log.item().typeOf(Payment.class),log.item().id.eq(payment.id));
        expect("from AuditLog log, Payment payment\n" +
           "where log.item.class = :a1 and log.item.id = payment.id");
    }
    
    @Test
    public void testOrder1(){
//        from Order order where maxindex(order.items) > 100
//        from(order).where(gt(maxindex(order.items()),100));
    }
    
    @Test
    public void testOrder2(){
//        from Order order where minelement(order.items) > 10000
//        from(order).where(gt(minelement(order.items()),10000));
    }
    
    @Test
    public void testOrder3(){
//        from Order order where order.items[0].id = 1234
        
//        from(order).where(eq(order.items(0).id,1234l));
//        expect("from Order order\nwhere order.items[0].id = :a1");
    }
    
    

}
