/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mysema.commons.lang.Pair;
import com.mysema.query.collections.Cat;
import com.mysema.query.collections.MiniApi;
import com.mysema.query.collections.QCat;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.expr.ArrayConstructorExpression;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ConstructorExpression;
import com.mysema.query.types.expr.Param;
import com.mysema.query.types.expr.QTuple;

public class ColQueryStandardTest {

    public static class Projection {

        public Projection(String str, Cat cat) {
        }

    }

    private final Date birthDate = new Date();

    private final java.sql.Date date = new java.sql.Date(birthDate.getTime());

    private final java.sql.Time time = new java.sql.Time(birthDate.getTime());

    private final QCat cat = new QCat("cat");

    private final QCat otherCat = new QCat("otherCat");

    private final List<Cat> data = Arrays.asList(
            new Cat("Bob", 1, birthDate),
            new Cat("Ruth", 2, birthDate),
            new Cat("Felix", 3, birthDate),
            new Cat("Allen", 4, birthDate),
            new Cat("Mary", 5, birthDate)
    );

    private QueryExecution standardTest = new QueryExecution(Module.COLLECTIONS, Target.MEM){
        @Override
        protected Pair<Projectable,List<Expression<?>>> createQuery() {
            return Pair.of(
                    (Projectable)MiniApi.from(cat, data).from(otherCat, data),
                    Collections.<Expression<?>>emptyList());
        }
        @Override
        protected Pair<Projectable,List<Expression<?>>> createQuery(BooleanExpression filter) {
            return Pair.of(
                    (Projectable)MiniApi.from(cat, data).from(otherCat, data).where(filter),
                    Collections.<Expression<?>>singletonList(cat.name));
        }
    };

    @Test
    public void test(){
        Cat kitten = data.get(0).getKittens().get(0);
        standardTest.runArrayTests(cat.kittenArray, otherCat.kittenArray, kitten, new Cat());
        standardTest.runBooleanTests(cat.name.isNull(), otherCat.kittens.isEmpty());
        standardTest.runCollectionTests(cat.kittens, otherCat.kittens, kitten, new Cat());
        standardTest.runDateTests(cat.dateField, otherCat.dateField, date);
        standardTest.runDateTimeTests(cat.birthdate, otherCat.birthdate, birthDate);
        standardTest.runListTests(cat.kittens, otherCat.kittens, kitten, new Cat());
        standardTest.runMapTests(cat.kittensByName, otherCat.kittensByName, "Kitty", kitten, "NoName", new Cat());

        // int
        standardTest.runNumericCasts(cat.id, otherCat.id, 1);
        standardTest.runNumericTests(cat.id, otherCat.id, 1);

        standardTest.runStringTests(cat.name, otherCat.name, "Bob");
        standardTest.runTimeTests(cat.timeField, otherCat.timeField, time);
        standardTest.report();
    }

    @Test
    public void tupleProjection(){
        List<Tuple> tuples = MiniApi.from(cat, data).list(new QTuple(cat.name, cat.birthdate));
        for (Tuple tuple : tuples){
            assertNotNull(tuple.get(cat.name));
            assertNotNull(tuple.get(cat.birthdate));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void arrayProjection(){
        List<String[]> results =  MiniApi.from(cat, data).list(new ArrayConstructorExpression<String>(String[].class, cat.name));
        assertFalse(results.isEmpty());
        for (String[] result : results){
            assertNotNull(result[0]);
        }
    }

    @Test
    public void constructorProjection(){
        List<Projection> projections =  MiniApi.from(cat, data).list(ConstructorExpression.create(Projection.class, cat.name, cat));
        assertFalse(projections.isEmpty());
        for (Projection projection : projections){
            assertNotNull(projection);
        }
    }

    @Test
    public void params(){
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Bob", MiniApi.from(cat, data).where(cat.name.eq(name)).set(name,"Bob").uniqueResult(cat.name));
    }

    @Test
    public void params_anon(){
        Param<String> name = new Param<String>(String.class);
        assertEquals("Bob", MiniApi.from(cat, data).where(cat.name.eq(name)).set(name,"Bob").uniqueResult(cat.name));
    }

    @Test(expected=ParamNotSetException.class)
    public void params_not_set(){
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Bob", MiniApi.from(cat, data).where(cat.name.eq(name)).uniqueResult(cat.name));
    }
}
