package com.mysema.query.grammar.hql;

import static com.mysema.query.Domain1.catalog;
import static com.mysema.query.Domain1.item;
import static com.mysema.query.Domain1.order;
import static com.mysema.query.Domain1.price;
import static com.mysema.query.Domain1.product;
import static com.mysema.query.grammar.HqlGrammar.*;

import org.junit.Test;

import com.mysema.query.Domain2;
import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.hql.domain.Catalog;
import com.mysema.query.grammar.hql.domain.Customer;
import com.mysema.query.grammar.hql.domain.Product;

/**
 * ComplexQueriesTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class ComplexQueriesTest extends HqlQueryBase<ComplexQueriesTest>{
    
    private Domain2.Association a = new Domain2.Association("a");
    private Customer c = new Customer();
    private Domain2.Tag g = new Domain2.Tag("g");    
    private Domain2.Thing h = new Domain2.Thing("h");    
    private long minAmount = 0;    
    private Product p = new Product();    
    private Domain2.Thing t = new Domain2.Thing("t");
    
    @Test
    public void testExample1(){
//        select order.id, sum(price.amount), count(item)
//        from Order as order
//            join order.lineItems as item
//            join item.product as product,
//            Catalog as catalog
//            join catalog.prices as price
//        where order.paid = false
//            and order.customer = :customer
//            and price.product = product
//            and catalog.effectiveDate < sysdate
//            and catalog.effectiveDate >= all (
//                select cat.effectiveDate 
//                from Catalog as cat
//                where cat.effectiveDate < sysdate
//            )
//        group by order
//        having sum(price.amount) > :minAmount
//        order by sum(price.amount) desc
        
        select(order.id, sum(price.amount), count(item))
        .from(order)
            .join(order.lineItems.as(item))
            .join(item.product.as(product))
            .join(catalog).join(catalog.prices.as(price))
        .where(order.paid.eq(false),
            order.customer().eq(c),
            price.product().eq(p),
            catalog.effectiveDate.lt(sysdate()), // lt as static method
            catalog.effectiveDate.goe(sysdate())) // goe as static method
        .groupBy(order)
        .having(sum(price.amount).gt(minAmount))
        .orderBy(sum(price.amount).desc());            
    }
    
    @Test
    public void testExample2(){
//        select order.id, sum(price.amount), count(item)
//        from Order as order
//            join order.lineItems as item
//            join item.product as product,
//            Catalog as catalog
//            join catalog.prices as price
//        where order.paid = false
//            and order.customer = :customer
//            and price.product = product
//            and catalog = :currentCatalog
//        group by order
//        having sum(price.amount) > :minAmount
//        order by sum(price.amount) desc        
        Customer c = new Customer();
        Product p = new Product();
        Catalog currentCatalog = new Catalog();
        long minAmount = 0l;
        
        select(order.id, sum(price.amount), count(item))
        .from(order)
            .innerJoin(order.lineItems.as(item))
            .innerJoin(item.product.as(product))
            .join(catalog)
            .innerJoin(catalog.prices.as(price))
        .where(not(order.paid),
            order.customer.eq(c),
            price.product.eq(p),
            catalog.eq(currentCatalog))
        .groupBy(order)
        .having(sum(price.amount).gt(minAmount))
        .orderBy(sum(price.amount).desc());
    }
    
    @Test
    public void testExample3(){
//        select count(payment), status.name 
//        from Payment as payment 
//            join payment.currentStatus as status
//            join payment.statusChanges as statusChange
//        where payment.status.name <> PaymentStatus.AWAITING_APPROVAL
//            or (
//                statusChange.timeStamp = ( 
//                    select max(change.timeStamp) 
//                    from PaymentStatusChange change 
//                    where change.payment = payment
//                )
//                and statusChange.user <> :currentUser
//            )
//        group by status.name, status.sortOrder
//        order by status.sortOrder        
    }
    @Test
    public void testExample4(){
//        select count(payment), status.name 
//        from Payment as payment
//            join payment.currentStatus as status
//        where payment.status.name <> PaymentStatus.AWAITING_APPROVAL
//            or payment.statusChanges[ maxIndex(payment.statusChanges) ].user <> :currentUser
//        group by status.name, status.sortOrder
//        order by status.sortOrder
    }
    @Test
    public void testExample5(){
//        select account, payment
//        from Account as account
//            left outer join account.payments as payment
//        where :currentUser in elements(account.holder.users)
//            and PaymentStatus.UNPAID = isNull(payment.currentStatus.name, PaymentStatus.UNPAID)
//        order by account.type.sortOrder, account.accountNumber, payment.dueDate
    }
    @Test
    public void testExample6(){
        
//        select account, payment
//        from Account as account
//            join account.holder.users as user
//            left outer join account.payments as payment
//        where :currentUser = user
//            and PaymentStatus.UNPAID = isNull(payment.currentStatus.name, PaymentStatus.UNPAID)
//        order by account.type.sortOrder, account.accountNumber, payment.dueDate
    }
    
    @Test
    public void testQuery1(){
//        "select g._keyword, count(g._keyword) from " + Thing.class.getName()
//        + " h inner join h._tags as g where h._code in" + "(select t._code from "
//        + Association.class.getName() + " a " + "inner join a._thing as t "
//        + "where a._association = :association "
//        + "and t._isHidden = :hidden) group by g._keyword order by count(g._keyword) desc");
        
//        select(g._keyword, count(g._keyword))
//        .from(h).innerJoin(h._tags.as(g))
//        .where(h._code.in(
//                select(t._code).from(a
        
    }
    
    public void testQuery2(){
//        "select g._keyword, count(g._keyword) from "
//        + Thing.class.getName()
//        + " h inner join h._tags as g where h._code in"
//        + "(select t._code from "
//        + Association.class.getName()
//        + " a "
//        + "inner join a._thing as t "
//        + "where a._association = :association "
//        + "and t._isHidden = :hidden and t._timeStamp > :lastweek) 
//        group by g._keyword order by count(g._keyword) desc");
    }

}
