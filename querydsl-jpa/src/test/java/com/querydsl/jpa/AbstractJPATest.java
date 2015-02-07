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

import static org.junit.Assert.*;
import static com.querydsl.core.Target.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.Calendar;
import java.util.Map.Entry;

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
import com.querydsl.jpa.domain.*;
import com.querydsl.jpa.domain.Company.Rating;
import com.querydsl.jpa.domain4.QBookMark;
import com.querydsl.jpa.domain4.QBookVersion;
import com.querydsl.jpa.hibernate.HibernateSubQuery;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.*;
import com.querydsl.core.types.expr.*;
import com.querydsl.core.types.path.*;
import com.querydsl.core.types.template.NumberTemplate;
import com.querydsl.core.testutil.ExcludeIn;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * @author tiwe
 *
 */
public abstract class AbstractJPATest {

    private static final Expression<?>[] NO_EXPRESSIONS = new Expression[0];

    private static final QCompany company = QCompany.company;

    private static final QAnimal animal = QAnimal.animal;

    private static final QCat cat = QCat.cat;

    private static final QCat otherCat = new QCat("otherCat");

    private static final BooleanExpression cond1 = cat.name.length().gt(0);

    private static final BooleanExpression cond2 = otherCat.name.length().gt(0);

    private static final Predicate condition = ExpressionUtils.and(
            (Predicate)ExpressionUtils.extract(cond1),
            (Predicate)ExpressionUtils.extract(cond2));

    private static final Date birthDate;

    private static final java.sql.Date date;

    private static final java.sql.Time time;

