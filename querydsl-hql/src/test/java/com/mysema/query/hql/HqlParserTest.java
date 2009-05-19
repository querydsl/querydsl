/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static com.mysema.query.grammar.Grammar.avg;
import static com.mysema.query.grammar.Grammar.in;
import static com.mysema.query.grammar.Grammar.not;
import static com.mysema.query.grammar.GrammarWithAlias.$;
import static com.mysema.query.grammar.GrammarWithAlias.alias;
import static com.mysema.query.grammar.HqlGrammar.all;
import static com.mysema.query.grammar.HqlGrammar.distinct;
import static com.mysema.query.grammar.HqlGrammar.exists;
import static com.mysema.query.grammar.HqlGrammar.indices;
import static com.mysema.query.grammar.HqlGrammar.max;
import static com.mysema.query.grammar.HqlGrammar.maxindex;
import static com.mysema.query.grammar.HqlGrammar.notExists;
import static com.mysema.query.grammar.HqlGrammar.some;
import static com.mysema.query.grammar.HqlGrammar.sum;
import static com.mysema.query.grammar.HqlGrammar.sysdate;
import static com.mysema.query.grammar.QMath.div;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import org.hibernate.hql.ast.HqlParser;
import org.junit.Test;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;

import com.mysema.query.SearchResults;
import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.HqlGrammar;
import com.mysema.query.grammar.HqlJoinMeta;
import com.mysema.query.grammar.HqlOps;
import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.QMath;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.hql.HqlDomain.Cat;
import com.mysema.query.hql.HqlDomain.Color;
import com.mysema.query.hql.HqlDomain.DomesticCat;
import com.mysema.query.hql.HqlDomain.Formula;
import com.mysema.query.hql.HqlDomain.Parameter;
import com.mysema.query.hql.HqlDomain.Payment;



