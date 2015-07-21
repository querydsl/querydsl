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
package com.querydsl.sql;

import static com.querydsl.sql.SQLExpressions.select;
import static com.querydsl.sql.SQLExpressions.union;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.domain.QSurvey;

public class SQLSubQueryTest {

    private static final QEmployee employee = QEmployee.employee;

    @Test(expected = IllegalArgumentException.class)
    public void UnknownOperator() {
        Operator op = new Operator() {
            public String name() {
                return "unknownfn";
            }
            public String toString() {
                return name();
            }
            public Class<?> getType() {
                return Object.class;
            }
        };
        SQLQuery<?> query = new SQLQuery<Void>();
        query.from(employee)
            .where(Expressions.booleanOperation(op, employee.id)).toString();
    }

    @Test
    public void List() {
        SubQueryExpression<?> subQuery = select(employee.id, Expressions.constant("XXX"), employee.firstname).from(employee);
        List<? extends Expression<?>> exprs = ((FactoryExpression) subQuery.getMetadata().getProjection()).getArgs();
        assertEquals(employee.id, exprs.get(0));
        assertEquals(ConstantImpl.create("XXX") , exprs.get(1));
        assertEquals(employee.firstname, exprs.get(2));
    }

    @Test
    public void List_Entity() {
        QEmployee employee2 = new QEmployee("employee2");
        Expression<?> expr = select(employee, employee2.id).from(employee)
             .innerJoin(employee.superiorIdKey, employee2);

        SQLSerializer serializer = new SQLSerializer(new Configuration(SQLTemplates.DEFAULT));
        serializer.handle(expr);

        assertEquals("(select EMPLOYEE.ID, EMPLOYEE.FIRSTNAME, EMPLOYEE.LASTNAME, EMPLOYEE.SALARY, EMPLOYEE.DATEFIELD, EMPLOYEE.TIMEFIELD, EMPLOYEE.SUPERIOR_ID, employee2.ID as col__ID7\n" +
                "from EMPLOYEE EMPLOYEE\n" +
                "inner join EMPLOYEE employee2\n" +
                "on EMPLOYEE.SUPERIOR_ID = employee2.ID)", serializer.toString());
    }

    @Test
    public void In() {
        SubQueryExpression<Integer> ints = select(employee.id).from(employee);
        QEmployee.employee.id.in(ints);
    }

    @Test
    public void In_Union() {
        SubQueryExpression<Integer> ints1 = select(employee.id).from(employee);
        SubQueryExpression<Integer> ints2 = select(employee.id).from(employee);
        QEmployee.employee.id.in(union(ints1, ints2));
    }

    @Test
    public void In_Union2() {
        SubQueryExpression<Integer> ints1 = select(employee.id).from(employee);
        SubQueryExpression<Integer> ints2 = select(employee.id).from(employee);
        QEmployee.employee.id.in(union(ints1, ints2));
    }

    @Test
    public void Unique() {
        SubQueryExpression<?> subQuery = select(employee.id, Expressions.constant("XXX"), employee.firstname).from(employee);
        List<? extends Expression<?>> exprs = ((FactoryExpression) subQuery.getMetadata().getProjection()).getArgs();
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
        SubQueryExpression<?> subQuery = select(survey.id, emp2.firstname).from(survey)
          .innerJoin(emp1)
           .on(survey.id.eq(emp1.id))
          .innerJoin(emp2)
           .on(emp1.superiorId.eq(emp2.superiorId), emp1.firstname.eq(emp2.firstname));

        assertEquals(3, subQuery.getMetadata().getJoins().size());
    }

    @Test
    public void Validate() {
        NumberPath<Long> operatorTotalPermits = Expressions.numberPath(Long.class, "operator_total_permits");
        QSurvey survey = new QSurvey("survey");

        // select survey.name, count(*) as operator_total_permits
        // from survey
        // where survey.name >= "A"
        // group by survey.name
        // order by operator_total_permits asc
        // limit 10

        Expression<?> e = select(survey.name, Wildcard.count.as(operatorTotalPermits)).from(survey)
            .where(survey.name.goe("A"))
            .groupBy(survey.name)
            .orderBy(operatorTotalPermits.asc())
            .limit(10)
            .as("top");

        select(Wildcard.all).from(e);
    }

    @Test
    public void Union() {
        QSurvey survey = QSurvey.survey;
        SubQueryExpression<Integer> q1 = select(survey.id).from(survey);
        SubQueryExpression<Integer> q2 = select(survey.id).from(survey);
        union(q1, q2);
        union(q1);
    }

    @Test
    public void Union_With() {
        QSurvey survey1 = new QSurvey("survey1");
        QSurvey survey2 = new QSurvey("survey2");
        QSurvey survey3 = new QSurvey("survey3");

        SQLQuery<Void> query = new SQLQuery<Void>();
        query.with(survey1, select(survey1.all()).from(survey1));
        query.union(
                select(survey2.all()).from(survey2),
                select(survey3.all()).from(survey3));

        assertEquals("with survey1 as (select survey1.NAME, survey1.NAME2, survey1.ID\n" +
                "from SURVEY survey1)\n" +
                "(select survey2.NAME, survey2.NAME2, survey2.ID\n" +
                "from SURVEY survey2)\n" +
                "union\n" +
                "(select survey3.NAME, survey3.NAME2, survey3.ID\n" +
                "from SURVEY survey3)", query.toString());

    }

}
