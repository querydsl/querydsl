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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.domain.QEmployeeNoPK;
import com.querydsl.sql.domain.QSurvey;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.expr.Wildcard;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.PathBuilder;
import com.querydsl.core.types.path.StringPath;

import org.junit.Test;

public class SQLSerializerTest {

    private static final QEmployee employee = QEmployee.employee;

    private static final QSurvey survey = QSurvey.survey;

    @Test
    public void Count() {
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.handle(employee.id.count().add(employee.id.countDistinct()));
        assertEquals("count(EMPLOYEE.ID) + count(distinct EMPLOYEE.ID)", serializer.toString());
    }

    @Test
    public void CountDistinct() {
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        SQLSubQuery query = new SQLSubQuery();
        query.from(QEmployeeNoPK.employee);
        query.distinct();
        serializer.serializeForQuery(query.getMetadata(), true);
        assertEquals("select count(*)\n" +
        "from (select distinct EMPLOYEE.ID, EMPLOYEE.FIRSTNAME, EMPLOYEE.LASTNAME, EMPLOYEE.SALARY, " +
            "EMPLOYEE.DATEFIELD, EMPLOYEE.TIMEFIELD, EMPLOYEE.SUPERIOR_ID\n" +
         "from EMPLOYEE EMPLOYEE) internal", serializer.toString());
    }

    @Test
    public void CountDistinct_PostgreSQL() {
        Configuration postgres = new Configuration(new PostgresTemplates());
        SQLSerializer serializer = new SQLSerializer(postgres);
        SQLSubQuery query = new SQLSubQuery();
        query.from(QEmployeeNoPK.employee);
        query.distinct();
        serializer.serializeForQuery(query.getMetadata(), true);
        assertEquals("select count(" +
            "distinct (EMPLOYEE.ID, EMPLOYEE.FIRSTNAME, EMPLOYEE.LASTNAME, EMPLOYEE.SALARY, " +
            "EMPLOYEE.DATEFIELD, EMPLOYEE.TIMEFIELD, EMPLOYEE.SUPERIOR_ID))\n" +
            "from EMPLOYEE EMPLOYEE", serializer.toString());
    }

    @Test
    public void DynamicQuery() {
        Path<Object> userPath = Expressions.path(Object.class, "user");
        NumberPath<Long> idPath = Expressions.numberPath(Long.class, userPath, "id");
        StringPath usernamePath = Expressions.stringPath(userPath, "username");
        Expression<?> sq = new SQLSubQuery()
            .from(userPath).where(idPath.eq(1l))
            .list(idPath, usernamePath);

        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.handle(sq);
        //USER is a reserved word in ANSI SQL 2008
        assertEquals("(select \"user\".id, \"user\".username\n" +
                "from \"user\"\n" +
                "where \"user\".id = ?)", serializer.toString());
    }

    @Test
    public void DynamicQuery2() {
        PathBuilder<Object> userPath = new PathBuilder<Object>(Object.class, "user");
        NumberPath<Long> idPath = userPath.getNumber("id", Long.class);
        StringPath usernamePath = userPath.getString("username");
        Expression<?> sq = new SQLSubQuery()
            .from(userPath).where(idPath.eq(1l))
            .list(idPath, usernamePath);

        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.handle(sq);
        //USER is a reserved word in ANSI SQL 2008
        assertEquals("(select \"user\".id, \"user\".username\n" +
                "from \"user\"\n" +
                "where \"user\".id = ?)", serializer.toString());
    }

    @Test
    public void In() {
        StringPath path = Expressions.stringPath("str");
        Expression<?> expr = ExpressionUtils.in(path, Arrays.asList("1", "2", "3"));

        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.handle(expr);
        assertEquals(Arrays.asList(path, path, path), serializer.getConstantPaths());
        assertEquals(3, serializer.getConstants().size());
    }

    @Test
    public void Or_In() {
        StringPath path = Expressions.stringPath("str");
        Expression<?> expr = ExpressionUtils.anyOf(
                ExpressionUtils.in(path, Arrays.asList("1", "2", "3")),
                ExpressionUtils.in(path, Arrays.asList("4", "5", "6")));

        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.handle(expr);
        assertEquals(Arrays.asList(path, path, path, path, path, path), serializer.getConstantPaths());
        assertEquals(6, serializer.getConstants().size());
    }

    @Test
    public void Some() {
        //select some((e.FIRSTNAME is not null)) from EMPLOYEE
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.handle(SQLExpressions.any(employee.firstname.isNotNull()));
        assertEquals("some(EMPLOYEE.FIRSTNAME is not null)", serializer.toString());
    }

