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

import static com.mysema.query.jpa.JPQLGrammar.sum;
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
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.mysema.commons.lang.Pair;
import com.mysema.query.group.GroupBy;
import com.mysema.query.group.QPair;
import com.mysema.query.jpa.JPQLGrammar;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.JPQLSubQuery;
import com.mysema.query.jpa.domain.Animal;
import com.mysema.query.jpa.domain.Author;
import com.mysema.query.jpa.domain.Book;
import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.DomesticCat;
import com.mysema.query.jpa.domain.Employee;
import com.mysema.query.jpa.domain.Foo;
import com.mysema.query.jpa.domain.JobFunction;
import com.mysema.query.jpa.domain.QAnimal;
import com.mysema.query.jpa.domain.QAuthor;
import com.mysema.query.jpa.domain.QBook;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.domain.QEmployee;
import com.mysema.query.jpa.domain.QFoo;
import com.mysema.query.jpa.domain.QShow;
import com.mysema.query.jpa.domain.QUser;
import com.mysema.query.jpa.domain.Show;
import com.mysema.query.jpa.domain4.QBookMark;
import com.mysema.query.jpa.domain4.QBookVersion;
import com.mysema.query.jpa.hibernate.HibernateSubQuery;
import com.mysema.query.types.ArrayConstructorExpression;
import com.mysema.query.types.Concatenation;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ListExpression;
import com.mysema.query.types.expr.Param;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.EnumPath;
import com.mysema.query.types.path.ListPath;
import com.mysema.query.types.path.StringPath;

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
            super(Projection.class, 
                    new Class[]{String.class, Cat.class}, new Expression[]{str, cat});
        }

    }
    
    private static final QAnimal animal = QAnimal.animal;

    private static final QCat cat = QCat.cat;

    private static final QCat otherCat = new QCat("otherCat");

    private static final BooleanExpression cond1 = cat.name.length().gt(0);

    private static final BooleanExpression cond2 = otherCat.name.length().gt(0);

    private final Date birthDate;

    private final java.sql.Date date;

    private final List<Cat> savedCats = new ArrayList<Cat>();

    private final java.sql.Time time;

    {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 2, 3, 4);
        cal.set(Calendar.MILLISECOND, 0);
        birthDate = cal.getTime();
        date = new java.sql.Date(cal.getTimeInMillis());
        time = new java.sql.Time(cal.getTimeInMillis());
    }

    protected abstract Target getTarget();

    protected abstract JPQLQuery query();
    
    protected JPQLSubQuery subQuery(){
        return new JPQLSubQuery();
    }

    protected abstract void save(Object entity);

    @Before
    public void setUp(){
        Cat prev = null;
        for (Cat cat : Arrays.asList(
                new Cat("Bob123", 1, 1.0),
                new Cat("Ruth123", 2, 2.0),
                new Cat("Felix123", 3, 3.0),
                new Cat("Allen123", 4, 4.0),
                new Cat("Mary_123", 5, 5.0))){
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
        
        Foo foo = new Foo();
        foo.id = 1;
        foo.names = Arrays.asList("a","b");
        save(foo);
    }

    @Test
    public void test(){
        Cat kitten = savedCats.get(0);
        Cat noKitten = savedCats.get(savedCats.size()-1);

        ProjectionsFactory projections = new ProjectionsFactory(Module.JPA, getTarget()){
            @Override
            public <A,Q extends SimpleExpression<A>> Collection<Expression<?>> list(ListPath<A,Q> expr, 
                    ListExpression<A,Q> other, A knownElement){
                // NOTE : expr.get(0) is only supported in the where clause
                return Collections.<Expression<?>>singleton(expr.size());
            }
        };
        
        QueryExecution standardTest = new QueryExecution(
                projections, 
                new FilterFactory(projections, Module.JPA, getTarget()), 
                new MatchingFiltersFactory(Module.JPA, getTarget())){

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
    public void Any_Simple(){
        assertEquals(1, query().from(cat).where(cat.kittens.any().name.eq("Ruth123")).count());
    }

    @Test
    public void Exists(){
        assertTrue(query().from(cat).where(cat.kittens.any().name.eq("Ruth123")).exists());
    }

    @Test
    public void NotExists(){
        assertTrue(query().from(cat).where(cat.kittens.any().name.eq("XXX")).notExists());
    }

    @Test
    public void Any_Usage(){
        assertEquals(1, query().from(cat).where(cat.kittens.any().name.eq("Ruth123")).count());
    }
    
    @Test
    public void Any_And_Lt(){
        assertEquals(1, query().from(cat).where(
                cat.kittens.any().name.eq("Ruth123"), 
                cat.kittens.any().bodyWeight.lt(10.0)).count());
    }
    
    @Test
    public void Any_And_Gt(){
        assertEquals(0, query().from(cat).where(
                cat.kittens.any().name.eq("Ruth123"), 
                cat.kittens.any().bodyWeight.gt(10.0)).count());
    }

    @Test
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
    public void Aggregates_UniqueResult_Min(){
        assertEquals(Integer.valueOf(1), query().from(cat).uniqueResult(cat.id.min()));
    }

    @Test
    public void Aggregates_UniqueResult_Max(){
        assertEquals(Integer.valueOf(6), query().from(cat).uniqueResult(cat.id.max()));
    }
    
    @Test
    public void Aggregates_List_Min(){
        assertEquals(Integer.valueOf(1), query().from(cat).list(cat.id.min()).get(0));
    }

    @Test
    public void Aggregates_List_Max(){
        assertEquals(Integer.valueOf(6), query().from(cat).list(cat.id.max()).get(0));
    }
    
    @Test
    public void DistinctResults(){
        System.out.println("-- list results");
        SearchResults<Date> res = query().from(cat).limit(2).listResults(cat.birthdate);
        assertEquals(2, res.getResults().size());
        assertEquals(6l, res.getTotal());
        System.out.println();

        System.out.println("-- list distinct results");
        res = query().from(cat).limit(2).listDistinctResults(cat.birthdate);
        assertEquals(1, res.getResults().size());
        assertEquals(1l, res.getTotal());
        System.out.println();

        System.out.println("-- list distinct");
        assertEquals(1, query().from(cat).listDistinct(cat.birthdate).size());
    }

    @Test
    public void In(){
        query().from(cat).where(cat.id.in(Arrays.asList(1,2,3))).count();
        query().from(cat).where(cat.name.in(Arrays.asList("A","B","C"))).count();
    }
    
    @Test
    public void In2(){
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
    public void StartsWith(){
        assertEquals(1, query().from(cat).where(cat.name.startsWith("R")).count());
    }
    
    @Test
    public void StartsWith2(){
        assertEquals(0, query().from(cat).where(cat.name.startsWith("X")).count());
    }

    @Test
    public void StartsWith3(){
        assertEquals(1, query().from(cat).where(cat.name.startsWith("Mary_")).count());
    }
    
    @Test
    public void StartsWith_IgnoreCase(){
        assertEquals(1, query().from(cat).where(cat.name.startsWithIgnoreCase("r")).count());
    }

    @Test
    public void EndsWith(){
        assertEquals(1, query().from(cat).where(cat.name.endsWith("h123")).count());
    }
    
    @Test
    public void EndsWith2(){
        assertEquals(0, query().from(cat).where(cat.name.endsWith("X")).count());
    }

    @Test
    public void EndsWith3(){
        assertEquals(1, query().from(cat).where(cat.name.endsWith("_123")).count());
    }
    
    @Test
    public void EndsWith_IgnoreCase(){
        assertEquals(1, query().from(cat).where(cat.name.endsWithIgnoreCase("H123")).count());
    }

    @Test
    public void Contains1(){
        assertEquals(1, query().from(cat).where(cat.name.contains("eli")).count());
    }

    @Test
    public void Contains2(){
        assertEquals(1l, query().from(cat).where(cat.kittens.contains(savedCats.get(0))).count());
    }

    @Test
    public void Contains3(){
        assertEquals(1l, query().from(cat).where(cat.name.contains("_")).count());
    }	

    @Test
    public void Length(){
        assertEquals(6, query().from(cat).where(cat.name.length().gt(0)).count());
    }

    @Test
    public void IndexOf(){
        assertEquals(Integer.valueOf(0), query().from(cat).where(cat.name.eq("Bob123"))
                .uniqueResult(cat.name.indexOf("B")));
    }
    
    @Test
    public void IndexOf2(){
        assertEquals(Integer.valueOf(1), query().from(cat).where(cat.name.eq("Bob123"))
                .uniqueResult(cat.name.indexOf("o")));
    }

    @Test
    public void StringOperations(){
        if (!getTarget().equals(Target.MYSQL)){ // NOTE : locate in MYSQL is case-insensitive
            assertEquals(0, query().from(cat).where(cat.name.startsWith("r")).count());
            assertEquals(0, query().from(cat).where(cat.name.endsWith("H123")).count());
            assertEquals(Integer.valueOf(2), query().from(cat).where(cat.name.eq("Bob123"))
                    .uniqueResult(cat.name.indexOf("b")));
        }
    }

    @Test
    public void Limit(){
        List<String> names1 = Arrays.asList("Allen123","Bob123");
        assertEquals(names1, query().from(cat).orderBy(cat.name.asc()).limit(2).list(cat.name));
    }

    @Test
    public void Limit2(){
        assertEquals(Collections.singletonList("Allen123"), 
                query().from(cat).orderBy(cat.name.asc()).limit(1).list(cat.name));
    }

    @Test
    public void Offset(){
        List<String> names2 = Arrays.asList("Felix123","Mary_123","Ruth123","Some");
        assertEquals(names2, query().from(cat).orderBy(cat.name.asc()).offset(2).list(cat.name));
    }

    @Test
    public void Limit_and_offset(){
        List<String> names3 = Arrays.asList("Felix123","Mary_123");
        assertEquals(names3, query().from(cat).orderBy(cat.name.asc()).limit(2).offset(2).list(cat.name));
    }

    @Test
    public void InstanceOf_Cat(){
        assertEquals(6l, query().from(cat).where(cat.instanceOf(Cat.class)).count());
    }
    
    @Test
    public void InstanceOf_DomensticCat(){
        assertEquals(0l, query().from(cat).where(cat.instanceOf(DomesticCat.class)).count());
    }

    @Test
    public void NestedProjection(){
        Concatenation concat = new Concatenation(cat.name, cat.name);
        List<Tuple> tuples = query().from(cat).list(new QTuple(cat.name, concat));
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples){
            assertEquals(
                tuple.get(concat),
                tuple.get(cat.name)+tuple.get(cat.name));
        }
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
    
    @Test
    public void TupleProjection_As_SearchResults() {
        SearchResults<Tuple> tuples = query().from(cat).limit(1)
                .listResults(new QTuple(cat.name, cat));
        assertEquals(1, tuples.getResults().size());
        assertTrue(tuples.getTotal() > 0);
    }
    
    @Test
    @Ignore
    public void Type() {
        assertEquals(Arrays.asList("C","C","C","C","C","C","A"),
                query().from(animal).orderBy(animal.id.asc()).list(JPQLGrammar.type(animal)));
    }
    
    @Test
    public void Type_Order() {
        assertEquals(Arrays.asList(10,1,2,3,4,5,6),
                query().from(animal).orderBy(JPQLGrammar.type(animal).asc(), animal.id.asc())
                       .list(animal.id));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void ArrayProjection(){
        List<String[]> results = query().from(cat)
                .list(new ArrayConstructorExpression<String>(String[].class, cat.name));
        assertFalse(results.isEmpty());
        for (String[] result : results){
            assertNotNull(result[0]);
        }
    }

    @Test
    public void ConstructorProjection(){
        List<Projection> projections = query().from(cat)
                .list(ConstructorExpression.create(Projection.class, cat.name, cat));
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
        assertEquals("Bob123",query().from(cat).where(cat.name.eq(name)).set(name, "Bob123")
                .uniqueResult(cat.name));
    }

    @Test
    public void Params_anon(){
        Param<String> name = new Param<String>(String.class);
        assertEquals("Bob123",query().from(cat).where(cat.name.eq(name)).set(name, "Bob123")
                .uniqueResult(cat.name));
    }

    @Test(expected=ParamNotSetException.class)
    public void Params_not_set(){
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Bob123",query().from(cat).where(cat.name.eq(name)).uniqueResult(cat.name));
    }

    @Test
    public void Null_as_uniqueResult(){
        assertNull(query().from(cat).where(cat.name.eq(UUID.randomUUID().toString()))
                .uniqueResult(cat));
    }

    @Test
    public void Map_ContainsKey(){
        QShow show = QShow.show;
        assertEquals(1l, query().from(show).where(show.acts.containsKey("a")).count());
    }

    @Test
    public void Map_ContainsKey2(){
        QShow show = QShow.show;
        assertEquals(1l, query().from(show).where(show.acts.containsKey("b")).count());
    }
    
    @Test
    public void Map_ContainsKey3(){
        QShow show = QShow.show;
        assertEquals(0l, query().from(show).where(show.acts.containsKey("c")).count());
    }
    
    @Test
    public void Map_ContainsValue(){
        QShow show = QShow.show;
        assertEquals(1l, query().from(show).where(show.acts.containsValue("A")).count());
    }
    
    @Test
    public void Map_ContainsValue2(){
        QShow show = QShow.show;
        assertEquals(1l, query().from(show).where(show.acts.containsValue("B")).count());
    }
    
    @Test
    public void Map_ContainsValue3(){
        QShow show = QShow.show;
        assertEquals(0l, query().from(show).where(show.acts.containsValue("C")).count());
    }
    
    @Test
    @Ignore
    public void Sum() throws RecognitionException, TokenStreamException {
        // NOT SUPPORTED
        query().from(cat).list(sum(cat.kittens.size()));
    }

    @Test
    @Ignore
    public void Sum_2() throws RecognitionException, TokenStreamException {
        // NOT SUPPORTED
        query().from(cat).where(sum(cat.kittens.size()).gt(0)).list(cat);
    }
    
    @Test
    public void SubQuery(){
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
    public void Count(){
        QShow show = QShow.show;
        assertTrue(query().from(show).count() > 0);
    }
    
    @Test
    public void One_To_One(){
        QEmployee employee = QEmployee.employee;
        QUser user = QUser.user;
        
        JPQLQuery query = query();
        query.from(employee);
        query.innerJoin(employee.user, user);
        query.list(employee);
    }
 
    @Test
    public void Enum_In() {
        QEmployee employee = QEmployee.employee;
        
        JPQLQuery query = query();
        query.from(employee).where(employee.lastName.eq("Smith"), employee.jobFunctions
                .contains(JobFunction.CODER));
        assertEquals(1l, query.count());
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
    public void List_ElementCollection_Of_String() {
        QFoo foo = QFoo.foo;
        StringPath str = new StringPath("str");
        
        List<String> strings = query().from(foo).innerJoin(foo.names, str).list(str);
        assertEquals(2, strings.size());
        assertTrue(strings.contains("a"));
        assertTrue(strings.contains("b"));
    }
    
    @Test
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
}
