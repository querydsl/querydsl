/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.mysema.commons.lang.Pair;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.DomesticCat;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.domain.QShow;
import com.mysema.query.jpa.domain.Show;
import com.mysema.query.types.ArrayConstructorExpression;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ListExpression;
import com.mysema.query.types.expr.Param;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.ListPath;

/**
 * @author tiwe
 *
 */
public abstract class AbstractStandardTest {

    public static class Projection {

        public Projection(String str, Cat cat) {}

    }

    public static class QProjection extends ConstructorExpression<Projection>{

        private static final long serialVersionUID = -5866362075090550839L;

        public QProjection(StringExpression str, QCat cat){
            super(Projection.class, new Class[]{String.class, Cat.class}, new Expression[]{str, cat});
        }

    }

    private static final QCat cat = QCat.cat;

    private static final QCat otherCat = new QCat("otherCat");

    private static final BooleanExpression cond1 = cat.name.length().gt(0);

    private static final BooleanExpression cond2 = otherCat.name.length().gt(0);

    private final Date birthDate;

    private final java.sql.Date date;

    private final Projections projections = new Projections(Module.HQL, getTarget()){
        @Override
        public <A,Q extends SimpleExpression<A>> Collection<Expression<?>> list(ListPath<A,Q> expr, ListExpression<A,Q> other, A knownElement){
            // NOTE : expr.get(0) is only supported in the where clause
            return Collections.<Expression<?>>singleton(expr.size());
        }
    };

    private final List<Cat> savedCats = new ArrayList<Cat>();

    private final QueryExecution standardTest = new QueryExecution(
            projections, new Filters(projections, Module.HQL, getTarget()), new MatchingFilters(Module.HQL, getTarget())){

        @Override
        protected Pair<Projectable, List<Expression<?>>> createQuery() {
            // NOTE : EclipseLink needs extra conditions cond1 and code2
            return Pair.of(
                    (Projectable)query().from(cat, otherCat).where(cond1, cond2),
                    Arrays.<Expression<?>>asList());
        }
        @Override
        protected Pair<Projectable, List<Expression<?>>> createQuery(BooleanExpression filter) {
            // NOTE : EclipseLink needs extra conditions cond1 and code2
            return Pair.of(
                    (Projectable)query().from(cat, otherCat).where(cond1, cond2, filter),
                    Arrays.<Expression<?>>asList(cat.name, otherCat.name));
        }
    };

    private final java.sql.Time time;

