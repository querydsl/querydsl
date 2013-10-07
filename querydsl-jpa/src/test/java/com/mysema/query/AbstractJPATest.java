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
package com.mysema.query;

import static com.mysema.query.Target.DERBY;
import static com.mysema.query.Target.HSQLDB;
import static com.mysema.query.Target.MYSQL;
import static com.mysema.query.Target.ORACLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.google.common.collect.Lists;
import com.mysema.commons.lang.Pair;
import com.mysema.query.group.Group;
import com.mysema.query.group.GroupBy;
import com.mysema.query.group.QPair;
import com.mysema.query.jpa.JPAExpressions;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.domain.Animal;
import com.mysema.query.jpa.domain.Author;
import com.mysema.query.jpa.domain.Book;
import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.Company;
import com.mysema.query.jpa.domain.Company.Rating;
import com.mysema.query.jpa.domain.DomesticCat;
import com.mysema.query.jpa.domain.DoubleProjection;
import com.mysema.query.jpa.domain.Employee;
import com.mysema.query.jpa.domain.Entity1;
import com.mysema.query.jpa.domain.Entity2;
import com.mysema.query.jpa.domain.FloatProjection;
import com.mysema.query.jpa.domain.Foo;
import com.mysema.query.jpa.domain.JobFunction;
import com.mysema.query.jpa.domain.QAnimal;
import com.mysema.query.jpa.domain.QAuthor;
import com.mysema.query.jpa.domain.QBook;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.domain.QCompany;
import com.mysema.query.jpa.domain.QDoubleProjection;
import com.mysema.query.jpa.domain.QEmployee;
import com.mysema.query.jpa.domain.QEntity1;
import com.mysema.query.jpa.domain.QFloatProjection;
import com.mysema.query.jpa.domain.QFoo;
import com.mysema.query.jpa.domain.QHuman;
import com.mysema.query.jpa.domain.QMammal;
import com.mysema.query.jpa.domain.QShow;
import com.mysema.query.jpa.domain.QSimpleTypes;
import com.mysema.query.jpa.domain.QUser;
import com.mysema.query.jpa.domain.QWorld;
import com.mysema.query.jpa.domain.Show;
import com.mysema.query.jpa.domain4.QBookMark;
import com.mysema.query.jpa.domain4.QBookVersion;
import com.mysema.query.jpa.hibernate.HibernateSubQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.ArrayConstructorExpression;
import com.mysema.query.types.Concatenation;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ListExpression;
import com.mysema.query.types.expr.Param;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.EnumPath;
import com.mysema.query.types.path.ListPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;
import com.mysema.testutil.ExcludeIn;

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
    public void Any_In1() {
        //select cat from Cat cat where exists (
        //  select cat_kittens from Cat cat_kittens where cat_kittens member of cat.kittens and cat_kittens in ?1)
        query().from(cat).where(cat.kittens.any().in(savedCats)).list(cat);
    }

    @Test
    public void Any_In11() {
        List<Integer> ids = Lists.newArrayList();
        for (Cat cat : savedCats) ids.add(cat.getId());
        query().from(cat).where(cat.kittens.any().id.in(ids)).list(cat);
    }

    @Test
    public void Any_In2() {
        query().from(cat).where(
                cat.kittens.any().in(savedCats),
                cat.kittens.any().in(savedCats.subList(0, 1)).not())
            .list(cat);
    }

    @Test
    public void Any_In3() {
        QEmployee employee = QEmployee.employee;
        query().from(employee).where(
                employee.jobFunctions.any().in(JobFunction.CODER, JobFunction.CONSULTANT))
                .list(employee);
    }

    @Test
    public void Any_Simple() {
        assertEquals(1, query().from(cat).where(cat.kittens.any().name.eq("Ruth123")).count());
    }

    @Test
    public void Any_Usage() {
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
    @NoHibernate
    @NoBatooJPA
    public void Case() {
        query().from(cat).list(cat.name.when("Bob").then(1).otherwise(2));
    }

    @Test(expected=ClassCastException.class)
    @NoEclipseLink
    @NoBatooJPA
    public void Case_Hibernate() {
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
    public void Cast() {
        query().from(cat).list(cat.bodyWeight.castToNum(Integer.class));
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
    @NoHibernate
    public void Constant() {
        //select cat.id, ?1 as const from Cat cat
        query().from(cat).list(new QTuple(cat.id, Expressions.constantAs("abc", new StringPath("const"))));
    }

    @Test(expected=NullPointerException.class)
    @NoEclipseLink
    @NoBatooJPA
    public void Constant_Hibernate() {
        //select cat.id, ?1 as const from Cat cat
        query().from(cat).list(new QTuple(cat.id, Expressions.constantAs("abc", new StringPath("const"))));
    }

    @Test
    @NoHibernate
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
    public void Enum_in() {
        assertEquals(1, query().from(company).where(company.ratingOrdinal.in(Rating.A, Rating.AA)).count());
        assertEquals(1, query().from(company).where(company.ratingString.in(Rating.A, Rating.AA)).count());
    }

    @Test
    @NoBatooJPA
    public void Enum_In() {
        QEmployee employee = QEmployee.employee;

        JPQLQuery query = query();
        query.from(employee).where(employee.lastName.eq("Smith"), employee.jobFunctions
                .contains(JobFunction.CODER));
        assertEquals(1l, query.count());
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
    public void In() {
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
    @NoEclipseLink
    @ExcludeIn(ORACLE)
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
        List<Tuple> tuples = query().from(cat).list(new QTuple(cat.name, concat));
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
        query().from(cat).where(cat.id.notIn(1,2,3)).count();
        query().from(cat).where(cat.name.notIn("A","B","C")).count();
    }

    @Test
    public void Null_as_uniqueResult() {
        assertNull(query().from(cat).where(cat.name.eq(UUID.randomUUID().toString()))
                .uniqueResult(cat));
    }

    @Test
    @NoOpenJPA // FIXME
    public void Offset() {
        List<String> names2 = Arrays.asList("Felix123","Mary_123","Ruth123","Some");
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
    @ExcludeIn(MYSQL)
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
    public void Substring() {
        for (String str : query().from(cat).list(cat.name.substring(1,2))) {
            assertEquals(1, str.length());
        }
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
        query().from(cat).uniqueResult(cat.bodyWeight.sum().negate());
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
        List<Tuple> tuples = query().from(cat).list(new QTuple(cat.name, cat));
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples) {
            assertNotNull(tuple.get(cat.name));
            assertNotNull(tuple.get(cat));
        }
    }

    @Test
    public void TupleProjection_As_SearchResults() {
        SearchResults<Tuple> tuples = query().from(cat).limit(1)
                .listResults(new QTuple(cat.name, cat));
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
