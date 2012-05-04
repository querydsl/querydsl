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

import static com.mysema.query.Constants.date;
import static com.mysema.query.Constants.employee;
import static com.mysema.query.Constants.employee2;
import static com.mysema.query.Constants.survey;
import static com.mysema.query.Constants.survey2;
import static com.mysema.query.Constants.time;
import static com.mysema.query.Target.DERBY;
import static com.mysema.query.Target.H2;
import static com.mysema.query.Target.HSQLDB;
import static com.mysema.query.Target.MYSQL;
import static com.mysema.query.Target.ORACLE;
import static com.mysema.query.Target.POSTGRES;
import static com.mysema.query.Target.SQLSERVER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.Pair;
import com.mysema.query.group.Group;
import com.mysema.query.group.GroupBy;
import com.mysema.query.sql.Beans;
import com.mysema.query.sql.Column;
import com.mysema.query.sql.QBeans;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.domain.Employee;
import com.mysema.query.sql.domain.IdName;
import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.sql.domain.QIdName;
import com.mysema.query.sql.domain.QSurvey;
import com.mysema.query.types.ArrayConstructorExpression;
import com.mysema.query.types.Concatenation;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.Coalesce;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.Param;
import com.mysema.query.types.expr.Wildcard;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.NumberSubQuery;
import com.mysema.query.types.query.SimpleSubQuery;
import com.mysema.query.types.template.NumberTemplate;
import com.mysema.testutil.ExcludeIn;
import com.mysema.testutil.IncludeIn;
import com.mysema.testutil.Label;

public abstract class SelectBaseTest extends AbstractBaseTest{

    public static class SimpleProjection {

        public SimpleProjection(String str, String str2) {
        }

    }

    private final QueryExecution standardTest = new QueryExecution(Module.SQL, getClass().getAnnotation(Label.class).value()){
        @Override
        protected Pair<Projectable, List<Expression<?>>> createQuery() {
            return Pair.of(
                    (Projectable)query().from(employee, employee2),
                    Collections.<Expression<?>>emptyList());
        }
        @Override
        protected Pair<Projectable, List<Expression<?>>> createQuery(BooleanExpression filter) {
            return Pair.of(
                    (Projectable)query().from(employee, employee2).where(filter),
                    Collections.<Expression<?>>singletonList(employee.firstname));
        }
    };

    @Test
    public void Aggregate_List(){
        int min = 30000, avg = 65000, max = 160000;
        // list
        assertEquals(min, query().from(employee).list(employee.salary.min()).get(0).intValue());
        assertEquals(avg, query().from(employee).list(employee.salary.avg()).get(0).intValue());
        assertEquals(max, query().from(employee).list(employee.salary.max()).get(0).intValue());
    }

    @Test
    public void Aggregate_UniqueResult(){
        int min = 30000, avg = 65000, max = 160000;
        // uniqueResult
        assertEquals(min, query().from(employee).uniqueResult(employee.salary.min()).intValue());
        assertEquals(avg, query().from(employee).uniqueResult(employee.salary.avg()).intValue());
        assertEquals(max, query().from(employee).uniqueResult(employee.salary.max()).intValue());
    }

    @Test
    @SkipForQuoted
    public void Alias_Quotes() {
        expectedQuery = "select e.FIRSTNAME as \"First Name\" from EMPLOYEE e";
        query().from(employee).list(employee.firstname.as("First Name"));
    }

