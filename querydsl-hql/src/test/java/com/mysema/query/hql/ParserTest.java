/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static com.mysema.query.hql.HQLGrammar.all;
import static com.mysema.query.hql.HQLGrammar.some;
import static com.mysema.query.hql.HQLGrammar.sum;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Iterator;

import javax.annotation.Nullable;

import org.hibernate.hql.ast.HqlParser;
import org.junit.Ignore;
import org.junit.Test;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.SearchResults;
import com.mysema.query.hql.domain.Cat;
import com.mysema.query.hql.domain.Catalog;
import com.mysema.query.hql.domain.Color;
import com.mysema.query.hql.domain.Customer;
import com.mysema.query.hql.domain.DomesticCat;
import com.mysema.query.hql.domain.Payment;
import com.mysema.query.hql.domain.Product;
import com.mysema.query.hql.domain.QFamily;
import com.mysema.query.hql.domain.QFooDTO;
import com.mysema.query.hql.domain.QItem;
import com.mysema.query.hql.domain.QProduct;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * HqlParserTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class ParserTest implements Constants {

    protected TestQuery query() {
        return new TestQuery();
    }
    
    protected HQLSubQuery sub(){
        return new HQLSubQuery();
    }
    
    @Test
    public void serialization(){
        TestQuery query = query();
        
        query.from(cat);
        assertEquals("from Cat cat", query.toString());
        
        query.from(fatcat);
        assertEquals("from Cat cat, Cat fatcat", query.toString());
    }

    @Test
    public void basic() throws RecognitionException, TokenStreamException{
        query().from(cat, fatcat).select(cat.name, fatcat.name).parse();
    }
    
    @Test
    public void beforeAndAfter() throws RecognitionException,
            TokenStreamException {
        
        EComparable<java.util.Date> ed = catalog.effectiveDate;
        query().from(catalog).where(ed.gt(EDate.currentDate()), ed.goe(EDate.currentDate()),
                ed.lt(EDate.currentDate()), ed.loe(EDate.currentDate())).select(catalog)
                .parse();
    }
    
    @Test
    public void joins() throws RecognitionException, TokenStreamException{
        query().from(cat).join(cat.mate).select(cat).parse();
        query().from(cat).innerJoin(cat.mate).select(cat).parse();
        query().from(cat).leftJoin(cat.mate).select(cat).parse();
    }

    @Test
    public void testSum() throws RecognitionException, TokenStreamException {
        query().from(cat).select(sum(cat.kittens.size())).parse();

        query().from(cat).where(sum(cat.kittens.size()).gt(0)).select(cat).parse();

        query().from(cat).where(cat.kittens.isEmpty()).select(cat).parse();

        query().from(cat).where(cat.kittens.isNotEmpty()).select(cat).parse();

//        query().from(cat)
//         .groupBy(cat.name)
//         .having(sum(cat.bodyWeight).gt(0))
//         .select(cat).parse();
    }

    /**
     * Section 9.2 - from *
     */
    @Test
    public void docoExamples92() throws Exception {
        // parse( "from eg.Cat" );
        query().from(cat).parse();

        // parse( "from eg.Cat as cat" );
        query().from(cat).parse();

        // parse( "from eg.Cat cat" );
        query().from(cat).parse();

        // parse( "from Formula, Parameter" );
        query().from(form, param).parse();

        // parse( "from Formula as form, Parameter as param" );
        query().from(form, param).parse();
    }

    /**
     * Section 9.3 - Associations and joins *
     */
    @Test
    public void docoExamples93() throws Exception {
        // parse(
        // "from eg.Cat as cat inner join cat.mate as mate left outer join cat.kittens as kitten"
        // );
        query().from(cat).innerJoin(cat.mate, mate).leftJoin(cat.kittens, kitten).parse();

        // parse( "from eg.Cat as cat left join cat.mate.kittens as kittens" );
        query().from(cat).leftJoin(cat.mate.kittens, kitten).parse();

        // parse( "from Formula form full join form.parameter param" );
        // HSQLDB doesn't support full join
        // from(form).fullJoin(form.parameter.as(param)).parse();

        // parse(
        // "from eg.Cat as cat join cat.mate as mate left join cat.kittens as kitten"
        // );
        query().from(cat).join(cat.mate, mate).leftJoin(cat.kittens, kitten).parse();

        // parse(
        // "from eg.Cat as cat\ninner join fetch cat.mate\nleft join fetch cat.kittens"
        // );
        query().from(cat).innerJoin(cat.mate, mate).leftJoin(cat.kittens, kitten).parse();
    }

    @Test
    public void docoExamples93_viaAlias() throws Exception {
        Cat c = alias(Cat.class, "cat");
        Cat k = alias(Cat.class, "kittens");
        Cat m = alias(Cat.class, "mate");

//        Formula f = alias(Formula.class, "formula");
//        Parameter p = alias(Parameter.class, "param");

        // parse(
        // "from eg.Cat as cat inner join cat.mate as mate left outer join cat.kittens as kitten"
        // );
        query().from($(c)).innerJoin($(c.getMate()),$(m)).leftJoin($(c.getKittens()),$(k)).parse();

        // parse( "from eg.Cat as cat left join cat.mate.kittens as kittens" );
        query().from($(c)).leftJoin($(c.getMate().getKittens()),$(k)).parse();

        // parse( "from Formula form full join form.parameter param" );
        // HSQLDB doesn't support full join
        // from($(f)).fullJoin($(f.getParameter()).as($(p))).parse();

        // parse(
        // "from eg.Cat as cat join cat.mate as mate left join cat.kittens as kitten"
        // );
        query().from($(c)).innerJoin($(c.getMate()),$(m)).leftJoin($(c.getKittens()),$(k)).parse();

        // parse(
        // "from eg.Cat as cat\ninner join fetch cat.mate\nleft join fetch cat.kittens"
        // );
        query().from($(c)).innerJoin($(c.getMate()),$(m)).leftJoin($(c.getKittens()),$(k)).parse();
    }

    /**
     * Section 9.4 - Select *
     */
    @Test
    public void docoExamples94() throws Exception {
        // parse( "select mate from eg.Cat as cat inner join cat.mate as mate"
        // );
        query().select(cat.mate).from(cat).innerJoin(cat.mate, mate).parse();

        // parse( "select cat.mate from eg.Cat cat" );
        query().select(cat.mate).from(cat).parse();

        // parse( "select elements(cat.kittens) from eg.Cat cat" );
        query().select(cat.kittens).from(cat).parse();

        // parse(
        // "select cat.name from eg.DomesticCat cat where cat.name like 'fri%'"
        // );
//        query().select(cat.name).from(cat).where(cat.name.like("fri%")).parse();

        // parse( "select cust.name.firstName from Customer as cust" );
        query().select(cust.name.firstName).from(cust).parse();

        // parse( "select mother, offspr, mate.name from eg.DomesticCat\n"
        // + " as mother inner join mother.mate as mate left outer join\n"
        // + "mother.kittens as offspr" );
        query().select(mother, offspr, mate).from(mother)
            .innerJoin(mother.mate, mate)
            .leftJoin(mother.kittens, offspr).parse();

        // parse( "select new Family(mother, mate, offspr)\n"
        // + "from eg.DomesticCat as mother\n"
        // + "join mother.mate as mate\n"
        // + "left join mother.kittens as offspr\n" );
        query().select(new QFamily(mother, mate, kitten)).from(mother)
            .innerJoin(mother.mate, mate)
            .leftJoin(mother.kittens, kitten).parse();
    }

    /**
     * Section 9.5 - Aggregate functions *
     */
    @Test
    public void docoExamples95() throws Exception {
        // parse(
        // "select avg(cat.weight), sum(cat.weight), max(cat.weight), count(cat)\n"
        // + "from eg.Cat cat" );
        query().select(cat.weight.avg(), cat.weight.sum(),
                cat.weight.max(), cat.count()).from(cat)
                .parse();

        // parse( "select cat, count( elements(cat.kittens) )\n"
        // + " from eg.Cat cat group by cat" );
        // NOTE : groupBy don't work properly in HSQLDB
        // q().select(cat, Grammar.count(cat.kittens)).from(cat).groupBy(cat);

        // parse( "select distinct cat.name from eg.Cat cat" );
        // q().select(distinct(cat.name)).from(cat).parse();

        // parse( "select count(distinct cat.name), count(cat) from eg.Cat cat"
        // );
        // q().select(Grammar.count(distinct(cat.name)),
        // Grammar.count(cat)).from(cat).parse();
    }

    @Test
    public void own_DistinctEntities() throws Exception {
        // q().select(distinct(cat)).from(cat).innerJoin(cat.kittens.as(kitten)).parse();
    }

    /**
     * Section 9.6 - Polymorphism *
     */
    @Test
    public void docoExamples96() throws Exception {
        // parse( "from eg.Cat as cat" );
        query().from(cat).parse();

        // parse( "from java.lang.Object o" );
        // parse( "from eg.Named n, eg.Named m where n.name = m.name" );
        query().from(m, n).where(n.name.eq(m.name)).parse();
    }

    /**
     * Section 9.7 - Where *
     */
    @Test
    public void docoExamples97() throws Exception {
        // init deep path
//        account._owner()._pid();

        // parse( "from eg.Cat as cat where cat.name='Fritz'" );
//        query().from(cat).where(cat.name.like("Fritz")).parse();

        // parse( "select foo\n"
        // + "from eg.Foo foo, eg.Bar bar\n"
        // + "where foo.startDate = bar.date\n" );
        query().select(foo).from(foo, bar).where(foo.startDate.eq(bar.date))
                .parse();

        // parse( "from eg.Cat cat where cat.mate.name is not null" );
        query().from(cat).where(cat.mate.name.isNotNull()).parse();

        // parse( "from eg.Cat cat, eg.Cat rival where cat.mate = rival.mate" );
        query().from(cat, rival).where(cat.mate.eq(rival.mate)).parse();

        // parse( "select cat, mate\n"
        // + "from eg.Cat cat, eg.Cat mate\n"
        // + "where cat.mate = mate" );
        query().select(cat, mate).from(cat, mate).where(cat.mate.eq(mate)).parse();

        // parse( "from eg.Cat as cat where cat.id = 123" );
        query().from(cat).where(cat.id.eq(123)).parse();

        // parse( "from eg.Cat as cat where cat.mate.id = 69" );
        query().from(cat).where(cat.mate.id.eq(69)).parse();

        // parse( "from bank.Person person\n"
        // + "where person.id.country = 'AU'\n"
        // + "and person.id.medicareNumber = 123456" );
        query().from(person).where(
                person.pid.country.eq("AU").and(
                        person.pid.medicareNumber.eq(123456))).parse();

        // parse( "from bank.Account account\n"
        // + "where account.owner.id.country = 'AU'\n"
        // + "and account.owner.id.medicareNumber = 123456" );
        query().from(account).where(account.owner.pid.medicareNumber.eq(123456))
                .parse();

        // parse( "from eg.Cat cat where cat.class = eg.DomesticCat" );
        query().from(cat).where(cat.instanceOf(DomesticCat.class)).parse();

        // parse( "from eg.AuditLog log, eg.Payment payment\n"
        // + "where log.item.class = 'eg.Payment' and log.item.id = payment.id"
        // );
        query().from(log, payment).where(
                log.item.instanceOf(Payment.class),
                log.item.id.eq(payment.id)).parse();
    }

    /**
     * Section 9.8 - Expressions *
     */
    @Test
    @Ignore
    public void docoExamples98() throws Exception {
        // init deep path
//        person._nationality()._calendar();

        // parse( "from eg.DomesticCat cat where cat.name between 'A' and 'B'"
        // );
        query().from(cat).where(cat.name.between("A", "B")).parse();

        // parse(
        // "from eg.DomesticCat cat where cat.name in ( 'Foo', 'Bar', 'Baz' )"
        // );
        query().from(cat).where(cat.name.in("Foo", "Bar", "Baz")).parse();

        // parse(
        // "from eg.DomesticCat cat where cat.name not between 'A' and 'B'" );
        query().from(cat).where(cat.name.notBetween("A", "B")).parse();

        // parse(
        // "from eg.DomesticCat cat where cat.name not in ( 'Foo', 'Bar', 'Baz' )"
        // );
        query().from(cat).where(cat.name.notIn("Foo", "Bar", "Baz")).parse();

        // parse( "from eg.Cat cat where cat.kittens.size > 0" );
        query().from(cat).where(cat.kittens.size().gt(0)).parse();

        // parse( "from eg.Cat cat where size(cat.kittens) > 0" );
        query().from(cat).where(cat.kittens.size().gt(0)).parse();

        // parse( "from Order ord where maxindex(ord.items) > 100" );
//        q().from(ord).where(maxindex(ord.items).gt(100)).parse();

        // parse( "from Order ord where minelement(ord.items) > 10000" );
        // NOTE : Invalid query

        // parse( "select mother from eg.Cat as mother, eg.Cat as kit\n"
        // + "where kit in elements(foo.kittens)" );
        query().select(mother).from(mother, kit).where(kit.in(mother.kittens))
                .parse();

        // parse( "select p from eg.NameList list, eg.Person p\n"
        // + "where p.name = some elements(list.names)" );
        query().select(p).from(list, p).where(p.name.eq(some(list.names))).parse();

        // parse( "from eg.Cat cat where exists elements(cat.kittens)" );
        query().from(cat).where(cat.kittens.isNotEmpty()).parse();

        // parse( "from eg.Player p where 3 > all elements(p.scores)" );
//        q().from(player).where(all(player.scores).lt(3)).parse();

        // parse( "from eg.Show show where 'fizard' in indices(show.acts)" );
//        q().from(show).where(in("fizard", indices(show.acts))).parse();

        // parse( "from Order ord where ord.items[0].id = 1234" );
//        q().from(ord).where(ord.items(0).id.eq(1234l)).parse();

        // parse( "select person from Person person, Calendar calendar\n"
        // + "where calendar.holidays['national day'] = person.birthDay\n"
        // + "and person.nationality.calendar = calendar" );
        query().select(person).from(person, calendar).where(
                calendar.holidays("national holiday").eq(person.birthDay).and(
                        person.nationality.calendar.eq(calendar))).parse();

        // parse( "select item from Item item, Order ord\n"
        // +
        // "where ord.items[ ord.deliveredItemIndices[0] ] = item and ord.id = 11"
        // );
        query().select(item).from(item, ord).where(
                ord.items(ord.deliveredItemIndices(0)).eq(item).and(
                        ord.id.eq(1l))).parse();

        // parse( "select item from Item item, Order ord\n"
        // + "where ord.items[ maxindex(ord.items) ] = item and ord.id = 11" );
//        q().select(item).from(item, ord).where(
//                ord.items(maxindex(ord.items)).eq(item).and(ord.id.eq(1l)))
//                .parse();
        //
        // parse( "select item from Item item, Order ord\n"
        // + "where ord.items[ size(ord.items) - 1 ] = item" );
        query().select(item).from(item, ord).where(
                ord.items(ord.items.size().subtract(1)).eq(item))
                .parse();
        //
        // parse( "from eg.DomesticCat cat where upper(cat.name) like 'FRI%'" );
//        query().from(cat).where(cat.name.upper().like("FRI%")).parse();
        //
        // parse( "select cust from Product prod, Store store\n"
        // + "inner join store.customers cust\n"
        // + "where prod.name = 'widget'\n"
        // + "and store.location.name in ( 'Melbourne', 'Sydney' )\n"
        // + "and prod = all elements(cust.currentOrder.lineItems)" );
        query()
                .select(cust)
                .from(prod, store)
                .innerJoin(store.customers, cust)
                .where(
                        prod.name.eq("widget").and(
                                store.location.name.in("Melbourne", "Sydney"))
                                .and(prod.eq(all(cust.currentOrder.lineItems))))
                .parse();

        prod.eq(new Product());
        prod.eq(new QProduct("p"));
        prod.eq(new QItem("p"));

    }

    @Test
    public void docoExamples99() throws Exception {
        // parse( "from eg.DomesticCat cat\n"
        // + "order by cat.name asc, cat.weight desc, cat.birthdate" );
        query().from(cat).orderBy(cat.name.asc(), cat.weight.desc(),
                cat.birthdate.asc()).parse();
    }

    @Test
    public void docoExamples910() throws Exception {
        // parse( "select cat.color, sum(cat.weight), count(cat)\n"
        // + "from eg.Cat cat group by cat.color" );
        query().select(cat.color, sum(cat.weight), cat.count()).from(cat)
                .groupBy(cat.color).parse();
    }


    @Test
    public void docoExamples910_2() throws Exception {
     // parse(
        // "select foo.id, avg( elements(foo.names) ), max( indices(foo.names) )\n"
        // + "from eg.Foo foo group by foo.id" );

//        q().select(foo.id, HQLGrammar.avg(foo.names), max(indices(foo.names)))
//                .from(foo).groupBy(foo.id).parse();

        // parse( "select cat.color, sum(cat.weight), count(cat)\n"
        // + "from eg.Cat cat group by cat.color\n"
        // + "having cat.color in (eg.Color.TABBY, eg.Color.BLACK)" );
        query().select(cat.color, sum(cat.weight), cat.count()).from(cat)
                .groupBy(cat.color).having(
                        cat.color.in(Color.TABBY, Color.BLACK)).parse();
    }
    
    @Test    
    @Ignore
    public void docoExamples910_3() throws Exception {
        // parse( "select cat from eg.Cat cat join cat.kittens kitten\n"
        // + "group by cat having avg(kitten.weight) > 100\n"
        // + "order by count(kitten) asc, sum(kitten.weight) desc" );
        query().select(cat).from(cat).join(cat.kittens, kitten).groupBy(cat)
                .having(kitten.weight.avg().gt(100.0)).orderBy(
                        kitten.count().asc(), sum(kitten.weight).desc())
                .parse();
        
//        select cat from Cat cat   
//        join cat.kittens as kitten 
//        group by cat 
//        having avg(kitten.weight) > :a1 
//        order by count(kitten) asc, sum(kitten.weight) desc
    }
    
    @Test
    public void docoExamples911() throws Exception {
        // parse( "from eg.Cat as fatcat where fatcat.weight > (\n"
        // + "select avg(cat.weight) from eg.DomesticCat cat)" );
        query().from(fatcat).where(
//                fatcat.weight.gt(HQLGrammar.select(avg(cat.weight)).from(cat)))
                fatcat.weight.gt(sub().from(cat).unique(cat.weight.avg())))
                .parse();

        // parse( "from eg.DomesticCat as cat where cat.name = some (\n"
        // + "select name.nickName from eg.Name as name)\n" );
        query().from(cat).where(
//                cat.name.eq(some(HQLGrammar.select(name.nickName).from(name))))
                cat.name.eq(some(sub().from(name).list(name.nickName))))
                .parse();

        // parse( "from eg.Cat as cat where not exists (\n"
        // + "from eg.Cat as mate where mate.mate = cat)" );
        query().from(cat).where(
                sub().from(mate).where(mate.mate.eq(cat)).list(mate).notExists())
                .parse();

        // parse( "from eg.DomesticCat as cat where cat.name not in (\n"
        // + "select name.nickName from eg.Name as name)" );
        query().from(cat).where(
//                cat.name.notIn(HQLGrammar.select(name.nickName).from(name)))
                cat.name.notIn(sub().from(name).list(name.nickName)))
                .parse();
    }

    @Test
    public void docoExamples912() throws Exception {
        // parse( "select ord.id, sum(price.amount), count(item)\n"
        // + "from Order as ord join ord.lineItems as item\n"
        // + "join item.product as product, Catalog as catalog\n"
        // + "join catalog.prices as price\n"
        // + "where ord.paid = false\n"
        // + "and ord.customer = :customer\n"
        // + "and price.product = product\n"
        // + "and catalog.effectiveDate < sysdate\n"
        // + "and catalog.effectiveDate >= all (\n"
        // +
        // "select cat.effectiveDate from Catalog as cat where cat.effectiveDate < sysdate)\n"
        // + "group by ord\n"
        // + "having sum(price.amount) > :minAmount\n"
        // + "order by sum(price.amount) desc" );
        // QCatalog cat = new QCatalog("cat");
        query().select(ord.id, sum(price.amount), item.count()).from(ord)
                .join(ord.lineItems, item).join(item.product, product)
                .from(catalog).join(catalog.prices, price).where(
                        ord.paid.not().and(ord.customer.eq(cust)).and(
                                price.product.eq(product)).and(
                                catalog.effectiveDate.gt(EDate.currentDate())).and(
                                catalog.effectiveDate.gt(all(                                        
                                        sub().from(catalog).where(
                                                catalog.effectiveDate.lt(EDate.currentDate()))
                                             .list(catalog.effectiveDate)                                ))))
                .groupBy(ord).having(sum(price.amount).gt(0l)).orderBy(
                        sum(price.amount).desc());

        // parse( "select ord.id, sum(price.amount), count(item)\n"
        // +
        // "from Order as ord join ord.lineItems as item join item.product as product,\n"
        // + "Catalog as catalog join catalog.prices as price\n"
        // + "where ord.paid = false and ord.customer = :customer\n"
        // + "and price.product = product and catalog = :currentCatalog\n"
        // + "group by ord having sum(price.amount) > :minAmount\n"
        // + "order by sum(price.amount) desc" );
        Customer c1 = new Customer();
        Catalog c2 = new Catalog();

        query().select(ord.id, sum(price.amount), item.count()).from(ord)
                .join(ord.lineItems, item).join(item.product, product)
                .from(catalog).join(catalog.prices, price).where(
                        ord.paid.not().and(ord.customer.eq(c1)).and(
                                price.product.eq(product)).and(catalog.eq(c2)))
                .groupBy(ord).having(sum(price.amount).gt(0l)).orderBy(
                        sum(price.amount).desc());

        // parse( "select count(payment), status.name \n"
        // + "from Payment as payment \n"
        // + "    join payment.currentStatus as status\n"
        // + "    join payment.statusChanges as statusChange\n"
        // + "where payment.status.name <> PaymentStatus.AWAITING_APPROVAL\n"
        // + "    or (\n"
        // + "        statusChange.timeStamp = ( \n"
        // + "            select max(change.timeStamp) \n"
        // + "            from PaymentStatusChange change \n"
        // + "            where change.payment = payment\n"
        // + "        )\n"
        // + "        and statusChange.user <> :currentUser\n"
        // + "    )\n"
        // + "group by status.name, status.sortOrder\n"
        // + "order by status.sortOrder" );
        // select(count(payment), status.name)
        // .from(payment).join(payment.currentStatus.as(status))
        // .join(payment.statusChanges.as(statusChange))
        // .where(payment.status().name.ne(PaymentStatus.AWAITING_APPROVAL)
        // .or(
        // statusChange.timeStamp.eq(select(max(change.timestamp))
        // .from(change).where(change.payment.eq(null)
        // )))
        // .groupBy(status.name.asc(), status.sortOrder.asc())
        // .orderBy(status.sortOrder);

        // parse( "select count(payment), status.name \n"
        // + "from Payment as payment\n"
        // + "    join payment.currentStatus as status\n"
        // + "where payment.status.name <> PaymentStatus.AWAITING_APPROVAL\n"
        // +
        // "    or payment.statusChanges[ maxIndex(payment.statusChanges) ].user <> :currentUser\n"
        // + "group by status.name, status.sortOrder\n"
        // + "order by status.sortOrder" );
        // HqlDomain.User currentUser = new HqlDomain.User();

        // select(count(payment), status.name)
        // .from(payment).join(payment.currentStatus.as(status))
        // .where(payment.status().name.ne(PaymentStatus.AWAITING_APPROVAL)
        // .or(payment.statusChanges(maxindex(payment.statusChanges)).user.ne(currentUser)))
        // .groupBy(status.name, status.sortOrder)
        // .orderBy(status.sortOrder);

        // parse( "select account, payment\n"
        // + "from Account as account\n"
        // + "    left outer join account.payments as payment\n"
        // + "where :currentUser in elements(account.holder.users)\n"
        // +
        // "    and PaymentStatus.UNPAID = isNull(payment.currentStatus.name, PaymentStatus.UNPAID)\n"
        // +
        // "order by account.type.sortOrder, account.accountNumber, payment.dueDate"
        // );
        // select(account, payment).from(account)
        // .leftJoin(account.payments.as(payment))
        // .where(in(currentUser, account.holder().users).and())

        // parse( "select account, payment\n"
        // + "from Account as account\n"
        // + "    join account.holder.users as user\n"
        // + "    left outer join account.payments as payment\n"
        // + "where :currentUser = user\n"
        // +
        // "    and PaymentStatus.UNPAID = isNull(payment.currentStatus.name, PaymentStatus.UNPAID)\n"
        // +
        // "order by account.type.sortOrder, account.accountNumber, payment.dueDate"
        // );
    }

    @Test
    @Ignore
    public void arrayExpr() throws Exception {
        // parse( "from Order ord where ord.items[0].id = 1234" );
        query().from(ord).where(ord.items(0).id.eq(1234l)).parse();
    }

    @Test
    public void multipleFromClasses() throws Exception {
        // parse( "FROM eg.mypackage.Cat qat, com.toadstool.Foo f" );
        query().from(qat, foo).parse();
        // parse( "FROM eg.mypackage.Cat qat, org.jabberwocky.Dipstick" );
    }

    @Test
    public void fromWithJoin() throws Exception {
        // parse(
        // "FROM eg.mypackage.Cat qat, com.toadstool.Foo f join net.sf.blurb.Blurb"
        // );
        // NOTE : invalid query!
        // parse(
        // "FROM eg.mypackage.Cat qat  left join com.multijoin.JoinORama , com.toadstool.Foo f join net.sf.blurb.Blurb"
        // );
        // TODO
    }

    @Test
    public void testSelect() throws Exception {
        // parse(
        // "SELECT f FROM eg.mypackage.Cat qat, com.toadstool.Foo f join net.sf.blurb.Blurb"
        // );
        // parse(
        // "SELECT DISTINCT bar FROM eg.mypackage.Cat qat  left join com.multijoin.JoinORama as bar, com.toadstool.Foo f join net.sf.blurb.Blurb"
        // );
        // parse( "SELECT count(*) FROM eg.mypackage.Cat qat" );
        query().select(Expr.countAll()).from(qat).parse();

        // parse( "SELECT avg(qat.weight) FROM eg.mypackage.Cat qat" );
        query().select(qat.weight.avg()).from(qat).parse();
    }

    @Test
    public void testWhere() throws Exception {
        // parse(
        // "FROM eg.mypackage.Cat qat where qat.name like '%fluffy%' or qat.toes > 5"
        // );
//        query().from(qat).where(qat.name.like("%fluffy%").or(qat.toes.gt(5)))
//                .parse();

        // parse(
        // "FROM eg.mypackage.Cat qat where not qat.name like '%fluffy%' or qat.toes > 5"
        // );
//        query().from(qat).where(not(qat.name.like("%fluffy%")).or(qat.toes.gt(5)))
//                .parse();

        // parse(
        // "FROM eg.mypackage.Cat qat where not qat.name not like '%fluffy%'" );
//        query().from(qat).where(not(qat.name.like("%fluffy%"))).parse();

        // parse(
        // "FROM eg.mypackage.Cat qat where qat.name in ('crater','bean','fluffy')"
        // );
        query().from(qat).where(qat.name.in("crater", "bean", "fluffy")).parse();

        // parse(
        // "FROM eg.mypackage.Cat qat where qat.name not in ('crater','bean','fluffy')"
        // );
        query().from(qat).where(qat.name.notIn("crater", "bean", "fluffy")).parse();

        // parse( "from Animal an where sqrt(an.bodyWeight)/2 > 10" );
        query().from(an).where(an.bodyWeight.sqrt().gt(10.0)).parse();

        query().from(an).where(an.bodyWeight.sqrt().divide(2d).gt(10.0))
                .parse();
        // parse(
        // "from Animal an where (an.bodyWeight > 10 and an.bodyWeight < 100) or an.bodyWeight is null"
        // );
        query().from(an).where(
                an.bodyWeight.gt(10).and(
                        an.bodyWeight.lt(100).or(an.bodyWeight.isNull())))
                .parse();
    }

    @Test
    public void testGroupBy() throws Exception {
        // parse( "FROM eg.mypackage.Cat qat group by qat.breed" );
        query().from(qat).groupBy(qat.breed).parse();

        // parse( "FROM eg.mypackage.Cat qat group by qat.breed, qat.eyecolor"
        // );
        query().from(qat).groupBy(qat.breed, qat.eyecolor).parse();
    }

    @Test
    public void testOrderBy() throws Exception {
        // parse( "FROM eg.mypackage.Cat qat order by avg(qat.toes)" );
        query().from(qat).orderBy(qat.toes.avg().asc()).parse();

        // parse( "from Animal an order by sqrt(an.bodyWeight)/2" );
        query().from(qat).orderBy(an.bodyWeight.sqrt().divide(2.0).asc()).parse();
    }

    @Test
    public void dubleLiteral() throws Exception {
        // parse( "from eg.Cat as tinycat where fatcat.weight < 3.1415" );
        query().from(cat).where(cat.weight.lt((int) 3.1415)).parse();

        // parse( "from eg.Cat as enormouscat where fatcat.weight > 3.1415e3" );
        query().from(cat).where(cat.weight.gt((int) 3.1415e3)).parse();
    }

    @Test
    public void complexConstructor() throws Exception {
        // parse( "select new Foo(count(bar)) from bar" );
        query().select(new QFooDTO(bar.count())).from(bar).parse();

        // parse(
        // "select new Foo(count(bar),(select count(*) from doofus d where d.gob = 'fat' )) from bar"
        // );
//        query().select(
//                   new QFooDTO(bar.count(), 
//                   query().from(d).where(d.gob.eq("fat")).countExpr()))
//               .from(bar)
//               .parse();
    }

    @Test
    public void inNotIn() throws Exception {
        // parse( "from foo where foo.bar in ('a' , 'b', 'c')" );
        query().from(foo).where(foo.bar.in("a", "b", "c")).parse();

        // parse( "from foo where foo.bar not in ('a' , 'b', 'c')" );
        query().from(foo).where(foo.bar.notIn("a", "b", "c")).parse();
    }

    @Test
    public void operatorPrecedence() throws Exception {
        // parse( "from foo where foo.bar = 123 + foo.baz * foo.not" );
        // parse(
        // "from foo where foo.bar like 'testzzz' || foo.baz or foo.bar in ('duh', 'gob')"
        // );
    }

    @Test
    public void unnamedParameter() throws Exception {
        // parse(
        // "select foo, bar from org.hibernate.test.Foo foo left outer join foo.foo bar where foo = ?"
        // ); // Added '?' as a valid expression.
    }

    @Test
    public void inElements() throws Exception {
        // parse(
        // "from bar in class org.hibernate.test.Bar, foo in elements(bar.baz.fooArray)"
        // ); // Added collectionExpr as a valid 'in' clause.
    }

    @Test
    public void dotElements() throws Exception {
        // parse(
        // "select distinct foo from baz in class org.hibernate.test.Baz, foo in elements(baz.fooArray)"
        // );
        // parse(
        // "select foo from baz in class org.hibernate.test.Baz, foo in elements(baz.fooSet)"
        // );
        // parse(
        // "select foo from baz in class org.hibernate.test.Baz, foo in elements(baz.fooArray)"
        // );
        // parse(
        // "from org.hibernate.test.Baz baz where 'b' in elements(baz.collectionComponent.nested.foos) and 1.0 in elements(baz.collectionComponent.nested.floats)"
        // );
    }

    @Test
    public void testSelectAll() throws Exception {
        // parse(
        // "select all s, s.other from s in class org.hibernate.test.Simple where s = :s"
        // );
    }

    @Test
    public void testNot() throws Exception {
        // Cover NOT optimization in HqlParser
        // parse( "from eg.Cat cat where not ( cat.kittens.size < 1 )" );
        query().from(cat).where(cat.kittens.size().lt(1).not()).parse();

        // parse( "from eg.Cat cat where not ( cat.kittens.size > 1 )" );
        query().from(cat).where(cat.kittens.size().gt(1).not()).parse();

        // parse( "from eg.Cat cat where not ( cat.kittens.size >= 1 )" );
        query().from(cat).where(cat.kittens.size().goe(1).not()).parse();

        // parse( "from eg.Cat cat where not ( cat.kittens.size <= 1 )" );
        query().from(cat).where(cat.kittens.size().loe(1).not()).parse();

        // parse(
        // "from eg.DomesticCat cat where not ( cat.name between 'A' and 'B' ) "
        // );
        query().from(cat).where(cat.name.between("A", "B").not()).parse();

        // parse(
        // "from eg.DomesticCat cat where not ( cat.name not between 'A' and 'B' ) "
        // );
        query().from(cat).where(cat.name.notBetween("A", "B").not()).parse();

        // parse( "from eg.Cat cat where not ( not cat.kittens.size <= 1 )" );
        query().from(cat).where(cat.kittens.size().loe(1).not().not()).parse();

        // parse( "from eg.Cat cat where not  not ( not cat.kittens.size <= 1 )"
        // );
        query().from(cat).where(cat.kittens.size().loe(1).not().not().not()).parse();
    }

    @Test
    public void ejbqlExtensions() throws Exception {
        // parse(
        // "select object(a) from Animal a where a.mother member of a.offspring"
        // );
        // parse(
        // "select object(a) from Animal a where a.mother member a.offspring" );
        // //no member of
        // parse( "select object(a) from Animal a where a.offspring is empty" );
    }

    @Test
    public void hb1042() throws Exception {
        // parse(
        // "select x from fmc_web.pool.Pool x left join x.containers c0 where (upper(x.name) = upper(':') and c0.id = 1)"
        // );
    }

    @Test
    public void hhh354() throws Exception {
        // parse( "from Foo f where f.full = 'yep'");
    }

    @Test
    public void ejbqlKeywordsAsIdentifier() throws Exception {
        // parse(
        // "from org.hibernate.test.Bar bar where bar.object.id = ? and bar.object.class = ?"
        // );
    }

    @Test
    public void constructorIn() throws Exception {
        // parse(
        // "from org.hibernate.test.Bar bar where (b.x, b.y, b.z) in (select foo, bar, baz from org.hibernate.test.Foo)"
        // );
    }

    @Test
    public void hhh719() throws Exception {
        // Some SQLs have function names with package qualifiers.
        // parse("from Foo f order by com.fooco.SpecialFunction(f.id)");
    }

    @Test
    public void hhh1107() throws Exception {
        // parse("from Animal where zoo.address.street = '123 Bogus St.'");
    }

    @Test
    public void hhh1247() throws Exception {
        // parse("select distinct user.party from com.itf.iceclaims.domain.party.user.UserImpl user inner join user.party.$RelatedWorkgroups relatedWorkgroups where relatedWorkgroups.workgroup.id = :workgroup and relatedWorkgroups.effectiveTime.start <= :datesnow and relatedWorkgroups.effectiveTime.end > :dateenow ");
    }
    
    @Test
    public void fetch() throws RecognitionException, TokenStreamException{
        // correct single invocation
        query().from(cat).innerJoin(cat.mate, mate).fetch().parse();
        // duplicate invocation doesn't break anything, but looks stupid
        query().from(cat).innerJoin(cat.mate, mate).fetch().fetch().parse();
    }

    @Test
    public void testCasts() throws Exception {
        ENumber<Integer> bw = cat.bodyWeight;
        query().from(cat).select(bw.byteValue(), bw.doubleValue(), bw.floatValue(),
                bw.intValue(), bw.longValue(), bw.shortValue(),
                bw.stringValue()).parse();

        query().from(cat).select(bw.castToNum(Byte.class)).parse();
    }

    class TestQuery extends HQLQueryBase<TestQuery> {
        public TestQuery() {
            super(new DefaultQueryMetadata(), new HQLTemplates());
        }

        public long count() {
            return 0;
        }

        @Nullable    
        public Iterator<Object[]> iterate(Expr<?> first, Expr<?> second,
                Expr<?>... rest) {
            return null;
        }

        @Nullable
        public <RT> Iterator<RT> iterate(Expr<RT> projection) {
            return null;
        }

        @Nullable
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
                assertEquals("At least one error occurred during parsing "
                        + input, 0, parser.getParseErrorHandler()
                        .getErrorCount());
            } finally {
//                clear();
                System.out.println();
            }
        }
    }

}
