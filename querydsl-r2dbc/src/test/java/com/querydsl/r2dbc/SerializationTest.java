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
package com.querydsl.r2dbc;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.r2dbc.dml.R2DBCDeleteClause;
import com.querydsl.r2dbc.dml.R2DBCInsertClause;
import com.querydsl.r2dbc.dml.R2DBCUpdateClause;
import com.querydsl.r2dbc.domain.QEmployee;
import com.querydsl.r2dbc.domain.QSurvey;
import io.r2dbc.spi.Connection;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SerializationTest {

    private static final QSurvey survey = QSurvey.survey;

    private final Connection connection = EasyMock.createMock(Connection.class);

    @Test
    public void innerJoin() {
        R2DBCQuery<?> query = new R2DBCQuery<Void>(connection, SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).innerJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\ninner join SURVEY s2", query.toString());
    }

    @Test
    public void leftJoin() {
        R2DBCQuery<?> query = new R2DBCQuery<Void>(connection, SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).leftJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\nleft join SURVEY s2", query.toString());
    }

    @Test
    public void rightJoin() {
        R2DBCQuery<?> query = new R2DBCQuery<Void>(connection, SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).rightJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\nright join SURVEY s2", query.toString());
    }

    @Test
    public void fullJoin() {
        R2DBCQuery<?> query = new R2DBCQuery<Void>(connection, SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).fullJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\nfull join SURVEY s2", query.toString());
    }

    @Test
    public void update() {
        R2DBCUpdateClause updateClause = new R2DBCUpdateClause(connection, SQLTemplates.DEFAULT, survey);
        updateClause.set(survey.id, 1);
        updateClause.set(survey.name, (String) null);
        assertEquals("update SURVEY\nset ID = ?, NAME = ?", updateClause.toString());
    }

    @Test
    public void update_where() {
        R2DBCUpdateClause updateClause = new R2DBCUpdateClause(connection, SQLTemplates.DEFAULT, survey);
        updateClause.set(survey.id, 1);
        updateClause.set(survey.name, (String) null);
        updateClause.where(survey.name.eq("XXX"));
        assertEquals("update SURVEY\nset ID = ?, NAME = ?\nwhere SURVEY.NAME = ?", updateClause.toString());
    }

    @Test
    public void insert() {
        R2DBCInsertClause insertClause = new R2DBCInsertClause(connection, SQLTemplates.DEFAULT, survey);
        insertClause.set(survey.id, 1);
        insertClause.set(survey.name, (String) null);
        assertEquals("insert into SURVEY (ID, NAME)\nvalues (?, ?)", insertClause.toString());
    }

    @Test
    public void delete_with_subQuery_exists() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");
        R2DBCDeleteClause delete = new R2DBCDeleteClause(connection, SQLTemplates.DEFAULT, survey1);
        delete.where(survey1.name.eq("XXX"), R2DBCExpressions.selectOne().from(employee).where(survey1.id.eq(employee.id)).exists());
        assertEquals("delete from SURVEY\n" +
                "where SURVEY.NAME = ? and exists (select 1\n" +
                "from EMPLOYEE e\n" +
                "where SURVEY.ID = e.ID)", delete.toString());
    }

    @Test
    public void nextval() {
        SubQueryExpression<?> sq = R2DBCExpressions.select(R2DBCExpressions.nextval("myseq")).from(QSurvey.survey);
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
        serializer.serialize(sq.getMetadata(), false);
        assertEquals("select nextval('myseq')\nfrom SURVEY SURVEY", serializer.toString());
    }

    @Test
    public void functionCall() {
        R2DBCRelationalFunctionCall<String> func = R2DBCExpressions.relationalFunctionCall(String.class, "TableValuedFunction", "parameter");
        PathBuilder<String> funcAlias = new PathBuilder<String>(String.class, "tokFunc");
        SubQueryExpression<?> expr = R2DBCExpressions.select(survey.name).from(survey)
                .join(func, funcAlias).on(survey.name.like(funcAlias.getString("prop")).not());

        SQLSerializer serializer = new SQLSerializer(new Configuration(new SQLServerTemplates()));
        serializer.serialize(expr.getMetadata(), false);
        assertEquals("select SURVEY.NAME\n" +
                "from SURVEY SURVEY\n" +
                "join TableValuedFunction(?) as tokFunc\n" +
                "on not (SURVEY.NAME like tokFunc.prop escape '\\')", serializer.toString());

    }

    @Test
    public void functionCall2() {
        R2DBCRelationalFunctionCall<String> func = R2DBCExpressions.relationalFunctionCall(String.class, "TableValuedFunction", "parameter");
        PathBuilder<String> funcAlias = new PathBuilder<String>(String.class, "tokFunc");
        R2DBCQuery<?> q = new R2DBCQuery<Void>(SQLServerTemplates.DEFAULT);
        q.from(survey)
                .join(func, funcAlias).on(survey.name.like(funcAlias.getString("prop")).not());

        assertEquals("from SURVEY SURVEY\n" +
                "join TableValuedFunction(?) as tokFunc\n" +
                "on not (SURVEY.NAME like tokFunc.prop escape '\\')", q.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void union1() {
        Expression<?> q = R2DBCExpressions.union(R2DBCExpressions.select(survey.all()).from(survey),
                R2DBCExpressions.select(survey.all()).from(survey));

        assertEquals("(select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n" +
                "from SURVEY SURVEY)\n" +
                "union\n" +
                "(select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n" +
                "from SURVEY SURVEY)", q.toString());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void union1_groupBy() {
        Expression<?> q = R2DBCExpressions.union(R2DBCExpressions.select(survey.all()).from(survey),
                R2DBCExpressions.select(survey.all()).from(survey))
                .groupBy(survey.id);

        assertEquals("(select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n" +
                "from SURVEY SURVEY)\n" +
                "union\n" +
                "(select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n" +
                "from SURVEY SURVEY)\n" +
                "group by SURVEY.ID", q.toString());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void union2() {
        Expression<?> q = new R2DBCQuery<Void>().union(survey,
                R2DBCExpressions.select(survey.all()).from(survey),
                R2DBCExpressions.select(survey.all()).from(survey));

        assertEquals("from ((select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n" +
                "from SURVEY SURVEY)\n" +
                "union\n" +
                "(select SURVEY.NAME, SURVEY.NAME2, SURVEY.ID\n" +
                "from SURVEY SURVEY)) as SURVEY", q.toString());

    }

    @Test
    public void with() {
        QSurvey survey2 = new QSurvey("survey2");
        R2DBCQuery<?> q = new R2DBCQuery<Void>();
        q.with(survey, survey.id, survey.name).as(
                R2DBCExpressions.select(survey2.id, survey2.name).from(survey2));

        assertEquals("with SURVEY (ID, NAME) as (select survey2.ID, survey2.NAME\n" +
                "from SURVEY survey2)\n\n" +
                "from dual", q.toString());
    }

    @Test
    public void with_complex() {
        QSurvey s = new QSurvey("s");
        R2DBCQuery<?> q = new R2DBCQuery<Void>();
        q.with(s, s.id, s.name).as(
                R2DBCExpressions.select(survey.id, survey.name).from(survey))
                .select(s.id, s.name, survey.id, survey.name).from(s, survey);

        assertEquals("with s (ID, NAME) as (select SURVEY.ID, SURVEY.NAME\n" +
                "from SURVEY SURVEY)\n" +
                "select s.ID, s.NAME, SURVEY.ID, SURVEY.NAME\n" +
                "from s s, SURVEY SURVEY", q.toString());
    }

    @Test
    public void with_tuple() {
        PathBuilder<Survey> survey = new PathBuilder<Survey>(Survey.class, "SURVEY");
        QSurvey survey2 = new QSurvey("survey2");
        R2DBCQuery<?> q = new R2DBCQuery<Void>();
        q.with(survey, survey.get(survey2.id), survey.get(survey2.name)).as(
                R2DBCExpressions.select(survey2.id, survey2.name).from(survey2));

        assertEquals("with SURVEY (ID, NAME) as (select survey2.ID, survey2.NAME\n" +
                "from SURVEY survey2)\n\n" +
                "from dual", q.toString());
    }

    @Test
    public void with_tuple2() {
        QSurvey survey2 = new QSurvey("survey2");
        R2DBCQuery<?> q = new R2DBCQuery<Void>();
        q.with(survey, survey.id, survey.name).as(
                R2DBCExpressions.select(survey2.id, survey2.name).from(survey2));

        assertEquals("with SURVEY (ID, NAME) as (select survey2.ID, survey2.NAME\n" +
                "from SURVEY survey2)\n\n" +
                "from dual", q.toString());
    }

    @Test
    public void with_singleColumn() {
        QSurvey survey2 = new QSurvey("survey2");
        R2DBCQuery<?> q = new R2DBCQuery<Void>();
        q.with(survey, new Path<?>[]{survey.id}).as(
                R2DBCExpressions.select(survey2.id).from(survey2));

        assertEquals("with SURVEY (ID) as (select survey2.ID\n" +
                "from SURVEY survey2)\n\n" +
                "from dual", q.toString());
    }


}