    private final List<Cat> savedCats = new ArrayList<Cat>();

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 2, 3, 4);
        cal.set(Calendar.MILLISECOND, 0);
        birthDate = cal.getTime();
        date = new java.sql.Date(cal.getTimeInMillis());
        time = new java.sql.Time(cal.getTimeInMillis());
    }

    protected Target getTarget() {
        return Mode.target.get();
    }

    protected abstract JPQLQuery query();

    protected abstract JPQLQuery testQuery();

    protected JPASubQuery subQuery() {
        return new JPASubQuery();
    }

    protected abstract void save(Object entity);

    @Before
    public void setUp() {
        if (query().from(cat).exists()) {
            savedCats.addAll(query().from(cat).orderBy(cat.id.asc()).list(cat));
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
    public void Add_BigDecimal() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");
        NumberPath<BigDecimal> bigd1 = entity.bigDecimal;
        NumberPath<BigDecimal> bigd2 = entity2.bigDecimal;

        query().from(entity, entity2)
               .where(bigd1.add(bigd2).loe(new BigDecimal("1.00")))
               .list(entity);
    }

    @Test
    public void Aggregates_List_Max() {
        assertEquals(Integer.valueOf(6), query().from(cat).list(cat.id.max()).get(0));
    }

    @Test
    public void Aggregates_List_Min() {
        assertEquals(Integer.valueOf(1), query().from(cat).list(cat.id.min()).get(0));
    }

    @Test
    public void Aggregates_UniqueResult_Max() {
        assertEquals(Integer.valueOf(6), query().from(cat).uniqueResult(cat.id.max()));
    }

    @Test
    public void Aggregates_UniqueResult_Min() {
        assertEquals(Integer.valueOf(1), query().from(cat).uniqueResult(cat.id.min()));
    }

    @Test
    public void Any_And_Gt() {
        assertEquals(0, query().from(cat).where(
                cat.kittens.any().name.eq("Ruth123"),
                cat.kittens.any().bodyWeight.gt(10.0)).count());
    }

    @Test
    public void Any_And_Lt() {
        assertEquals(1, query().from(cat).where(
                cat.kittens.any().name.eq("Ruth123"),
                cat.kittens.any().bodyWeight.lt(10.0)).count());
    }

    @Test
    public void Any_In_Order() {
        assertFalse(query().from(cat).orderBy(cat.kittens.any().name.asc()).list(cat).isEmpty());
    }

    @Test
    public void Any_In_Projection() {
        assertFalse(query().from(cat).list(cat.kittens.any()).isEmpty());
    }

    @Test
    public void Any_In_Projection2() {
        assertFalse(query().from(cat).list(cat.kittens.any().name).isEmpty());
    }

    @Test
    public void Any_In_Projection3() {
        assertFalse(query().from(cat).list(cat.kittens.any().name, cat.kittens.any().bodyWeight).isEmpty());
    }

    @Test
    public void Any_In1() {
        //select cat from Cat cat where exists (
        //  select cat_kittens from Cat cat_kittens where cat_kittens member of cat.kittens and cat_kittens in ?1)
        assertFalse(query().from(cat).where(cat.kittens.any().in(savedCats)).list(cat).isEmpty());
    }

    @Test
    public void Any_In11() {
        List<Integer> ids = Lists.newArrayList();
        for (Cat cat : savedCats) ids.add(cat.getId());
        assertFalse(query().from(cat).where(cat.kittens.any().id.in(ids)).list(cat).isEmpty());
    }

    @Test
    public void Any_In2() {
        assertFalse(query().from(cat).where(
                cat.kittens.any().in(savedCats),
                cat.kittens.any().in(savedCats.subList(0, 1)).not())
            .list(cat).isEmpty());
    }

    @Test
    @NoBatooJPA
    public void Any_In3() {
        QEmployee employee = QEmployee.employee;
        assertFalse(query().from(employee).where(
                employee.jobFunctions.any().in(JobFunction.CODER, JobFunction.CONSULTANT))
                .list(employee).isEmpty());
    }

    @Test
    public void Any_Simple() {
        assertEquals(1, query().from(cat).where(cat.kittens.any().name.eq("Ruth123")).count());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void ArrayProjection() {
        List<String[]> results = query().from(cat)
                .list(new ArrayConstructorExpression<String>(String[].class, cat.name));
        assertFalse(results.isEmpty());
        for (String[] result : results) {
            assertNotNull(result[0]);
        }
    }

    @Test
    public void As() {
        assertTrue(query().from(QAnimal.animal.as(QCat.class)).count() > 0);
    }

    @Test
    @NoBatooJPA
    public void Case() {
        query().from(cat).list(cat.name.when("Bob").then(1).otherwise(2));
    }

    @Test
    public void Case2() {
        query().from(cat)
            .list(Expressions.cases().when(cat.toes.eq(2)).then(cat.id.multiply(2))
                    .when(cat.toes.eq(3)).then(cat.id.multiply(3))
                    .otherwise(4));
    }

    @Test
    public void Case3() {
        query().from(cat)
            .list(Expressions.cases()
                    .when(cat.toes.in(2, 3)).then(cat.id.multiply(cat.toes))
                    .otherwise(4));
    }

    @Test
    public void CaseBuilder() {
        QCat cat2 = new QCat("cat2");
        NumberExpression<Integer> casex = new CaseBuilder()
                .when(cat.weight.isNull().and(cat.weight.isNull())).then(0)
                .when(cat.weight.isNull()).then(cat2.weight)
                .when(cat2.weight.isNull()).then(cat.weight)
                .otherwise(cat.weight.add(cat2.weight));

        query().from(cat, cat2).orderBy(casex.asc()).list(cat.id, cat2.id);
        query().from(cat, cat2).orderBy(casex.desc()).list(cat.id, cat2.id);
    }

    @Test
    public void Cast() {
        List<Cat> cats = query().from(cat).list(cat);
        List<Integer> weights = query().from(cat).list(cat.bodyWeight.castToNum(Integer.class));
        for (int i = 0; i < cats.size(); i++) {
            assertEquals(Integer.valueOf((int)(cats.get(i).getBodyWeight())), weights.get(i));
        }
    }

    @Test
    public void Cast_ToString() {
        for (Tuple tuple : query().from(cat).list(cat.breed, cat.breed.stringValue())) {
            assertEquals(
                    tuple.get(cat.breed).toString(),
                    tuple.get(cat.breed.stringValue()));
        }
    }

    @Test
    public void Collection_Predicates() {
        ListPath<Cat, QCat> path = cat.kittens;
        List<Predicate> predicates = Arrays.<Predicate>asList(
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
            query().from(cat).where(pred).list(cat);
        }
    }

    @Test
    public void Collection_Projections() {
        ListPath<Cat, QCat> path = cat.kittens;
        List<Expression<?>> projections = Arrays.<Expression<?>>asList(
//            path.count(),
//            path.countDistinct()
        );
        for (Expression<?> proj : projections) {
            System.err.println(proj);
            query().from(cat).list(proj);
        }
    }

    @Test
    @NoHibernate // https://github.com/querydsl/querydsl/issues/290
    public void Constant() {
        //select cat.id, ?1 as const from Cat cat
        List<Cat> cats = query().from(cat).list(cat);
        Path<String> path = new StringPath("const");
        List<Tuple> tuples = query().from(cat).list(cat.id, Expressions.constantAs("abc", path));
        for (int i = 0; i < cats.size(); i++) {
            assertEquals(Integer.valueOf(cats.get(i).getId()), tuples.get(i).get(cat.id));
            assertEquals("abc", tuples.get(i).get(path));
        }
    }

    @Test(expected=ClassCastException.class)
    @NoEclipseLink
    @NoBatooJPA
    public void Constant_Hibernate() {
        //select cat.id, ?1 as const from Cat cat
        query().from(cat).list(cat.id, Expressions.constantAs("abc", new StringPath("const")));
    }

    @Test
    @NoHibernate // https://github.com/querydsl/querydsl/issues/290
    public void Constant2() {
        assertFalse(query().from(cat).map(cat.id, Expressions.constant("name")).isEmpty());
    }

    @Test
    public void ConstructorProjection() {
        List<Projection> projections = query().from(cat)
                .list(ConstructorExpression.create(Projection.class, cat.name, cat));
        assertFalse(projections.isEmpty());
        for (Projection projection : projections) {
            assertNotNull(projection);
        }
    }

    @Test
    public void ConstructorProjection2() {
        List<Projection> projections = query().from(cat).list(new QProjection(cat.name, cat));
        assertFalse(projections.isEmpty());
        for (Projection projection : projections) {
            assertNotNull(projection);
        }
    }

    @Test
    public void Contains_Ic() {
        QFoo foo = QFoo.foo;
        assertEquals(1, query().from(foo).where(foo.bar.containsIgnoreCase("München")).count());
    }

    @Test
    public void Contains1() {
        assertEquals(1, query().from(cat).where(cat.name.contains("eli")).count());
    }

    @Test
    public void Contains2() {
        assertEquals(1l, query().from(cat).where(cat.kittens.contains(savedCats.get(0))).count());
    }

    @Test
    public void Contains3() {
        assertEquals(1l, query().from(cat).where(cat.name.contains("_")).count());
    }

    @Test
    public void Contains4() {
        QEmployee employee = QEmployee.employee;
        query().from(employee)
               .where(
                   employee.jobFunctions.contains(JobFunction.CODER),
                   employee.jobFunctions.contains(JobFunction.CONSULTANT),
                   employee.jobFunctions.size().eq(2))
               .list(employee);
    }

    @Test
    public void Count() {
        QShow show = QShow.show;
        assertTrue(query().from(show).count() > 0);
    }

    @Test
    public void Count_Distinct() {
        QCat cat = QCat.cat;
        query().from(cat)
            .groupBy(cat.id)
            .list(cat.id, cat.breed.countDistinct());
    }

    @Test
    @NoBatooJPA
    @NoHibernate
    public void Count_Distinct2() {
        QCat cat = QCat.cat;
        query().from(cat)
            .groupBy(cat.id)
            .list(cat.id, cat.birthdate.dayOfMonth().countDistinct());
    }

    @Test
    public void DistinctResults() {
        System.out.println("-- list results");
        SearchResults<Date> res = query().from(cat).limit(2).listResults(cat.birthdate);
        assertEquals(2, res.getResults().size());
        assertEquals(6l, res.getTotal());
        System.out.println();

        System.out.println("-- list distinct results");
        res = query().from(cat).limit(2).distinct().listResults(cat.birthdate);
        assertEquals(1, res.getResults().size());
        assertEquals(1l, res.getTotal());
        System.out.println();

        System.out.println("-- list distinct");
        assertEquals(1, query().from(cat).distinct().list(cat.birthdate).size());
    }

    @Test
    public void Date() {
        query().from(cat).list(cat.birthdate.year());
        query().from(cat).list(cat.birthdate.yearMonth());
        query().from(cat).list(cat.birthdate.month());
        //query().from(cat).list(cat.birthdate.week());
        query().from(cat).list(cat.birthdate.dayOfMonth());
        query().from(cat).list(cat.birthdate.hour());
        query().from(cat).list(cat.birthdate.minute());
        query().from(cat).list(cat.birthdate.second());
    }

    @Test
    @ExcludeIn(ORACLE)
    public void Divide() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");

        query().from(entity, entity2)
               .where(entity.ddouble.divide(entity2.ddouble).loe(2.0))
               .list(entity);

        query().from(entity, entity2)
                .where(entity.ddouble.divide(entity2.iint).loe(2.0))
                .list(entity);

        query().from(entity, entity2)
                .where(entity.iint.divide(entity2.ddouble).loe(2.0))
                .list(entity);

        query().from(entity, entity2)
                .where(entity.iint.divide(entity2.iint).loe(2))
                .list(entity);
    }

    @Test
    @ExcludeIn(ORACLE)
    public void Divide_BigDecimal() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");
        NumberPath<BigDecimal> bigd1 = entity.bigDecimal;
        NumberPath<BigDecimal> bigd2 = entity2.bigDecimal;

        query().from(entity, entity2)
               .where(bigd1.divide(bigd2).loe(new BigDecimal("1.00")))
               .list(entity);

        query().from(entity, entity2)
               .where(entity.ddouble.divide(bigd2).loe(new BigDecimal("1.00")))
               .list(entity);

        query().from(entity, entity2)
               .where(bigd1.divide(entity.ddouble).loe(new BigDecimal("1.00")))
               .list(entity);
    }

    @Test
    public void EndsWith() {
        assertEquals(1, query().from(cat).where(cat.name.endsWith("h123")).count());
    }

    @Test
    public void EndsWith_IgnoreCase() {
        assertEquals(1, query().from(cat).where(cat.name.endsWithIgnoreCase("H123")).count());
    }

    @Test
    public void EndsWith2() {
        assertEquals(0, query().from(cat).where(cat.name.endsWith("X")).count());
    }

    @Test
    public void EndsWith3() {
        assertEquals(1, query().from(cat).where(cat.name.endsWith("_123")).count());
    }

    @Test
    @NoBatooJPA
    public void Enum_Eq() {
        assertEquals(1, query().from(company).where(company.ratingOrdinal.eq(Rating.A)).count());
        assertEquals(1, query().from(company).where(company.ratingString.eq(Rating.AA)).count());
    }

    @Test
    @NoBatooJPA
    public void Enum_In() {
        assertEquals(1, query().from(company).where(company.ratingOrdinal.in(Rating.A, Rating.AA)).count());
        assertEquals(1, query().from(company).where(company.ratingString.in(Rating.A, Rating.AA)).count());
    }

    @Test
    @NoBatooJPA
    public void Enum_In2() {
        QEmployee employee = QEmployee.employee;

        JPQLQuery query = query();
        query.from(employee).where(employee.lastName.eq("Smith"), employee.jobFunctions
                .contains(JobFunction.CODER));
        assertEquals(1l, query.count());
    }

    @Test
    public void Enum_StartsWith() {
        assertEquals(1, query().from(company).where(company.ratingString.stringValue().startsWith("A")).count());
    }

    @Test
    public void Exists() {
        assertTrue(query().from(cat).where(cat.kittens.any().name.eq("Ruth123")).exists());
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void Fetch() {
        QMammal mammal = QMammal.mammal;
        QHuman human = new QHuman("mammal");
        query().from(mammal)
            .leftJoin(human.hairs).fetch()
            .list(mammal);
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void Fetch2() {
        QWorld world = QWorld.world;
        QMammal mammal = QMammal.mammal;
        QHuman human = new QHuman("mammal");
        query().from(world)
            .leftJoin(world.mammals, mammal).fetch()
            .leftJoin(human.hairs).fetch()
            .list(world);
    }

    @Test
    @ExcludeIn({MYSQL, DERBY})
    @NoBatooJPA
    public void GroupBy() {
        QAuthor author = QAuthor.author;
        QBook book = QBook.book;

        for (int i = 0; i < 10; i++) {
            Author a = new Author();
            a.setName(String.valueOf(i));
            save(a);
            for (int j = 0; j < 2; j++) {
                Book b = new Book();
                b.setTitle(String.valueOf(i)+" "+String.valueOf(j));
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
    public void GroupBy2() {
//        select cat0_.name as col_0_0_, cat0_.breed as col_1_0_, sum(cat0_.bodyWeight) as col_2_0_
//        from animal_ cat0_ where cat0_.DTYPE in ('C', 'DC') and cat0_.bodyWeight>?
//        group by cat0_.name , cat0_.breed
        query().from(cat)
            .where(cat.bodyWeight.gt(0))
            .groupBy(cat.name, cat.breed)
            .list(cat.name, cat.breed, cat.bodyWeight.sum());
    }

    @Test
    @NoEclipseLink
    public void GroupBy_YearMonth() {
        query().from(cat)
               .groupBy(cat.birthdate.yearMonth())
               .orderBy(cat.birthdate.yearMonth().asc())
               .list(cat.id.count());
    }

    @Test
    @Ignore // FIXME
    public void GroupBy_Count() {
        List<Integer> ids = query().from(cat).groupBy(cat.id).list(cat.id);
        long count = query().from(cat).groupBy(cat.id).count();
        SearchResults<Integer> results = query().from(cat).groupBy(cat.id)
                .limit(1).listResults(cat.id);

        long catCount = query().from(cat).count();
        assertEquals(catCount, ids.size());
        assertEquals(catCount, count);
        assertEquals(catCount, results.getResults().size());
        assertEquals(catCount, results.getTotal());
    }

    @Test
    @Ignore // FIXME
    public void GroupBy_Distinct_Count() {
        List<Integer> ids = query().from(cat).groupBy(cat.id).distinct().list(NumberTemplate.ONE);
        SearchResults<Integer> results = query().from(cat).groupBy(cat.id)
                .limit(1).distinct().listResults(NumberTemplate.ONE);

        assertEquals(1, ids.size());
        assertEquals(1, results.getResults().size());
        assertEquals(1, results.getTotal());
    }

    @Test
    public void In() {
        assertEquals(3l, query().from(cat).where(cat.name.in("Bob123", "Ruth123", "Felix123")).count());

        query().from(cat).where(cat.id.in(Arrays.asList(1,2,3))).count();
        query().from(cat).where(cat.name.in(Arrays.asList("A","B","C"))).count();
    }

    @Test
    public void In2() {
        query().from(cat).where(cat.id.in(1,2,3)).count();
        query().from(cat).where(cat.name.in("A","B","C")).count();
    }

    @Test
    public void In3() {
        query().from(cat).where(cat.name.in("A,B,C".split(","))).count();
    }

    @Test
    public void In4() {
        //$.parameterRelease.id.eq(releaseId).and($.parameterGroups.any().id.in(filter.getGroups()));
        query().from(cat).where(cat.id.eq(1), cat.kittens.any().id.in(1,2,3)).list(cat);
    }

    @Test
    public void In5() {
        query().from(cat).where(cat.mate.in(savedCats)).count();
    }

    @Test
    @Ignore
    public void In6() {
        //query().from(cat).where(cat.kittens.in(savedCats)).count();
    }

    @Test
    public void In7() {
        query().from(cat).where(cat.kittens.any().in(savedCats)).count();
    }

    @Test
    public void In_Empty() {
        assertEquals(0, query().from(cat).where(cat.name.in(ImmutableList.<String>of())).count());
    }

    @Test
    @NoOpenJPA
    public void IndexOf() {
        assertEquals(Integer.valueOf(0), query().from(cat).where(cat.name.eq("Bob123"))
                .uniqueResult(cat.name.indexOf("B")));
    }

    @Test
    @NoOpenJPA
    public void IndexOf2() {
        assertEquals(Integer.valueOf(1), query().from(cat).where(cat.name.eq("Bob123"))
                .uniqueResult(cat.name.indexOf("o")));
    }

    @Test
    public void InstanceOf_Cat() {
        assertEquals(6l, query().from(cat).where(cat.instanceOf(Cat.class)).count());
    }

    @Test
    public void InstanceOf_DomesticCat() {
        assertEquals(0l, query().from(cat).where(cat.instanceOf(DomesticCat.class)).count());
    }

    @Test
    public void InstanceOf_Entity1() {
        QEntity1 entity1 = QEntity1.entity1;
        assertEquals(2l, query().from(entity1).where(entity1.instanceOf(Entity1.class)).count());
    }

    @Test
    public void InstanceOf_Entity2() {
        QEntity1 entity1 = QEntity1.entity1;
        assertEquals(1l, query().from(entity1).where(entity1.instanceOf(Entity2.class)).count());
    }

    @Test
    @NoHibernate // https://hibernate.atlassian.net/browse/HHH-6686
    public void IsEmpty_ElementCollection() {
        QEmployee employee = QEmployee.employee;
        query().from(employee).where(employee.jobFunctions.isEmpty()).count();
    }

    @Test
    public void IsEmpty_Relation() {
        query().from(cat).where(cat.kittensSet.isEmpty()).count();
    }

    @Test
    @NoEclipseLink
    @ExcludeIn({ORACLE, TERADATA})
    public void JoinEmbeddable() {
        QBookVersion bookVersion = QBookVersion.bookVersion;
        QBookMark bookMark = QBookMark.bookMark;

        query().from(bookVersion)
            .join(bookVersion.definition.bookMarks, bookMark)
            .where(
                bookVersion.definition.bookMarks.size().eq(1),
                bookMark.page.eq(2357L).or(bookMark.page.eq(2356L)))
            .list(bookVersion);
    }

    @Test
    public void Length() {
        assertEquals(6, query().from(cat).where(cat.name.length().gt(0)).count());
    }

    @Test
    public void Like() {
        query().from(cat).where(cat.name.like("!")).count();
        query().from(cat).where(cat.name.like("\\")).count();
    }

    @Test
    public void Limit() {
        List<String> names1 = Arrays.asList("Allen123","Bob123");
        assertEquals(names1, query().from(cat).orderBy(cat.name.asc()).limit(2).list(cat.name));
    }

    @Test
    public void Limit_and_offset() {
        List<String> names3 = Arrays.asList("Felix123","Mary_123");
        assertEquals(names3, query().from(cat).orderBy(cat.name.asc()).limit(2).offset(2).list(cat.name));
    }

    @Test
    public void Limit2() {
        assertEquals(Collections.singletonList("Allen123"),
                query().from(cat).orderBy(cat.name.asc()).limit(1).list(cat.name));
    }

    @Test
    public void Limit3() {
        assertEquals(6, query().from(cat).limit(Long.MAX_VALUE).list(cat).size());
    }

    @Test
    public void List_ElementCollection_Of_Enum() {
        QEmployee employee = QEmployee.employee;
        //QJobFunction jobFunction = QJobFunction.jobFunction;
        EnumPath<JobFunction> jobFunction = new EnumPath<JobFunction>(JobFunction.class, "jf");

        List<JobFunction> jobFunctions = query().from(employee)
                .innerJoin(employee.jobFunctions, jobFunction).list(jobFunction);
        assertEquals(4, jobFunctions.size());
    }

    @Test
    @NoBatooJPA
    public void List_ElementCollection_Of_String() {
        QFoo foo = QFoo.foo;
        StringPath str = new StringPath("str");

        List<String> strings = query().from(foo).innerJoin(foo.names, str).list(str);
        assertEquals(2, strings.size());
        assertTrue(strings.contains("a"));
        assertTrue(strings.contains("b"));
    }

    @Test
    public void Map_Get() {
        QShow show = QShow.show;
        query().from(show).list(show.acts.get("a"));
    }

    @Test
    @NoHibernate
    public void Map_Get2() {
        QShow show = QShow.show;
        assertEquals(1, query().from(show).where(show.acts.get("a").eq("A")).count());
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void Map_ContainsKey() {
        QShow show = QShow.show;
        assertEquals(1l, query().from(show).where(show.acts.containsKey("a")).count());
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void Map_ContainsKey2() {
        QShow show = QShow.show;
        assertEquals(1l, query().from(show).where(show.acts.containsKey("b")).count());
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void Map_ContainsKey3() {
        QShow show = QShow.show;
        assertEquals(0l, query().from(show).where(show.acts.containsKey("c")).count());
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void Map_ContainsValue() {
        QShow show = QShow.show;
        assertEquals(1l, query().from(show).where(show.acts.containsValue("A")).count());
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void Map_ContainsValue2() {
        QShow show = QShow.show;
        assertEquals(1l, query().from(show).where(show.acts.containsValue("B")).count());
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void Map_ContainsValue3() {
        QShow show = QShow.show;
        assertEquals(0l, query().from(show).where(show.acts.containsValue("C")).count());
    }

    @Test
    @Ignore
    public void Map_Join() {
        //select m.text from Show s join s.acts a where key(a) = 'B'
        QShow show = QShow.show;
        StringPath act = new StringPath("act");
        query().from(show).join(show.acts, act);
    }

    @Test
    public void Max() {
        query().from(cat).uniqueResult(cat.bodyWeight.max());
    }

    @Test
    public void Min() {
        query().from(cat).uniqueResult(cat.bodyWeight.min());
    }

    @Test
    @ExcludeIn(ORACLE)
    public void Multiply() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");
        query().from(entity, entity2)
               .where(entity.ddouble.multiply(entity2.ddouble).loe(2.0))
               .list(entity);
    }

    @Test
    @ExcludeIn(ORACLE)
    public void Multiply_BigDecimal() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");
        NumberPath<BigDecimal> bigd1 = entity.bigDecimal;
        NumberPath<BigDecimal> bigd2 = entity2.bigDecimal;

        query().from(entity, entity2)
               .where(bigd1.multiply(bigd2).loe(new BigDecimal("1.00")))
               .list(entity);
    }

    @Test
    public void NestedProjection() {
        Concatenation concat = new Concatenation(cat.name, cat.name);
        List<Tuple> tuples = query().from(cat).list(cat.name, concat);
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples) {
            assertEquals(
                tuple.get(concat),
                tuple.get(cat.name)+tuple.get(cat.name));
        }
    }

    @Test
    public void Not_Exists() {
        assertTrue(query().from(cat).where(cat.kittens.any().name.eq("XXX")).notExists());
    }

    @Test
    public void Not_In() {
        long all = query().from(cat).count();
        assertEquals(all - 3l, query().from(cat).where(cat.name.notIn("Bob123", "Ruth123", "Felix123")).count());

        query().from(cat).where(cat.id.notIn(1,2,3)).count();
        query().from(cat).where(cat.name.notIn("A","B","C")).count();
    }

    @Test
    @NoBatooJPA
    public void Not_In_Empty() {
        long count = query().from(cat).count();
        assertEquals(count, query().from(cat).where(cat.name.notIn(Collections.<String>emptyList())).count());
    }

    @Test
    public void Null_as_uniqueResult() {
        assertNull(query().from(cat).where(cat.name.eq(UUID.randomUUID().toString()))
                .uniqueResult(cat));
    }

    @Test
    @NoEclipseLink
    public void Numeric() {
        QNumeric numeric = QNumeric.numeric;
        BigDecimal singleResult = query().from(numeric).singleResult(numeric.value);
        assertEquals(26.9, singleResult.doubleValue(), 0.001);
    }

    @Test
    @NoOpenJPA
    @NoBatooJPA // FIXME
    public void Offset1() {
        List<String> names2 = Arrays.asList("Bob123", "Felix123", "Mary_123", "Ruth123", "Some");
        assertEquals(names2, query().from(cat).orderBy(cat.name.asc()).offset(1).list(cat.name));
    }

    @Test
    @NoOpenJPA
    @NoBatooJPA // FIXME
    public void Offset2() {
        List<String> names2 = Arrays.asList("Felix123", "Mary_123", "Ruth123", "Some");
        assertEquals(names2, query().from(cat).orderBy(cat.name.asc()).offset(2).list(cat.name));
    }

    @Test
    public void One_To_One() {
        QEmployee employee = QEmployee.employee;
        QUser user = QUser.user;

        JPQLQuery query = query();
        query.from(employee);
        query.innerJoin(employee.user, user);
        query.list(employee);
    }

    @Test
    public void Order() {
        NumberPath<Double> weight = new NumberPath<Double>(Double.class, "weight");
        query().from(cat).orderBy(weight.asc()).list(cat.bodyWeight.as(weight));
    }

    @Test
    public void Order_By_Count() {
        NumberPath<Long> count = Expressions.numberPath(Long.class, "c");
        query().from(cat)
            .groupBy(cat.id)
            .orderBy(count.asc())
            .list(cat.id, cat.id.count().as(count));
    }

    @Test
    public void Order_StringValue() {
        int count = (int)query().from(cat).count();
        assertEquals(count, query().from(cat).orderBy(cat.id.stringValue().asc()).list(cat).size());
    }

    @Test
    @NoBatooJPA // can't be parsed
    public void Order_StringValue_To_Integer() {
        int count = (int)query().from(cat).count();
        assertEquals(count, query().from(cat).orderBy(cat.id.stringValue().castToNum(Integer.class).asc()).list(cat).size());
    }

    @Test
    @NoBatooJPA // can't be parsed
    public void Order_StringValue_ToLong() {
        int count = (int)query().from(cat).count();
        assertEquals(count, query().from(cat).orderBy(cat.id.stringValue().castToNum(Long.class).asc()).list(cat).size());
    }

    @Test
    @NoBatooJPA // can't be parsed
    public void Order_StringValue_ToBigInteger() {
        int count = (int)query().from(cat).count();
        assertEquals(count, query().from(cat).orderBy(cat.id.stringValue().castToNum(BigInteger.class).asc()).list(cat).size());
    }

    @Test
    @NoBatooJPA
    public void Order_NullsFirst() {
        assertNull(query().from(cat)
            .orderBy(cat.dateField.asc().nullsFirst())
            .singleResult(cat.dateField));
    }

    @Test
    @NoBatooJPA
    public void Order_NullsLast() {
        assertNotNull(query().from(cat)
            .orderBy(cat.dateField.asc().nullsLast())
            .singleResult(cat.dateField));
    }

    @Test
    public void Params() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Bob123",query().from(cat).where(cat.name.eq(name)).set(name, "Bob123")
                .uniqueResult(cat.name));
    }

    @Test
    public void Params_anon() {
        Param<String> name = new Param<String>(String.class);
        assertEquals("Bob123",query().from(cat).where(cat.name.eq(name)).set(name, "Bob123")
                .uniqueResult(cat.name));
    }

    @Test(expected=ParamNotSetException.class)
    public void Params_not_set() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Bob123",query().from(cat).where(cat.name.eq(name)).uniqueResult(cat.name));
    }

    @Test
    public void Precedence() {
        StringPath str = cat.name;
        Predicate where = str.like("Bob%").and(str.like("%ob123"))
                      .or(str.like("Ruth%").and(str.like("%uth123")));
        assertEquals(2l, query().from(cat).where(where).count());
    }

    @Test
    public void Precedence2() {
        StringPath str = cat.name;
        Predicate where = str.like("Bob%").and(str.like("%ob123")
                      .or(str.like("Ruth%"))).and(str.like("%uth123"));
        assertEquals(0l, query().from(cat).where(where).count());
    }

    @Test
    public void Precedence3() {
        Predicate where = cat.name.eq("Bob123").and(cat.id.eq(1))
                      .or(cat.name.eq("Ruth123").and(cat.id.eq(2)));
        assertEquals(2l, query().from(cat).where(where).count());
    }

    @Test
    public void FactoryExpression_In_GroupBy() {
        Expression<Cat> catBean = Projections.bean(Cat.class, cat.id, cat.name);
        assertFalse(query().from(cat).groupBy(catBean).list(catBean).isEmpty());
    }

    @Test
    @Ignore
    public void Size() {
        // NOT SUPPORTED
        query().from(cat).list(cat, cat.kittens.size());
    }

    @Test
    public void StartsWith() {
        assertEquals(1, query().from(cat).where(cat.name.startsWith("R")).count());
    }

    @Test
    public void StartsWith_IgnoreCase() {
        assertEquals(1, query().from(cat).where(cat.name.startsWithIgnoreCase("r")).count());
    }

    @Test
    public void StartsWith2() {
        assertEquals(0, query().from(cat).where(cat.name.startsWith("X")).count());
    }

    @Test
    public void StartsWith3() {
        assertEquals(1, query().from(cat).where(cat.name.startsWith("Mary_")).count());
    }

    @Test
    @ExcludeIn({MYSQL, TERADATA})
    @NoOpenJPA
    public void StringOperations() {
        // NOTE : locate in MYSQL is case-insensitive
        assertEquals(0, query().from(cat).where(cat.name.startsWith("r")).count());
        assertEquals(0, query().from(cat).where(cat.name.endsWith("H123")).count());
        assertEquals(Integer.valueOf(2), query().from(cat).where(cat.name.eq("Bob123"))
                .uniqueResult(cat.name.indexOf("b")));
    }

    @Test
    public void SubQuery() {
        QShow show = QShow.show;
        QShow show2 = new QShow("show2");
        query().from(show).where(subQuery().from(show2)
               .where(show2.id.ne(show.id)).count().gt(0)).count();
    }

    @Test
    public void SubQuery2() {
        QCat cat = QCat.cat;
        QCat other = new QCat("other");
        List<Cat> cats = query().from(cat)
            .where(cat.name.in(new HibernateSubQuery().from(other)
            .groupBy(other.name).list(other.name)))
            .list(cat);
        assertNotNull(cats);
    }

    @Test
    public void SubQuery3() {
        QCat cat = QCat.cat;
        QCat other = new QCat("other");
        query().from(cat)
            .where(cat.name.eq(new JPASubQuery().from(other)
                                       .where(other.name.indexOf("B").eq(0))
                                       .unique(other.name)))
            .list(cat);
    }

    @Test
    public void SubQuery4() {
        QCat cat = QCat.cat;
        QCat other = new QCat("other");
        query().from(cat)
               .list(cat.name, new JPASubQuery().from(other).where(other.name.eq(cat.name)).count());
    }

    @Test
    public void SubQuery5() {
        QEmployee employee = QEmployee.employee;
        QEmployee employee2 = new QEmployee("e2");
        query().from(employee)
                .where(subQuery().from(employee2)
                        .list(employee2.id).count().gt(1))
                .count();
    }

    @Test
    public void Substring() {
        for (String str : query().from(cat).list(cat.name.substring(1,2))) {
            assertEquals(1, str.length());
        }
    }

    @Test
    @NoBatooJPA
    @ExcludeIn(ORACLE)
    public void Substring2() {
        QCompany company = QCompany.company;
        StringExpression name = company.name;
        Integer companyId = query().from(company).singleResult(company.id);
        JPQLQuery query = query().from(company).where(company.id.eq(companyId));
        String str = query.singleResult(company.name);

        assertEquals(Integer.valueOf(29),
                query.singleResult(name.length().subtract(11)));

        assertEquals(str.substring(0, 7),
                query.singleResult(name.substring(0, 7)));

        assertEquals(str.substring(15),
                query.singleResult(name.substring(15)));

        assertEquals(str.substring(str.length()),
                query.singleResult(name.substring(name.length())));

        assertEquals(str.substring(str.length() - 11),
                query.singleResult(name.substring(name.length().subtract(11))));
    }

    @Test
    @ExcludeIn(DERBY)
    public void Substring_From_Right() {
        query().from(cat)
            .where(cat.name.substring(-1, 1).eq(cat.name.substring(-2, 1)))
            .list(cat);
    }

    @Test
    @ExcludeIn({HSQLDB, DERBY})
    public void Substring_From_Right2() {
        query().from(cat)
            .where(cat.name.substring(cat.name.length().subtract(1), cat.name.length())
                    .eq(cat.name.substring(cat.name.length().subtract(2), cat.name.length().subtract(1))))
            .list(cat);
    }

    @Test
    @ExcludeIn(ORACLE)
    public void Subtract_BigDecimal() {
        QSimpleTypes entity = new QSimpleTypes("entity1");
        QSimpleTypes entity2 = new QSimpleTypes("entity2");
        NumberPath<BigDecimal> bigd1 = entity.bigDecimal;
        NumberPath<BigDecimal> bigd2 = entity2.bigDecimal;

        query().from(entity, entity2)
               .where(bigd1.subtract(bigd2).loe(new BigDecimal("1.00")))
               .list(entity);
    }

    @Test
    @Ignore
    public void Sum() throws RecognitionException, TokenStreamException {
        // NOT SUPPORTED
        query().from(cat).list(cat.kittens.size().sum());
    }

    @Test
    @Ignore
    public void Sum_2() throws RecognitionException, TokenStreamException {
        // NOT SUPPORTED
        query().from(cat).where(cat.kittens.size().sum().gt(0)).list(cat);
    }

    @Test
    public void Sum_3() {
        query().from(cat).uniqueResult(cat.bodyWeight.sum());
    }

    @Test
    public void Sum_3_Projected() {
        double val = query().from(cat).uniqueResult(cat.bodyWeight.sum());
        DoubleProjection projection = query().from(cat)
                .uniqueResult(new QDoubleProjection(cat.bodyWeight.sum()));
        assertEquals(val, projection.val, 0.001);
    }

    @Test
    public void Sum_4() {
        Double dbl = query().from(cat).uniqueResult(cat.bodyWeight.sum().negate());
        assertNotNull(dbl);
    }

    @Test
    public void Sum_5() {
        QShow show = QShow.show;
        Long lng = query().from(show).uniqueResult(show.id.sum());
        assertNotNull(lng);
    }

    @Test
    public void Sum_of_Integer() {
        QCat cat2 = new QCat("cat2");
        query().from(cat)
                .where(new JPASubQuery()
                        .from(cat2).where(cat2.eq(cat.mate))
                        .unique(cat2.breed.sum()).gt(0))
                .list(cat);
    }

    @Test
    public void Sum_of_Float() {
        QCat cat2 = new QCat("cat2");
        query().from(cat)
                .where(new JPASubQuery()
                        .from(cat2).where(cat2.eq(cat.mate))
                        .unique(cat2.floatProperty.sum()).gt(0.0))
                .list(cat);
    }

    @Test
    public void Sum_of_Double() {
        QCat cat2 = new QCat("cat2");
        query().from(cat)
                .where(new JPASubQuery()
                        .from(cat2).where(cat2.eq(cat.mate))
                        .unique(cat2.bodyWeight.sum()).gt(0.0))
                .list(cat);
    }

    @Test
    public void Sum_as_Float() {
        float val = query().from(cat).uniqueResult(cat.floatProperty.sum());
        assertTrue(val > 0);
    }

    @Test
    public void Sum_as_Float_Projected() {
        float val = query().from(cat).uniqueResult(cat.floatProperty.sum());
        FloatProjection projection = query().from(cat)
                .uniqueResult(new QFloatProjection(cat.floatProperty.sum()));
        assertEquals(val, projection.val, 0.001);
    }

    @Test
    public void Sum_as_Float2() {
        float val = query().from(cat).uniqueResult(cat.floatProperty.sum().negate());
        assertTrue(val < 0);
    }

    @Test
    public void Sum_Coalesce() {
        int val = query().from(cat).uniqueResult(cat.weight.sum().coalesce(0));
        assertTrue(val == 0);
    }

    @Test
    public void Sum_NoRows_Double() {
        query().from(cat)
            .where(cat.name.eq(UUID.randomUUID().toString()))
            .uniqueResult(cat.bodyWeight.sum());
    }

    @Test
    public void Sum_NoRows_Float() {
        query().from(cat)
            .where(cat.name.eq(UUID.randomUUID().toString()))
            .uniqueResult(cat.floatProperty.sum());
    }

    @Test
    @NoEclipseLink @NoOpenJPA @NoBatooJPA
    public void test() {
        Cat kitten = savedCats.get(0);
        Cat noKitten = savedCats.get(savedCats.size()-1);

        ProjectionsFactory projections = new ProjectionsFactory(Module.JPA, getTarget()) {
            @Override
            public <A,Q extends SimpleExpression<A>> Collection<Expression<?>> list(ListPath<A,Q> expr,
                    ListExpression<A,Q> other, A knownElement) {
                // NOTE : expr.get(0) is only supported in the where clause
                return Collections.<Expression<?>>singleton(expr.size());
            }
        };

        final EntityPath<?>[] sources = new EntityPath[]{cat, otherCat};
        final Predicate[] conditions = new Predicate[]{condition};
        final Expression<?>[] projection = new Expression[]{cat.name, otherCat.name};

        QueryExecution standardTest = new QueryExecution(
                projections,
                new FilterFactory(projections, Module.JPA, getTarget()),
                new MatchingFiltersFactory(Module.JPA, getTarget())) {

            @Override
            protected Pair<Projectable, Expression<?>[]> createQuery() {
                // NOTE : EclipseLink needs extra conditions cond1 and code2
                return Pair.of(
                        (Projectable)testQuery().from(sources).where(conditions),
                        NO_EXPRESSIONS);
            }

            @Override
            protected Pair<Projectable, Expression<?>[]> createQuery(Predicate filter) {
                // NOTE : EclipseLink needs extra conditions cond1 and code2
                return Pair.of(
                        (Projectable)testQuery().from(sources).where(condition, filter),
                        projection);
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
    public void TupleProjection() {
        List<Tuple> tuples = query().from(cat).list(cat.name, cat);
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples) {
            assertNotNull(tuple.get(cat.name));
            assertNotNull(tuple.get(cat));
        }
    }

    @Test
    public void TupleProjection_As_SearchResults() {
        SearchResults<Tuple> tuples = query().from(cat).limit(1)
                .listResults(cat.name, cat);
        assertEquals(1, tuples.getResults().size());
        assertTrue(tuples.getTotal() > 0);
    }

    @Test
    @ExcludeIn(DERBY)
    public void Transform_GroupBy() {
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
    public void Transform_GroupBy2() {
        QCat kitten = new QCat("kitten");
        Map<List<?>, Group> result = query().from(cat).innerJoin(cat.kittens, kitten)
            .transform(GroupBy.groupBy(cat.id, kitten.id)
                    .as(cat, kitten));

        assertFalse(result.isEmpty());
        for (Tuple row : query().from(cat).innerJoin(cat.kittens, kitten)
                                .list(cat, kitten)) {
            assertNotNull(result.get(Arrays.asList(row.get(cat).getId(), row.get(kitten).getId())));
        }
    }

    @Test
    @ExcludeIn(DERBY)
    public void Transform_GroupBy_Alias() {
        QCat kitten = new QCat("kitten");
        SimplePath<Cat> k = new SimplePath<Cat>(Cat.class, "k");
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
    public void Treat() {
        QDomesticCat domesticCat = QDomesticCat.domesticCat;
        query().from(cat)
               .innerJoin(cat.mate, domesticCat._super)
               .where(domesticCat.name.eq("Bobby"))
               .count();
    }

    @Test
    @Ignore
    public void Type() {
        assertEquals(Arrays.asList("C","C","C","C","C","C","A"),
                query().from(animal).orderBy(animal.id.asc()).list(JPAExpressions.type(animal)));
    }

    @Test
    @NoOpenJPA
    public void Type_Order() {
        assertEquals(Arrays.asList(10,1,2,3,4,5,6),
                query().from(animal).orderBy(JPAExpressions.type(animal).asc(), animal.id.asc())
                       .list(animal.id));
    }
}

