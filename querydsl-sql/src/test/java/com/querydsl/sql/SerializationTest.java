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
import static com.querydsl.sql.SQLExpressions.selectOne;
import static com.querydsl.sql.SQLExpressions.union;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;

import org.easymock.EasyMock;
import org.junit.Test;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.domain.QSurvey;

public class SerializationTest {

    private static final QSurvey survey = QSurvey.survey;

    private final Connection connection = EasyMock.createMock(Connection.class);

    @Test
    public void InnerJoin() {
        SQLQuery<?> query = new SQLQuery<Void>(connection,SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).innerJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\ninner join SURVEY s2", query.toString());
    }

    @Test
    public void LeftJoin() {
        SQLQuery<?> query = new SQLQuery<Void>(connection,SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).leftJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\nleft join SURVEY s2", query.toString());
    }

    @Test
    public void RightJoin() {
        SQLQuery<?> query = new SQLQuery<Void>(connection,SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).rightJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\nright join SURVEY s2", query.toString());
    }

    @Test
    public void FullJoin() {
        SQLQuery<?> query = new SQLQuery<Void>(connection,SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).fullJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\nfull join SURVEY s2", query.toString());
    }

    @Test
    public void Update() {
        SQLUpdateClause updateClause = new SQLUpdateClause(connection,SQLTemplates.DEFAULT,survey);
        updateClause.set(survey.id, 1);
        updateClause.set(survey.name, (String)null);
        assertEquals("update SURVEY\nset ID = ?, NAME = ?", updateClause.toString());
    }

    @Test
    public void Update_Where() {
        SQLUpdateClause updateClause = new SQLUpdateClause(connection,SQLTemplates.DEFAULT,survey);
        updateClause.set(survey.id, 1);
        updateClause.set(survey.name, (String)null);
        updateClause.where(survey.name.eq("XXX"));
        assertEquals("update SURVEY\nset ID = ?, NAME = ?\nwhere SURVEY.NAME = ?", updateClause.toString());
    }

    @Test
    public void Insert() {
        SQLInsertClause insertClause = new SQLInsertClause(connection,SQLTemplates.DEFAULT,survey);
        insertClause.set(survey.id, 1);
        insertClause.set(survey.name, (String)null);
        assertEquals("insert into SURVEY (ID, NAME)\nvalues (?, ?)", insertClause.toString());
    }

