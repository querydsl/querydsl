/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jpa;

import static com.querydsl.jpa.Constants.*;

import static com.querydsl.core.Target.*;
import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.core.alias.Alias.alias;
import static com.querydsl.jpa.JPAExpressions.select;
import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.domain.*;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class ParsingTest extends AbstractQueryTest {

    @Test
    @Ignore
    public void arrayExpr() throws Exception {
        query().from(ord).where(ord.items(0).id.eq(1234L)).parse();
    }

    @Test
    public void basic() throws RecognitionException, TokenStreamException {
        query().from(cat, fatcat).select(cat.name, fatcat.name).parse();
    }

    @Test
    @ExcludeIn(SQLSERVER)
    public void beforeAndAfter() throws RecognitionException, TokenStreamException {
        ComparableExpression<java.util.Date> ed = catalog.effectiveDate;
        query()
            .from(catalog)
            .where(
                ed.gt(DateExpression.currentDate()),
                ed.goe(DateExpression.currentDate()),
                ed.lt(DateExpression.currentDate()),
                ed.loe(DateExpression.currentDate()))
            .select(catalog).parse();
    }

    @Test
    @ExcludeIn(ORACLE)
    public void complexConstructor() throws Exception {
        query().from(bar).select(new QFooDTO(bar.count())).parse();
    }

    @Test
    public void docoExamples910() throws Exception {
        query().from(cat)
               .groupBy(cat.color)
               .select(cat.color, cat.weight.sum(), cat.count()).parse();
    }

    @Test
    public void docoExamples910_2() throws Exception {
        query().from(cat)
               .groupBy(cat.color)
               .having(cat.color.in(Color.TABBY, Color.BLACK))
               .select(cat.color, cat.weight.sum(), cat.count()).parse();
    }

    @Test
    @Ignore
    public void docoExamples910_3() throws Exception {
        query().from(cat).join(cat.kittens, kitten)
               .groupBy(cat)
               .having(kitten.weight.avg().gt(100.0))
               .orderBy(kitten.count().asc(), kitten.weight.sum().desc())
               .select(cat)
               .parse();
    }

    @Test
    public void docoExamples911() throws Exception {
        query().from(fatcat).where(
                fatcat.weight.gt(select(cat.weight.avg()).from(cat)))
                .parse();
    }

    @Test
    public void docoExamples911_2() throws Exception {
        query().from(cat).where(
                cat.name.eqAny(select(name.nickName).from(name)))
                .parse();
    }

    @Test
    public void docoExamples911_3() throws Exception {
        query().from(cat).where(
                select(mate).from(mate).where(mate.mate.eq(cat)).notExists())
                .parse();
    }

    @Test
    public void docoExamples911_4() throws Exception {
        query().from(cat).where(
                selectFrom(mate).where(mate.mate.eq(cat)).exists())
                .parse();
    }

    @Test
    public void docoExamples911_5() throws Exception {
        query().from(cat).where(
                cat.name.notIn(select(name.nickName).from(name)))
                .parse();
    }

    @Test
    public void docoExamples912() throws Exception {
        query().from(ord, cust)
                .join(ord.lineItems, item).join(item.product, product)
                .from(catalog).join(catalog.prices, price).where(
                        ord.paid.not().and(ord.customer.eq(cust)).and(
                                price.product.eq(product)).and(
                                catalog.effectiveDate.gt(DateExpression.currentDate())).and(
                                catalog.effectiveDate.gtAny(
                                        select(catalog.effectiveDate).from(catalog).where(
                                                catalog.effectiveDate.lt(DateExpression.currentDate())))))
                .groupBy(ord).having(price.amount.sum().gt(0L))
                .orderBy(price.amount.sum().desc())
                .select(ord.id, price.amount.sum(), item.count());

        Customer c1 = new Customer();
        Catalog c2 = new Catalog();

        query().from(ord)
               .join(ord.lineItems, item).join(item.product, product)
               .from(catalog).join(catalog.prices, price).where(
                        ord.paid.not().and(ord.customer.eq(c1)).and(
                                price.product.eq(product)).and(catalog.eq(c2)))
                .groupBy(ord).having(price.amount.sum().gt(0L))
                .orderBy(price.amount.sum().desc())
                .select(ord.id, price.amount.sum(), item.count());

    }

    @Test
    public void docoExamples92() throws Exception {
        query().from(cat).parse();
    }

    @Test
    public void docoExamples92_2() throws Exception {
        query().from(cat).parse();
    }

    @Test
    public void docoExamples92_3() throws Exception {
        query().from(form, param).parse();
    }

    @Test
    public void docoExamples93() throws Exception {
        query().from(cat).innerJoin(cat.mate, mate).leftJoin(cat.kittens, kitten).parse();
    }

    @Test
    public void docoExamples93_2() throws Exception {
        query().from(cat).leftJoin(cat.mate.kittens, kitten).parse();
    }

    @Test
    public void docoExamples93_3() throws Exception {
        query().from(cat).join(cat.mate, mate).leftJoin(cat.kittens, kitten).parse();
    }

    @Test
    public void docoExamples93_4() throws Exception {
        query().from(cat).innerJoin(cat.mate, mate).leftJoin(cat.kittens, kitten).parse();
    }

    @Test
    public void docoExamples93_viaAlias() throws Exception {
        Cat c = alias(Cat.class, "cat");
        Cat k = alias(Cat.class, "kittens");
        Cat m = alias(Cat.class, "mate");

        query().from($(c)).innerJoin($(c.getMate()),$(m)).leftJoin($(c.getKittens()),$(k)).parse();
    }

    @Test
    public void docoExamples93_viaAlias2() throws Exception {
        Cat c = alias(Cat.class, "cat");
        Cat k = alias(Cat.class, "kittens");

        query().from($(c)).leftJoin($(c.getMate().getKittens()),$(k)).parse();
    }

    @Test
    public void docoExamples93_viaAlias3() throws Exception {
        Cat c = alias(Cat.class, "cat");
        Cat k = alias(Cat.class, "kittens");
        Cat m = alias(Cat.class, "mate");

        query().from($(c)).innerJoin($(c.getMate()),$(m)).leftJoin($(c.getKittens()),$(k)).parse();
    }

    @Test
    public void docoExamples93_viaAlias4() throws Exception {
        Cat c = alias(Cat.class, "cat");
        Cat k = alias(Cat.class, "kittens");
        Cat m = alias(Cat.class, "mate");

        query().from($(c)).innerJoin($(c.getMate()),$(m)).leftJoin($(c.getKittens()),$(k)).parse();
    }

    @Test
    public void docoExamples94() throws Exception {
        query().from(cat).innerJoin(cat.mate, mate).select(cat.mate).parse();
    }

    @Test
    public void docoExamples94_2() throws Exception {
        query().from(cat).select(cat.mate).parse();
    }

    @Test
    @NoOpenJPA @NoBatooJPA
    public void docoExamples94_3() throws Exception {
        query().from(cat).select(cat.kittens).parse();
    }

    @Test
    public void docoExamples94_4() throws Exception {
        query().from(cust).select(cust.name.firstName).parse();
    }

    @Test
    public void docoExamples94_5() throws Exception {
        query().from(mother)
        .innerJoin(mother.mate, mate)
        .leftJoin(mother.kittens, offspr)
        .select(mother, offspr, mate).parse();
    }

    @Test
    public void docoExamples94_6() throws Exception {
        query().from(mother)
        .innerJoin(mother.mate, mate)
        .leftJoin(mother.kittens, kitten)
        .select(new QFamily(mother, mate, kitten)).parse();
    }

    @Test
    public void docoExamples95() throws Exception {
        query().from(cat)
               .select(cat.weight.avg(), cat.weight.sum(), cat.weight.max(), cat.count())
               .parse();
    }

    @Test
    public void docoExamples96() throws Exception {
        query().from(cat).parse();
    }

    @Test
    public void docoExamples96_2() throws Exception {
        query().from(m, n).where(n.name.eq(m.name)).parse();
    }

    @Test
    @ExcludeIn(ORACLE)
    public void docoExamples97() throws Exception {
        query().from(foo, bar).where(foo.startDate.eq(bar.date)).select(foo).parse();
    }

    @Test
    public void docoExamples97_2() throws Exception {
        query().from(cat).where(cat.mate.name.isNotNull()).parse();
    }

    @Test
    public void docoExamples97_3() throws Exception {
        query().from(cat, rival).where(cat.mate.eq(rival.mate)).parse();
    }

    @Test
    public void docoExamples97_4() throws Exception {
        query().from(cat, mate).where(cat.mate.eq(mate)).select(cat, mate).parse();
    }

    @Test
    public void docoExamples97_5() throws Exception {
        query().from(cat).where(cat.id.eq(123)).parse();
    }

    @Test
    public void docoExamples97_6() throws Exception {
        query().from(cat).where(cat.mate.id.eq(69)).parse();
    }

    @Test
    public void docoExamples97_7() throws Exception {
        query().from(person).where(
                person.pid.country.eq("AU"),
                person.pid.medicareNumber.eq(123456)).parse();
    }

    @Test
    public void docoExamples97_8() throws Exception {
        query().from(account).where(account.owner.pid.medicareNumber.eq(123456)).parse();
    }

    @Test
    public void docoExamples97_9() throws Exception {
        query().from(cat).where(cat.instanceOf(DomesticCat.class)).parse();
    }

    @Test
    @Ignore
    //@NoEclipseLink
    public void docoExamples97_10() throws Exception {
        query().from(log, payment).where(
                log.item.instanceOf(Payment.class),
                log.item.id.eq(payment.id)).parse();
    }

    @Test
    public void docoExamples97_10_2() throws Exception {
        query().from(log, payment).innerJoin(log.item, item).where(
                item.instanceOf(Payment.class),
                item.id.eq(payment.id)).parse();
    }

    @Test
    public void docoExamples98_1() throws Exception {
        query().from(cat).where(cat.name.between("A", "B")).parse();
    }

    @Test
    public void docoExamples98_2() throws Exception {
        query().from(cat).where(cat.name.in("Foo", "Bar", "Baz")).parse();
    }

    @Test
    public void docoExamples98_3() throws Exception {
        query().from(cat).where(cat.name.notBetween("A", "B")).parse();
    }

    @Test
    public void docoExamples98_4() throws Exception {
        query().from(cat).where(cat.name.notIn("Foo", "Bar", "Baz")).parse();
    }

    @Test
    public void docoExamples98_5() throws Exception {
        query().from(cat).where(cat.kittens.size().gt(0)).parse();
    }

    @Test
    public void docoExamples98_6() throws Exception {
        query().from(mother, kit).select(mother).where(kit.in(mother.kittens)).parse();
    }

    @Test
    @NoEclipseLink
    public void docoExamples98_7() throws Exception {
        query().from(list, p).select(p).where(p.name.eqAny(list.names)).parse();
    }

    @Test
    public void docoExamples98_8() throws Exception {
        query().from(cat).where(cat.kittens.isNotEmpty()).parse();
    }

    @Test
    public void docoExamples98_9() throws Exception {
        query().from(person, calendar).select(person).where(
                calendar.holidays("national holiday").eq(person.birthDay),
                person.nationality.calendar.eq(calendar)).parse();
    }

    @Test
    @ExcludeIn({DERBY, HSQLDB, ORACLE})
    public void docoExamples98_10() throws Exception {
        query().from(item, ord).select(item).where(
                ord.items(ord.deliveredItemIndices(0)).eq(item),
                ord.id.eq(1L)).parse();
    }

    @Test
    @NoEclipseLink
    @ExcludeIn({DERBY, HSQLDB, H2, MYSQL, ORACLE, POSTGRESQL})
    @Ignore
    public void docoExamples98_11() throws Exception {
        query().from(item, ord).select(item).where(
                ord.items(ord.items.size().subtract(1)).eq(item))
                .parse();
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    @ExcludeIn({DERBY, HSQLDB, ORACLE})
    public void docoExamples98_12() throws Exception {
        query()
        .from(prod, store)
        .innerJoin(store.customers, cust)
        .select(cust)
        .where(
            prod.name.eq("widget"),
            store.location.name.in("Melbourne", "Sydney"),
            prod.eqAll(cust.currentOrder.lineItems))
        .parse();

    }

    @Test
    public void docoExamples98() throws Exception {
        prod.eq(new Product());
        prod.eq(new QProduct("p"));
        prod.eq(new QItem("p"));

    }

    @Test
    public void docoExamples99() throws Exception {
        query().from(cat).orderBy(cat.name.asc(), cat.weight.desc(),
                cat.birthdate.asc()).parse();
    }

    @Test
    public void doubleLiteral() throws Exception {
        query().from(cat).where(cat.weight.lt((int) 3.1415)).parse();
    }

    @Test
    public void doubleLiteral2() throws Exception {
        query().from(cat).where(cat.weight.gt((int) 3.1415e3)).parse();
    }

    @Test
    @NoOpenJPA
    public void fetch() throws RecognitionException, TokenStreamException {
        query().from(cat).innerJoin(cat.mate, mate).fetchJoin().parse();
    }

    @Test
    @NoOpenJPA
    public void fetch2() throws RecognitionException, TokenStreamException {
        query().from(cat).innerJoin(cat.mate, mate).fetchJoin().fetchJoin().parse();
    }

    @Test
    public void in() throws Exception {
        query().from(foo).where(foo.bar.in("a", "b", "c")).parse();
    }

    @Test
    public void notIn() throws Exception {
        query().from(foo).where(foo.bar.notIn("a", "b", "c")).parse();
    }

    @Test
    @NoEclipseLink @NoOpenJPA
    public void joinFlags1() throws RecognitionException, TokenStreamException {
        query().from(cat).fetchAll().parse();
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void joinFlags2() throws RecognitionException, TokenStreamException {
        query().from(cat).fetchAll().from(cat1).fetchAll().parse();
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void joinFlags3() throws RecognitionException, TokenStreamException {
        query().from(cat).fetchAll().from(cat1).fetchAll().parse();
    }

    @Test
    public void joins() throws RecognitionException, TokenStreamException {
        query().from(cat).join(cat.mate, mate).select(cat).parse();
    }

    @Test
    public void innerJoin() throws RecognitionException, TokenStreamException {
        query().from(cat).innerJoin(cat.mate, mate).select(cat).parse();
    }

    @Test
    public void leftJoin()  throws RecognitionException, TokenStreamException {
        query().from(cat).leftJoin(cat.mate, mate).select(cat).parse();
    }

    @Test
    @NoOpenJPA @NoBatooJPA
    public void joins2() throws RecognitionException, TokenStreamException {
        query().from(cat).join(cat.mate, mate).on(mate.name.eq("Bob")).parse();
    }

    @Test
    public void multipleFromClasses() throws Exception {
        query().from(qat, foo).parse();
    }

    @Test
    public void serialization() {
        QueryHelper query = query();

        query.from(cat);
        assertEquals("select cat\nfrom Cat cat", query.toString());

        query.from(fatcat);
        assertEquals("select cat\nfrom Cat cat, Cat fatcat", query.toString());
    }

    @Test
    @NoEclipseLink @NoOpenJPA
    @ExcludeIn(MYSQL)
    public void casts_byte() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.byteValue()).parse();
    }

    @Test
    @NoOpenJPA
    public void casts_double() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.doubleValue()).parse();
    }

    @Test
    @NoOpenJPA
    @ExcludeIn(MYSQL)
    public void casts_float() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.floatValue()).parse();
    }

    @Test
    @NoOpenJPA
    @ExcludeIn(MYSQL)
    public void casts_int() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.intValue()).parse();
    }

    @Test
    @NoOpenJPA
    @ExcludeIn({DERBY, HSQLDB, MYSQL})
    public void casts_long() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.longValue()).parse();
    }

    @Test
    @NoEclipseLink @NoOpenJPA
    @ExcludeIn(MYSQL)
    public void casts_short() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.shortValue()).parse();
    }

    @Test
    @NoOpenJPA
    @ExcludeIn({DERBY, HSQLDB, MYSQL})
    public void casts_string() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.stringValue()).parse();
    }

    @Test
    @NoEclipseLink @NoOpenJPA
    @ExcludeIn(MYSQL)
    public void casts_2() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.castToNum(Byte.class)).parse();
    }

    @Test
    @Ignore
    public void groupBy() throws Exception {
        query().from(qat).groupBy(qat.breed).parse();
    }

    @Test
    @Ignore
    public void groupBy_2() throws Exception {
        query().from(qat).groupBy(qat.breed, qat.eyecolor).parse();
    }

    @Test
    public void not() throws Exception {
        query().from(cat).where(cat.kittens.size().lt(1).not()).parse();
    }

    @Test
    public void not_2() throws Exception {
        query().from(cat).where(cat.kittens.size().gt(1).not()).parse();
    }

    @Test
    public void not_3() throws Exception {
        query().from(cat).where(cat.kittens.size().goe(1).not()).parse();
    }

    @Test
    public void not_4() throws Exception {
        query().from(cat).where(cat.kittens.size().loe(1).not()).parse();
    }

    @Test
    public void not_5() throws Exception {
        query().from(cat).where(cat.name.between("A", "B").not()).parse();
    }

    @Test
    public void not_6() throws Exception {
        query().from(cat).where(cat.name.notBetween("A", "B").not()).parse();
    }

    @Test
    public void not_7() throws Exception {
        query().from(cat).where(cat.kittens.size().loe(1).not().not()).parse();
    }

    @Test
    public void not_8() throws Exception {
        query().from(cat).where(cat.kittens.size().loe(1).not().not().not()).parse();
    }


    @Test
    @Ignore
    public void orderBy() throws Exception {
        // NOT SUPPORTED
        query().from(qat).orderBy(qat.toes.avg().asc()).parse();
    }

    @Test
    @NoOpenJPA
    public void orderBy_2() throws Exception {
        query().from(an).orderBy(an.bodyWeight.sqrt().divide(2.0).asc()).parse();
    }

    @Test
    public void select1() throws Exception {
//        query().select(Ops.AggOps.COUNT_ALL_AGG_EXPR).from(qat).parse();

        query().from(qat).select(qat.weight.avg()).parse();
    }

    @Test
    @Ignore
    public void sum() throws RecognitionException, TokenStreamException {
        // NOT SUPPORTED
        query().from(cat).select(cat.kittens.size().sum()).parse();
    }

    @Test
    @Ignore
    public void sum_2() throws RecognitionException, TokenStreamException {
        // NOT SUPPORTED
        query().from(cat).where(cat.kittens.size().sum().gt(0)).select(cat).parse();
    }

    @Test
    public void sum_3() throws RecognitionException, TokenStreamException {
        query().from(cat).where(cat.kittens.isEmpty()).select(cat).parse();
    }

    @Test
    public void sum_4() throws RecognitionException, TokenStreamException {
        query().from(cat).where(cat.kittens.isNotEmpty()).select(cat).parse();
    }

    @Test
    public void where() throws Exception {
        query().from(qat).where(qat.name.in("crater", "bean", "fluffy")).parse();
    }

    @Test
    public void where_2() throws Exception {
        query().from(qat).where(qat.name.notIn("crater", "bean", "fluffy")).parse();
    }

    @Test
    public void where_3() throws Exception {
        query().from(an).where(an.bodyWeight.sqrt().gt(10.0)).parse();
    }

    @Test
    public void where_4() throws Exception {
        query().from(an).where(an.bodyWeight.sqrt().divide(2d).gt(10.0)).parse();
    }

    @Test
    public void where_5() throws Exception {
        query().from(an).where(
                an.bodyWeight.gt(10),
                an.bodyWeight.lt(100).or(an.bodyWeight.isNull()))
            .parse();
    }

}
