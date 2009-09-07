/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.hql.HQLGrammar.sum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.hql.domain.QAccount;
import com.mysema.query.hql.domain.QInheritedProperties;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.custom.CString;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * FeaturesTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class FeaturesTest extends AbstractQueryTest{

    @Test
    public void testDomainConstruction() {
        QInheritedProperties i = new QInheritedProperties("i");
        assertNotNull(i.superclassProperty);
        assertNotNull(i.classProperty);

        QAccount a = new QAccount("a");
        assertNotNull(a.embeddedData.someData);
    }

    @Test
    public void testBasicStructure() {
        assertNull(cat.getMetadata().getParent());
        assertEquals(cat, cat.alive.getMetadata().getParent());
        assertEquals("cat", cat.getMetadata().getExpression().toString());
    }

    @Test
    public void testArgumentHandling() {
        // Kitty is reused, so it should be used via one named parameter
        toString(
                "cat.name = :a1 or cust.name.firstName = :a2 or kitten.name = :a1",
                cat.name.eq("Kitty").or(cust.name.firstName.eq("Hans")).or(
                        kitten.name.eq("Kitty")));
    }



    @Test
    public void testBasicOperations() {
        toString("cat.bodyWeight = kitten.bodyWeight", cat.bodyWeight
                .eq(kitten.bodyWeight));
        toString("cat.bodyWeight != kitten.bodyWeight", cat.bodyWeight
                .ne(kitten.bodyWeight));

        toString("cat.bodyWeight + kitten.bodyWeight = kitten.bodyWeight", 
                cat.bodyWeight.add(kitten.bodyWeight).eq(kitten.bodyWeight));
    }

    @Test
    public void testBooleanOpeations() {
        toString("cust is null or cat is null", cust.isNull().or(cat.isNull()));
        toString("cust is null and cat is null", cust.isNull()
                .and(cat.isNull()));
        toString("not (cust is null)", cust.isNull().not());
        cat.name.eq(cust.name.firstName).and(
                cat.bodyWeight.eq(kitten.bodyWeight));
        cat.name.eq(cust.name.firstName).or(
                cat.bodyWeight.eq(kitten.bodyWeight));
    }

    /**
     * The Class MyCustomExpr.
     */
    public static class MyCustomExpr extends CString {
        public MyCustomExpr(Expr<?>... args) {
            super(Arrays.asList(args), new TemplateFactory().create("myCustom({0},{1})"));
        }
    }

    @Test
    public void testCustomExpressions() {
        toString("myCustom(cust,cat)", new MyCustomExpr(cust, cat));
    }

    @Test
    public void testCastOperations() {
        // cast(... as ...), where the second argument is the name of a
        // Hibernate type, and extract(... from ...) if ANSI cast() and
        // extract() is supported by the underlying database
    }

    @Test
    public void testCollectionOperations() {
        // HQL functions that take collection-valued path expressions: size(),
        // minelement(), maxelement(), minindex(), maxindex(), along with the
        // special elements() and indices functions which may be quantified
        // using some, all, exists, any, in.
        cat.kittens.size();
//        minelement(cat.kittens);
//        maxelement(cat.kittens);
//        minindex(cat.kittens);
//        maxindex(cat.kittens);
        toString("cat.kittens[0]", cat.kittens(0));
        toString("cat.kittens[0]", cat.kittens.get(0));

        // some, all, exists, any, in.
    }

    @Test
    public void testConstructors() {
        EConstructor<com.mysema.query.hql.domain.Cat> c = new EConstructor<com.mysema.query.hql.domain.Cat>(
                com.mysema.query.hql.domain.Cat.class, cat.name);
        toString("new " + com.mysema.query.hql.domain.Cat.class.getName()
                + "(cat.name)", c);
        toString("new " + getClass().getName() + "$BookmarkDTO()",
                new _BookmarkDTO());
        toString("new " + getClass().getName() + "$BookmarkDTO(cat.name)",
                new _BookmarkDTO(cat.name));
    }

    @Test
    public void testEqualsAndNotEqualsForAllExpressions() {
        toString("cat.name = cust.name.firstName", cat.name
                .eq(cust.name.firstName));
        toString("cat.name != cust.name.firstName", cat.name
                .ne(cust.name.firstName));
    }

    @Test
    public void testGrammarConstructs() {
        cat.bodyWeight.add(kitten.bodyWeight);
    }

    @Test
    public void testGroupingOperationsAndNullChecks() {
        // in, not in, between, is null, is not null, is empty, is not empty,
        // member of and not member of
        // in,
        // not in,
        // between,
        // is null,
        // is not null,
        // is empty,
        // is not empty,
        // member of
        // not member of
        kitten.in(cat.kittens);
        kitten.in(cat.kittens).not();
        kitten.bodyWeight.between(10, 20);
        kitten.bodyWeight.isNull();
        kitten.bodyWeight.isNotNull();
        cat.kittens.isEmpty();
        cat.kittens.isNotEmpty();
    }

    @Test
    public void testHQLIndexOperations() {
        // the HQL index() function, that applies to aliases of a joined indexed
        // collection
    }

    @Test
    public void testIsNullAndIsNotNullInFunctionalWay() {
        toString("cat.bodyWeight is null", cat.bodyWeight.isNull());
    }

    @Test
    public void testLogicalOperations() {
        // logical operations and, or, not
        toString("cat = kitten or kitten = cat", cat.eq(kitten).or(
                kitten.eq(cat)));
        toString("cat = kitten and kitten = cat", cat.eq(kitten).and(
                kitten.eq(cat)));
        toString("cat is null and (kitten is null or kitten.bodyWeight > :a1)",
                cat.isNull().and(kitten.isNull().or(kitten.bodyWeight.gt(10))));
    }


    // Parentheses ( ), indicating grouping

    @Test
    public void testOrderExpressionInFunctionalWay() {
        cat.bodyWeight.asc();
        cat.bodyWeight.add(kitten.bodyWeight).asc();
    }

    @Test
    public void testSQLScalarOperations() {
        // Any database-supported SQL scalar function like sign(), trunc(),
        // rtrim(), sin()
    }

    @Test
    public void testStringConcatenations() {
        // string concatenation ...||... or concat(...,...)
        toString("cat.name || kitten.name", cat.name.concat(kitten.name));
    }

    // coalesce() and nullif()

    @Test
    public void testStringConversionOperations() {
        // str() for converting numeric or temporal values to a readable string
        toString("str(cat.bodyWeight)", cat.bodyWeight.stringValue());
    }

    @Test
    public void testStringOperationsInFunctionalWay() {
        toString("cat.name || cust.name.firstName", cat.name
                .concat(cust.name.firstName));
//        toString("cat.name like :a1", cat.name.like("A%"));
        toString("lower(cat.name)", cat.name.lower());
    }

    @Test
    public void testToString() {
        toString("cat", cat);
        toString("cat.alive", cat.alive);
        toString("cat.bodyWeight", cat.bodyWeight);
        toString("cat.name", cat.name);

        toString("cust.name", cust.name);
        toString("cust.name.firstName = :a1", cust.name.firstName.eq("Martin"));

//        toString("cat.kittens as kitten", cat.kittens.as(kitten));

        toString("cat.bodyWeight + :a1", cat.bodyWeight.add(10));
        toString("cat.bodyWeight - :a1", cat.bodyWeight.sub(10));
        toString("cat.bodyWeight * :a1", cat.bodyWeight.mult(10));
        toString("cat.bodyWeight / :a1", cat.bodyWeight.div(10));

//        toString("cat.bodyWeight as bw", cat.bodyWeight.as("bw"));

        toString("kitten in elements(cat.kittens)", kitten.in(cat.kittens));

        // toString("distinct cat.bodyWeight", distinct(cat.bodyWeight));

        toString("count(*)", Expr.countAll());
        // toString("count(distinct cat.bodyWeight)",
        // Grammar.count(distinct(cat.bodyWeight)));
        toString("count(cat)", cat.count());
    }

    /**
     * specs :
     * http://opensource.atlassian.com/projects/hibernate/browse/HHH-1538
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testBug326650() {
        assertEquals(Long.class, sum($((byte) 0)).getType());
        assertEquals(Long.class, sum($((short) 0)).getType());
        assertEquals(Long.class, sum($((int) 0)).getType());
        assertEquals(Long.class, sum($((long) 0)).getType());

        assertEquals(Double.class, sum($((float) 0)).getType());
        assertEquals(Double.class, sum($((double) 0)).getType());

        assertEquals(BigInteger.class, sum($(new BigInteger("0"))).getType());
        assertEquals(BigDecimal.class, sum($(new BigDecimal("0"))).getType());

        // sum to var
        ENumber<Long> sum = (ENumber) sum($(0)); // via Java level cast
        sum = sum($(0)).longValue();
        assertNotNull(sum);

        // sum comparison

        sum($(0)).gt(0);
        sum($(0)).intValue().gt(0);

    }

    /**
     * The Class _BookmarkDTO.
     */
    public static final class _BookmarkDTO extends EConstructor<BookmarkDTO> {
        public _BookmarkDTO() {
            super(BookmarkDTO.class);
        }

        public _BookmarkDTO(Expr<java.lang.String> address) {
            super(BookmarkDTO.class, address);
        }
    }

    /**
     * The Class BookmarkDTO.
     */
    public static final class BookmarkDTO {

    }

}