/**
 * HqlParserTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
// TODO : refactor this
public class HqlParserTest implements Constants{
    
	protected TestQuery q(){
		return new TestQuery();
	}
	
    @Test
    public void testBeforeAndAfter() throws RecognitionException, TokenStreamException{
        EComparable<java.util.Date> ed = catalog.effectiveDate;
        q().from(catalog)
            .where(
                ed.after(sysdate()), 
                ed.aoe(sysdate()),
                ed.before(sysdate()), 
                ed.boe(sysdate()))                
            .select(catalog).parse();
    }
    
    @Test
    public void testSum() throws RecognitionException, TokenStreamException{
        q().from(cat)
            .select(sum(cat.kittens.size())).parse();    
        
        q().from(cat)
            .where(sum(cat.kittens.size()).gt(0))
            .select(cat).parse();
        
//        from(cat)
//            .groupBy(cat.name)
//            .having(sum(cat.bodyWeight).gt(0))
//            .select(cat).parse();
    }
    
    /**
     * Section 9.2 - from *
     */
    @Test
    public void testDocoExamples92() throws Exception {       
//        parse( "from eg.Cat" );
        q().from(cat).parse();
        
//        parse( "from eg.Cat as cat" );
        q().from(cat).parse();
        
//        parse( "from eg.Cat cat" );
        q().from(cat).parse();
        
//        parse( "from Formula, Parameter" );
        q().from(form, param).parse();
        
//        parse( "from Formula as form, Parameter as param" );
        q().from(form, param).parse();
    }

    /**
     * Section 9.3 - Associations and joins *
     */
    @Test
    public void testDocoExamples93() throws Exception {
//        parse( "from eg.Cat as cat inner join cat.mate as mate left outer join cat.kittens as kitten" );
    	q().from(cat).innerJoin(cat.mate.as(mate)).leftJoin(cat.kittens.as(kitten)).parse();
        
//        parse( "from eg.Cat as cat left join cat.mate.kittens as kittens" );
    	q().from(cat).leftJoin(cat.mate.kittens.as(kittens)).parse();
        
//        parse( "from Formula form full join form.parameter param" );
        // HSQLDB doesn't support full join
//        from(form).fullJoin(form.parameter.as(param)).parse();
        
//        parse( "from eg.Cat as cat join cat.mate as mate left join cat.kittens as kitten" );
    	q().from(cat).join(cat.mate.as(mate)).leftJoin(cat.kittens.as(kitten)).parse();
        
//        parse( "from eg.Cat as cat\ninner join fetch cat.mate\nleft join fetch cat.kittens" );
    	q().from(cat).innerJoin(cat.mate).leftJoin(HqlJoinMeta.FETCH, cat.kittens).parse();
    }

    @Test
    public void testDocoExamples93_viaAlias() throws Exception {
        Cat c = alias(Cat.class, "cat");
        Cat k = alias(Cat.class, "kittens");
        Cat m = alias(Cat.class, "mate");
        
        Formula f = alias(Formula.class, "formula"); 
        Parameter p = alias(Parameter.class, "param");
        
//        parse( "from eg.Cat as cat inner join cat.mate as mate left outer join cat.kittens as kitten" );
        q().from($(c)).innerJoin($(c.getMate()).as($(m)))
            .leftJoin($(c.getKittens()).as($(k))).parse();
        
//        parse( "from eg.Cat as cat left join cat.mate.kittens as kittens" );
        q().from($(c)).leftJoin($(c.getMate().getKittens()).as($(k))).parse();
        
//        parse( "from Formula form full join form.parameter param" );
        // HSQLDB doesn't support full join
//        from($(f)).fullJoin($(f.getParameter()).as($(p))).parse();
        
//        parse( "from eg.Cat as cat join cat.mate as mate left join cat.kittens as kitten" );
        q().from($(c)).innerJoin($(c.getMate()).as($(m)))
            .leftJoin($(c.getKittens()).as($(k))).parse();
        
//        parse( "from eg.Cat as cat\ninner join fetch cat.mate\nleft join fetch cat.kittens" );
        q().from($(c)).innerJoin(HqlJoinMeta.FETCH, $(c.getMate()).as($(m)))
            .leftJoin(HqlJoinMeta.FETCH, $(c.getKittens()).as($(k))).parse();
    }
    
    /**
     * Section 9.4 - Select *
     */
    @Test
    public void testDocoExamples94() throws Exception {        
//        parse( "select mate from eg.Cat as cat inner join cat.mate as mate" );
    	q().select(mate).from(cat).innerJoin(cat.mate.as(mate)).parse();
        
//        parse( "select cat.mate from eg.Cat cat" );
    	q().select(cat.mate).from(cat).parse();
        
//        parse( "select elements(cat.kittens) from eg.Cat cat" );
    	q().select(cat.kittens).from(cat).parse();
        
//        parse( "select cat.name from eg.DomesticCat cat where cat.name like 'fri%'" );
    	q().select(cat.name).from(cat).where(cat.name.like("fri%")).parse();
        
//        parse( "select cust.name.firstName from Customer as cust" );
    	q().select(cust.name.firstName).from(cust).parse();
        
//        parse( "select mother, offspr, mate.name from eg.DomesticCat\n"
//                + " as mother inner join mother.mate as mate left outer join\n"
//                + "mother.kittens as offspr" );
    	q().select(mother, offspr, mate.name).from(mother)
            .innerJoin(mother.mate.as(mate)).leftJoin(mother.kittens.as(offspr)).parse();
        
//        parse( "select new Family(mother, mate, offspr)\n"
//                + "from eg.DomesticCat as mother\n"
//                + "join mother.mate as mate\n"
//                + "left join mother.kittens as offspr\n" );
    	q().select(new QFamily(mother, mate, offspr))
            .from(mother).innerJoin(mother.mate.as(mate))
            .leftJoin(mother.kittens.as(offspr)).parse();
    }

    /**
     * Section 9.5 - Aggregate functions *
     */
    @Test
    public void testDocoExamples95() throws Exception {
//        parse( "select avg(cat.weight), sum(cat.weight), max(cat.weight), count(cat)\n"
//                + "from eg.Cat cat" );
    	q().select(avg(cat.weight), sum(cat.weight), QMath.max(cat.weight), Grammar.count(cat)).from(cat).parse();
        
//        parse( "select cat, count( elements(cat.kittens) )\n"
//                + " from eg.Cat cat group by cat" );
        // NOTE : groupBy don't work properly in HSQLDB
//    	q().select(cat, Grammar.count(cat.kittens)).from(cat).groupBy(cat);
        
//        parse( "select distinct cat.name from eg.Cat cat" );
    	q().select(distinct(cat.name)).from(cat).parse();
        
//        parse( "select count(distinct cat.name), count(cat) from eg.Cat cat" );
    	q().select(Grammar.count(distinct(cat.name)), Grammar.count(cat)).from(cat).parse();
    }
    
    @Test
    public void test_own_DistinctEntities() throws Exception{
    	q().select(distinct(cat)).from(cat).innerJoin(cat.kittens.as(kitten)).parse();
    }

    /**
     * Section 9.6 - Polymorphism *
     */
    @Test
    public void testDocoExamples96() throws Exception {
//        parse( "from eg.Cat as cat" );
    	q().from(cat).parse();
        
//        parse( "from java.lang.Object o" );
//        parse( "from eg.Named n, eg.Named m where n.name = m.name" );
    	q().from(m,n).where(n.name.eq(m.name)).parse();
    }

    /**
     * Section 9.7 - Where *
     */
    @Test
    public void testDocoExamples97() throws Exception {
        // init deep path        
        account._owner()._pid();
        
//        parse( "from eg.Cat as cat where cat.name='Fritz'" );
        q().from(cat).where(cat.name.like("Fritz")).parse();
        
//        parse( "select foo\n"
//                + "from eg.Foo foo, eg.Bar bar\n"
//                + "where foo.startDate = bar.date\n" );
        q().select(foo).from(foo, bar).where(foo.startDate.eq(bar.date)).parse();
        
//        parse( "from eg.Cat cat where cat.mate.name is not null" );
        q().from(cat).where(cat.mate.name.isnotnull()).parse();
        
//        parse( "from eg.Cat cat, eg.Cat rival where cat.mate = rival.mate" );
        q().from(cat, rival).where(cat.mate.eq(rival.mate)).parse();
        
//        parse( "select cat, mate\n"
//                + "from eg.Cat cat, eg.Cat mate\n"
//                + "where cat.mate = mate" );
        q().select(cat, mate).from(cat, mate).where(cat.mate.eq(mate)).parse();
        
//        parse( "from eg.Cat as cat where cat.id = 123" );
        q().from(cat).where(cat.id.eq(123)).parse();
        
//        parse( "from eg.Cat as cat where cat.mate.id = 69" );
        q().from(cat).where(cat.mate.id.eq(69)).parse();
        
//        parse( "from bank.Person person\n"
//                + "where person.id.country = 'AU'\n"
//                + "and person.id.medicareNumber = 123456" );
        q().from(person).where(person.pid.country.eq("AU").and(person.pid.medicareNumber.eq(123456))).parse();
        
//        parse( "from bank.Account account\n"
//                + "where account.owner.id.country = 'AU'\n"
//                + "and account.owner.id.medicareNumber = 123456" );
        q().from(account).where(account.owner.pid.medicareNumber.eq(123456)).parse();
        
//        parse( "from eg.Cat cat where cat.class = eg.DomesticCat" );
        q().from(cat).where(cat.typeOf(DomesticCat.class)).parse();
        
//        parse( "from eg.AuditLog log, eg.Payment payment\n"
//                + "where log.item.class = 'eg.Payment' and log.item.id = payment.id" );
        q().from(log, payment).where(log.item.typeOf(Payment.class).and(log.item.id.eq(payment.id))).parse();
    }

    /**
     * Section 9.8 - Expressions *
     */
    @Test
    
    public void testDocoExamples98() throws Exception {
        // init deep path
        person._nationality()._calendar();
        
//        parse( "from eg.DomesticCat cat where cat.name between 'A' and 'B'" );
        q().from(cat).where(cat.name.between("A", "B")).parse();
        
//        parse( "from eg.DomesticCat cat where cat.name in ( 'Foo', 'Bar', 'Baz' )" );
        q().from(cat).where(cat.name.in("Foo","Bar","Baz")).parse();
        
//        parse( "from eg.DomesticCat cat where cat.name not between 'A' and 'B'" );
        q().from(cat).where(cat.name.notBetween("A", "B")).parse();
        
//        parse( "from eg.DomesticCat cat where cat.name not in ( 'Foo', 'Bar', 'Baz' )" );
        q().from(cat).where(cat.name.notIn("Foo","Bar","Baz")).parse();
        
//        parse( "from eg.Cat cat where cat.kittens.size > 0" );
        q().from(cat).where(cat.kittens.size().gt(0)).parse();        
        
//        parse( "from eg.Cat cat where size(cat.kittens) > 0" );
        q().from(cat).where(cat.kittens.size().gt(0)).parse();
        
//        parse( "from Order ord where maxindex(ord.items) > 100" );
        q().from(ord).where(maxindex(ord.items).gt(100)).parse();
        
//        parse( "from Order ord where minelement(ord.items) > 10000" );
//        NOTE : Invalid query
        
//        parse( "select mother from eg.Cat as mother, eg.Cat as kit\n"
//                + "where kit in elements(foo.kittens)" );
        q().select(mother).from(mother, kit).where(kit.in(mother.kittens)).parse();
        
//        parse( "select p from eg.NameList list, eg.Person p\n"
//                + "where p.name = some elements(list.names)" );
        q().select(p).from(list,p).where(p.name.eq(some(list.names))).parse();
        
//        parse( "from eg.Cat cat where exists elements(cat.kittens)" );
        q().from(cat).where(exists(cat.kittens)).parse();
        
//        parse( "from eg.Player p where 3 > all elements(p.scores)" );
        q().from(player).where(all(player.scores).lt(3)).parse();
        
//        parse( "from eg.Show show where 'fizard' in indices(show.acts)" );
        q().from(show).where(in("fizard",indices(show.acts))).parse();
        
//        parse( "from Order ord where ord.items[0].id = 1234" );
        q().from(ord).where(ord.items(0).id.eq(1234l)).parse();
        
//        parse( "select person from Person person, Calendar calendar\n"
//                + "where calendar.holidays['national day'] = person.birthDay\n"
//                + "and person.nationality.calendar = calendar" );
        q().select(person).from(person, calendar)
            .where(calendar.holidays("national holiday").eq(person.birthDay)
            .and(person.nationality.calendar.eq(calendar))).parse();
        
//        parse( "select item from Item item, Order ord\n"
//                + "where ord.items[ ord.deliveredItemIndices[0] ] = item and ord.id = 11" );
        q().select(item).from(item, ord).where(ord.items(ord.deliveredItemIndices(0)).eq(item).and(ord.id.eq(1l))).parse();
        
//        parse( "select item from Item item, Order ord\n"
//                + "where ord.items[ maxindex(ord.items) ] = item and ord.id = 11" );
        q().select(item).from(item, ord).where(ord.items(maxindex(ord.items)).eq(item).and(ord.id.eq(1l))).parse();
//
//        parse( "select item from Item item, Order ord\n"
//                + "where ord.items[ size(ord.items) - 1 ] = item" );
        q().select(item).from(item, ord).where(ord.items(QMath.sub(ord.items.size(),1)).eq(item)).parse();
//
//        parse( "from eg.DomesticCat cat where upper(cat.name) like 'FRI%'" );
        q().from(cat).where(cat.name.upper().like("FRI%")).parse();
//
//        parse( "select cust from Product prod, Store store\n"
//                + "inner join store.customers cust\n"
//                + "where prod.name = 'widget'\n"
//                + "and store.location.name in ( 'Melbourne', 'Sydney' )\n"
//                + "and prod = all elements(cust.currentOrder.lineItems)" );
        q().select(cust).from(prod, store).innerJoin(store.customers.as(cust))
            .where(prod.name.eq("widget")
            .and(store.location.name.in("Melbourne","Sydney"))
            .and(prod.eq(all(cust.currentOrder.lineItems)))
            ).parse();
        
        prod.eq(new HqlDomain.Product());
        prod.eq(new QProduct("p"));
        prod.eq(new QItem("p"));
        
    }

    @Test
    public void testDocoExamples99() throws Exception {
//        parse( "from eg.DomesticCat cat\n"
//                + "order by cat.name asc, cat.weight desc, cat.birthdate" );
    	q().from(cat).orderBy(cat.name.asc(), cat.weight.desc(), cat.birthdate.asc()).parse();
    }

    @Test
    public void testDocoExamples910() throws Exception {
//        parse( "select cat.color, sum(cat.weight), count(cat)\n"
//                + "from eg.Cat cat group by cat.color" );
    	q().select(cat.color, sum(cat.weight), Grammar.count(cat)).from(cat).groupBy(cat.color).parse();
        
//        parse( "select foo.id, avg( elements(foo.names) ), max( indices(foo.names) )\n"
//                + "from eg.Foo foo group by foo.id" );
        
    	q().select(foo.id, HqlGrammar.avg(foo.names), max(indices(foo.names))).from(foo).groupBy(foo.id).parse();
        
//        parse( "select cat.color, sum(cat.weight), count(cat)\n"
//                + "from eg.Cat cat group by cat.color\n"
//                + "having cat.color in (eg.Color.TABBY, eg.Color.BLACK)" );
    	q().select(cat.color, sum(cat.weight), Grammar.count(cat)).from(cat).groupBy(cat.color)
            .having(cat.color.in(Color.TABBY, Color.BLACK)).parse();
        
//        parse( "select cat from eg.Cat cat join cat.kittens kitten\n"
//                + "group by cat having avg(kitten.weight) > 100\n"
//                + "order by count(kitten) asc, sum(kitten.weight) desc" );
    	q().select(cat).from(cat).join(cat.kittens.as(kitten))
            .groupBy(cat).having(avg(kitten.weight).gt(100.0))
            .orderBy(Grammar.count(kitten).asc(), sum(kitten.weight).desc()).parse();
    }

    @Test
    public void testDocoExamples911() throws Exception {
//        parse( "from eg.Cat as fatcat where fatcat.weight > (\n"
//                + "select avg(cat.weight) from eg.DomesticCat cat)" );
    	q().from(fatcat).where(fatcat.weight.gt(
                HqlGrammar.select(avg(cat.weight)).from(cat))).parse();
        
//        parse( "from eg.DomesticCat as cat where cat.name = some (\n"
//                + "select name.nickName from eg.Name as name)\n" );
    	q().from(cat).where(cat.name.eq(some(
                HqlGrammar.select(name.nickName).from(name)))).parse();
        
//        parse( "from eg.Cat as cat where not exists (\n"
//                + "from eg.Cat as mate where mate.mate = cat)" );
    	q().from(cat).where(notExists(HqlGrammar.from(mate).where(mate.mate.eq(cat)))).parse();
        
//        parse( "from eg.DomesticCat as cat where cat.name not in (\n"
//                + "select name.nickName from eg.Name as name)" );
    	q().from(cat).where(cat.name.notIn(HqlGrammar.select(name.nickName).from(name))).parse();
    }

    @Test
    public void testDocoExamples912() throws Exception {
//        parse( "select ord.id, sum(price.amount), count(item)\n"
//                + "from Order as ord join ord.lineItems as item\n"
//                + "join item.product as product, Catalog as catalog\n"
//                + "join catalog.prices as price\n"
//                + "where ord.paid = false\n"
//                + "and ord.customer = :customer\n"
//                + "and price.product = product\n"
//                + "and catalog.effectiveDate < sysdate\n"
//                + "and catalog.effectiveDate >= all (\n"
//                + "select cat.effectiveDate from Catalog as cat where cat.effectiveDate < sysdate)\n"
//                + "group by ord\n"
//                + "having sum(price.amount) > :minAmount\n"
//                + "order by sum(price.amount) desc" );
//        QCatalog cat = new QCatalog("cat");
    	q().select(ord.id, sum(price.amount), Grammar.count(item))
            .from(ord).join(ord.lineItems.as(item))
                .join(item.product.as(product)).from(catalog)
                .join(catalog.prices.as(price))
            .where(not(ord.paid)
                .and(ord.customer.eq(cust))
                .and(price.product.eq(product))
                .and(catalog.effectiveDate.after(sysdate()))
                .and(catalog.effectiveDate.after(all(
                    HqlGrammar.select(catalog.effectiveDate).from(catalog)
                    .where(catalog.effectiveDate.before(sysdate()))
                ))))
            .groupBy(ord)
            .having(sum(price.amount).gt(0l))
            .orderBy(sum(price.amount).desc());
                    
                    
//        parse( "select ord.id, sum(price.amount), count(item)\n"
//                + "from Order as ord join ord.lineItems as item join item.product as product,\n"
//                + "Catalog as catalog join catalog.prices as price\n"
//                + "where ord.paid = false and ord.customer = :customer\n"
//                + "and price.product = product and catalog = :currentCatalog\n"
//                + "group by ord having sum(price.amount) > :minAmount\n"
//                + "order by sum(price.amount) desc" );
        HqlDomain.Customer c1 = new HqlDomain.Customer();
        HqlDomain.Catalog c2 = new HqlDomain.Catalog();
        
        q().select(ord.id, sum(price.amount), Grammar.count(item))
            .from(ord).join(ord.lineItems.as(item)).join(item.product.as(product))
            .from(catalog).join(catalog.prices.as(price))
            .where(not(ord.paid).and(ord.customer.eq(c1))
                .and(price.product.eq(product)).and(catalog.eq(c2)))
            .groupBy(ord).having(sum(price.amount).gt(0l))
            .orderBy(sum(price.amount).desc());
        
//        parse( "select count(payment), status.name \n"
//                + "from Payment as payment \n"
//                + "    join payment.currentStatus as status\n"
//                + "    join payment.statusChanges as statusChange\n"
//                + "where payment.status.name <> PaymentStatus.AWAITING_APPROVAL\n"
//                + "    or (\n"
//                + "        statusChange.timeStamp = ( \n"
//                + "            select max(change.timeStamp) \n"
//                + "            from PaymentStatusChange change \n"
//                + "            where change.payment = payment\n"
//                + "        )\n"
//                + "        and statusChange.user <> :currentUser\n"
//                + "    )\n"
//                + "group by status.name, status.sortOrder\n"
//                + "order by status.sortOrder" );
//        select(count(payment), status.name)
//            .from(payment).join(payment.currentStatus.as(status))
//                .join(payment.statusChanges.as(statusChange))
//            .where(payment.status().name.ne(PaymentStatus.AWAITING_APPROVAL)
//                .or(
//                    statusChange.timeStamp.eq(select(max(change.timestamp))
//                    .from(change).where(change.payment.eq(null)
//                )))
//            .groupBy(status.name.asc(), status.sortOrder.asc())
//            .orderBy(status.sortOrder);        
        
//        parse( "select count(payment), status.name \n"
//                + "from Payment as payment\n"
//                + "    join payment.currentStatus as status\n"
//                + "where payment.status.name <> PaymentStatus.AWAITING_APPROVAL\n"
//                + "    or payment.statusChanges[ maxIndex(payment.statusChanges) ].user <> :currentUser\n"
//                + "group by status.name, status.sortOrder\n"
//                + "order by status.sortOrder" );
//        HqlDomain.User currentUser = new HqlDomain.User();
        
//        select(count(payment), status.name)
//            .from(payment).join(payment.currentStatus.as(status))
//            .where(payment.status().name.ne(PaymentStatus.AWAITING_APPROVAL)
//                    .or(payment.statusChanges(maxindex(payment.statusChanges)).user.ne(currentUser)))
//            .groupBy(status.name, status.sortOrder)
//            .orderBy(status.sortOrder);            
        
//        parse( "select account, payment\n"
//                + "from Account as account\n"
//                + "    left outer join account.payments as payment\n"
//                + "where :currentUser in elements(account.holder.users)\n"
//                + "    and PaymentStatus.UNPAID = isNull(payment.currentStatus.name, PaymentStatus.UNPAID)\n"
//                + "order by account.type.sortOrder, account.accountNumber, payment.dueDate" );
//        select(account, payment).from(account)
//            .leftJoin(account.payments.as(payment))
//        .where(in(currentUser, account.holder().users).and())
        
//        parse( "select account, payment\n"
//                + "from Account as account\n"
//                + "    join account.holder.users as user\n"
//                + "    left outer join account.payments as payment\n"
//                + "where :currentUser = user\n"
//                + "    and PaymentStatus.UNPAID = isNull(payment.currentStatus.name, PaymentStatus.UNPAID)\n"
//                + "order by account.type.sortOrder, account.accountNumber, payment.dueDate" );
    }

    @Test
    public void testArrayExpr() throws Exception {
//        parse( "from Order ord where ord.items[0].id = 1234" );
    	q().from(ord).where(ord.items(0).id.eq(1234l)).parse();
    }

    @Test
    public void testMultipleFromClasses() throws Exception {
//        parse( "FROM eg.mypackage.Cat qat, com.toadstool.Foo f" );
    	q().from(qat,foo).parse();
//        parse( "FROM eg.mypackage.Cat qat, org.jabberwocky.Dipstick" );
    }

    @Test
    public void testFromWithJoin() throws Exception {
//        parse( "FROM eg.mypackage.Cat qat, com.toadstool.Foo f join net.sf.blurb.Blurb" );
        // NOTE : invalid query!
//        parse( "FROM eg.mypackage.Cat qat  left join com.multijoin.JoinORama , com.toadstool.Foo f join net.sf.blurb.Blurb" );
        // TODO
    }

    @Test
    public void testSelect() throws Exception {
//        parse( "SELECT f FROM eg.mypackage.Cat qat, com.toadstool.Foo f join net.sf.blurb.Blurb" );
//        parse( "SELECT DISTINCT bar FROM eg.mypackage.Cat qat  left join com.multijoin.JoinORama as bar, com.toadstool.Foo f join net.sf.blurb.Blurb" );        
//        parse( "SELECT count(*) FROM eg.mypackage.Cat qat" );
    	q().select(Grammar.count()).from(qat).parse();
        
//        parse( "SELECT avg(qat.weight) FROM eg.mypackage.Cat qat" );
    	q().select(avg(qat.weight)).from(qat).parse();
    }

    @Test
    public void testWhere() throws Exception {
//        parse( "FROM eg.mypackage.Cat qat where qat.name like '%fluffy%' or qat.toes > 5" );
    	q().from(qat).where(qat.name.like("%fluffy%").or(qat.toes.gt(5))).parse();
        
//        parse( "FROM eg.mypackage.Cat qat where not qat.name like '%fluffy%' or qat.toes > 5" );
    	q().from(qat).where(not(qat.name.like("%fluffy%")).or(qat.toes.gt(5))).parse();
        
//        parse( "FROM eg.mypackage.Cat qat where not qat.name not like '%fluffy%'" );
    	q().from(qat).where(not(qat.name.like("%fluffy%"))).parse();
        
//        parse( "FROM eg.mypackage.Cat qat where qat.name in ('crater','bean','fluffy')" );
    	q().from(qat).where(qat.name.in("crater","bean","fluffy")).parse();
        
//        parse( "FROM eg.mypackage.Cat qat where qat.name not in ('crater','bean','fluffy')" );
    	q().from(qat).where(qat.name.notIn("crater","bean","fluffy")).parse();
        
//        parse( "from Animal an where sqrt(an.bodyWeight)/2 > 10" );
    	q().from(an).where(QMath.sqrt(an.bodyWeight).gt(10.0)).parse();
        
    	q().from(an).where(div(QMath.sqrt(an.bodyWeight),2d).gt(10.0)).parse();
//        parse( "from Animal an where (an.bodyWeight > 10 and an.bodyWeight < 100) or an.bodyWeight is null" );
    	q().from(an).where(an.bodyWeight.gt(10).and(an.bodyWeight.lt(100).or(an.bodyWeight.isnull()))).parse();
    }

    @Test
    public void testGroupBy() throws Exception {
//        parse( "FROM eg.mypackage.Cat qat group by qat.breed" );
    	q().from(qat).groupBy(qat.breed).parse();
        
//        parse( "FROM eg.mypackage.Cat qat group by qat.breed, qat.eyecolor" );
    	q().from(qat).groupBy(qat.breed, qat.eyecolor).parse();
    }

    @Test
    public void testOrderBy() throws Exception {
//        parse( "FROM eg.mypackage.Cat qat order by avg(qat.toes)" );
    	q().from(qat).orderBy(avg(qat.toes).asc()).parse();
        
//        parse( "from Animal an order by sqrt(an.bodyWeight)/2" );
    	q().from(qat).orderBy(QMath.sqrt(div(an.bodyWeight,2)).asc()).parse();
    }

    @Test
    public void testDoubleLiteral() throws Exception {
//        parse( "from eg.Cat as tinycat where fatcat.weight < 3.1415" );
    	q().from(cat).where(cat.weight.lt((int)3.1415)).parse();
        
//        parse( "from eg.Cat as enormouscat where fatcat.weight > 3.1415e3" );
    	q().from(cat).where(cat.weight.gt((int)3.1415e3)).parse();
    }

    @Test
    public void testComplexConstructor() throws Exception {
//        parse( "select new Foo(count(bar)) from bar" );    
    	q().select(new QFooDTO(Grammar.count(bar))).from(bar).parse();
        
//        parse( "select new Foo(count(bar),(select count(*) from doofus d where d.gob = 'fat' )) from bar" );
    	q().select(new QFooDTO(Grammar.count(bar), HqlGrammar.select(Grammar.count()).from(d).where(d.gob.eq("fat")))).from(bar).parse();
    }

    @Test
    public void testInNotIn() throws Exception {
//        parse( "from foo where foo.bar in ('a' , 'b', 'c')" );
    	q().from(foo).where(foo.bar.in("a","b","c")).parse();
        
//        parse( "from foo where foo.bar not in ('a' , 'b', 'c')" );
    	q().from(foo).where(foo.bar.notIn("a","b","c")).parse();
    }

    @Test
    public void testOperatorPrecedence() throws Exception {
//        parse( "from foo where foo.bar = 123 + foo.baz * foo.not" );
//        parse( "from foo where foo.bar like 'testzzz' || foo.baz or foo.bar in ('duh', 'gob')" );
    }

    @Test
    public void testUnnamedParameter() throws Exception {
//        parse( "select foo, bar from org.hibernate.test.Foo foo left outer join foo.foo bar where foo = ?" ); // Added '?' as a valid expression.
    }

    @Test
    public void testInElements() throws Exception {
//        parse( "from bar in class org.hibernate.test.Bar, foo in elements(bar.baz.fooArray)" );   // Added collectionExpr as a valid 'in' clause.
    }

    @Test
    public void testDotElements() throws Exception {
//        parse( "select distinct foo from baz in class org.hibernate.test.Baz, foo in elements(baz.fooArray)" );
//        parse( "select foo from baz in class org.hibernate.test.Baz, foo in elements(baz.fooSet)" );
//        parse( "select foo from baz in class org.hibernate.test.Baz, foo in elements(baz.fooArray)" );
//        parse( "from org.hibernate.test.Baz baz where 'b' in elements(baz.collectionComponent.nested.foos) and 1.0 in elements(baz.collectionComponent.nested.floats)" );
    }

    @Test
    public void testSelectAll() throws Exception {
//        parse( "select all s, s.other from s in class org.hibernate.test.Simple where s = :s" );
    }

    @Test
    public void testNot() throws Exception {
        // Cover NOT optimization in HqlParser
//        parse( "from eg.Cat cat where not ( cat.kittens.size < 1 )" );
    	q().from(cat).where(not(cat.kittens.size().lt(1))).parse();
        
//        parse( "from eg.Cat cat where not ( cat.kittens.size > 1 )" );
    	q().from(cat).where(not(cat.kittens.size().gt(1))).parse();
        
//        parse( "from eg.Cat cat where not ( cat.kittens.size >= 1 )" );
    	q().from(cat).where(not(cat.kittens.size().goe(1))).parse();
        
//        parse( "from eg.Cat cat where not ( cat.kittens.size <= 1 )" );
    	q().from(cat).where(not(cat.kittens.size().loe(1))).parse();
        
//        parse( "from eg.DomesticCat cat where not ( cat.name between 'A' and 'B' ) " );
    	q().from(cat).where(not(cat.name.between("A", "B"))).parse();
        
//        parse( "from eg.DomesticCat cat where not ( cat.name not between 'A' and 'B' ) " );
    	q().from(cat).where(not(cat.name.notBetween("A", "B"))).parse();
        
//        parse( "from eg.Cat cat where not ( not cat.kittens.size <= 1 )" );
    	q().from(cat).where(not(not(cat.kittens.size().loe(1)))).parse();
        
//        parse( "from eg.Cat cat where not  not ( not cat.kittens.size <= 1 )" );
    	q().from(cat).where(not(not(not(cat.kittens.size().loe(1))))).parse();
    }

    @Test
    public void testEjbqlExtensions() throws Exception {
//        parse( "select object(a) from Animal a where a.mother member of a.offspring" );
//        parse( "select object(a) from Animal a where a.mother member a.offspring" ); //no member of
//        parse( "select object(a) from Animal a where a.offspring is empty" );
    }

    @Test
    public void testHB1042() throws Exception {
//        parse( "select x from fmc_web.pool.Pool x left join x.containers c0 where (upper(x.name) = upper(':') and c0.id = 1)" );
    }

    @Test
    public void testHHH354() throws Exception {
//        parse( "from Foo f where f.full = 'yep'");
    }

    @Test
    public void testEjbqlKeywordsAsIdentifier() throws Exception {
//        parse( "from org.hibernate.test.Bar bar where bar.object.id = ? and bar.object.class = ?" );
    }

    @Test
    public void testConstructorIn() throws Exception {
//        parse( "from org.hibernate.test.Bar bar where (b.x, b.y, b.z) in (select foo, bar, baz from org.hibernate.test.Foo)" );
    }

    @Test
    public void testHHH719() throws Exception {
        // Some SQLs have function names with package qualifiers.
//        parse("from Foo f order by com.fooco.SpecialFunction(f.id)");
    }

    @Test
    public void testHHH1107() throws Exception {
//        parse("from Animal where zoo.address.street = '123 Bogus St.'");
    }

    @Test
    public void testHHH1247() throws Exception {
//        parse("select distinct user.party from com.itf.iceclaims.domain.party.user.UserImpl user inner join user.party.$RelatedWorkgroups relatedWorkgroups where relatedWorkgroups.workgroup.id = :workgroup and relatedWorkgroups.effectiveTime.start <= :datesnow and relatedWorkgroups.effectiveTime.end > :dateenow ");
    }
    
    @Test
    public void testCasts() throws Exception {
        ENumber<Integer> bw = cat.bodyWeight;
        q().from(cat).select(
                bw.byteValue(), 
                bw.doubleValue(),
                bw.floatValue(),
                bw.intValue(),
                bw.longValue(),
                bw.shortValue(),
                bw.stringValue()).parse();
        
        q().from(cat).select(bw.castToNum(Byte.class)).parse();
    }
    
    class TestQuery extends HqlQueryBase<TestQuery> {
    	public TestQuery() {
    		super(new HqlOps());
    	}
    	public long count() {
    		return 0;
    	}
    	public Iterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest) {
    		return null;
    	}
    	public <RT> Iterator<RT> iterate(Expr<RT> projection) {
    		return null;
    	}
    	public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
    		return null;
    	}
    	public TestQuery select(Expr<?>... exprs) {
    		addToProjection(exprs);
    		return this;
    	}
    	public void parse() throws RecognitionException, TokenStreamException {
    		try {
    			String input = toString();
    			System.out.println("input: " + input.replace('\n', ' '));
    			HqlParser parser = HqlParser.getInstance(input);
    			parser.setFilter(false);
    			parser.statement();
    			AST ast = parser.getAST();
    			ByteArrayOutputStream baos = new ByteArrayOutputStream();
    			parser.showAst(ast, new PrintStream(baos));
    			assertEquals("At least one error occurred during parsing " + input,
    					0, parser.getParseErrorHandler().getErrorCount());
    		} finally {
    			clear();
    			System.out.println();
    		}
    	}
    }
    
}