    @Test
    public void StartsWith() {
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        QSurvey s1 = new QSurvey("s1");
        serializer.handle(s1.name.startsWith("X"));
        assertEquals("s1.NAME like ? escape '\\'", serializer.toString());
        assertEquals(Arrays.asList("X%"), serializer.getConstants());
    }

    @Test
    public void From_Function() {
        SQLQuery query = query();
        query.from(Expressions.template(Survey.class, "functionCall()")).join(survey);
        query.where(survey.name.isNotNull());
        assertEquals("from functionCall()\njoin SURVEY SURVEY\nwhere SURVEY.NAME is not null", query.toString());
    }

    @Test
    public void Join_To_Function_With_Alias() {
        SQLQuery query = query();
        query.from(survey).join(RelationalFunctionCall.create(Survey.class, "functionCall"), Expressions.path(Survey.class, "fc"));
        query.where(survey.name.isNotNull());
        assertEquals("from SURVEY SURVEY\njoin functionCall() as fc\nwhere SURVEY.NAME is not null", query.toString());
    }

    @Test
    public void Join_To_Function_In_Derby() {
        SQLQuery query = new SQLQuery(new DerbyTemplates());
        query.from(survey).join(RelationalFunctionCall.create(Survey.class, "functionCall"), Expressions.path(Survey.class, "fc"));
        query.where(survey.name.isNotNull());
        assertEquals("from SURVEY SURVEY\njoin table(functionCall()) as fc\nwhere SURVEY.NAME is not null", query.toString());
    }

    @Test
    public void Keyword_After_Dot() {
        SQLQuery query = new SQLQuery(MySQLTemplates.DEFAULT);
        PathBuilder<Survey> surveyBuilder = new PathBuilder<Survey>(Survey.class, "survey");
        query.from(surveyBuilder).where(surveyBuilder.get("not").isNotNull());
        assertFalse(query.toString().contains("`"));
    }

    @Test
    public void Like() {
        Expression<?> expr = Expressions.stringTemplate("'%a%'").contains("%a%");
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.handle(expr);
        assertEquals("'%a%' like ? escape '\\'", serializer.toString());
    }

    @Test
    public void Override() {
        Configuration conf = new Configuration(new DerbyTemplates());
        conf.registerTableOverride("SURVEY", "surveys");

        SQLQuery query = new SQLQuery(conf);
        query.from(survey);
        assertEquals("from surveys SURVEY", query.toString());
    }

    @Test
    public void ColumnOverrides() {
        Configuration conf = new Configuration(new DerbyTemplates());
        conf.registerColumnOverride("SURVEY", "NAME", "LABEL");

        SQLQuery query = new SQLQuery(conf);
        query.from(survey).where(survey.name.isNull());
        assertEquals("from SURVEY SURVEY\n" +
                "where SURVEY.LABEL is null", query.toString());
    }

    @Test
    public void ColumnOverrides2() {
        Configuration conf = new Configuration(new DerbyTemplates());
        conf.registerColumnOverride("PUBLIC", "SURVEY", "NAME", "LABEL");

        SQLQuery query = new SQLQuery(conf);
        query.from(survey).where(survey.name.isNull());
        assertEquals("from SURVEY SURVEY\n" +
                "where SURVEY.LABEL is null", query.toString());
    }

    @Test
    public void Complex_SubQuery() {
        // create sub queries
        List<SubQueryExpression<Tuple>> sq = new ArrayList<SubQueryExpression<Tuple>>();
        String[] strs = new String[]{"a","b","c"};
        for(String str : strs) {
            Expression<Boolean> alias = Expressions.cases().when(survey.name.eq(str)).then(true).otherwise(false);
            sq.add(sq().from(survey).distinct().unique(survey.name, alias));
        }

        // master querydsl
        PathBuilder<Tuple> subAlias = new PathBuilder<Tuple>(Tuple.class, "sub");
        SubQueryExpression<?> master = sq()
                .from(sq().union(sq).as(subAlias))
                .groupBy(subAlias.get("prop1"))
                .list(subAlias.get("prop2"));

        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.serialize(master.getMetadata(), false);
        System.err.println(serializer);
    }

    private SQLQuery query() {
        return new SQLQuery(Configuration.DEFAULT);
    }

    private SQLSubQuery sq() {
        return new SQLSubQuery();
    }

    @Test
    public void Boolean() {
        QSurvey s = new QSurvey("s");
        BooleanBuilder bb1 = new BooleanBuilder();
        bb1.and(s.name.eq(s.name));

        BooleanBuilder bb2 = new BooleanBuilder();
        bb2.or(s.name.eq(s.name));
        bb2.or(s.name.eq(s.name));

        String str = new SQLSerializer(Configuration.DEFAULT).handle(bb1.and(bb2)).toString();
        assertEquals("s.NAME = s.NAME and (s.NAME = s.NAME or s.NAME = s.NAME)", str);
    }