    @Test
    public void Delete_with_SubQuery_exists() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");
        SQLDeleteClause delete = new SQLDeleteClause(connection, SQLTemplates.DEFAULT,survey1);
        delete.where(survey1.name.eq("XXX"), selectOne().from(employee).where(survey1.id.eq(employee.id)).exists());
        assertEquals("delete from SURVEY\n" +
                     "where SURVEY.NAME = ? and exists (select 1\n" +
                     "from EMPLOYEE e\n" +
                     "where SURVEY.ID = e.ID)", delete.toString());
    }

    @Test
    public void Nextval() {
        SubQueryExpression<?> sq = select(SQLExpressions.nextval("myseq")).from(QSurvey.survey);
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.serialize(sq.getMetadata(), false);
        assertEquals("select nextval('myseq')\nfrom SURVEY SURVEY", serializer.toString());
    }

    @Test
    public void FunctionCall() {
        RelationalFunctionCall<String> func = SQLExpressions.relationalFunctionCall(String.class, "TableValuedFunction", "parameter");
        PathBuilder<String> funcAlias = new PathBuilder<String>(String.class, "tokFunc");
        SubQueryExpression<?> expr = select(survey.name).from(survey)
            .join(func, funcAlias).on(survey.name.like(funcAlias.getString("prop")).not());

        SQLSerializer serializer = new SQLSerializer(new Configuration(new SQLServerTemplates()));
        serializer.serialize(expr.getMetadata(), false);
        assertEquals("select SURVEY.NAME\n" +
                "from SURVEY SURVEY\n" +
                "join TableValuedFunction(?) as tokFunc\n" +
                "on not (SURVEY.NAME like tokFunc.prop escape '\\')", serializer.toString());

    }

    @Test
    public void FunctionCall2() {
        RelationalFunctionCall<String> func = SQLExpressions.relationalFunctionCall(String.class, "TableValuedFunction", "parameter");
        PathBuilder<String> funcAlias = new PathBuilder<String>(String.class, "tokFunc");
        SQLQuery<?> q = new SQLQuery<Void>(SQLServerTemplates.DEFAULT);
        q.from(survey)
            .join(func, funcAlias).on(survey.name.like(funcAlias.getString("prop")).not());

        assertEquals("from SURVEY SURVEY\n" +
                "join TableValuedFunction(?) as tokFunc\n" +
                "on not (SURVEY.NAME like tokFunc.prop escape '\\')", q.toString());
    }

    @Test
    public void FunctionCall3() {
        RelationalFunctionCall<String> func = SQLExpressions.relationalFunctionCall(String.class, "TableValuedFunction", "parameter");
        PathBuilder<String> funcAlias = new PathBuilder<String>(String.class, "tokFunc");
        SQLQuery<?> q = new SQLQuery<Void>(HSQLDBTemplates.DEFAULT);
        q.from(survey)
            .join(func, funcAlias).on(survey.name.like(funcAlias.getString("prop")).not());

        assertEquals("from SURVEY SURVEY\n" +
                "join table(TableValuedFunction(?)) as tokFunc\n" +
                "on not (SURVEY.NAME like tokFunc.prop escape '\\')", q.toString());
    }

    @Test
    public void Union() {
        Expression<?> q = union(select(survey.all()).from(survey),
                select(survey.all()).from(survey));

        assertEquals("(select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n" +
            "from SURVEY SURVEY)\n" +
            "union\n" +
            "(select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n" +
            "from SURVEY SURVEY)", q.toString());

    }

    @Test
    public void Union_GroupBy() {
        Expression<?> q = union(select(survey.all()).from(survey),
                select(survey.all()).from(survey))
                .groupBy(survey.id);

        assertEquals("(select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n" +
            "from SURVEY SURVEY)\n" +
            "union\n" +
            "(select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n" +
            "from SURVEY SURVEY)\n"+
            "group by SURVEY.ID", q.toString());

    }

    @Test
    public void Union2() {
        Expression<?> q = new SQLQuery<Void>().union(survey,
                select(survey.all()).from(survey),
                select(survey.all()).from(survey));

        assertEquals("from ((select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n"+
            "from SURVEY SURVEY)\n" +
            "union\n" +
            "(select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n" +
            "from SURVEY SURVEY)) as SURVEY", q.toString());

    }

    @Test
    public void With() {
        QSurvey survey2 = new QSurvey("survey2");
        SQLQuery<?> q = new SQLQuery<Void>();
        q.with(survey, survey.id, survey.name).as(
                select(survey2.id, survey2.name).from(survey2));

        assertEquals("with SURVEY (ID, NAME) as (select survey2.ID, survey2.NAME\n" +
            "from SURVEY survey2)\n\n" +
            "from dual", q.toString());
    }

    @Test
    public void With_Tuple() {
        PathBuilder<Survey> survey = new PathBuilder<Survey>(Survey.class, "SURVEY");
        QSurvey survey2 = new QSurvey("survey2");
        SQLQuery<?> q = new SQLQuery<Void>();
        q.with(survey, survey.get(survey2.id), survey.get(survey2.name)).as(
                select(survey2.id, survey2.name).from(survey2));

        assertEquals("with SURVEY (ID, NAME) as (select survey2.ID, survey2.NAME\n" +
            "from SURVEY survey2)\n\n" +
            "from dual", q.toString());
    }

    @Test
    public void With_Tuple2() {
        QSurvey survey2 = new QSurvey("survey2");
        SQLQuery<?> q = new SQLQuery<Void>();
        q.with(survey, survey.id, survey.name).as(
                select(survey2.id, survey2.name).from(survey2));

        assertEquals("with SURVEY (ID, NAME) as (select survey2.ID, survey2.NAME\n" +
                "from SURVEY survey2)\n\n" +
                "from dual", q.toString());
    }

    @Test
    public void With_SingleColumn() {
        QSurvey survey2 = new QSurvey("survey2");
        SQLQuery<?> q = new SQLQuery<Void>();
        q.with(survey, new Path[]{ survey.id }).as(
                select(survey2.id).from(survey2));

        assertEquals("with SURVEY (ID) as (select survey2.ID\n" +
            "from SURVEY survey2)\n\n" +
            "from dual", q.toString());
    }



}