    {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 2, 3, 4);
        cal.set(Calendar.MILLISECOND, 0);
        birthDate = cal.getTime();
        date = new java.sql.Date(cal.getTimeInMillis());
        time = new java.sql.Time(cal.getTimeInMillis());
    }

    protected JPQLQuery catQuery(){
        return query().from(cat);
    }

    protected abstract Target getTarget();

    protected abstract JPQLQuery query();

    protected abstract void save(Object entity);

    @Before
    public void setUp(){
        Cat prev = null;
        for (Cat cat : Arrays.asList(
                new Cat("Bob123", 1, 1.0),
                new Cat("Ruth123", 2, 2.0),
                new Cat("Felix123", 3, 3.0),
                new Cat("Allen123", 4, 4.0),
                new Cat("Mary123", 5, 5.0))){
            if (prev != null){
                cat.addKitten(prev);
            }
            cat.setBirthdate(birthDate);
            cat.setDateField(date);
            cat.setTimeField(time);
            save(cat);
            savedCats.add(cat);
            prev = cat;
        }

        Cat cat = new Cat("Some", 6, 6.0);
        cat.setBirthdate(birthDate);
        save(cat);
        savedCats.add(cat);

        Show show = new Show(1);
        show.acts = new HashMap<String,String>();
        show.acts.put("a","A");
        show.acts.put("b","B");
        save(show);
    }

    @Test
    public void test(){
        Cat kitten = savedCats.get(0);
        Cat noKitten = savedCats.get(savedCats.size()-1);

        //        standardTest.runArrayTests(cat.kittensArray, otherCat.kittensArray, kitten, noKitten);
        standardTest.runBooleanTests(cat.name.isNull(), otherCat.kittens.isEmpty());
        standardTest.runCollectionTests(cat.kittens, otherCat.kittens, kitten, noKitten);
        standardTest.runDateTests(cat.dateField, otherCat.dateField, date);
        standardTest.runDateTimeTests(cat.birthdate, otherCat.birthdate, birthDate);
        standardTest.runListTests(cat.kittens, otherCat.kittens, kitten, noKitten);
        //        standardTest.mapTests(cat.kittensByName, otherCat.kittensByName, "Kitty", kitten);

        // int
        standardTest.runNumericCasts(cat.id, otherCat.id, 1);
        standardTest.runNumericTests(cat.id, otherCat.id, 1);

        // double
        standardTest.runNumericCasts(cat.bodyWeight, otherCat.bodyWeight, 1.0);
        standardTest.runNumericTests(cat.bodyWeight, otherCat.bodyWeight, 1.0);

        standardTest.runStringTests(cat.name, otherCat.name, kitten.getName());
        standardTest.runTimeTests(cat.timeField, otherCat.timeField, time);

        standardTest.report();
    }

    @Test
    public void Any_Simple(){
        assertEquals(1, catQuery().where(cat.kittens.any().name.eq("Ruth123")).count());
    }

    @Test
    public void Exists(){
        assertTrue(catQuery().where(cat.kittens.any().name.eq("Ruth123")).exists());
    }

    @Test
    public void NotExists(){
        assertTrue(catQuery().where(cat.kittens.any().name.eq("XXX")).notExists());
    }

    @Test
    public void Any_And(){
        assertEquals(1, catQuery().where(cat.kittens.any().name.eq("Ruth123"), cat.kittens.any().bodyWeight.lt(10.0)).count());
        assertEquals(0, catQuery().where(cat.kittens.any().name.eq("Ruth123"), cat.kittens.any().bodyWeight.gt(10.0)).count());
    }

    @Test
    public void Aggregates_UniqueResult(){
        // uniqueResult
        assertEquals(Integer.valueOf(1), catQuery().uniqueResult(cat.id.min()));
        assertEquals(Integer.valueOf(6), catQuery().uniqueResult(cat.id.max()));
    }

    @Test
    public void Aggregates_List(){
        // list
        assertEquals(Integer.valueOf(1), catQuery().list(cat.id.min()).get(0));
        assertEquals(Integer.valueOf(6), catQuery().list(cat.id.max()).get(0));
    }

    @Test
    public void DistinctResults(){
        System.out.println("-- list results");
        SearchResults<Date> res = catQuery().limit(2).listResults(cat.birthdate);
        assertEquals(2, res.getResults().size());
        assertEquals(6l, res.getTotal());
        System.out.println();

        System.out.println("-- list distinct results");
        res = catQuery().limit(2).listDistinctResults(cat.birthdate);
        assertEquals(1, res.getResults().size());
        assertEquals(1l, res.getTotal());
        System.out.println();

        System.out.println("-- list distinct");
        assertEquals(1, catQuery().listDistinct(cat.birthdate).size());
    }

    @Test
    public void In(){
        catQuery().where(cat.id.in(Arrays.asList(1,2,3))).count();
        catQuery().where(cat.name.in(Arrays.asList("A","B","C"))).count();
    }
    
    @Test
    public void StartsWith(){
        // startsWith
        assertEquals(1, catQuery().where(cat.name.startsWith("R")).count());
        assertEquals(0, catQuery().where(cat.name.startsWith("X")).count());
        assertEquals(1, catQuery().where(cat.name.startsWithIgnoreCase("r")).count());
    }

    @Test
    public void EndsWith(){
        // endsWith
        assertEquals(1, catQuery().where(cat.name.endsWith("h123")).count());
        assertEquals(0, catQuery().where(cat.name.endsWith("X")).count());
        assertEquals(1, catQuery().where(cat.name.endsWithIgnoreCase("H123")).count());
    }

    @Test
    public void Contains1(){
        // contains
        assertEquals(1, catQuery().where(cat.name.contains("eli")).count());
    }
    
    @Test
    public void Contains2(){
        catQuery().where(cat.kittens.contains(savedCats.get(0))).count();
    }

    @Test
    public void Length(){
        // length
        assertEquals(6, catQuery().where(cat.name.length().gt(0)).count());
    }

    @Test
    public void IndexOf(){
        // indexOf
        assertEquals(Integer.valueOf(0), catQuery().where(cat.name.eq("Bob123")).uniqueResult(cat.name.indexOf("B")));
        assertEquals(Integer.valueOf(1), catQuery().where(cat.name.eq("Bob123")).uniqueResult(cat.name.indexOf("o")));
    }

    @Test
    public void StringOperations(){
        // case-sensitivity
        if (!getTarget().equals(Target.MYSQL)){ // NOTE : locate in MYSQL is case-insensitive
            assertEquals(0, catQuery().where(cat.name.startsWith("r")).count());
            assertEquals(0, catQuery().where(cat.name.endsWith("H123")).count());
            assertEquals(Integer.valueOf(2), catQuery().where(cat.name.eq("Bob123")).uniqueResult(cat.name.indexOf("b")));
        }
    }

    @Test
    public void Limit(){
        // limit
        List<String> names1 = Arrays.asList("Allen123","Bob123");
        assertEquals(names1, catQuery().orderBy(cat.name.asc()).limit(2).list(cat.name));
    }

    @Test
    public void Offset(){
        // offset
        List<String> names2 = Arrays.asList("Felix123","Mary123","Ruth123","Some");
        assertEquals(names2, catQuery().orderBy(cat.name.asc()).offset(2).list(cat.name));
    }

    @Test
    public void Limit_and_offset(){
        // limit + offset
        List<String> names3 = Arrays.asList("Felix123","Mary123");
        assertEquals(names3, catQuery().orderBy(cat.name.asc()).limit(2).offset(2).list(cat.name));
    }

    @Test
    public void InstanceOf(){
        assertEquals(6l, query().from(cat).where(cat.instanceOf(Cat.class)).count());
        assertEquals(0l, query().from(cat).where(cat.instanceOf(DomesticCat.class)).count());
    }

    @Test
    public void TupleProjection(){
        List<Tuple> tuples = query().from(cat).list(new QTuple(cat.name, cat));
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples){
            assertNotNull(tuple.get(cat.name));
            assertNotNull(tuple.get(cat));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void ArrayProjection(){
        List<String[]> results = query().from(cat).list(new ArrayConstructorExpression<String>(String[].class, cat.name));
        assertFalse(results.isEmpty());
        for (String[] result : results){
            assertNotNull(result[0]);
        }
    }

    @Test
    public void ConstructorProjection(){
        List<Projection> projections = query().from(cat).list(ConstructorExpression.create(Projection.class, cat.name, cat));
        assertFalse(projections.isEmpty());
        for (Projection projection : projections){
            assertNotNull(projection);
        }
    }

    @Test
    public void ConstructorProjection2(){
        List<Projection> projections = query().from(cat).list(new QProjection(cat.name, cat));
        assertFalse(projections.isEmpty());
        for (Projection projection : projections){
            assertNotNull(projection);
        }
    }

    @Test
    public void Params(){
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Bob123",query().from(cat).where(cat.name.eq(name)).set(name, "Bob123").uniqueResult(cat.name));
    }

    @Test
    public void Params_anon(){
        Param<String> name = new Param<String>(String.class);
        assertEquals("Bob123",query().from(cat).where(cat.name.eq(name)).set(name, "Bob123").uniqueResult(cat.name));
    }

    @Test(expected=ParamNotSetException.class)
    public void Params_not_set(){
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Bob123",query().from(cat).where(cat.name.eq(name)).uniqueResult(cat.name));
    }

    @Test
    public void Null_as_uniqueResult(){
        assertNull(query().from(cat).where(cat.name.eq(UUID.randomUUID().toString())).uniqueResult(cat));
    }
    
    @Test
    public void Map_ContainsKey(){
        QShow show = QShow.show;
        assertEquals(1l, query().from(show).where(show.acts.containsKey("a")).count());
        assertEquals(1l, query().from(show).where(show.acts.containsKey("b")).count());
        assertEquals(0l, query().from(show).where(show.acts.containsKey("c")).count());
    }

    @Test
    public void Map_ContainsValue(){
        QShow show = QShow.show;
        assertEquals(1l, query().from(show).where(show.acts.containsValue("A")).count());
        assertEquals(1l, query().from(show).where(show.acts.containsValue("B")).count());
        assertEquals(0l, query().from(show).where(show.acts.containsValue("C")).count());
    }

}