    @Test
    public void All(){
        for (Expression<?> expr : survey.all()){
            Path<?> path = (Path<?>)expr;
            assertEquals(survey, path.getMetadata().getParent());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void Array_Projection(){
        List<String[]> results = query().from(employee).list(
                new ArrayConstructorExpression<String>(String[].class, employee.firstname));
        assertFalse(results.isEmpty());
        for (String[] result : results){
            assertNotNull(result[0]);
        }
    }
    
    @Test
    public void RelationalPath_Projection() {
        List<Object[]> results = query().from(employee, employee2).where(employee.id.eq(employee2.id))
                .list(employee, employee2);
        assertFalse(results.isEmpty());
        for (Object[] row : results) {
            Employee e1 = (Employee)row[0];
            Employee e2 = (Employee)row[1];
            assertEquals(e1.getId(), e2.getId());
        }
    }

    @Test
    public void Beans(){
        QEmployee EMPLOYEE = new QEmployee("EMPLOYEE");
        List<Beans> rows = query().from(employee, EMPLOYEE).list(new QBeans(employee, EMPLOYEE));
        assertFalse(rows.isEmpty());
        for (Beans row : rows){
            assertEquals(Employee.class, row.get(employee).getClass());
            assertEquals(Employee.class, row.get(EMPLOYEE).getClass());            
        }
    }
    
    @Test
    @ExcludeIn({DERBY,MYSQL})
    public void Casts() throws SQLException {
        NumberExpression<?> num = employee.id;
        Expression<?>[] expr = new Expression[] {
                num.byteValue(),
                num.doubleValue(),
                num.floatValue(),
                num.intValue(),
                num.longValue(),
                num.shortValue(),
                num.stringValue() };

        for (Expression<?> e : expr) {
            for (Object o : query().from(employee).list(e)){
                assertEquals(e.getType(), o.getClass());
            }
        }
    }

    @Test
    public void Coalesce(){
        Coalesce<String> c = new Coalesce<String>(employee.firstname, employee.lastname).add("xxx");
        query().from(employee).where(c.eq("xxx")).list(employee.id);
    }

    @Test
    public void Compact_Join(){
        // verbose
        query().from(employee).innerJoin(employee2)
        .on(employee.superiorId.eq(employee2.id))
        .list(employee.id, employee2.id);

        // compact
        query().from(employee)
        .innerJoin(employee.superiorIdKey, employee2)
        .list(employee.id, employee2.id);

    }

    @Test
    public void Complex_boolean(){
        BooleanExpression first = employee.firstname.eq("Mike").and(employee.lastname.eq("Smith"));
        BooleanExpression second = employee.firstname.eq("Joe").and(employee.lastname.eq("Divis"));
        assertEquals(2, query().from(employee).where(first.or(second)).count());

        assertEquals(0, query().from(employee).where(
                employee.firstname.eq("Mike"),
                employee.lastname.eq("Smith").or(employee.firstname.eq("Joe")),
                employee.lastname.eq("Divis")
        ).count());
    }

    @Test
    public void ComplexSubQuery(){
        // alias for the salary
        NumberPath<BigDecimal> sal = new NumberPath<BigDecimal>(BigDecimal.class, "sal");
        // alias for the subquery
        PathBuilder<Object[]> sq = new PathBuilder<Object[]>(Object[].class, "sq");
        // query execution
        query().from(
                sq().from(employee)
                .list(employee.salary.add(employee.salary).add(employee.salary).as(sal)).as(sq)
        ).list(sq.get(sal).avg(), sq.get(sal).min(), sq.get(sal).max());
    }

    @Test
    public void Constructor() throws Exception {
        for (IdName idName : query().from(survey).list(new QIdName(survey.id, survey.name))) {
            System.out.println("id and name : " + idName.getId() + ","+ idName.getName());
        }
    }

    @Test
    public void Constructor_Projection(){
        // constructor projection
        for (IdName idAndName : query().from(survey).list(new QIdName(survey.id, survey.name))){
            assertNotNull(idAndName);
            assertNotNull(idAndName.getId());
            assertNotNull(idAndName.getName());
        }

    }

    @Test
    public void Constructor_Projection2(){
        List<SimpleProjection> projections =query().from(employee).list(
                ConstructorExpression.create(SimpleProjection.class, 
                        employee.firstname, employee.lastname));
        assertFalse(projections.isEmpty());
        for (SimpleProjection projection : projections){
            assertNotNull(projection);
        }
    }

    @Test
    @SkipForQuoted
    public void Count_All() {
        expectedQuery = "select count(*) as rowCount from EMPLOYEE e";
        NumberPath<Long> rowCount = new NumberPath<Long>(Long.class, "rowCount");
        query().from(employee).uniqueResult(Wildcard.count.as(rowCount));
    }

    @Test
    public void Custom_Projection(){
        List<Projection> tuples = query().from(employee).list(
                new QProjection(employee.firstname, employee.lastname));
        assertFalse(tuples.isEmpty());
        for (Projection tuple : tuples){
            assertNotNull(tuple.get(employee.firstname));
            assertNotNull(tuple.get(employee.lastname));
            assertNotNull(tuple.getExpr(employee.firstname));
            assertNotNull(tuple.getExpr(employee.lastname));
        }
    }
    
    @Test
    public void Distinct_List(){
        List<Integer> lengths1 = query().from(employee).listDistinct(employee.firstname.length());
        List<Integer> lengths2 = query().from(employee).distinct().list(employee.firstname.length());
        assertEquals(lengths1, lengths2);
    }
    
    @Test
    public void Distinct_Count(){
        long count1 = query().from(employee).countDistinct();
        long count2 = query().from(employee).distinct().count();
        assertEquals(count1, count2);
    }


    @Test
    public void getResultSet() throws IOException, SQLException{
        ResultSet results = query().from(survey).getResults(survey.id, survey.name);
        while(results.next()){
            System.out.println(results.getInt(1) +","+results.getString(2));
        }
        results.close();
    }

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public void IllegalUnion() throws SQLException {
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id.max());
        query().from(employee).union(sq1, sq2).list();
    }

    @Test
    public void Join() throws Exception {
        for (String name : query().from(survey, survey2)
                .where(survey.id.eq(survey2.id)).list(survey.name)) {
            System.out.println(name);
        }
    }
    
    @Test
    public void In(){
        query().from(employee).where(employee.id.in(Arrays.asList(1,2))).list(employee);
    }

    @Test
    public void Inner_Join() throws SQLException {
        query().from(employee).innerJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .list(employee.id, employee2.id);
    }

    @Test
    public void Left_Join() throws SQLException {
        query().from(employee).leftJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .list(employee.id, employee2.id);
    }

    @Test
    public void Right_Join() throws SQLException {
        query().from(employee).rightJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .list(employee.id, employee2.id);
    }

    @Test
    @IncludeIn({POSTGRES})
    public void Full_Join() throws SQLException {
        query().from(employee).fullJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .list(employee.id, employee2.id);
    }

    @Test
    public void Joins() throws SQLException {
        for (Object[] row : query().from(employee).innerJoin(employee2)
                .on(employee.superiorId.eq(employee2.superiorId))
                .where(employee2.id.eq(10))
                .list(employee.id, employee2.id)) {
            System.out.println(row[0] + ", " + row[1]);
        }
    }

    @Test
    public void Exists(){
        assertTrue(query().from(employee).where(employee.firstname.eq("Barbara")).exists());
    }

    @Test
    public void NotExists(){
        assertTrue(query().from(employee).where(employee.firstname.eq("Barb")).notExists());
    }

    @Test
    public void Limit() throws SQLException {
        // limit
        query().from(employee)
        .orderBy(employee.firstname.asc())
        .limit(4).list(employee.id);
    }

    @Test
    public void Limit_And_Offset() throws SQLException {
        // limit and offset
        query().from(employee)
        .orderBy(employee.firstname.asc())
        .limit(4).offset(3).list(employee.id);
    }

    @Test
    public void Limit_and_Order(){
        // limit
        List<String> names1 = Arrays.asList("Barbara","Daisy","Helen","Jennifer");
        assertEquals(names1, query().from(employee)
                .orderBy(employee.firstname.asc())
                .limit(4)
                .list(employee.firstname));
    }

    @Test
    public void Limit_and_Offset_and_Order(){
        // limit + offset
        List<String> names2 = Arrays.asList("Helen","Jennifer","Jim","Joe");
        assertEquals(names2, query().from(employee)
                .orderBy(employee.firstname.asc())
                .limit(4).offset(2)
                .list(employee.firstname));
    }

    @Test
    @ExcludeIn({ORACLE,DERBY,SQLSERVER})
    @SkipForQuoted
    public void Limit_and_Offset2() throws SQLException {
        // limit
        expectedQuery = "select e.ID from EMPLOYEE e limit ?";
        query().from(employee).limit(4).list(employee.id);

        // limit offset
        expectedQuery = "select e.ID from EMPLOYEE e limit ? offset ?";
        query().from(employee).limit(4).offset(3).list(employee.id);

    }
    
    @Test
    public void ListResults() {
        SearchResults<Integer> results = query().from(employee)
                .limit(10).offset(1).orderBy(employee.id.asc())
                .listResults(employee.id);
        assertEquals(10, results.getTotal());        
    }

    @Test
    @ExcludeIn({HSQLDB, H2, MYSQL})
    public void Offset_Only(){
        // offset
        query().from(employee)
        .orderBy(employee.firstname.asc())
        .offset(3).list(employee.id);
    }

    @Test
    public void Params(){
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Mike",query()
                .from(employee).where(employee.firstname.eq(name))
                .set(name, "Mike")
                .uniqueResult(employee.firstname));
    }

