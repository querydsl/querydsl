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

import static com.querydsl.core.Target.*;
import static com.querydsl.jpa.JPAExpressions.select;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.Calendar;
import java.util.Map.Entry;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.*;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.group.QPair;
import com.querydsl.core.group.MockTuple;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.domain.*;
import com.querydsl.jpa.domain.Company.Rating;
import com.querydsl.jpa.domain4.QBookMark;
import com.querydsl.jpa.domain4.QBookVersion;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * @author tiwe
 *
 */
public abstract class AbstractJPATest {

    private static final Expression<?>[] NO_EXPRESSIONS = new Expression<?>[0];

    private static final QCompany company = QCompany.company;

    private static final QAnimal animal = QAnimal.animal;

    private static final QCat cat = QCat.cat;

    private static final QCat otherCat = new QCat("otherCat");

    private static final BooleanExpression cond1 = cat.name.length().gt(0);

    private static final BooleanExpression cond2 = otherCat.name.length().gt(0);

    private static final Predicate condition = ExpressionUtils.and(
            (Predicate) ExpressionUtils.extract(cond1),
            (Predicate) ExpressionUtils.extract(cond2));

    private static final Date birthDate;

    private static final java.sql.Date date;

    private static final java.sql.Time time;

    private final List<Cat> savedCats = new ArrayList<Cat>();

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 2, 3, 4);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        birthDate = cal.getTime();
        date = new java.sql.Date(cal.getTimeInMillis());
        time = new java.sql.Time(cal.getTimeInMillis());
    }

    protected Target getTarget() {
        return Mode.target.get();
    }

    protected abstract JPQLQuery<?> query();

    protected abstract JPQLQuery<?> testQuery();

    protected abstract void save(Object entity);

    @Before
    public void setUp() {
        if (query().from(cat).fetchCount() > 0) {
            savedCats.addAll(query().from(cat).orderBy(cat.id.asc()).select(cat).fetch());
            return;
        }

        Cat prev = null;
        for (Cat cat : Arrays.asList(
                new Cat("Bob123", 1, 1.0),
                new Cat("Ruth123", 2, 2.0),
                new Cat("Felix123", 3, 3.0),
                new Cat("Allen123", 4, 4.0),
                new Cat("Mary_123", 5, 5.0))) {
            if (prev != null) {
                cat.addKitten(prev);
            }
            cat.setBirthdate(birthDate);
            cat.setDateField(date);
            cat.setTimeField(time);
            cat.setColor(Color.BLACK);
            cat.setMate(prev);
            save(cat);
            savedCats.add(cat);
            prev = cat;
        }

        Animal animal = new Animal(10);
        animal.setBodyWeight(10.5);
        save(animal);

        Cat cat = new Cat("Some", 6, 6.0);
        cat.setBirthdate(birthDate);
        save(cat);
        savedCats.add(cat);

        Show show = new Show(1);
        show.acts = new HashMap<String,String>();
        show.acts.put("a","A");
        show.acts.put("b","B");
        save(show);

        Company company = new Company();
        company.name = "1234567890123456789012345678901234567890"; // 40
        company.id = 1;
        company.ratingOrdinal = Company.Rating.A;
        company.ratingString = Company.Rating.AA;
        save(company);

        Employee employee = new Employee();
        employee.id = 1;
        employee.lastName = "Smith";
        employee.jobFunctions.add(JobFunction.CODER);
        save(employee);

        Employee employee2 = new Employee();
        employee2.id = 2;
        employee2.lastName = "Doe";
        employee2.jobFunctions.add(JobFunction.CODER);
        employee2.jobFunctions.add(JobFunction.CONSULTANT);
        employee2.jobFunctions.add(JobFunction.CONTROLLER);
        save(employee2);

        save(new Entity1(1));
        save(new Entity1(2));
        save(new Entity2(3));

        Foo foo = new Foo();
        foo.id = 1;
        foo.names = Arrays.asList("a","b");
        foo.bar = "München";
        save(foo);

        Numeric numeric = new Numeric();
        numeric.setValue(BigDecimal.valueOf(26.9));
        save(numeric);
    }

    @Test
    @ExcludeIn(ORACLE)
    public void add_bigDecimal() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");
        NumberPath<BigDecimal> bigd1 = entity.bigDecimal;
        NumberPath<BigDecimal> bigd2 = entity2.bigDecimal;

        assertEquals(Arrays.asList(),
                query().from(entity, entity2)
                        .where(bigd1.add(bigd2).loe(new BigDecimal("1.00")))
                        .select(entity).fetch());
    }

    @Test
    public void aggregates_list_max() {
        assertEquals(Integer.valueOf(6), query().from(cat).select(cat.id.max()).fetchFirst());
    }

    @Test
    public void aggregates_list_min() {
        assertEquals(Integer.valueOf(1), query().from(cat).select(cat.id.min()).fetchFirst());
    }

    @Test
    public void aggregates_uniqueResult_max() {
        assertEquals(Integer.valueOf(6), query().from(cat).select(cat.id.max()).fetchFirst());
    }

    @Test
    public void aggregates_uniqueResult_min() {
        assertEquals(Integer.valueOf(1), query().from(cat).select(cat.id.min()).fetchFirst());
    }

    @Test
    public void alias() {
        assertEquals(6, query().from(cat).select(cat.id.as(cat.id)).fetch().size());
    }

    @Test
    public void any_and_gt() {
        assertEquals(0, query().from(cat).where(
                cat.kittens.any().name.eq("Ruth123"),
                cat.kittens.any().bodyWeight.gt(10.0)).fetchCount());
    }

    @Test
    public void any_and_lt() {
        assertEquals(1, query().from(cat).where(
                cat.kittens.any().name.eq("Ruth123"),
                cat.kittens.any().bodyWeight.lt(10.0)).fetchCount());
    }

    @Test
    public void any_in_order() {
        assertFalse(query().from(cat).orderBy(cat.kittens.any().name.asc()).select(cat).fetch().isEmpty());
    }

    @Test
    public void any_in_projection() {
        assertFalse(query().from(cat).select(cat.kittens.any()).fetch().isEmpty());
    }

    @Test
    public void any_in_projection2() {
        assertFalse(query().from(cat).select(cat.kittens.any().name).fetch().isEmpty());
    }

    @Test
    public void any_in_projection3() {
        assertFalse(query().from(cat).select(cat.kittens.any().name, cat.kittens.any().bodyWeight).fetch().isEmpty());
    }

    @Test
    public void any_in1() {
        //select cat from Cat cat where exists (
        //  select cat_kittens from Cat cat_kittens where cat_kittens member of cat.kittens and cat_kittens in ?1)
        assertFalse(query().from(cat).where(cat.kittens.any().in(savedCats)).select(cat).fetch().isEmpty());
    }

    @Test
    public void any_in11() {
        List<Integer> ids = Lists.newArrayList();
        for (Cat cat : savedCats) {
            ids.add(cat.getId());
        }
        assertFalse(query().from(cat).where(cat.kittens.any().id.in(ids)).select(cat).fetch().isEmpty());
    }

    @Test
    public void any_in2() {
        assertFalse(query().from(cat).where(
                cat.kittens.any().in(savedCats),
                cat.kittens.any().in(savedCats.subList(0, 1)).not())
                .select(cat).fetch().isEmpty());
    }

    @Test
    @NoBatooJPA
    public void any_in3() {
        QEmployee employee = QEmployee.employee;
        assertFalse(query().from(employee).where(
                employee.jobFunctions.any().in(JobFunction.CODER, JobFunction.CONSULTANT))
                .select(employee).fetch().isEmpty());
    }

    @Test
    public void any_simple() {
        assertEquals(1, query().from(cat).where(cat.kittens.any().name.eq("Ruth123")).fetchCount());
    }

    @Test
    public void any_any() {
        assertEquals(1, query().from(cat).where(cat.kittens.any().kittens.any().name.eq("Ruth123")).fetchCount());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void arrayProjection() {
        List<String[]> results = query().from(cat)
                .select(new ArrayConstructorExpression<String>(String[].class, cat.name)).fetch();
        assertFalse(results.isEmpty());
        for (String[] result : results) {
            assertNotNull(result[0]);
        }
    }

    @Test
    public void as() {
        assertTrue(query().from(QAnimal.animal.as(QCat.class)).fetchCount() > 0);
    }

    @Test
    public void between() {
        assertEquals(ImmutableList.of(2, 3, 4, 5),
                query().from(cat).where(cat.id.between(2, 5)).orderBy(cat.id.asc()).select(cat.id).fetch());
    }

    @Test
    @NoBatooJPA
    public void case1() {
        assertEquals(ImmutableList.of(1, 2, 2, 2, 2, 2),
                query().from(cat).orderBy(cat.id.asc())
                        .select(cat.name.when("Bob123").then(1).otherwise(2)).fetch());
    }

    @Test
    @NoBatooJPA
    public void case1_long() {
        assertEquals(ImmutableList.of(1L, 2L, 2L, 2L, 2L, 2L),
                query().from(cat).orderBy(cat.id.asc())
                        .select(cat.name.when("Bob123").then(1L).otherwise(2L)).fetch());
        List<Integer> rv = query().from(cat).select(cat.name.when("Bob").then(1).otherwise(2)).fetch();
        assertInstancesOf(Integer.class, rv);
    }

    @Test
    @NoEclipseLink
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-8653
    public void case1_date() {
        List<LocalDate> rv = query().from(cat).select(cat.name.when("Bob").then(new LocalDate())
                .otherwise(new LocalDate().plusDays(1))).fetch();
        assertInstancesOf(LocalDate.class, rv);
    }

    @Test
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-8653
    @NoEclipseLink({MYSQL, POSTGRESQL})
    public void case1_date2() {
        List<java.sql.Date> rv = query().from(cat).select(cat.name.when("Bob").then(new java.sql.Date(0))
                .otherwise(new java.sql.Date(0))).fetch();
        assertInstancesOf(java.sql.Date.class, rv);
    }

    @Test
    @NoEclipseLink
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-8653
    public void case1_time() {
        List<LocalTime> rv = query().from(cat).select(cat.name.when("Bob").then(new LocalTime())
                .otherwise(new LocalTime().plusHours(1))).fetch();
        assertInstancesOf(LocalTime.class, rv);
    }

    @Test
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-8653
    @NoEclipseLink({MYSQL, POSTGRESQL})
    public void case1_time2() {
        List<java.sql.Time> rv = query().from(cat).select(cat.name.when("Bob").then(new java.sql.Time(0))
                .otherwise(new java.sql.Time(0))).fetch();
        assertInstancesOf(java.sql.Time.class, rv);
    }

    @Test
    @NoEclipseLink
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-8653
    public void case1_timestamp() {
        List<DateTime> rv = query().from(cat).select(cat.name.when("Bob").then(new DateTime())
                .otherwise(new DateTime().plusHours(1))).fetch();
        assertInstancesOf(DateTime.class, rv);
    }

    @Test
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-8653
    @NoEclipseLink({MYSQL, POSTGRESQL})
    public void case1_timestamp2() {
        List<java.sql.Timestamp> rv = query().from(cat).select(cat.name.when("Bob").then(new java.sql.Timestamp(0))
                .otherwise(new java.sql.Timestamp(0))).fetch();
        assertInstancesOf(java.sql.Timestamp.class, rv);
    }

    @Test
    public void case2() {
        assertEquals(ImmutableList.of(4, 4, 4, 4, 4, 4),
                query().from(cat)
                        .select(Expressions.cases().when(cat.toes.eq(2)).then(cat.id.multiply(2))
                                .when(cat.toes.eq(3)).then(cat.id.multiply(3))
                                .otherwise(4)).fetch());
    }

    @Test
    public void case3() {
        assertEquals(ImmutableList.of(4, 4, 4, 4, 4, 4),
                query().from(cat).select(Expressions.cases()
                        .when(cat.toes.in(2, 3)).then(cat.id.multiply(cat.toes))
                        .otherwise(4)).fetch());
    }

    @Test
    @ExcludeIn(MYSQL) // doesn't work in Eclipselink
    public void case4() {
        NumberExpression<Float> numExpression = cat.bodyWeight.floatValue().divide(otherCat.bodyWeight.floatValue()).multiply(100);
        NumberExpression<Float> numExpression2 = cat.id.when(0).then(0.0F).otherwise(numExpression);
        assertEquals(ImmutableList.of(200, 150, 133, 125, 120),
                query().from(cat, otherCat)
                        .where(cat.id.eq(otherCat.id.add(1)))
                        .orderBy(cat.id.asc(), otherCat.id.asc())
                        .select(numExpression2.intValue()).fetch());
    }

    @Test
    @NoEclipseLink // EclipseLink uses a left join for cat.mate
    public void case5() {
        assertEquals(ImmutableList.of(0, 1, 1, 1),
                query().from(cat).orderBy(cat.id.asc())
                        .select(cat.mate.when(savedCats.get(0)).then(0).otherwise(1)).fetch());
    }

    private static <T> void assertInstancesOf(Class<T> clazz, Iterable<T> rows) {
        for (T row : rows) {
            assertEquals(row.toString(), clazz, row.getClass());
        }
    }

    @Test
    public void caseBuilder() {
        QCat cat2 = new QCat("cat2");
        NumberExpression<Integer> casex = new CaseBuilder()
                .when(cat.weight.isNull().and(cat.weight.isNull())).then(0)
                .when(cat.weight.isNull()).then(cat2.weight)
                .when(cat2.weight.isNull()).then(cat.weight)
                .otherwise(cat.weight.add(cat2.weight));

        query().from(cat, cat2).orderBy(casex.asc()).select(cat.id, cat2.id).fetch();
        query().from(cat, cat2).orderBy(casex.desc()).select(cat.id, cat2.id).fetch();
    }

    @Test
    public void cast() {
        List<Cat> cats = query().from(cat).select(cat).fetch();
        List<Integer> weights = query().from(cat).select(cat.bodyWeight.castToNum(Integer.class)).fetch();
        for (int i = 0; i < cats.size(); i++) {
            assertEquals(Integer.valueOf((int) (cats.get(i).getBodyWeight())), weights.get(i));
        }
    }

    @Test
    @ExcludeIn(SQLSERVER)
    public void cast_toString() {
        for (Tuple tuple : query().from(cat).select(cat.breed, cat.breed.stringValue()).fetch()) {
            assertEquals(
                    tuple.get(cat.breed).toString(),
                    tuple.get(cat.breed.stringValue()));
        }
    }

    @Test
    @ExcludeIn(SQLSERVER)
    public void cast_toString_append() {
        for (Tuple tuple : query().from(cat).select(cat.breed, cat.breed.stringValue().append("test")).fetch()) {
            assertEquals(
                    tuple.get(cat.breed).toString() + "test",
                    tuple.get(cat.breed.stringValue().append("test")));
        }
    }

    @Test
    public void collection_predicates() {
        ListPath<Cat, QCat> path = cat.kittens;
        List<Predicate> predicates = Arrays.asList(
//            path.eq(savedCats),
//            path.in(savedCats),
//            path.isNotNull(),
//            path.isNull(),
//            path.ne(savedCats),
//            path.notIn(savedCats)
//            path.when(other)
        );
        for (Predicate pred : predicates) {
            System.err.println(pred);
            query().from(cat).where(pred).select(cat).fetch();
        }
    }

    @Test
    public void collection_projections() {
        ListPath<Cat, QCat> path = cat.kittens;
        List<Expression<?>> projections = Arrays.asList(
//            path.fetchCount(),
//            path.countDistinct()
        );
        for (Expression<?> proj : projections) {
            System.err.println(proj);
            query().from(cat).select(proj).fetch();
        }
    }

    @Test
    public void constant() {
        //select cat.id, ?1 as const from Cat cat
        List<Cat> cats = query().from(cat).select(cat).fetch();
        Path<String> path = Expressions.stringPath("const");
        List<Tuple> tuples = query().from(cat).select(cat.id, Expressions.constantAs("abc", path)).fetch();
        for (int i = 0; i < cats.size(); i++) {
            assertEquals(Integer.valueOf(cats.get(i).getId()), tuples.get(i).get(cat.id));
            assertEquals("abc", tuples.get(i).get(path));
        }
    }

    @Test
    public void constant2() {
        assertFalse(query().from(cat).select(cat.id, Expressions.constant("name")).fetch().isEmpty());
    }

    @Test
    public void constructorProjection() {
        List<Projection> projections = query().from(cat)
                .select(Projections.constructor(Projection.class, cat.name, cat)).fetch();
        assertFalse(projections.isEmpty());
        for (Projection projection : projections) {
            assertNotNull(projection);
        }
    }

    @Test
    public void constructorProjection2() {
        List<Projection> projections = query().from(cat).select(new QProjection(cat.name, cat)).fetch();
        assertFalse(projections.isEmpty());
        for (Projection projection : projections) {
            assertNotNull(projection);
        }
    }

    @Test
    public void constructorProjection3() {
        List<Projection> projections = query().from(cat).select(new QProjection(cat.id, Expressions.FALSE)).fetch();
        assertFalse(projections.isEmpty());
        for (Projection projection : projections) {
            assertNotNull(projection);
        }
    }

    @Test
    public void contains_ic() {
        QFoo foo = QFoo.foo;
        assertEquals(1, query().from(foo).where(foo.bar.containsIgnoreCase("München")).fetchCount());
    }

    @Test
    public void contains1() {
        assertEquals(1, query().from(cat).where(cat.name.contains("eli")).fetchCount());
    }

    @Test
    public void contains2() {
        assertEquals(1L, query().from(cat).where(cat.kittens.contains(savedCats.get(0))).fetchCount());
    }

    @Test
    public void contains3() {
        assertEquals(1L, query().from(cat).where(cat.name.contains("_")).fetchCount());
    }

    @Test
    public void contains4() {
        QEmployee employee = QEmployee.employee;
        assertEquals(Arrays.asList(),
                query().from(employee)
                        .where(
                                employee.jobFunctions.contains(JobFunction.CODER),
                                employee.jobFunctions.contains(JobFunction.CONSULTANT),
                                employee.jobFunctions.size().eq(2))
                        .select(employee).fetch());
    }

    @Test
    public void count() {
        QShow show = QShow.show;
        assertTrue(query().from(show).fetchCount() > 0);
    }

    @Test
    public void count_distinct() {
        QCat cat = QCat.cat;
        query().from(cat)
               .groupBy(cat.id)
               .select(cat.id, cat.breed.countDistinct()).fetch();
    }

    @Test
    @NoBatooJPA
    @NoHibernate
    public void count_distinct2() {
        QCat cat = QCat.cat;
        query().from(cat)
               .groupBy(cat.id)
               .select(cat.id, cat.birthdate.dayOfMonth().countDistinct()).fetch();
    }

    @Test
    @NoEclipseLink
    public void distinct_orderBy() {
        QCat cat = QCat.cat;
        List<Tuple> result = query().select(cat.id, cat.mate.id)
                .distinct()
                .from(cat)
                .orderBy(
                        cat.mate.id.asc().nullsFirst(),
                        cat.id.asc().nullsFirst()
                ).fetch();
        assertThat(result, Matchers.<Tuple>contains(
                new MockTuple(new Object[]{1, null}),
                new MockTuple(new Object[]{6, null}),
                new MockTuple(new Object[]{2, 1}),
                new MockTuple(new Object[]{3, 2}),
                new MockTuple(new Object[]{4, 3}),
                new MockTuple(new Object[]{5, 4})
        ));
    }

    @Test
    @NoHibernate
    @ExcludeIn(MYSQL)
    public void distinct_orderBy2() {
        QCat cat = QCat.cat;
        List<Tuple> result = query().select(cat.id, cat.mate.id)
                .distinct()
                .from(cat)
                .orderBy(cat.mate.id.asc().nullsFirst()).fetch();
        assertThat(result, Matchers.<Tuple>contains(
                new MockTuple(new Object[]{2, 1}),
                new MockTuple(new Object[]{3, 2}),
                new MockTuple(new Object[]{4, 3}),
                new MockTuple(new Object[]{5, 4})
        ));
    }

    @Test
    @NoEclipseLink(HSQLDB)
    public void count_distinct3() {
        QCat kitten = new QCat("kitten");
        assertEquals(4, query().from(cat).leftJoin(cat.kittens, kitten).select(kitten.countDistinct()).fetchOne().intValue());
        assertEquals(6, query().from(cat).leftJoin(cat.kittens, kitten).select(kitten.countDistinct()).fetchCount());
    }

    @Test
    public void distinctResults() {
        System.out.println("-- fetch results");
        QueryResults<Date> res = query().from(cat).limit(2).select(cat.birthdate).fetchResults();
        assertEquals(2, res.getResults().size());
        assertEquals(6L, res.getTotal());
        System.out.println();

        System.out.println("-- fetch distinct results");
        res = query().from(cat).limit(2).distinct().select(cat.birthdate).fetchResults();
        assertEquals(1, res.getResults().size());
        assertEquals(1L, res.getTotal());
        System.out.println();

        System.out.println("-- fetch distinct");
        assertEquals(1, query().from(cat).distinct().select(cat.birthdate).fetch().size());
    }

    @Test
    public void date() {
        assertEquals(2000, query().from(cat).select(cat.birthdate.year()).fetchFirst().intValue());
        assertEquals(200002, query().from(cat).select(cat.birthdate.yearMonth()).fetchFirst().intValue());
        assertEquals(2, query().from(cat).select(cat.birthdate.month()).fetchFirst().intValue());
        assertEquals(2, query().from(cat).select(cat.birthdate.dayOfMonth()).fetchFirst().intValue());
        assertEquals(3, query().from(cat).select(cat.birthdate.hour()).fetchFirst().intValue());
        assertEquals(4, query().from(cat).select(cat.birthdate.minute()).fetchFirst().intValue());
        assertEquals(0, query().from(cat).select(cat.birthdate.second()).fetchFirst().intValue());
    }

    @Test
    @NoEclipseLink({DERBY, HSQLDB})
    @NoHibernate({DERBY, POSTGRESQL, SQLSERVER})
    public void date_yearWeek() {
        int value = query().from(cat).select(cat.birthdate.yearWeek()).fetchFirst();
        assertTrue(value == 200006 || value == 200005);
    }

    @Test
    @NoEclipseLink({DERBY, HSQLDB})
    @NoHibernate({DERBY, POSTGRESQL, SQLSERVER})
    public void date_week() {
        int value = query().from(cat).select(cat.birthdate.week()).fetchFirst();
        assertTrue(value == 6 || value == 5);
    }

    @Test
    @ExcludeIn(ORACLE)
    public void divide() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");

        assertEquals(Arrays.asList(),
                query().from(entity, entity2)
                .where(entity.ddouble.divide(entity2.ddouble).loe(2.0))
                .select(entity).fetch());

        assertEquals(Arrays.asList(),
                query().from(entity, entity2)
                .where(entity.ddouble.divide(entity2.iint).loe(2.0))
                .select(entity).fetch());

        assertEquals(Arrays.asList(),
                query().from(entity, entity2)
                .where(entity.iint.divide(entity2.ddouble).loe(2.0))
                .select(entity).fetch());

        assertEquals(Arrays.asList(),
                query().from(entity, entity2)
                        .where(entity.iint.divide(entity2.iint).loe(2))
                        .select(entity).fetch());
    }

    @Test
    @ExcludeIn(ORACLE)
    public void divide_bigDecimal() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");
        NumberPath<BigDecimal> bigd1 = entity.bigDecimal;
        NumberPath<BigDecimal> bigd2 = entity2.bigDecimal;

        assertEquals(Arrays.asList(),
                query().from(entity, entity2)
                        .where(bigd1.divide(bigd2).loe(new BigDecimal("1.00")))
                        .select(entity).fetch());

        assertEquals(Arrays.asList(),
                query().from(entity, entity2)
                .where(entity.ddouble.divide(bigd2).loe(new BigDecimal("1.00")))
                .select(entity).fetch());

        assertEquals(Arrays.asList(),
                query().from(entity, entity2)
                .where(bigd1.divide(entity.ddouble).loe(new BigDecimal("1.00")))
                .select(entity).fetch());
    }

    @Test
    public void endsWith() {
        assertEquals(1, query().from(cat).where(cat.name.endsWith("h123")).fetchCount());
    }

    @Test
    public void endsWith_ignoreCase() {
        assertEquals(1, query().from(cat).where(cat.name.endsWithIgnoreCase("H123")).fetchCount());
    }

    @Test
    public void endsWith2() {
        assertEquals(0, query().from(cat).where(cat.name.endsWith("X")).fetchCount());
    }

    @Test
    public void endsWith3() {
        assertEquals(1, query().from(cat).where(cat.name.endsWith("_123")).fetchCount());
    }

    @Test
    @NoBatooJPA
    public void enum_eq() {
        assertEquals(1, query().from(company).where(company.ratingOrdinal.eq(Rating.A)).fetchCount());
        assertEquals(1, query().from(company).where(company.ratingString.eq(Rating.AA)).fetchCount());
    }

    @Test
    @NoBatooJPA
    public void enum_in() {
        assertEquals(1, query().from(company).where(company.ratingOrdinal.in(Rating.A, Rating.AA)).fetchCount());
        assertEquals(1, query().from(company).where(company.ratingString.in(Rating.A, Rating.AA)).fetchCount());
    }

    @Test
    @NoBatooJPA
    public void enum_in2() {
        QEmployee employee = QEmployee.employee;

        JPQLQuery<?> query = query();
        query.from(employee).where(employee.lastName.eq("Smith"), employee.jobFunctions
                .contains(JobFunction.CODER));
        assertEquals(1L, query.fetchCount());
    }

    @Test
    @ExcludeIn(SQLSERVER)
    public void enum_startsWith() {
        assertEquals(1, query().from(company).where(company.ratingString.stringValue().startsWith("A")).fetchCount());
    }

    @Test
    @NoEclipseLink(HSQLDB)
    public void factoryExpressions() {
        QCat cat = QCat.cat;
        QCat cat2 = new QCat("cat2");
        QCat kitten = new QCat("kitten");
        JPQLQuery<Tuple> query = query().from(cat)
                .leftJoin(cat.mate, cat2)
                .leftJoin(cat2.kittens, kitten)
                .select(Projections.tuple(cat.id, new QFamily(cat, cat2, kitten).skipNulls()));
        assertEquals(6, query.fetch().size());
        assertNotNull(query.limit(1).fetchOne());
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void fetch() {
        QMammal mammal = QMammal.mammal;
        QHuman human = new QHuman("mammal");
        query().from(mammal)
            .leftJoin(human.hairs).fetchJoin()
            .select(mammal).fetch();
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void fetch2() {
        QWorld world = QWorld.world;
        QMammal mammal = QMammal.mammal;
        QHuman human = new QHuman("mammal");
        query().from(world)
            .leftJoin(world.mammals, mammal).fetchJoin()
            .leftJoin(human.hairs).fetchJoin()
            .select(world).fetch();
    }

    @Test
    @ExcludeIn({MYSQL, DERBY})
    @NoBatooJPA
    public void groupBy() {
        QAuthor author = QAuthor.author;
        QBook book = QBook.book;

        for (int i = 0; i < 10; i++) {
            Author a = new Author();
            a.setName(String.valueOf(i));
            save(a);
            for (int j = 0; j < 2; j++) {
                Book b = new Book();
                b.setTitle(String.valueOf(i) + " " + String.valueOf(j));
                b.setAuthor(a);
                save(b);
            }
        }

        Map<Long, List<Pair<Long, String>>> map = query()
            .from(author)
            .join(author.books, book)
            .transform(GroupBy
                    .groupBy(author.id)
                    .as(GroupBy.list(QPair.create(book.id, book.title))));

        for (Entry<Long, List<Pair<Long, String>>> entry : map.entrySet()) {
            System.out.println("author = " + entry.getKey());

            for (Pair<Long,String> pair : entry.getValue()) {
                System.out.println("  book = " + pair.getFirst() + "," + pair.getSecond());
            }
        }
    }

    @Test
    public void groupBy2() {
//        select cat0_.name as col_0_0_, cat0_.breed as col_1_0_, sum(cat0_.bodyWeight) as col_2_0_
//        from animal_ cat0_ where cat0_.DTYPE in ('C', 'DC') and cat0_.bodyWeight>?
//        group by cat0_.name , cat0_.breed
        query().from(cat)
            .where(cat.bodyWeight.gt(0))
            .groupBy(cat.name, cat.breed)
            .select(cat.name, cat.breed, cat.bodyWeight.sum()).fetch();
    }

    @Test
    @NoEclipseLink
    public void groupBy_yearMonth() {
        query().from(cat)
               .groupBy(cat.birthdate.yearMonth())
               .orderBy(cat.birthdate.yearMonth().asc())
               .select(cat.id.count()).fetch();
    }

    @Test
    @Ignore // FIXME
    public void groupBy_count() {
        List<Integer> ids = query().from(cat).groupBy(cat.id).select(cat.id).fetch();
        long count = query().from(cat).groupBy(cat.id).fetchCount();
        QueryResults<Integer> results = query().from(cat).groupBy(cat.id)
                .limit(1).select(cat.id).fetchResults();

        long catCount = query().from(cat).fetchCount();
        assertEquals(catCount, ids.size());
        assertEquals(catCount, count);
        assertEquals(catCount, results.getResults().size());
        assertEquals(catCount, results.getTotal());
    }

    @Test
    @Ignore // FIXME
    public void groupBy_distinct_count() {
        List<Integer> ids = query().from(cat).groupBy(cat.id).distinct().select(Expressions.ONE).fetch();
        QueryResults<Integer> results = query().from(cat).groupBy(cat.id)
                .limit(1).distinct().select(Expressions.ONE).fetchResults();

        assertEquals(1, ids.size());
        assertEquals(1, results.getResults().size());
        assertEquals(1, results.getTotal());
    }

    @Test
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-1902
    public void groupBy_select() {
        // select length(my_column) as column_size from my_table group by column_size
        NumberPath<Integer> length = Expressions.numberPath(Integer.class, "len");
        assertEquals(ImmutableList.of(4, 6, 7, 8),
                query().select(cat.name.length().as(length)).from(cat).orderBy(length.asc()).groupBy(length).fetch());
    }

    @Test
    public void groupBy_results() {
        QueryResults<Integer> results = query().from(cat).groupBy(cat.id).select(cat.id).fetchResults();
        assertEquals(6, results.getTotal());
        assertEquals(6, results.getResults().size());
    }

    @Test
    public void groupBy_results2() {
        QueryResults<Integer> results = query().from(cat).groupBy(cat.birthdate).select(cat.id.max()).fetchResults();
        assertEquals(1, results.getTotal());
        assertEquals(1, results.getResults().size());
    }

    @Test
    public void in() {
        assertEquals(3L, query().from(cat).where(cat.name.in("Bob123", "Ruth123", "Felix123")).fetchCount());
        assertEquals(3L, query().from(cat).where(cat.id.in(Arrays.asList(1, 2, 3))).fetchCount());
        assertEquals(0L, query().from(cat).where(cat.name.in(Arrays.asList("A", "B", "C"))).fetchCount());
    }

    @Test
    public void in2() {
        assertEquals(3L, query().from(cat).where(cat.id.in(1, 2, 3)).fetchCount());
        assertEquals(0L, query().from(cat).where(cat.name.in("A", "B", "C")).fetchCount());
    }

    @Test
    public void in3() {
        assertEquals(0, query().from(cat).where(cat.name.in("A,B,C".split(","))).fetchCount());
    }

    @Test
    public void in4() {
        //$.parameterRelease.id.eq(releaseId).and($.parameterGroups.any().id.in(filter.getGroups()));
        assertEquals(Arrays.asList(),
                query().from(cat).where(cat.id.eq(1), cat.kittens.any().id.in(1, 2, 3)).select(cat).fetch());
    }

    @Test
    public void in5() {
        assertEquals(4L, query().from(cat).where(cat.mate.in(savedCats)).fetchCount());
    }

    @Test
    @Ignore
    public void in6() {
        //query().from(cat).where(cat.kittens.in(savedCats)).fetchCount();
    }

    @Test
    public void in7() {
        assertEquals(4L, query().from(cat).where(cat.kittens.any().in(savedCats)).fetchCount());
    }

    @Test
    public void in_empty() {
        assertEquals(0, query().from(cat).where(cat.name.in(ImmutableList.<String>of())).fetchCount());
    }

    @Test
    @NoOpenJPA
    public void indexOf() {
        assertEquals(Integer.valueOf(0), query().from(cat).where(cat.name.eq("Bob123"))
                .select(cat.name.indexOf("B")).fetchFirst());
    }

    @Test
    @NoOpenJPA
    public void indexOf2() {
        assertEquals(Integer.valueOf(1), query().from(cat).where(cat.name.eq("Bob123"))
                .select(cat.name.indexOf("o")).fetchFirst());
    }

    @Test
    public void instanceOf_cat() {
        assertEquals(6L, query().from(cat).where(cat.instanceOf(Cat.class)).fetchCount());
    }

    @Test
    public void instanceOf_domesticCat() {
        assertEquals(0L, query().from(cat).where(cat.instanceOf(DomesticCat.class)).fetchCount());
    }

    @Test
    public void instanceOf_entity1() {
        QEntity1 entity1 = QEntity1.entity1;
        assertEquals(2L, query().from(entity1).where(entity1.instanceOf(Entity1.class)).fetchCount());
    }

    @Test
    public void instanceOf_entity2() {
        QEntity1 entity1 = QEntity1.entity1;
        assertEquals(1L, query().from(entity1).where(entity1.instanceOf(Entity2.class)).fetchCount());
    }

    @Test
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-6686
    public void isEmpty_elementCollection() {
        QEmployee employee = QEmployee.employee;
        assertEquals(0, query().from(employee).where(employee.jobFunctions.isEmpty()).fetchCount());
    }

    @Test
    public void isEmpty_relation() {
        assertEquals(6L, query().from(cat).where(cat.kittensSet.isEmpty()).fetchCount());
    }

    @Test
    @NoEclipseLink
    @ExcludeIn({ORACLE, TERADATA})
    public void joinEmbeddable() {
        QBookVersion bookVersion = QBookVersion.bookVersion;
        QBookMark bookMark = QBookMark.bookMark;

        assertEquals(Arrays.asList(),
                query().from(bookVersion)
                        .join(bookVersion.definition.bookMarks, bookMark)
                        .where(
                                bookVersion.definition.bookMarks.size().eq(1),
                                bookMark.page.eq(2357L).or(bookMark.page.eq(2356L)))
                        .select(bookVersion).fetch());
    }

    @Test
    public void length() {
        assertEquals(6, query().from(cat).where(cat.name.length().gt(0)).fetchCount());
    }

    @Test
    public void like() {
        assertEquals(0, query().from(cat).where(cat.name.like("!")).fetchCount());
        assertEquals(0, query().from(cat).where(cat.name.like("\\")).fetchCount());
    }

    @Test
    public void limit() {
        List<String> names1 = Arrays.asList("Allen123","Bob123");
        assertEquals(names1, query().from(cat).orderBy(cat.name.asc()).limit(2).select(cat.name).fetch());
    }

    @Test
    public void limit_and_offset() {
        List<String> names3 = Arrays.asList("Felix123", "Mary_123");
        assertEquals(names3, query().from(cat).orderBy(cat.name.asc()).limit(2).offset(2).select(cat.name).fetch());
    }

    @Test
    public void limit2() {
        assertEquals(Collections.singletonList("Allen123"),
                query().from(cat).orderBy(cat.name.asc()).limit(1).select(cat.name).fetch());
    }

    @Test
    public void limit3() {
        assertEquals(6, query().from(cat).limit(Long.MAX_VALUE).select(cat).fetch().size());
    }

    @Test
    public void list_elementCollection_of_enum() {
        QEmployee employee = QEmployee.employee;
        //QJobFunction jobFunction = QJobFunction.jobFunction;
        EnumPath<JobFunction> jobFunction = Expressions.enumPath(JobFunction.class, "jf");

        List<JobFunction> jobFunctions = query().from(employee)
                .innerJoin(employee.jobFunctions, jobFunction).select(jobFunction).fetch();
        assertEquals(4, jobFunctions.size());
    }

    @Test
    @NoBatooJPA
    public void list_elementCollection_of_string() {
        QFoo foo = QFoo.foo;
        StringPath str = Expressions.stringPath("str");

        List<String> strings = query().from(foo).innerJoin(foo.names, str).select(str).fetch();
        assertEquals(2, strings.size());
        assertTrue(strings.contains("a"));
        assertTrue(strings.contains("b"));
    }

    @Test
    @NoEclipseLink(HSQLDB)
    public void list_order_get() {
        QCat cat = QCat.cat;
        assertEquals(6, query().from(cat).orderBy(cat.kittens.get(0).name.asc()).fetch().size());
    }

    @Test
    @NoEclipseLink(HSQLDB)
    public void list_order_get2() {
        QCat cat = QCat.cat;
        assertEquals(6, query().from(cat).orderBy(cat.mate.kittens.get(0).name.asc()).fetch().size());
    }

    @Test
    public void map_get() {
        QShow show = QShow.show;
        assertEquals(Arrays.asList("A"), query().from(show).select(show.acts.get("a")).fetch());
    }

    @Test
    @NoHibernate
    public void map_get2() {
        QShow show = QShow.show;
        assertEquals(1, query().from(show).where(show.acts.get("a").eq("A")).fetchCount());
    }

    @Test
    @NoEclipseLink
    public void map_order_get() {
        QShow show = QShow.show;
        assertEquals(1, query().from(show).orderBy(show.parent.acts.get("A").asc()).fetch().size());
    }

    @Test
    @NoEclipseLink
    public void map_order_get2() {
        QShow show = QShow.show;
        QShow parent = new QShow("parent");
        assertEquals(1, query().from(show).leftJoin(show.parent, parent)
                .orderBy(parent.acts.get("A").asc()).fetch().size());
    }

    @Test
    public void map_containsKey() {
        QShow show = QShow.show;
        assertEquals(1L, query().from(show).where(show.acts.containsKey("a")).fetchCount());
    }

    @Test
    public void map_containsKey2() {
        QShow show = QShow.show;
        assertEquals(1L, query().from(show).where(show.acts.containsKey("b")).fetchCount());
    }

    @Test
    public void map_containsKey3() {
        QShow show = QShow.show;
        assertEquals(0L, query().from(show).where(show.acts.containsKey("c")).fetchCount());
    }

    @Test
    public void map_containsValue() {
        QShow show = QShow.show;
        assertEquals(1L, query().from(show).where(show.acts.containsValue("A")).fetchCount());
    }

    @Test
    public void map_containsValue2() {
        QShow show = QShow.show;
        assertEquals(1L, query().from(show).where(show.acts.containsValue("B")).fetchCount());
    }

    @Test
    public void map_containsValue3() {
        QShow show = QShow.show;
        assertEquals(0L, query().from(show).where(show.acts.containsValue("C")).fetchCount());
    }

    @Test
    public void map_contains() {
        QShow show = QShow.show;
        assertEquals(1L, query().from(show).where(show.acts.contains("a", "A")).fetchCount());
        assertEquals(0L, query().from(show).where(show.acts.contains("X", "X")).fetchCount());
    }

    @Test
    public void map_groupBy() {
      QShow show = QShow.show;
      assertEquals(1, query().from(show).select(show.acts.get("X")).groupBy(show.acts.get("a")).fetchCount());
    }

    @Test
    @Ignore
    public void map_join() {
        //select m.text from Show s join s.acts a where key(a) = 'B'
        QShow show = QShow.show;
        StringPath act = Expressions.stringPath("act");
        assertEquals(Arrays.asList(), query().from(show).join(show.acts, act).select(act).fetch());
    }

    @Test
    public void max() {
        assertEquals(6.0, query().from(cat).select(cat.bodyWeight.max()).fetchFirst().doubleValue(), 0.0001);
    }

    @Test
    public void min() {
        assertEquals(1.0, query().from(cat).select(cat.bodyWeight.min()).fetchFirst().doubleValue(), 0.0001);
    }

    @Test
    @ExcludeIn(ORACLE)
    public void multiply() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");

        assertEquals(Arrays.asList(),
                query().from(entity, entity2)
                        .where(entity.ddouble.multiply(entity2.ddouble).loe(2.0))
                        .select(entity).fetch());
    }

    @Test
    @ExcludeIn(ORACLE)
    public void multiply_bigDecimal() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");
        NumberPath<BigDecimal> bigd1 = entity.bigDecimal;
        NumberPath<BigDecimal> bigd2 = entity2.bigDecimal;

        assertEquals(Arrays.asList(),
                query().from(entity, entity2)
                        .where(bigd1.multiply(bigd2).loe(new BigDecimal("1.00")))
                        .select(entity).fetch());
    }

    @Test
    public void nestedProjection() {
        Concatenation concat = new Concatenation(cat.name, cat.name);
        List<Tuple> tuples = query().from(cat).select(cat.name, concat).fetch();
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples) {
            assertEquals(
                tuple.get(concat),
                tuple.get(cat.name) + tuple.get(cat.name));
        }
    }

    @Test
    public void not_in() {
        long all = query().from(cat).fetchCount();
        assertEquals(all - 3L, query().from(cat).where(cat.name.notIn("Bob123", "Ruth123", "Felix123")).fetchCount());

        assertEquals(3L, query().from(cat).where(cat.id.notIn(1, 2, 3)).fetchCount());
        assertEquals(6L, query().from(cat).where(cat.name.notIn("A", "B", "C")).fetchCount());
    }

    @Test
    @NoBatooJPA
    public void not_in_empty() {
        long count = query().from(cat).fetchCount();
        assertEquals(count, query().from(cat).where(cat.name.notIn(Collections.<String>emptyList())).fetchCount());
    }

    @Test
    public void null_as_uniqueResult() {
        assertNull(query().from(cat).where(cat.name.eq(UUID.randomUUID().toString()))
                .select(cat).fetchFirst());
    }

    @Test
    @NoEclipseLink
    public void numeric() {
        QNumeric numeric = QNumeric.numeric;
        BigDecimal singleResult = query().from(numeric).select(numeric.value).fetchFirst();
        assertEquals(26.9, singleResult.doubleValue(), 0.001);
    }

    @Test
    @NoOpenJPA
    @NoBatooJPA // FIXME
    public void offset1() {
        List<String> names2 = Arrays.asList("Bob123", "Felix123", "Mary_123", "Ruth123", "Some");
        assertEquals(names2, query().from(cat).orderBy(cat.name.asc()).offset(1).select(cat.name).fetch());
    }

    @Test
    @NoOpenJPA
    @NoBatooJPA // FIXME
    public void offset2() {
        List<String> names2 = Arrays.asList("Felix123", "Mary_123", "Ruth123", "Some");
        assertEquals(names2, query().from(cat).orderBy(cat.name.asc()).offset(2).select(cat.name).fetch());
    }

    @Test
    public void one_to_one() {
        QEmployee employee = QEmployee.employee;
        QUser user = QUser.user;

        JPQLQuery<?> query = query();
        query.from(employee);
        query.innerJoin(employee.user, user);
        query.select(employee).fetch();
    }

    @Test
    public void order() {
        NumberPath<Double> weight = Expressions.numberPath(Double.class, "weight");
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0),
                query().from(cat).orderBy(weight.asc()).select(cat.bodyWeight.as(weight)).fetch());
    }

    @Test
    public void order_by_count() {
        NumberPath<Long> count = Expressions.numberPath(Long.class, "c");
        query().from(cat)
            .groupBy(cat.id)
            .orderBy(count.asc())
            .select(cat.id, cat.id.count().as(count)).fetch();
    }

    @Test
    public void order_stringValue() {
        int count = (int) query().from(cat).fetchCount();
        assertEquals(count, query().from(cat).orderBy(cat.id.stringValue().asc()).select(cat).fetch().size());
    }

    @Test
    @NoBatooJPA // can't be parsed
    public void order_stringValue_to_integer() {
        int count = (int) query().from(cat).fetchCount();
        assertEquals(count, query().from(cat).orderBy(cat.id.stringValue().castToNum(Integer.class).asc()).select(cat).fetch().size());
    }

    @Test
    @NoBatooJPA // can't be parsed
    public void order_stringValue_toLong() {
        int count = (int) query().from(cat).fetchCount();
        assertEquals(count, query().from(cat).orderBy(cat.id.stringValue().castToNum(Long.class).asc()).select(cat).fetch().size());
    }

    @Test
    @NoBatooJPA // can't be parsed
    public void order_stringValue_toBigInteger() {
        int count = (int) query().from(cat).fetchCount();
        assertEquals(count, query().from(cat).orderBy(cat.id.stringValue().castToNum(BigInteger.class).asc()).select(cat).fetch().size());
    }

    @Test
    @NoBatooJPA
    @ExcludeIn(SQLSERVER)
    public void order_nullsFirst() {
        assertNull(query().from(cat)
                .orderBy(cat.dateField.asc().nullsFirst())
                .select(cat.dateField).fetchFirst());
    }

    @Test
    @NoBatooJPA
    @ExcludeIn(SQLSERVER)
    public void order_nullsLast() {
        assertNotNull(query().from(cat)
                .orderBy(cat.dateField.asc().nullsLast())
                .select(cat.dateField).fetchFirst());
    }

    @Test
    public void params() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Bob123", query().from(cat).where(cat.name.eq(name)).set(name, "Bob123")
                .select(cat.name).fetchFirst());
    }

    @Test
    public void params_anon() {
        Param<String> name = new Param<String>(String.class);
        assertEquals("Bob123",query().from(cat).where(cat.name.eq(name)).set(name, "Bob123")
                .select(cat.name).fetchFirst());
    }

    @Test(expected = ParamNotSetException.class)
    public void params_not_set() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Bob123", query().from(cat).where(cat.name.eq(name)).select(cat.name).fetchFirst());
    }

    @Test
    public void precedence() {
        StringPath str = cat.name;
        Predicate where = str.like("Bob%").and(str.like("%ob123"))
                      .or(str.like("Ruth%").and(str.like("%uth123")));
        assertEquals(2L, query().from(cat).where(where).fetchCount());
    }

    @Test
    public void precedence2() {
        StringPath str = cat.name;
        Predicate where = str.like("Bob%").and(str.like("%ob123")
                      .or(str.like("Ruth%"))).and(str.like("%uth123"));
        assertEquals(0L, query().from(cat).where(where).fetchCount());
    }

    @Test
    public void precedence3() {
        Predicate where = cat.name.eq("Bob123").and(cat.id.eq(1))
                      .or(cat.name.eq("Ruth123").and(cat.id.eq(2)));
        assertEquals(2L, query().from(cat).where(where).fetchCount());
    }

    @Test
    public void factoryExpression_in_groupBy() {
        Expression<Cat> catBean = Projections.bean(Cat.class, cat.id, cat.name);
        assertFalse(query().from(cat).groupBy(catBean).select(catBean).fetch().isEmpty());
    }

    @Test
    @Ignore
    public void size() {
        // NOT SUPPORTED
        query().from(cat).select(cat, cat.kittens.size()).fetch();
    }

    @Test
    public void startsWith() {
        assertEquals(1, query().from(cat).where(cat.name.startsWith("R")).fetchCount());
    }

    @Test
    public void startsWith_ignoreCase() {
        assertEquals(1, query().from(cat).where(cat.name.startsWithIgnoreCase("r")).fetchCount());
    }

    @Test
    public void startsWith2() {
        assertEquals(0, query().from(cat).where(cat.name.startsWith("X")).fetchCount());
    }

    @Test
    public void startsWith3() {
        assertEquals(1, query().from(cat).where(cat.name.startsWith("Mary_")).fetchCount());
    }

    @Test
    @ExcludeIn({MYSQL, SQLSERVER, TERADATA})
    @NoOpenJPA
    public void stringOperations() {
        // NOTE : locate in MYSQL is case-insensitive
        assertEquals(0, query().from(cat).where(cat.name.startsWith("r")).fetchCount());
        assertEquals(0, query().from(cat).where(cat.name.endsWith("H123")).fetchCount());
        assertEquals(Integer.valueOf(2), query().from(cat).where(cat.name.eq("Bob123"))
                .select(cat.name.indexOf("b")).fetchFirst());
    }

    @Test
    public void subQuery() {
        QShow show = QShow.show;
        QShow show2 = new QShow("show2");
        assertEquals(0,
                query().from(show).where(select(show2.count()).from(show2)
                        .where(show2.id.ne(show.id)).gt(0L)).fetchCount());
    }

    @Test
    public void subQuery2() {
        QCat cat = QCat.cat;
        QCat other = new QCat("other");
        assertEquals(savedCats, query().from(cat)
                .where(cat.name.in(select(other.name).from(other)
                        .groupBy(other.name)))
                .orderBy(cat.id.asc())
                .select(cat).fetch());
    }

    @Test
    public void subQuery3() {
        QCat cat = QCat.cat;
        QCat other = new QCat("other");
        assertEquals(savedCats.subList(0, 1), query().from(cat)
                .where(cat.name.eq(select(other.name).from(other)
                        .where(other.name.indexOf("B").eq(0))))
                .select(cat).fetch());
    }

    @Test
    public void subQuery4() {
        QCat cat = QCat.cat;
        QCat other = new QCat("other");
        query().from(cat)
                .select(cat.name, select(other.count()).from(other).where(other.name.eq(cat.name))).fetch();
    }

    @Test
    public void subQuery5() {
        QEmployee employee = QEmployee.employee;
        QEmployee employee2 = new QEmployee("e2");
        assertEquals(2, query().from(employee)
                .where(select(employee2.id.count()).from(employee2).gt(1L))
                .fetchCount());
    }

    @Test
    public void substring() {
        for (String str : query().from(cat).select(cat.name.substring(1,2)).fetch()) {
            assertEquals(1, str.length());
        }
    }

    @Test
    @NoBatooJPA
    @ExcludeIn(ORACLE)
    public void substring2() {
        QCompany company = QCompany.company;
        StringExpression name = company.name;
        Integer companyId = query().from(company).select(company.id).fetchFirst();
        JPQLQuery<?> query = query().from(company).where(company.id.eq(companyId));
        String str = query.select(company.name).fetchFirst();

        assertEquals(Integer.valueOf(29),
                query.select(name.length().subtract(11)).fetchFirst());

        assertEquals(str.substring(0, 7),
                query.select(name.substring(0, 7)).fetchFirst());

        assertEquals(str.substring(15),
                query.select(name.substring(15)).fetchFirst());

        assertEquals(str.substring(str.length()),
                query.select(name.substring(name.length())).fetchFirst());

        assertEquals(str.substring(str.length() - 11),
                query.select(name.substring(name.length().subtract(11))).fetchFirst());
    }

    @Test
    @Ignore // FIXME
    @ExcludeIn(DERBY)
    public void substring_from_right() {
        assertEquals(Arrays.asList(), query().from(cat)
                .where(cat.name.substring(-1, 1).eq(cat.name.substring(-2, 1)))
                .select(cat).fetch());
    }

    @Test
    @ExcludeIn({HSQLDB, DERBY})
    public void substring_from_right2() {
        assertEquals(Arrays.asList(), query().from(cat)
                .where(cat.name.substring(cat.name.length().subtract(1), cat.name.length())
                        .eq(cat.name.substring(cat.name.length().subtract(2), cat.name.length().subtract(1))))
                .select(cat).fetch());
    }

    @Test
    @ExcludeIn(ORACLE)
    public void subtract_bigDecimal() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");
        NumberPath<BigDecimal> bigd1 = entity.bigDecimal;
        NumberPath<BigDecimal> bigd2 = entity2.bigDecimal;

        assertEquals(Arrays.asList(), query().from(entity, entity2)
                .where(bigd1.subtract(bigd2).loe(new BigDecimal("1.00")))
                .select(entity).fetch());
    }

    @Test
    @Ignore
    public void sum() throws RecognitionException, TokenStreamException {
        // NOT SUPPORTED
        query().from(cat).select(cat.kittens.size().sum()).fetch();
    }

    @Test
    @Ignore
    public void sum_2() throws RecognitionException, TokenStreamException {
        // NOT SUPPORTED
        query().from(cat).where(cat.kittens.size().sum().gt(0)).select(cat).fetch();
    }

    @Test
    public void sum_3() {
        assertEquals(21.0, query().from(cat).select(cat.bodyWeight.sum()).fetchFirst().doubleValue(), 0.0001);
    }

    @Test
    public void sum_3_projected() {
        double val = query().from(cat).select(cat.bodyWeight.sum()).fetchFirst();
        DoubleProjection projection = query().from(cat)
                .select(new QDoubleProjection(cat.bodyWeight.sum())).fetchFirst();
        assertEquals(val, projection.val, 0.001);
    }

    @Test
    public void sum_4() {
        Double dbl = query().from(cat).select(cat.bodyWeight.sum().negate()).fetchFirst();
        assertNotNull(dbl);
    }

    @Test
    public void sum_5() {
        QShow show = QShow.show;
        Long lng = query().from(show).select(show.id.sum()).fetchFirst();
        assertNotNull(lng);
    }

    @Test
    public void sum_of_integer() {
        QCat cat2 = new QCat("cat2");
        assertEquals(Arrays.asList(), query().from(cat)
                .where(select(cat2.breed.sum())
                        .from(cat2).where(cat2.eq(cat.mate)).gt(0))
                .select(cat).fetch());
    }

    @Test
    public void sum_of_float() {
        QCat cat2 = new QCat("cat2");
        query().from(cat)
               .where(select(cat2.floatProperty.sum())
                      .from(cat2).where(cat2.eq(cat.mate)).gt(0.0f))
               .select(cat).fetch();
    }

    @Test
    public void sum_of_double() {
        QCat cat2 = new QCat("cat2");
        query().from(cat)
               .where(select(cat2.bodyWeight.sum())
                      .from(cat2).where(cat2.eq(cat.mate)).gt(0.0))
               .select(cat).fetch();
    }

    @Test
    public void sum_as_float() {
        float val = query().from(cat).select(cat.floatProperty.sum()).fetchFirst();
        assertTrue(val > 0);
    }

    @Test
    public void sum_as_float_projected() {
        float val = query().from(cat).select(cat.floatProperty.sum()).fetchFirst();
        FloatProjection projection = query().from(cat)
                .select(new QFloatProjection(cat.floatProperty.sum())).fetchFirst();
        assertEquals(val, projection.val, 0.001);
    }

    @Test
    public void sum_as_float2() {
        float val = query().from(cat).select(cat.floatProperty.sum().negate()).fetchFirst();
        assertTrue(val < 0);
    }

    @Test
    public void sum_coalesce() {
        int val = query().from(cat).select(cat.weight.sum().coalesce(0)).fetchFirst();
        assertTrue(val == 0);
    }

    @Test
    public void sum_noRows_double() {
        assertEquals(null, query().from(cat)
                .where(cat.name.eq(UUID.randomUUID().toString()))
                .select(cat.bodyWeight.sum()).fetchFirst());
    }

    @Test
    public void sum_noRows_float() {
        assertEquals(null, query().from(cat)
                .where(cat.name.eq(UUID.randomUUID().toString()))
                .select(cat.floatProperty.sum()).fetchFirst());
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void test() {
        Cat kitten = savedCats.get(0);
        Cat noKitten = savedCats.get(savedCats.size() - 1);

        ProjectionsFactory projections = new ProjectionsFactory(Module.JPA, getTarget()) {
            @Override
            public <A,Q extends SimpleExpression<A>> Collection<Expression<?>> list(ListPath<A,Q> expr,
                    ListExpression<A,Q> other, A knownElement) {
                // NOTE : expr.get(0) is only supported in the where clause
                return Collections.<Expression<?>>singleton(expr.size());
            }
        };

        final EntityPath<?>[] sources = {cat, otherCat};
        final Predicate[] conditions = {condition};
        final Expression<?>[] projection = {cat.name, otherCat.name};

        QueryExecution standardTest = new QueryExecution(
                projections,
                new FilterFactory(projections, Module.JPA, getTarget()),
                new MatchingFiltersFactory(Module.JPA, getTarget())) {

            @Override
            protected Fetchable<?> createQuery() {
                // NOTE : EclipseLink needs extra conditions cond1 and code2
                return testQuery().from(sources).where(conditions);
            }

            @Override
            protected Fetchable<?> createQuery(Predicate filter) {
                // NOTE : EclipseLink needs extra conditions cond1 and code2
                return testQuery().from(sources).where(condition, filter).select(projection);
            }
        };

        // standardTest.runArrayTests(cat.kittensArray, otherCat.kittensArray, kitten, noKitten);
        standardTest.runBooleanTests(cat.name.isNull(), otherCat.kittens.isEmpty());
        standardTest.runCollectionTests(cat.kittens, otherCat.kittens, kitten, noKitten);
        standardTest.runDateTests(cat.dateField, otherCat.dateField, date);
        standardTest.runDateTimeTests(cat.birthdate, otherCat.birthdate, birthDate);
        standardTest.runListTests(cat.kittens, otherCat.kittens, kitten, noKitten);
        // standardTest.mapTests(cat.kittensByName, otherCat.kittensByName, "Kitty", kitten);

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
    public void tupleProjection() {
        List<Tuple> tuples = query().from(cat).select(cat.name, cat).fetch();
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples) {
            assertNotNull(tuple.get(cat.name));
            assertNotNull(tuple.get(cat));
        }
    }

    @Test
    public void tupleProjection_as_queryResults() {
        QueryResults<Tuple> tuples = query().from(cat).limit(1)
                .select(cat.name, cat).fetchResults();
        assertEquals(1, tuples.getResults().size());
        assertTrue(tuples.getTotal() > 0);
    }

    @Test
    @ExcludeIn(DERBY)
    public void transform_groupBy() {
        QCat kitten = new QCat("kitten");
        Map<Integer, Cat> result = query().from(cat).innerJoin(cat.kittens, kitten)
            .transform(GroupBy.groupBy(cat.id)
                    .as(Projections.constructor(Cat.class, cat.name, cat.id,
                            GroupBy.list(Projections.constructor(Cat.class, kitten.name, kitten.id)))));

        for (Cat entry : result.values()) {
            assertEquals(1, entry.getKittens().size());
        }
    }

    @Test
    @ExcludeIn(DERBY)
    public void transform_groupBy2() {
        QCat kitten = new QCat("kitten");
        Map<List<?>, Group> result = query().from(cat).innerJoin(cat.kittens, kitten)
            .transform(GroupBy.groupBy(cat.id, kitten.id)
                    .as(cat, kitten));

        assertFalse(result.isEmpty());
        for (Tuple row : query().from(cat).innerJoin(cat.kittens, kitten)
                .select(cat, kitten).fetch()) {
            assertNotNull(result.get(Arrays.asList(row.get(cat).getId(), row.get(kitten).getId())));
        }
    }

    @Test
    @ExcludeIn(DERBY)
    public void transform_groupBy_alias() {
        QCat kitten = new QCat("kitten");
        SimplePath<Cat> k = Expressions.path(Cat.class, "k");
        Map<Integer, Group> result = query().from(cat).innerJoin(cat.kittens, kitten)
            .transform(GroupBy.groupBy(cat.id)
                    .as(cat.name, cat.id,
                        GroupBy.list(Projections.constructor(Cat.class, kitten.name, kitten.id).as(k))));

        for (Group entry : result.values()) {
            assertNotNull(entry.getOne(cat.id));
            assertNotNull(entry.getOne(cat.name));
            assertFalse(entry.getList(k).isEmpty());
        }
    }

    @Test
    @NoBatooJPA
    public void treat() {
        QDomesticCat domesticCat = QDomesticCat.domesticCat;
        assertEquals(0, query().from(cat)
                .innerJoin(cat.mate, domesticCat._super)
                .where(domesticCat.name.eq("Bobby"))
                .fetchCount());
    }

    @Test
    @Ignore
    public void type() {
        assertEquals(Arrays.asList("C","C","C","C","C","C","A"),
                query().from(animal).orderBy(animal.id.asc()).select(JPAExpressions.type(animal)).fetch());
    }

    @Test
    @NoOpenJPA
    public void type_order() {
        assertEquals(Arrays.asList(10, 1, 2, 3, 4, 5, 6),
                query().from(animal).orderBy(JPAExpressions.type(animal).asc(), animal.id.asc())
                        .select(animal.id).fetch());
    }

    @Test
    @ExcludeIn(DERBY)
    public void byte_array() {
        QSimpleTypes simpleTypes = QSimpleTypes.simpleTypes;
        assertEquals(ImmutableList.of(), query().from(simpleTypes)
                .where(simpleTypes.byteArray.eq(new byte[]{0, 1})).select(simpleTypes).fetch());
    }
}

