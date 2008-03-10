package com.mysema.query.grammar.hql;

import static com.mysema.query.grammar.Grammar.avg;
import static com.mysema.query.grammar.Grammar.max;
import static com.mysema.query.grammar.HqlGrammar.count;
import static com.mysema.query.grammar.HqlGrammar.distinct;
import static com.mysema.query.grammar.HqlGrammar.maxindex;
import static com.mysema.query.grammar.HqlGrammar.sum;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.hibernate.hql.ast.HqlParser;
import org.junit.Test;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;

import com.mysema.query.Domain1;
import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.hql.domain.DomesticCat;
import com.mysema.query.grammar.hql.domain.Payment;


/**
 * QueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlParserTest extends HqlQueryBase<HqlParserTest>{
    
    private Domain1.Account account = new Domain1.Account("account");
    
    private Domain1.AuditLog log = new Domain1.AuditLog("log");
    
    private Domain1.Bar bar = new Domain1.Bar("bar");
    
    private Domain1.Calendar calendar = new Domain1.Calendar("calendar");
    
    private Domain1.Cat cat = new Domain1.Cat("cat");
    private Domain1.Cat kittens = new Domain1.Cat("kittens");
    private Domain1.Cat kitten = new Domain1.Cat("kitten");
    private Domain1.Cat kit = new Domain1.Cat("kit");
    private Domain1.Cat mate = new Domain1.Cat("mate");
    private Domain1.Cat mother = new Domain1.Cat("mother");
    private Domain1.Cat offspr = new Domain1.Cat("offspr");
    private Domain1.Cat rival = new Domain1.Cat("rival");
    
    private Domain1.Customer cust = new Domain1.Customer("cust");
    
    private Domain1.Foo foo = new Domain1.Foo("foo");
    
    private Domain1.Formula form = new Domain1.Formula("form");
    
    private Domain1.Order ord = new Domain1.Order("ord");
    
    private Domain1.Payment payment = new Domain1.Payment("payment");
    
    private Domain1.Parameter param = new Domain1.Parameter("param");
    
    private Domain1.Person person = new Domain1.Person("person");
    
    /**
     * Section 9.2 - from *
     */
    @Test
    public void testDocoExamples92() throws Exception {        
//        parse( "from eg.Cat" );
        from(cat).parse();
//        parse( "from eg.Cat as cat" );
//        parse( "from eg.Cat cat" );
//        parse( "from Formula, Parameter" );
        from(form, param).parse();
//        parse( "from Formula as form, Parameter as param" );
        from(form, param).parse();
    }

    /**
     * Section 9.3 - Associations and joins *
     */
    @Test
    public void testDocoExamples93() throws Exception {
//        parse( "from eg.Cat as cat inner join cat.mate as mate left outer join cat.kittens as kitten" );
        from(cat).innerJoin(cat.mate.as(mate)).leftJoin(cat.kittens.as(kitten)).parse();
//        parse( "from eg.Cat as cat left join cat.mate.kittens as kittens" );
        from(cat).leftJoin(cat.mate().kittens.as(kittens)).parse();
//        parse( "from Formula form full join form.parameter param" );
        from(form).fullJoin(form.parameter.as(param)).parse();
//        parse( "from eg.Cat as cat join cat.mate as mate left join cat.kittens as kitten" );
        from(cat).join(cat.mate.as(mate)).leftJoin(cat.kittens.as(kitten)).parse();
//        parse( "from eg.Cat as cat\ninner join fetch cat.mate\nleft join fetch cat.kittens" );
        from(cat).innerJoin(cat.mate).leftJoin(cat.kittens).parse();
    }

    /**
     * Section 9.4 - Select *
     */
    @Test
    public void testDocoExamples94() throws Exception {        
//        parse( "select mate from eg.Cat as cat inner join cat.mate as mate" );
        select(mate).from(cat).innerJoin(cat.mate.as(mate)).parse();
//        parse( "select cat.mate from eg.Cat cat" );
        select(cat.mate).from(cat).parse();
//        parse( "select elements(cat.kittens) from eg.Cat cat" );
        select(cat.kittens).from(cat).parse();
//        parse( "select cat.name from eg.DomesticCat cat where cat.name like 'fri%'" );
        select(cat.name).from(cat).where(cat.name.like("fri%")).parse();
//        parse( "select cust.name.firstName from Customer as cust" );
        select(cust.name().firstName).from(cust).parse();
//        parse( "select mother, offspr, mate.name from eg.DomesticCat\n"
//                + " as mother inner join mother.mate as mate left outer join\n"
//                + "mother.kittens as offspr" );
        select(mother, offspr, mate.name).from(mother)
            .innerJoin(mother.mate.as(mate)).leftJoin(mother.kittens.as(offspr)).parse();
//        parse( "select new Family(mother, mate, offspr)\n"
//                + "from eg.DomesticCat as mother\n"
//                + "join mother.mate as mate\n"
//                + "left join mother.kittens as offspr\n" );
        select(new Domain1.Family(mother, mate, offspr))
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
        select(avg(cat.weight), sum(cat.weight), max(cat.weight), count(cat)).from(cat).parse();
//        parse( "select cat, count( elements(cat.kittens) )\n"
//                + " from eg.Cat cat group by cat" );
        select(cat, count(cat.kittens)).from(cat).groupBy(cat).parse();
//        parse( "select distinct cat.name from eg.Cat cat" );
        select(distinct(cat.name)).from(cat).parse();
//        parse( "select count(distinct cat.name), count(cat) from eg.Cat cat" );
        select(count(distinct(cat.name)), count(cat)).from(cat).parse();
    }

    /**
     * Section 9.6 - Polymorphism *
     */
    @Test
    public void testDocoExamples96() throws Exception {
//        parse( "from eg.Cat as cat" );
        from(cat).parse();
//        parse( "from java.lang.Object o" );
//        parse( "from eg.Named n, eg.Named m where n.name = m.name" );
    }

    /**
     * Section 9.7 - Where *
     */
    @Test
    public void testDocoExamples97() throws Exception {
//        parse( "from eg.Cat as cat where cat.name='Fritz'" );
        from(cat).where(cat.name.like("Fritz")).parse();
//        parse( "select foo\n"
//                + "from eg.Foo foo, eg.Bar bar\n"
//                + "where foo.startDate = bar.date\n" );
        select(foo).from(foo, bar).where(foo.startDate.eq(bar.date)).parse();
//        parse( "from eg.Cat cat where cat.mate.name is not null" );
        from(cat).where(cat.mate().name.isnotnull()).parse();
//        parse( "from eg.Cat cat, eg.Cat rival where cat.mate = rival.mate" );
        from(cat, rival).where(cat.mate.eq(rival.mate)).parse();
//        parse( "select cat, mate\n"
//                + "from eg.Cat cat, eg.Cat mate\n"
//                + "where cat.mate = mate" );
        select(cat, mate).from(cat, mate).where(cat.mate.eq(mate)).parse();
//        parse( "from eg.Cat as cat where cat.id = 123" );
        from(cat).where(cat.id.eq(123)).parse();
//        parse( "from eg.Cat as cat where cat.mate.id = 69" );
        from(cat).where(cat.mate().id.eq(69)).parse();
//        parse( "from bank.Person person\n"
//                + "where person.id.country = 'AU'\n"
//                + "and person.id.medicareNumber = 123456" );
        from(person).where(person.id().country.eq("AU").and(person.id().medicareNumber.eq(123456))).parse();
//        parse( "from bank.Account account\n"
//                + "where account.owner.id.country = 'AU'\n"
//                + "and account.owner.id.medicareNumber = 123456" );
        from(account).where(account.owner().id().medicareNumber.eq(123456)).parse();
//        parse( "from eg.Cat cat where cat.class = eg.DomesticCat" );
        from(cat).where(cat.typeOf(DomesticCat.class)).parse();
//        parse( "from eg.AuditLog log, eg.Payment payment\n"
//                + "where log.item.class = 'eg.Payment' and log.item.id = payment.id" );
        from(log, payment).where(log.item.typeOf(Payment.class).and(log.item().id.eq(payment.id))).parse();
    }

    /**
     * Section 9.8 - Expressions *
     */
    @Test
    public void testDocoExamples98() throws Exception {
//        parse( "from eg.DomesticCat cat where cat.name between 'A' and 'B'" );
        from(cat).where(cat.name.between("A", "B")).parse();
//        parse( "from eg.DomesticCat cat where cat.name in ( 'Foo', 'Bar', 'Baz' )" );
        from(cat).where(cat.name.in("Foo","Bar","Baz")).parse();
//        parse( "from eg.DomesticCat cat where cat.name not between 'A' and 'B'" );
        from(cat).where(cat.name.notBetween("A", "B")).parse();
//        parse( "from eg.DomesticCat cat where cat.name not in ( 'Foo', 'Bar', 'Baz' )" );
        from(cat).where(cat.name.notIn("Foo","Bar","Baz")).parse();
//        parse( "from eg.Cat cat where cat.kittens.size > 0" );
        from(cat).where(cat.kittens.size().gt(0)).parse();        
//        parse( "from eg.Cat cat where size(cat.kittens) > 0" );
        from(cat).where(cat.kittens.size().gt(0)).parse();
//        parse( "from Order ord where maxindex(ord.items) > 100" );
        from(ord).where(maxindex(ord.items).gt(100)).parse();
//        parse( "from Order ord where minelement(ord.items) > 10000" );
        // TODO
//
//        parse( "select mother from eg.Cat as mother, eg.Cat as kit\n"
//                + "where kit in elements(foo.kittens)" );
        select(mother).from(mother, kit).where(kit.in(mother.kittens)).parse();
//        parse( "select p from eg.NameList list, eg.Person p\n"
//                + "where p.name = some elements(list.names)" );
        // TODO
//        parse( "from eg.Cat cat where exists elements(cat.kittens)" );
        // TODO
//        parse( "from eg.Player p where 3 > all elements(p.scores)" );
        // TODO
//        parse( "from eg.Show show where 'fizard' in indices(show.acts)" );
        // TODO
//        parse( "from Order ord where ord.items[0].id = 1234" );
        from(ord).where(ord.items(0).id.eq(1234l)).parse();
//        parse( "select person from Person person, Calendar calendar\n"
//                + "where calendar.holidays['national day'] = person.birthDay\n"
//                + "and person.nationality.calendar = calendar" );
        // TODO
//        parse( "select item from Item item, Order ord\n"
//                + "where ord.items[ ord.deliveredItemIndices[0] ] = item and ord.id = 11" );
//        parse( "select item from Item item, Order ord\n"
//                + "where ord.items[ maxindex(ord.items) ] = item and ord.id = 11" );
//
//        parse( "select item from Item item, Order ord\n"
//                + "where ord.items[ size(ord.items) - 1 ] = item" );
//
//        parse( "from eg.DomesticCat cat where upper(cat.name) like 'FRI%'" );
//
//        parse( "select cust from Product prod, Store store\n"
//                + "inner join store.customers cust\n"
//                + "where prod.name = 'widget'\n"
//                + "and store.location.name in ( 'Melbourne', 'Sydney' )\n"
//                + "and prod = all elements(cust.currentOrder.lineItems)" );

    }

    @Test
    public void testDocoExamples99() throws Exception {
//        parse( "from eg.DomesticCat cat\n"
//                + "order by cat.name asc, cat.weight desc, cat.birthdate" );
    }

    @Test
    public void testDocoExamples910() throws Exception {
//        parse( "select cat.color, sum(cat.weight), count(cat)\n"
//                + "from eg.Cat cat group by cat.color" );
//        parse( "select foo.id, avg( elements(foo.names) ), max( indices(foo.names) )\n"
//                + "from eg.Foo foo group by foo.id" );
//        parse( "select cat.color, sum(cat.weight), count(cat)\n"
//                + "from eg.Cat cat group by cat.color\n"
//                + "having cat.color in (eg.Color.TABBY, eg.Color.BLACK)" );
//        parse( "select cat from eg.Cat cat join cat.kittens kitten\n"
//                + "group by cat having avg(kitten.weight) > 100\n"
//                + "order by count(kitten) asc, sum(kitten.weight) desc" );
    }

    @Test
    public void testDocoExamples911() throws Exception {
//        parse( "from eg.Cat as fatcat where fatcat.weight > (\n"
//                + "select avg(cat.weight) from eg.DomesticCat cat)" );
//        parse( "from eg.DomesticCat as cat where cat.name = some (\n"
//                + "select name.nickName from eg.Name as name)\n" );
//        parse( "from eg.Cat as cat where not exists (\n"
//                + "from eg.Cat as mate where mate.mate = cat)" );
//        parse( "from eg.DomesticCat as cat where cat.name not in (\n"
//                + "select name.nickName from eg.Name as name)" );
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
//
//        parse( "select ord.id, sum(price.amount), count(item)\n"
//                + "from Order as ord join ord.lineItems as item join item.product as product,\n"
//                + "Catalog as catalog join catalog.prices as price\n"
//                + "where ord.paid = false and ord.customer = :customer\n"
//                + "and price.product = product and catalog = :currentCatalog\n"
//                + "group by ord having sum(price.amount) > :minAmount\n"
//                + "order by sum(price.amount) desc" );
//
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
//        parse( "select count(payment), status.name \n"
//                + "from Payment as payment\n"
//                + "    join payment.currentStatus as status\n"
//                + "where payment.status.name <> PaymentStatus.AWAITING_APPROVAL\n"
//                + "    or payment.statusChanges[ maxIndex(payment.statusChanges) ].user <> :currentUser\n"
//                + "group by status.name, status.sortOrder\n"
//                + "order by status.sortOrder" );
//        parse( "select account, payment\n"
//                + "from Account as account\n"
//                + "    left outer join account.payments as payment\n"
//                + "where :currentUser in elements(account.holder.users)\n"
//                + "    and PaymentStatus.UNPAID = isNull(payment.currentStatus.name, PaymentStatus.UNPAID)\n"
//                + "order by account.type.sortOrder, account.accountNumber, payment.dueDate" );
//        parse( "select account, payment\n"
//                + "from Account as account\n"
//                + "    join account.holder.users as user\n"
//                + "    left outer join account.payments as payment\n"
//                + "where :currentUser = user\n"
//                + "    and PaymentStatus.UNPAID = isNull(payment.currentStatus.name, PaymentStatus.UNPAID)\n"
//                + "order by account.type.sortOrder, account.accountNumber, payment.dueDate" );
    }

    @Test
    public void testExamples1() throws Exception {
//        parse( "select new org.hibernate.test.S(s.count, s.address)\n"
//                + "from s in class Simple" );
//        parse( "select s.name, sysdate, trunc(s.pay), round(s.pay) from s in class Simple" );
//        parse( "select round(s.pay, 2) from s" );
//        parse( "select abs(round(s.pay)) from s in class Simple" );
//        parse( "select trunc(round(sysdate)) from s in class Simple" );
    }

    @Test
    public void testArrayExpr() throws Exception {
//        parse( "from Order ord where ord.items[0].id = 1234" );
    }

    @Test
    public void testMultipleActualParameters() throws Exception {
//        parse( "select round(s.pay, 2) from s" );
    }

    @Test
    public void testMultipleFromClasses() throws Exception {
//        parse( "FROM eg.mypackage.Cat qat, com.toadstool.Foo f" );
//        parse( "FROM eg.mypackage.Cat qat, org.jabberwocky.Dipstick" );
    }

    @Test
    public void testFromWithJoin() throws Exception {
//        parse( "FROM eg.mypackage.Cat qat, com.toadstool.Foo f join net.sf.blurb.Blurb" );
//        parse( "FROM eg.mypackage.Cat qat  left join com.multijoin.JoinORama , com.toadstool.Foo f join net.sf.blurb.Blurb" );
    }

    @Test
    public void testSelect() throws Exception {
//        parse( "SELECT f FROM eg.mypackage.Cat qat, com.toadstool.Foo f join net.sf.blurb.Blurb" );
//        parse( "SELECT DISTINCT bar FROM eg.mypackage.Cat qat  left join com.multijoin.JoinORama as bar, com.toadstool.Foo f join net.sf.blurb.Blurb" );
//        parse( "SELECT count(*) FROM eg.mypackage.Cat qat" );
//        parse( "SELECT avg(qat.weight) FROM eg.mypackage.Cat qat" );
    }

    @Test
    public void testWhere() throws Exception {
//        parse( "FROM eg.mypackage.Cat qat where qat.name like '%fluffy%' or qat.toes > 5" );
//        parse( "FROM eg.mypackage.Cat qat where not qat.name like '%fluffy%' or qat.toes > 5" );
//        parse( "FROM eg.mypackage.Cat qat where not qat.name not like '%fluffy%'" );
//        parse( "FROM eg.mypackage.Cat qat where qat.name in ('crater','bean','fluffy')" );
//        parse( "FROM eg.mypackage.Cat qat where qat.name not in ('crater','bean','fluffy')" );
//        parse( "from Animal an where sqrt(an.bodyWeight)/2 > 10" );
//        parse( "from Animal an where (an.bodyWeight > 10 and an.bodyWeight < 100) or an.bodyWeight is null" );
    }

    @Test
    public void testGroupBy() throws Exception {
//        parse( "FROM eg.mypackage.Cat qat group by qat.breed" );
//        parse( "FROM eg.mypackage.Cat qat group by qat.breed, qat.eyecolor" );
    }

    @Test
    public void testOrderBy() throws Exception {
//        parse( "FROM eg.mypackage.Cat qat order by avg(qat.toes)" );
//        parse( "from Animal an order by sqrt(an.bodyWeight)/2" );
    }

    @Test
    public void testDoubleLiteral() throws Exception {
//        parse( "from eg.Cat as tinycat where fatcat.weight < 3.1415" );
//        parse( "from eg.Cat as enormouscat where fatcat.weight > 3.1415e3" );
    }

    @Test
    public void testComplexConstructor() throws Exception {
//        parse( "select new Foo(count(bar)) from bar" );
//        parse( "select new Foo(count(bar),(select count(*) from doofus d where d.gob = 'fat' )) from bar" );
    }

    @Test
    public void testInNotIn() throws Exception {
//        parse( "from foo where foo.bar in ('a' , 'b', 'c')" );
//        parse( "from foo where foo.bar not in ('a' , 'b', 'c')" );
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
//        parse( "from eg.Cat cat where not ( cat.kittens.size > 1 )" );
//        parse( "from eg.Cat cat where not ( cat.kittens.size >= 1 )" );
//        parse( "from eg.Cat cat where not ( cat.kittens.size <= 1 )" );
//        parse( "from eg.DomesticCat cat where not ( cat.name between 'A' and 'B' ) " );
//        parse( "from eg.DomesticCat cat where not ( cat.name not between 'A' and 'B' ) " );
//        parse( "from eg.Cat cat where not ( not cat.kittens.size <= 1 )" );
//        parse( "from eg.Cat cat where not  not ( not cat.kittens.size <= 1 )" );
    }

    @Test
    public void testOtherSyntax() throws Exception {
//        parse( "select bar from org.hibernate.test.Bar bar order by ((bar.x - :valueX)*(bar.x - :valueX))" );
//        parse( "from bar in class org.hibernate.test.Bar, foo in elements(bar.baz.fooSet)" );
//        parse( "from one in class org.hibernate.test.One, many in elements(one.manies) where one.id = 1 and many.id = 1" );
//        parse( "from org.hibernate.test.Inner _inner join _inner.middles middle" );
//        parse( "FROM m IN CLASS org.hibernate.test.Master WHERE NOT EXISTS ( FROM d IN elements(m.details) WHERE NOT d.i=5 )" );
//        parse( "FROM m IN CLASS org.hibernate.test.Master WHERE NOT 5 IN ( SELECT d.i FROM d IN elements(m.details) )" );
//        parse( "SELECT m FROM m IN CLASS org.hibernate.test.Master, d IN elements(m.details) WHERE d.i=5" );
//        parse( "SELECT m FROM m IN CLASS org.hibernate.test.Master, d IN elements(m.details) WHERE d.i=5" );
//        parse( "SELECT m.id FROM m IN CLASS org.hibernate.test.Master, d IN elements(m.details) WHERE d.i=5" );
        // I'm not sure about these... [jsd]
//        parse("select bar.string, foo.string from bar in class org.hibernate.test.Bar inner join bar.baz as baz inner join elements(baz.fooSet) as foo where baz.name = 'name'");
//        parse("select bar.string, foo.string from bar in class org.hibernate.test.Bar, bar.baz as baz, elements(baz.fooSet) as foo where baz.name = 'name'");
//        parse("select count(*) where this.amount>-1 and this.name is null");
//        parse("from sm in class org.hibernate.test.SubMulti where exists sm.children.elements");
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
    public void testKeywordInPath() throws Exception {
//        // The keyword 'order' used as a property name.
//        parse( "from Customer c where c.order.status = 'argh'" );
//        // The keyword 'order' and 'count' used as a property name.
//        parse( "from Customer c where c.order.count > 3" );
//        // The keywords 'where', 'order' and 'count' used as a property name.
//        parse( "select c.where from Customer c where c.order.count > 3" );
//        parse( "from Interval i where i.end <:end" );
//        parse( "from Letter l where l.case = :case" );
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
    
    private void parse() throws RecognitionException, TokenStreamException{
        String input = toString();
        System.out.println( "input: " + input.replace('\n', ' '));
        HqlParser parser = HqlParser.getInstance( input );
        parser.setFilter( false );
        parser.statement();
        AST ast = parser.getAST();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        parser.showAst( ast, new PrintStream( baos ) );
        try{
            assertEquals( "At least one error occurred during parsing " + input, 0, parser.getParseErrorHandler().getErrorCount() );            
        }finally{
            clear();
        }
        
    }
    

}