    @Test
    public void Params_anon(){
        Param<String> name = new Param<String>(String.class);
        assertEquals("Mike",query()
                .from(employee).where(employee.firstname.eq(name))
                .set(name, "Mike")
                .uniqueResult(employee.firstname));
    }

    @Test(expected=ParamNotSetException.class)
    public void Params_not_set(){
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Mike",query()
                .from(employee).where(employee.firstname.eq(name))
                .uniqueResult(employee.firstname));
    }

    @Test
    @ExcludeIn({DERBY,HSQLDB,ORACLE})
    @SkipForQuoted
    public void Path_Alias(){
        expectedQuery = "select e.LASTNAME, sum(e.SALARY) as salarySum " +
        		"from EMPLOYEE e " +
        		"group by e.LASTNAME having salarySum > ?";

        NumberExpression<BigDecimal> salarySum = employee.salary.sum().as("salarySum");
        query().from(employee)
        .groupBy(employee.lastname)
        .having(salarySum.gt(10000))
        .list(employee.lastname, salarySum);
    }

    @Test
    public void Projection() throws IOException{
        CloseableIterator<Object[]> results = query().from(survey).iterate(survey.all());
        assertTrue(results.hasNext());
        while (results.hasNext()){
            assertEquals(3, results.next().length);
        }
        results.close();
    }

    @Test
    public void Projection_and_TwoColumns(){
        // projection and two columns
        for (Object[] row : query().from(survey)
                .list(new QIdName(survey.id, survey.name), survey.id, survey.name)){
            assertEquals(3, row.length);
            assertEquals(IdName.class, row[0].getClass());
            assertEquals(Integer.class, row[1].getClass());
            assertEquals(String.class, row[2].getClass());
        }
    }

    @Test
    public void Projection2() throws IOException{
        // TODO : add assertions
        CloseableIterator<Object[]> results = query().from(survey).iterate(survey.id, survey.name);
        assertTrue(results.hasNext());
        while (results.hasNext()){
            assertEquals(2, results.next().length);
        }
        results.close();
    }

    @Test
    public void Projection3() throws IOException{
        CloseableIterator<String> names = query().from(survey).iterate(survey.name);
        assertTrue(names.hasNext());
        while (names.hasNext()){
            System.out.println(names.next());
        }
        names.close();
    }

    @Test
    public void TemplateExpression(){
        NumberExpression<Integer> one = NumberTemplate.create(Integer.class, "1");
        query().from(survey).list(one.as("col1"));
    }
    
    @Test
    public void Query_with_Constant() throws Exception {
        for (Object[] row : query().from(survey)
                .where(survey.id.eq(1))
                .list(survey.id, survey.name)) {
            System.out.println(row[0] + ", " + row[1]);
        }
    }

    @Test
    public void Query1() throws Exception {
        for (String s : query().from(survey).list(survey.name)) {
            System.out.println(s);
        }
    }

    @Test
    public void Query2() throws Exception {
        for (Object[] row : query().from(survey).list(survey.id, survey.name)) {
            System.out.println(row[0] + ", " + row[1]);
        }
    }
    

    @Test
    @Ignore
    @ExcludeIn({ORACLE, DERBY, SQLSERVER})
    public void Select_BooleanExpr() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).list(survey.id.eq(0)));
    }

    @Test
    @Ignore
    @ExcludeIn({ORACLE, DERBY, SQLSERVER})
    public void Select_BooleanExpr2() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).list(survey.id.gt(0)));
    }

    @Test
    public void Select_Concat() throws SQLException {
        System.out.println(query().from(survey).list(survey.name.append("Hello World")));
    }

    @Test
    @SkipForQuoted
    public void Serialization(){
        SQLQuery query = query();

        query.from(survey);
        assertEquals("from SURVEY s", query.toString());

        query.from(survey2);
        assertEquals("from SURVEY s, SURVEY s2", query.toString());
    }

    @Test
    public void Single_Column(){
        // single column
        for (String s : query().from(survey).list(survey.name)){
            assertNotNull(s);
        }

    }
    
    @Test
    public void Single_Column_via_Object_type(){
        for (Object s : query().from(survey)
                .list(new PathImpl<Object>(Object.class, survey.name.getMetadata()))){
            assertEquals(String.class, s.getClass());
        }
    }

    @Test
    @SkipForQuoted
    public void SubQueries() throws SQLException {
        // subquery in where block
        expectedQuery = "select e.ID from EMPLOYEE e "
            + "where e.ID = (select max(e.ID) "
            + "from EMPLOYEE e)";
        List<Integer> list = query().from(employee)
        .where(employee.id.eq(sq().from(employee).unique(employee.id.max())))
        .list(employee.id);
        assertFalse(list.isEmpty());
    }
    
    @Test
    public void StandardTest(){
        standardTest.runBooleanTests(employee.firstname.isNull(), employee2.lastname.isNotNull());
        standardTest.runDateTests(employee.datefield, employee2.datefield, date);

        // int
        standardTest.runNumericCasts(employee.id, employee2.id, 1);
        standardTest.runNumericTests(employee.id, employee2.id, 1);

        // BigDecimal
        standardTest.runNumericTests(employee.salary, employee2.salary, new BigDecimal("30000.00"));

        standardTest.runStringTests(employee.firstname, employee2.firstname, "Jennifer");
        standardTest.runTimeTests(employee.timefield, employee2.timefield, time);

        standardTest.report();
    }

    @Test
    public void StringFunctions2() throws SQLException {
        for (BooleanExpression where : Arrays.<BooleanExpression> asList(
                employee.firstname.startsWith("a"),
                employee.firstname.startsWithIgnoreCase("a"),
                employee.firstname.endsWith("a"),
                employee.firstname.endsWithIgnoreCase("a"))) {
            query().from(employee).where(where).list(employee.firstname);
        }
    }

    @Test
    public void SubQuery_Alias() {
        query().from(sq().from(employee).list(employee.all()).as(employee2)).list(employee2.all());
    }
    
    @Test
    public void SubQuery_with_Alias(){
        List<Integer> ids1 = query().from(employee).list(employee.id);
        List<Integer> ids2 = query().from(sq().from(employee).list(employee.id), employee).list(employee.id);
        assertEquals(ids1, ids2);
    }
    
    @Test
    public void SubQuery_with_Alias2(){
        List<Integer> ids1 = query().from(employee).list(employee.id);
        List<Integer> ids2 = query().from(sq().from(employee).list(employee.id).as(employee)).list(employee.id);
        assertEquals(ids1, ids2);
    }
    
    @Test
    public void SubQuery_InnerJoin(){
        ListSubQuery<Integer> sq = sq().from(employee2).list(employee2.id);
        QEmployee sqEmp = new QEmployee("sq");
        query().from(employee).innerJoin(sq, sqEmp).on(sqEmp.id.eq(employee.id)).list(employee.id);

    }

    @Test
    public void SubQuery_LeftJoin(){
        ListSubQuery<Integer> sq = sq().from(employee2).list(employee2.id);
        QEmployee sqEmp = new QEmployee("sq");
        query().from(employee).leftJoin(sq, sqEmp).on(sqEmp.id.eq(employee.id)).list(employee.id);

    }

    @Test
    public void SubQuery_RightJoin(){
        ListSubQuery<Integer> sq = sq().from(employee2).list(employee2.id);
        QEmployee sqEmp = new QEmployee("sq");
        query().from(employee).rightJoin(sq, sqEmp).on(sqEmp.id.eq(employee.id)).list(employee.id);
    }

    @Test
    public void SubQuerySerialization(){
        SQLSubQuery query = sq();
        query.from(survey);
        assertEquals("from SURVEY s", query.toString());

        query.from(survey2);
        assertEquals("from SURVEY s, SURVEY s2", query.toString());
    }

    @Test
    public void SubQuerySerialization2(){
        NumberPath<BigDecimal> sal = new NumberPath<BigDecimal>(BigDecimal.class, "sal");
        PathBuilder<Object[]> sq = new PathBuilder<Object[]>(Object[].class, "sq");
        SQLSerializer serializer = new SQLSerializer(SQLTemplates.DEFAULT);

        serializer.handle(
                sq()
                .from(employee)
                .list(employee.salary.add(employee.salary).add(employee.salary).as(sal))
                .as(sq));
        assertEquals(
                "(select (e.SALARY + e.SALARY + e.SALARY) as sal\nfrom EMPLOYEE e) as sq", 
                serializer.toString());
    }

    @Test
    public void Syntax_For_Employee() throws SQLException {
        query().from(employee).groupBy(employee.superiorId)
        .orderBy(employee.superiorId.asc())
        .list(employee.salary.avg(),employee.id.max());

        query().from(employee).groupBy(employee.superiorId)
        .having(employee.id.max().gt(5))
        .orderBy(employee.superiorId.asc())
        .list(employee.salary.avg(), employee.id.max());

        query().from(employee).groupBy(employee.superiorId)
        .having(employee.superiorId.isNotNull())
        .orderBy(employee.superiorId.asc())
        .list(employee.salary.avg(),employee.id.max());
    }

    @Test
    public void Tuple_Projection(){
        List<Tuple> tuples = query().from(employee)
                .list(new QTuple(employee.firstname, employee.lastname));
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples){
            assertNotNull(tuple.get(employee.firstname));
            assertNotNull(tuple.get(employee.lastname));
        }
    }
        
    @Test
    @SuppressWarnings("serial")
    public void MappingProjection() {
        List<Pair<String, String>> pairs = query().from(employee)
                .list(new MappingProjection<Pair<String,String>>(Pair.class, 
                      employee.firstname, employee.lastname) {
            @Override
            protected Pair<String, String> map(Tuple row) {
                return Pair.of(row.get(employee.firstname), row.get(employee.lastname));
            }            
        });
        
        for (Pair<String, String> pair : pairs) {
            assertNotNull(pair.getFirst());
            assertNotNull(pair.getSecond());
        }
    }
    
    @Test
    public void Nested_Tuple_Projection(){
        Concatenation concat = new Concatenation(employee.firstname, employee.lastname);
        List<Tuple> tuples = query().from(employee)
                .list(new QTuple(employee.firstname, employee.lastname, concat));
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples){
            String firstName = tuple.get(employee.firstname);
            String lastName = tuple.get(employee.lastname);
            assertEquals(firstName + lastName, tuple.get(concat));
        }
    }

    @Test
    public void TwoColumns(){
        // two columns
        for (Object[] row : query().from(survey).list(survey.id, survey.name)){
            assertEquals(2, row.length);
            assertEquals(Integer.class, row[0].getClass());
            assertEquals(String.class, row[1].getClass());
        }
    }

    @Test
    public void TwoColumns_and_Projection(){
        // two columns and projection
        for (Object[] row : query().from(survey)
                .list(survey.id, survey.name, new QIdName(survey.id, survey.name))){
            assertEquals(3, row.length);
            assertEquals(Integer.class, row[0].getClass());
            assertEquals(String.class, row[1].getClass());
            assertEquals(IdName.class, row[2].getClass());
        }
    }

    
    @Test
    @SuppressWarnings("unchecked")
    public void Union() throws SQLException {
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id.min());
        List<Integer> list = query().union(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void Union_All() {
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id.min());
        List<Integer> list = query().unionAll(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void Union_Multiple_Columns() throws SQLException {
        SubQueryExpression<Object[]> sq1 = sq().from(employee).unique(employee.firstname, employee.lastname);
        SubQueryExpression<Object[]> sq2 = sq().from(employee).unique(employee.lastname, employee.firstname);
        List<Object[]> list = query().union(sq1, sq2).list();
        assertFalse(list.isEmpty());
        for (Object[] row : list){
            assertNotNull(row[0]);
            assertNotNull(row[1]);
        }
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void Union_Empty_Result() throws SQLException {
        SubQueryExpression<Integer> sq1 = sq().from(employee).where(employee.firstname.eq("XXX")).unique(employee.id);
        SubQueryExpression<Integer> sq2 = sq().from(employee).where(employee.firstname.eq("YYY")).unique(employee.id);
        List<Integer> list = query().union(sq1, sq2).list();
        assertTrue(list.isEmpty());
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void Union2() throws SQLException {
        List<Integer> list = query().union(
                sq().from(employee).unique(employee.id.max()),
                sq().from(employee).unique(employee.id.min())).list();
        assertFalse(list.isEmpty());

    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void Union3() throws SQLException {
        SimpleSubQuery<Object[]> sq3 = sq().from(employee).unique(new Expression[]{employee.id.max()});
        SimpleSubQuery<Object[]> sq4 = sq().from(employee).unique(new Expression[]{employee.id.min()});
        List<Object[]> list2 = query().union(sq3, sq4).list();
        assertFalse(list2.isEmpty());
    }
    
    @Test
    @ExcludeIn({DERBY})
    public void Union4() {
        SimpleSubQuery<Object[]> sq1 = sq().from(employee).unique(employee.id, employee.firstname);
        SimpleSubQuery<Object[]> sq2 = sq().from(employee).unique(employee.id, employee.firstname);
        query().union(employee, sq1, sq2).list(employee.id.count());
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void Union_With_Order() throws SQLException {
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id);
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id);
        List<Integer> list = query().union(sq1, sq2).orderBy(employee.id.asc()).list();
        assertFalse(list.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void Union_Multi_Column_Projection_List() throws IOException{
        SubQueryExpression<Object[]> sq1 = sq().from(employee).unique(employee.id.max(), employee.id.max().subtract(1));
        SubQueryExpression<Object[]> sq2 = sq().from(employee).unique(employee.id.min(), employee.id.min().subtract(1));

        List<Object[]> list = query().union(sq1, sq2).list();
        assertEquals(2, list.size());
        assertTrue(list.get(0) != null);
        assertTrue(list.get(1) != null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void Union_Multi_Column_Projection_Iterate() throws IOException{
        SubQueryExpression<Object[]> sq1 = sq().from(employee).unique(employee.id.max(), employee.id.max().subtract(1));
        SubQueryExpression<Object[]> sq2 = sq().from(employee).unique(employee.id.min(), employee.id.min().subtract(1));

        CloseableIterator<Object[]> iterator = query().union(sq1,sq2).iterate();
        try{
            assertTrue(iterator.hasNext());
            assertTrue(iterator.next() != null);
            assertTrue(iterator.next() != null);
            assertFalse(iterator.hasNext());
        }finally{
            iterator.close();
        }
    }
        
    @SuppressWarnings("unchecked")
    @Test
    public void Union_Single_Column_Projections_List() throws IOException{
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id.min());

        List<Integer> list = query().union(sq1, sq2).list();
        assertEquals(2, list.size());
        assertTrue(list.get(0) != null);
        assertTrue(list.get(1) != null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void Union_Single_Column_Projections_Iterate() throws IOException{
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id.min());

        CloseableIterator<Integer> iterator = query().union(sq1,sq2).iterate();
        try{
            assertTrue(iterator.hasNext());
            assertTrue(iterator.next() != null);
            assertTrue(iterator.next() != null);
            assertFalse(iterator.hasNext());
        }finally{
            iterator.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void Union_FactoryExpression() {
        ListSubQuery<Employee> sq1 = sq().from(employee)
                .list(Projections.constructor(Employee.class, employee.id));
        ListSubQuery<Employee> sq2 = sq().from(employee)
                .list(Projections.constructor(Employee.class, employee.id));        
        List<Employee> employees = query().union(sq1, sq2).list();
        for (Employee employee : employees){
            assertNotNull(employee);
        }
    }
    
    @Test
    public void Unique_Constructor_Projection(){
        IdName idAndName = query().from(survey).limit(1).uniqueResult(new QIdName(survey.id, survey.name));
        assertNotNull(idAndName);
        assertNotNull(idAndName.getId());
        assertNotNull(idAndName.getName());

    }

    @Test
    public void Unique_Single(){
        String s = query().from(survey).limit(1).uniqueResult(survey.name);
        assertNotNull(s);

    }
    
    @Test
    public void Single(){
        assertNotNull(query().from(survey).singleResult(survey.name));
    }
    
    @Test
    public void Single_Array(){
        assertNotNull(query().from(survey).singleResult(new Expression<?>[]{survey.name}));
    }

    @Test
    public void Unique_Wildcard(){
        // unique wildcard
        Object[] row = query().from(survey).limit(1).uniqueResult(survey.all());
        assertNotNull(row);
        assertEquals(3, row.length);
        assertNotNull(row[0]);
        assertNotNull(row[0] +" is not null", row[1]);

    }
    
    @Test(expected=NonUniqueResultException.class)
    public void UniqueResultContract(){
        query().from(employee).uniqueResult(employee.all());
    }

    @Test
    public void Various() throws SQLException {
        for (String s : query().from(survey).list(survey.name.lower())){
            assertEquals(s, s.toLowerCase());
        }

        for (String s : query().from(survey).list(survey.name.append("abc"))){
            assertTrue(s.endsWith("abc"));
        }

        System.out.println(query().from(survey).list(survey.id.sqrt()));

    }

    @Test
    public void Where_Exists() throws SQLException {
        NumberSubQuery<Integer> sq1 = sq().from(employee).unique(employee.id.max());

        query().from(employee).where(sq1.exists()).count();

        query().from(employee).where(sq1.exists().not()).count();
    }

    @Test
    public void Wildcard(){
        // wildcard
        for (Object[] row : query().from(survey).list(survey.all())){
            assertNotNull(row);
            assertEquals(3, row.length);
            assertNotNull(row[0]);
            assertNotNull(row[0] + " is not null", row[1]);
        }
    }

    @Test
    @SkipForQuoted
    public void Wildcard_All() {
        expectedQuery = "select * from EMPLOYEE e";
        query().from(employee).list(Wildcard.all);
    }

    @Test
    public void Wildcard_and_QTuple(){
        // wildcard and QTuple
        for (Tuple tuple : query().from(survey).list(new QTuple(survey.all()))){
            assertNotNull(tuple.get(survey.id));
            assertNotNull(tuple.get(survey.name));
        }
    }
    
    @Test
    public void QBeanUsage(){
        QSurvey survey = QSurvey.survey;
        PathBuilder<Object[]> sq = new PathBuilder<Object[]>(Object[].class, "sq");
        List<Survey> surveys = 
            query().from(
                sq().from(survey).list(survey.all()).as("sq"))
            .list(new QBean<Survey>(Survey.class, Collections.singletonMap("name", sq.get(survey.name))));        
        assertFalse(surveys.isEmpty());

    }
    
    @Test
    public void Operation_in_Constant_list(){
        query().from(survey).where(survey.name.charAt(0).in(Arrays.asList('a'))).count();
        query().from(survey).where(survey.name.charAt(0).in(Arrays.asList('a','b'))).count();
        query().from(survey).where(survey.name.charAt(0).in(Arrays.asList('a','b','c'))).count();
    }
    
    @Test
    public void Path_in_Constant_list(){
        query().from(survey).where(survey.name.in(Arrays.asList("a"))).count();
        query().from(survey).where(survey.name.in(Arrays.asList("a","b"))).count();
        query().from(survey).where(survey.name.in(Arrays.asList("a","b","c"))).count();
    }
    
    @Test    
    public void GroupBy_Superior() {
        SQLQuery qry = query()
            .from(employee)
            .innerJoin(employee._superiorIdKey, employee2);
        
        QTuple subordinates = new QTuple(employee2.id, employee2.firstname, employee2.lastname);

        Map<Integer, Group> results = qry.transform(
            GroupBy.groupBy(employee.id).as(employee.firstname, employee.lastname, 
            GroupBy.map(employee2.id, subordinates)));
        
        assertEquals(2, results.size());
        
        // Mike Smith
        Group group = results.get(1);
        assertEquals("Mike", group.getOne(employee.firstname));
        assertEquals("Smith", group.getOne(employee.lastname));

        Map<Integer, Tuple> emps = group.getMap(employee2.id, subordinates);
        assertEquals(4, emps.size());
        assertEquals("Steve", emps.get(12).get(employee2.firstname));

        // Mary Smith
        group = results.get(2);
        assertEquals("Mary", group.getOne(employee.firstname));
        assertEquals("Smith", group.getOne(employee.lastname));
        
        emps = group.getMap(employee2.id, subordinates);
        assertEquals(4, emps.size());
        assertEquals("Mason", emps.get(21).get(employee2.lastname));
    }
    
    public static class Survey {
        
        @Column("NAME")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
    }

}
