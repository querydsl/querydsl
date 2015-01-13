/*
 * Copyright 2011, Mysema Ltd
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

import static com.querydsl.core.Target.*;
import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.core.alias.Alias.alias;
import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.jpa.domain.*;
import com.querydsl.core.types.expr.ComparableExpression;
import com.querydsl.core.types.expr.DateExpression;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.testutil.ExcludeIn;

import antlr.RecognitionException;
import antlr.TokenStreamException;

public class ParsingTest extends AbstractQueryTest {

    @Test
    @Ignore
    public void ArrayExpr() throws Exception {
        query().from(ord).where(ord.items(0).id.eq(1234l)).parse();
    }

    @Test
    public void Basic() throws RecognitionException, TokenStreamException{
        query().from(cat, fatcat).select(cat.name, fatcat.name).parse();
    }

    @Test
    public void BeforeAndAfter() throws RecognitionException, TokenStreamException {
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
    public void ComplexConstructor() throws Exception {
        query().from(bar).select(new QFooDTO(bar.count())).parse();
    }

    @Test
    public void DocoExamples910() throws Exception {
        query().from(cat)
               .groupBy(cat.color)
               .select(cat.color, cat.weight.sum(), cat.count()).parse();
    }

    @Test
    public void DocoExamples910_2() throws Exception {
        query().from(cat)
               .groupBy(cat.color)
               .having(cat.color.in(Color.TABBY, Color.BLACK))
               .select(cat.color, cat.weight.sum(), cat.count()).parse();
    }

    @Test
    @Ignore
    public void DocoExamples910_3() throws Exception {
        query().from(cat).join(cat.kittens, kitten)
               .groupBy(cat)
               .having(kitten.weight.avg().gt(100.0))
               .orderBy(kitten.count().asc(), kitten.weight.sum().desc())
               .select(cat)
               .parse();
    }

    @Test
    public void DocoExamples911() throws Exception {
        query().from(fatcat).where(
                fatcat.weight.gt(sub().from(cat).unique(cat.weight.avg())))
                .parse();
    }

    @Test
    public void DocoExamples911_2() throws Exception {
        query().from(cat).where(
                cat.name.eqAny(sub().from(name).list(name.nickName)))
                .parse();
    }
    
    @Test
    public void DocoExamples911_3() throws Exception {
        query().from(cat).where(
                sub().from(mate).where(mate.mate.eq(cat)).list(mate).notExists())
                .parse();
    }
    
    @Test
    public void DocoExamples911_4() throws Exception {
        query().from(cat).where(
                sub().from(mate).where(mate.mate.eq(cat)).exists())
                .parse();
    }
    
    @Test
    public void DocoExamples911_5() throws Exception {
        query().from(cat).where(
                cat.name.notIn(sub().from(name).list(name.nickName)))
                .parse();
    }
    
    @Test
    public void DocoExamples912() throws Exception {
        query().from(ord, cust)
                .join(ord.lineItems, item).join(item.product, product)
                .from(catalog).join(catalog.prices, price).where(
                        ord.paid.not().and(ord.customer.eq(cust)).and(
                                price.product.eq(product)).and(
                                catalog.effectiveDate.gt(DateExpression.currentDate())).and(
                                catalog.effectiveDate.gtAny(
                                        sub().from(catalog).where(
                                                catalog.effectiveDate.lt(DateExpression.currentDate()))
                                             .list(catalog.effectiveDate))))
                .groupBy(ord).having(price.amount.sum().gt(0l))
                .orderBy(price.amount.sum().desc())
                .select(ord.id, price.amount.sum(), item.count());

        Customer c1 = new Customer();
        Catalog c2 = new Catalog();

        query().from(ord)
               .join(ord.lineItems, item).join(item.product, product)
               .from(catalog).join(catalog.prices, price).where(
                        ord.paid.not().and(ord.customer.eq(c1)).and(
                                price.product.eq(product)).and(catalog.eq(c2)))
                .groupBy(ord).having(price.amount.sum().gt(0l))
                .orderBy(price.amount.sum().desc())
                .select(ord.id, price.amount.sum(), item.count());

    }

    @Test
    public void DocoExamples92() throws Exception {
        query().from(cat).parse();
    }
    
    @Test
    public void DocoExamples92_2() throws Exception {
        query().from(cat).parse();
    }
    
    @Test
    public void DocoExamples92_3() throws Exception {
        query().from(form, param).parse();
    }
    
    @Test
    public void DocoExamples93() throws Exception {
        query().from(cat).innerJoin(cat.mate, mate).leftJoin(cat.kittens, kitten).parse();
    }
    
    @Test
    public void DocoExamples93_2() throws Exception {
        query().from(cat).leftJoin(cat.mate.kittens, kitten).parse();                
    }
    
    @Test
    public void DocoExamples93_3() throws Exception {
        query().from(cat).join(cat.mate, mate).leftJoin(cat.kittens, kitten).parse();        
    }
    
    @Test
    public void DocoExamples93_4() throws Exception {
        query().from(cat).innerJoin(cat.mate, mate).leftJoin(cat.kittens, kitten).parse();
    }

    @Test
    public void DocoExamples93_viaAlias() throws Exception {
        Cat c = alias(Cat.class, "cat");
        Cat k = alias(Cat.class, "kittens");
        Cat m = alias(Cat.class, "mate");

        query().from($(c)).innerJoin($(c.getMate()),$(m)).leftJoin($(c.getKittens()),$(k)).parse();
    }
    
    @Test
    public void DocoExamples93_viaAlias2() throws Exception {
        Cat c = alias(Cat.class, "cat");
        Cat k = alias(Cat.class, "kittens");
        
        query().from($(c)).leftJoin($(c.getMate().getKittens()),$(k)).parse();
    }
    
    @Test
    public void DocoExamples93_viaAlias3() throws Exception {
        Cat c = alias(Cat.class, "cat");
        Cat k = alias(Cat.class, "kittens");
        Cat m = alias(Cat.class, "mate");
     
        query().from($(c)).innerJoin($(c.getMate()),$(m)).leftJoin($(c.getKittens()),$(k)).parse();
    }
    
    @Test
    public void DocoExamples93_viaAlias4() throws Exception {
        Cat c = alias(Cat.class, "cat");
        Cat k = alias(Cat.class, "kittens");
        Cat m = alias(Cat.class, "mate");
        
        query().from($(c)).innerJoin($(c.getMate()),$(m)).leftJoin($(c.getKittens()),$(k)).parse();        
    }    

    @Test
    public void DocoExamples94() throws Exception {
        query().from(cat).innerJoin(cat.mate, mate).select(cat.mate).parse();
    }
    
    @Test
    public void DocoExamples94_2() throws Exception {
        query().from(cat).select(cat.mate).parse();
    }
    
    @Test
    @NoOpenJPA @NoBatooJPA
    public void DocoExamples94_3() throws Exception {
        query().from(cat).select(cat.kittens).parse();
    }
    
    @Test
    public void DocoExamples94_4() throws Exception {
        query().from(cust).select(cust.name.firstName).parse();
    }
    
    @Test
    public void DocoExamples94_5() throws Exception {
        query().from(mother)
        .innerJoin(mother.mate, mate)
        .leftJoin(mother.kittens, offspr)
        .select(mother, offspr, mate).parse();
    }
    
    @Test
    public void DocoExamples94_6() throws Exception {
        query().from(mother)
        .innerJoin(mother.mate, mate)
        .leftJoin(mother.kittens, kitten)
        .select(new QFamily(mother, mate, kitten)).parse();
    }

    @Test
    public void DocoExamples95() throws Exception {
        query().from(cat)
               .select(cat.weight.avg(), cat.weight.sum(), cat.weight.max(), cat.count())
               .parse();
    }

    @Test
    public void DocoExamples96() throws Exception {
        query().from(cat).parse();
    }
    
    @Test
    public void DocoExamples96_2() throws Exception {
        query().from(m, n).where(n.name.eq(m.name)).parse();
    }

    @Test
    @ExcludeIn(ORACLE)
    public void DocoExamples97() throws Exception {
        query().from(foo, bar).where(foo.startDate.eq(bar.date)).select(foo).parse();
    }
    
    @Test
    public void DocoExamples97_2() throws Exception {
        query().from(cat).where(cat.mate.name.isNotNull()).parse();
    }
    
    @Test
    public void DocoExamples97_3() throws Exception {
        query().from(cat, rival).where(cat.mate.eq(rival.mate)).parse();
    }
    
    @Test
    public void DocoExamples97_4() throws Exception {
        query().from(cat, mate).where(cat.mate.eq(mate)).select(cat, mate).parse();
    }
    
    @Test
    public void DocoExamples97_5() throws Exception {
        query().from(cat).where(cat.id.eq(123)).parse();
    }
    
    @Test
    public void DocoExamples97_6() throws Exception {
        query().from(cat).where(cat.mate.id.eq(69)).parse();
    }
    
    @Test
    public void DocoExamples97_7() throws Exception {
        query().from(person).where(
                person.pid.country.eq("AU"),
                person.pid.medicareNumber.eq(123456)).parse();
    }
    
    @Test
    public void DocoExamples97_8() throws Exception {
        query().from(account).where(account.owner.pid.medicareNumber.eq(123456)).parse();
    }
    
    @Test
    public void DocoExamples97_9() throws Exception {
        query().from(cat).where(cat.instanceOf(DomesticCat.class)).parse();
    }
    
    @Test
    @Ignore
    //@NoEclipseLink
    public void DocoExamples97_10() throws Exception {
        query().from(log, payment).where(
                log.item.instanceOf(Payment.class),
                log.item.id.eq(payment.id)).parse();
    }
    
    @Test
    public void DocoExamples97_10_2() throws Exception {
        query().from(log, payment).innerJoin(log.item, item).where(
                item.instanceOf(Payment.class),
                item.id.eq(payment.id)).parse();
    }
    
    @Test
    public void DocoExamples98_1() throws Exception {
        query().from(cat).where(cat.name.between("A", "B")).parse();
    }
    
    @Test
    public void DocoExamples98_2() throws Exception {
        query().from(cat).where(cat.name.in("Foo", "Bar", "Baz")).parse();
    }
    
    @Test
    public void DocoExamples98_3() throws Exception {
        query().from(cat).where(cat.name.notBetween("A", "B")).parse();
    }
    
    @Test
    public void DocoExamples98_4() throws Exception {
        query().from(cat).where(cat.name.notIn("Foo", "Bar", "Baz")).parse();
    }
    
    @Test
    public void DocoExamples98_5() throws Exception {
        query().from(cat).where(cat.kittens.size().gt(0)).parse();
    }
    
    @Test
    public void DocoExamples98_6() throws Exception {
        query().from(mother, kit).select(mother).where(kit.in(mother.kittens)).parse();
    }
    
    @Test
    @NoEclipseLink
    public void DocoExamples98_7() throws Exception {
        query().from(list, p).select(p).where(p.name.eqAny(list.names)).parse();
    }
    
    @Test
    public void DocoExamples98_8() throws Exception {
        query().from(cat).where(cat.kittens.isNotEmpty()).parse();
    }
    
    @Test
    public void DocoExamples98_9() throws Exception {
        query().from(person, calendar).select(person).where(
                calendar.holidays("national holiday").eq(person.birthDay),
                person.nationality.calendar.eq(calendar)).parse();
    }
    
    @Test
    @ExcludeIn({DERBY, HSQLDB, ORACLE})
    public void DocoExamples98_10() throws Exception {
        query().from(item, ord).select(item).where(
                ord.items(ord.deliveredItemIndices(0)).eq(item),
                ord.id.eq(1l)).parse();
    }
    
    @Test
    @NoEclipseLink
    @ExcludeIn({DERBY, HSQLDB, H2, MYSQL, ORACLE, POSTGRES})
    @Ignore
    public void DocoExamples98_11() throws Exception {
        query().from(item, ord).select(item).where(
                ord.items(ord.items.size().subtract(1)).eq(item))
                .parse();
    }
    
    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    @ExcludeIn({DERBY, HSQLDB, ORACLE})
    public void DocoExamples98_12() throws Exception {
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
    public void DocoExamples98() throws Exception {
        prod.eq(new Product());
        prod.eq(new QProduct("p"));
        prod.eq(new QItem("p"));

    }

    @Test
    public void DocoExamples99() throws Exception {
        query().from(cat).orderBy(cat.name.asc(), cat.weight.desc(),
                cat.birthdate.asc()).parse();
    }

    @Test
    public void DoubleLiteral() throws Exception {
        query().from(cat).where(cat.weight.lt((int) 3.1415)).parse();
    }
    
    @Test
    public void DoubleLiteral2() throws Exception {
        query().from(cat).where(cat.weight.gt((int) 3.1415e3)).parse();
    }

    @Test
    @NoOpenJPA
    public void Fetch() throws RecognitionException, TokenStreamException{
        query().from(cat).innerJoin(cat.mate, mate).fetch().parse();               
    }
    
    @Test
    @NoOpenJPA
    public void Fetch2() throws RecognitionException, TokenStreamException{
        query().from(cat).innerJoin(cat.mate, mate).fetch().fetch().parse();
    }

    @Test
    public void In() throws Exception {
        query().from(foo).where(foo.bar.in("a", "b", "c")).parse();        
    }
    
    @Test
    public void NotIn() throws Exception {
        query().from(foo).where(foo.bar.notIn("a", "b", "c")).parse();
    }

    @Test
    @NoEclipseLink @NoOpenJPA 
    public void JoinFlags1() throws RecognitionException, TokenStreamException{
        query().from(cat).fetchAll().parse();
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void JoinFlags2() throws RecognitionException, TokenStreamException{
        query().from(cat).fetchAll().from(cat1).fetchAll().parse();
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void JoinFlags3() throws RecognitionException, TokenStreamException{
        query().from(cat).fetchAll().from(cat1).fetchAll().parse();
    }

    @Test
    public void Joins() throws RecognitionException, TokenStreamException{
        query().from(cat).join(cat.mate, mate).select(cat).parse();
    }
    
    @Test
    public void InnerJoin() throws RecognitionException, TokenStreamException{
        query().from(cat).innerJoin(cat.mate, mate).select(cat).parse();
    }

    @Test
    public void LeftJoin()  throws RecognitionException, TokenStreamException{
        query().from(cat).leftJoin(cat.mate, mate).select(cat).parse();
    }
    
    @Test 
    @NoOpenJPA @NoBatooJPA
    public void Joins2() throws RecognitionException, TokenStreamException{
        query().from(cat).join(cat.mate, mate).on(mate.name.eq("Bob")).parse();
    }

    @Test
    public void MultipleFromClasses() throws Exception {
        query().from(qat, foo).parse();
    }

    @Test
    public void Serialization() {
        QueryHelper query = query();

        query.from(cat);
        assertEquals("select cat\nfrom Cat cat", query.toString());

        query.from(fatcat);
        assertEquals("select cat\nfrom Cat cat, Cat fatcat", query.toString());
    }

    @Test
    @NoEclipseLink @NoOpenJPA
    @ExcludeIn(MYSQL)
    public void Casts_Byte() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.byteValue()).parse();     
    }
    
    @Test
    @NoOpenJPA
    public void Casts_Double() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.doubleValue()).parse();     
    }
    
    @Test
    @NoOpenJPA
    @ExcludeIn(MYSQL)
    public void Casts_Float() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.floatValue()).parse();     
    }
    
    @Test
    @NoOpenJPA
    @ExcludeIn(MYSQL)
    public void Casts_Int() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.intValue()).parse();     
    }
    
    @Test
    @NoOpenJPA
    @ExcludeIn({DERBY, HSQLDB, MYSQL})
    public void Casts_Long() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.longValue()).parse();     
    }
    
    @Test
    @NoEclipseLink @NoOpenJPA
    @ExcludeIn(MYSQL)
    public void Casts_Short() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.shortValue()).parse();     
    }
    
    @Test
    @NoOpenJPA
    @ExcludeIn({DERBY, HSQLDB, MYSQL})
    public void Casts_String() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.stringValue()).parse();     
    }
    
    @Test
    @NoEclipseLink @NoOpenJPA
    @ExcludeIn(MYSQL)
    public void Casts_2() throws Exception {
        NumberExpression<Double> bw = cat.bodyWeight;
        query().from(cat).select(bw.castToNum(Byte.class)).parse();   
    }

    @Test
    @Ignore
    public void GroupBy() throws Exception {
        query().from(qat).groupBy(qat.breed).parse();
    }
    
    @Test
    @Ignore
    public void GroupBy_2() throws Exception {
        query().from(qat).groupBy(qat.breed, qat.eyecolor).parse();   
    }

    @Test
    public void Not() throws Exception {
        query().from(cat).where(cat.kittens.size().lt(1).not()).parse();
    }
    
    @Test
    public void Not_2() throws Exception {
        query().from(cat).where(cat.kittens.size().gt(1).not()).parse();   
    }
    
    @Test
    public void Not_3() throws Exception {
        query().from(cat).where(cat.kittens.size().goe(1).not()).parse();
    }
    
    @Test
    public void Not_4() throws Exception {
        query().from(cat).where(cat.kittens.size().loe(1).not()).parse();
    }
    
    @Test
    public void Not_5() throws Exception {
        query().from(cat).where(cat.name.between("A", "B").not()).parse();
    }
    
    @Test
    public void Not_6() throws Exception {
        query().from(cat).where(cat.name.notBetween("A", "B").not()).parse();
    }
    
    @Test
    public void Not_7() throws Exception {
        query().from(cat).where(cat.kittens.size().loe(1).not().not()).parse();
    }
    
    @Test
    public void Not_8() throws Exception {
        query().from(cat).where(cat.kittens.size().loe(1).not().not().not()).parse();
    }

        
    @Test
    @Ignore
    public void OrderBy() throws Exception {
        // NOT SUPPORTED
        query().from(qat).orderBy(qat.toes.avg().asc()).parse();
    }
   
    @Test
    @NoOpenJPA
    public void OrderBy_2() throws Exception {
        query().from(an).orderBy(an.bodyWeight.sqrt().divide(2.0).asc()).parse();
    }

    @Test
    public void Select() throws Exception {
//        querydsl().select(Ops.AggOps.COUNT_ALL_AGG_EXPR).from(qat).parse();

        query().from(qat).select(qat.weight.avg()).parse();
    }

    @Test
    @Ignore
    public void Sum() throws RecognitionException, TokenStreamException {
        // NOT SUPPORTED
        query().from(cat).select(cat.kittens.size().sum()).parse();
    }

    @Test
    @Ignore
    public void Sum_2() throws RecognitionException, TokenStreamException {
        // NOT SUPPORTED
        query().from(cat).where(cat.kittens.size().sum().gt(0)).select(cat).parse();
    }
    
    @Test
    public void Sum_3() throws RecognitionException, TokenStreamException {
        query().from(cat).where(cat.kittens.isEmpty()).select(cat).parse();
    }
    
    @Test
    public void Sum_4() throws RecognitionException, TokenStreamException {
        query().from(cat).where(cat.kittens.isNotEmpty()).select(cat).parse();
    }
    
    @Test
    public void Where() throws Exception {
        query().from(qat).where(qat.name.in("crater", "bean", "fluffy")).parse();
    }
    
    @Test
    public void Where_2() throws Exception {
        query().from(qat).where(qat.name.notIn("crater", "bean", "fluffy")).parse();
    }
    
    @Test
    public void Where_3() throws Exception {
        query().from(an).where(an.bodyWeight.sqrt().gt(10.0)).parse();
    }
    
    @Test
    public void Where_4() throws Exception {
        query().from(an).where(an.bodyWeight.sqrt().divide(2d).gt(10.0)).parse();
    }
    
    @Test
    public void Where_5() throws Exception {
        query().from(an).where(
                an.bodyWeight.gt(10),
                an.bodyWeight.lt(100).or(an.bodyWeight.isNull()))
            .parse();
    }

}