    @Test
    public void List_In_Query() {
        Expression<?> expr = Expressions.list(survey.id, survey.name).in(sq().from(survey).list(survey.id, survey.name));

        String str = new SQLSerializer(Configuration.DEFAULT).handle(expr).toString();
        assertEquals("(SURVEY.ID, SURVEY.NAME) in (select SURVEY.ID, SURVEY.NAME\nfrom SURVEY SURVEY)", str);
    }

    @Test
    public void WithRecursive() {
        /*with sub (id, firstname, superior_id) as (
            select id, firstname, superior_id from employee where firstname like 'Mike'
            union all
            select employee.id, employee.firstname, employee.superior_id from sub, employee
            where employee.superior_id = sub.id)
        select * from sub;*/

        QEmployee e = QEmployee.employee;
        PathBuilder<Tuple> sub = new PathBuilder<Tuple>(Tuple.class, "sub");
        SQLQuery query = new SQLQuery(SQLTemplates.DEFAULT);
        query.withRecursive(sub,
                sq().unionAll(
                    sq().from(e).where(e.firstname.eq("Mike"))
                        .list(e.id, e.firstname, e.superiorId),
                    sq().from(e, sub).where(e.superiorId.eq(sub.get(e.id)))
                        .list(e.id, e.firstname, e.superiorId)))
             .from(sub);

        QueryMetadata md = query.getMetadata();
        md.addProjection(Wildcard.all);
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.serialize(md, false);
        assertEquals("with recursive sub as ((select EMPLOYEE.ID, EMPLOYEE.FIRSTNAME, EMPLOYEE.SUPERIOR_ID\n" +
                "from EMPLOYEE EMPLOYEE\n" +
                "where EMPLOYEE.FIRSTNAME = ?)\n" +
                "union all\n" +
                "(select EMPLOYEE.ID, EMPLOYEE.FIRSTNAME, EMPLOYEE.SUPERIOR_ID\n" +
                "from EMPLOYEE EMPLOYEE, sub\n" +
                "where EMPLOYEE.SUPERIOR_ID = sub.ID))\n" +
                "select *\n" +
                "from sub", serializer.toString());

    }

    @Test
    public void WithRecursive2() {
        /*with sub (id, firstname, superior_id) as (
            select id, firstname, superior_id from employee where firstname like 'Mike'
            union all
            select employee.id, employee.firstname, employee.superior_id from sub, employee
            where employee.superior_id = sub.id)
        select * from sub;*/

        QEmployee e = QEmployee.employee;
        PathBuilder<Tuple> sub = new PathBuilder<Tuple>(Tuple.class, "sub");
        SQLQuery query = new SQLQuery(SQLTemplates.DEFAULT);
        query.withRecursive(sub, sub.get(e.id), sub.get(e.firstname), sub.get(e.superiorId)).as(
                sq().unionAll(
                    sq().from(e).where(e.firstname.eq("Mike"))
                        .list(e.id, e.firstname, e.superiorId),
                    sq().from(e, sub).where(e.superiorId.eq(sub.get(e.id)))
                        .list(e.id, e.firstname, e.superiorId)))
             .from(sub);

        QueryMetadata md = query.getMetadata();
        md.addProjection(Wildcard.all);
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.serialize(md, false);
        assertEquals("with recursive sub (ID, FIRSTNAME, SUPERIOR_ID) as ((select EMPLOYEE.ID, EMPLOYEE.FIRSTNAME, EMPLOYEE.SUPERIOR_ID\n" +
                "from EMPLOYEE EMPLOYEE\n" +
                "where EMPLOYEE.FIRSTNAME = ?)\n" +
                "union all\n" +
                "(select EMPLOYEE.ID, EMPLOYEE.FIRSTNAME, EMPLOYEE.SUPERIOR_ID\n" +
                "from EMPLOYEE EMPLOYEE, sub\n" +
                "where EMPLOYEE.SUPERIOR_ID = sub.ID))\n" +
                "select *\n" +
                "from sub", serializer.toString());

    }

    @Test
    public void UseLiterals() {
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.setUseLiterals(true);

        int offset = TimeZone.getDefault().getRawOffset();
        Expression<?> expr = SQLExpressions.datediff(DatePart.year, employee.datefield, new java.sql.Date(-offset));
        serializer.handle(expr);
        assertEquals("datediff('year',EMPLOYEE.DATEFIELD,(date '1970-01-01'))", serializer.toString());
    }

}
