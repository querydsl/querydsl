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
package com.querydsl.sql;

import java.util.List;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.domain.QSurvey;
import com.querydsl.core.types.*;
import com.querydsl.core.types.expr.BooleanOperation;
import com.querydsl.core.types.expr.Wildcard;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.query.ListSubQuery;
import com.querydsl.core.types.query.NumberSubQuery;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SQLSubQueryTest {

    private static final QEmployee employee = QEmployee.employee;

    @Test
    public void UnknownOperator() {
        Operator op = new OperatorImpl(SQLSubQueryTest.class.getName(), "unknownfn");
        SQLSubQuery query = new SQLSubQuery();
        query.from(employee)
            .where(BooleanOperation.create(op, employee.id));

        assertEquals("from EMPLOYEE EMPLOYEE\nwhere com.querydsl.sql.SQLSubQueryTest#unknownfn(EMPLOYEE.ID)", query.toString());
    }

    @Test
    public void Multiple_Projections() {
        SQLSubQuery query = new SQLSubQuery();
        query.from(employee);
        assertEquals(1, query.list(employee).getMetadata().getProjection().size());
        assertEquals(1, query.list(employee).getMetadata().getProjection().size());
    }

    @Test
    public void List() {
        SQLSubQuery query = new SQLSubQuery();
        query.from(employee);
        ListSubQuery<?> subQuery = query.list(employee.id, "XXX", employee.firstname);
        List<? extends Expression<?>> exprs = subQuery.getMetadata().getProjection();
        assertEquals(employee.id, exprs.get(0));
        assertEquals(ConstantImpl.create("XXX") , exprs.get(1));
        assertEquals(employee.firstname, exprs.get(2));
    }

    @Test
    public void List_Entity() {
        QEmployee employee2 = new QEmployee("employee2");
        SQLSubQuery query = new SQLSubQuery();
        Expression<?> expr = query.from(employee)
             .innerJoin(employee.superiorIdKey, employee2)
             .list(employee, employee2.id);

        SQLSerializer serializer = new SQLSerializer(new Configuration(SQLTemplates.DEFAULT));
        serializer.handle(expr);

        assertEquals("(select EMPLOYEE.ID, EMPLOYEE.FIRSTNAME, EMPLOYEE.LASTNAME, EMPLOYEE.SALARY, EMPLOYEE.DATEFIELD, EMPLOYEE.TIMEFIELD, EMPLOYEE.SUPERIOR_ID, employee2.ID\n" +
                "from EMPLOYEE EMPLOYEE\n" +
                "inner join EMPLOYEE employee2\n" +
                "on EMPLOYEE.SUPERIOR_ID = employee2.ID)", serializer.toString());
    }

    @Test
    public void In() {
        ListSubQuery<Integer> ints = new SQLSubQuery().from(employee).list(employee.id);
        QEmployee.employee.id.in(ints);
    }

    @Test
    public void In_Union() {
        ListSubQuery<Integer> ints1 = new SQLSubQuery().from(employee).list(employee.id);
        ListSubQuery<Integer> ints2 = new SQLSubQuery().from(employee).list(employee.id);
        QEmployee.employee.id.in(new SQLSubQuery().union(ints1, ints2));
    }

    @Test
    public void In_Union2() {
        NumberSubQuery<Integer> ints1 = new SQLSubQuery().from(employee).unique(employee.id);
        NumberSubQuery<Integer> ints2 = new SQLSubQuery().from(employee).unique(employee.id);
        QEmployee.employee.id.in(new SQLSubQuery().union(ints1, ints2));
    }

    @Test
    public void Unique() {
        SQLSubQuery query = new SQLSubQuery();
        query.from(employee);
        SubQueryExpression<?> subQuery = query.unique(employee.id, "XXX", employee.firstname);
        List<? extends Expression<?>> exprs = subQuery.getMetadata().getProjection();
        assertEquals(employee.id, exprs.get(0));
        assertEquals(ConstantImpl.create("XXX") , exprs.get(1));
        assertEquals(employee.firstname, exprs.get(2));
    }

    @Test
    public void Complex() {
        // related to #584795
        QSurvey survey = new QSurvey("survey");
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SubQueryExpression<?> subQuery = new SQLSubQuery().from(survey)
          .innerJoin(emp1)
           .on(survey.id.eq(emp1.id))
          .innerJoin(emp2)
           .on(emp1.superiorId.eq(emp2.superiorId), emp1.firstname.eq(emp2.firstname))
          .list(survey.id, emp2.firstname);

        assertEquals(3, subQuery.getMetadata().getJoins().size());
    }

    @Test
    public void Validate() {
        NumberPath<Long> operatorTotalPermits = new NumberPath<Long>(Long.class, "operator_total_permits");
        QSurvey survey = new QSurvey("survey");

        // select survey.name, count(*) as operator_total_permits
        // from survey
        // where survey.name >= "A"
        // group by survey.name
        // order by operator_total_permits asc
        // limit 10

        Expression<?> e = new SQLSubQuery().from(survey)
            .where(survey.name.goe("A"))
            .groupBy(survey.name)
            .orderBy(operatorTotalPermits.asc())
            .limit(10)
            .list(survey.name, Wildcard.count.as(operatorTotalPermits))
            .as("top");

        new SQLQuery(null, SQLTemplates.DEFAULT).from(e);
    }

    @Test
    public void Union() {
        ListSubQuery<Object> q1 = new ListSubQuery<Object>(Object.class, new DefaultQueryMetadata());
        ListSubQuery<Object> q2 = new ListSubQuery<Object>(Object.class, new DefaultQueryMetadata());
        new SQLSubQuery().union(q1, q2);
        new SQLSubQuery().union(q1);
    }

    @Test
    public void Union_With() {
        QSurvey survey1 = new QSurvey("survey1");
        QSurvey survey2 = new QSurvey("survey2");
        QSurvey survey3 = new QSurvey("survey3");

        SQLQuery query = new SQLQuery(SQLTemplates.DEFAULT);
        query.with(survey1, new SQLSubQuery().from(survey1).list(survey1.all()));
        query.union(
              new SQLSubQuery().from(survey2).list(survey2.all()),
              new SQLSubQuery().from(survey3).list(survey3.all()));

        assertEquals("with survey1 as (select survey1.NAME, survey1.NAME2, survey1.ID\n" +
                "from SURVEY survey1)\n" +
                "(select survey2.NAME, survey2.NAME2, survey2.ID\n" +
                "from SURVEY survey2)\n" +
                "union\n" +
                "(select survey3.NAME, survey3.NAME2, survey3.ID\n" +
                "from SURVEY survey3)", query.toString());

    }

}
