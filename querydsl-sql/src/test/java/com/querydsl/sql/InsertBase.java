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

import static com.querydsl.sql.Constants.survey;
import static com.querydsl.sql.Constants.survey2;
import static com.querydsl.core.Target.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.Tuple;
import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.Mapper;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.domain.*;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathImpl;
import com.querydsl.core.types.expr.Param;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;

public class InsertBase extends AbstractBaseTest {

    private void reset() throws SQLException{
        delete(survey).execute();
        insert(survey).values(1, "Hello World", "Hello").execute();

        delete(QDateTest.qDateTest).execute();
    }

    @Before
    public void setUp() throws SQLException {
        reset();
    }

    @After
    public void tearDown() throws SQLException{
        reset();
    }

    @Test
    @ExcludeIn(SQLITE) // https://bitbucket.org/xerial/sqlite-jdbc/issue/133/prepstmtsetdate-int-date-calendar-seems
    public void Insert_Dates() {
        QDateTest dateTest = QDateTest.qDateTest;
        LocalDate localDate = new LocalDate(1978, 1, 2);

        Path<LocalDate> localDateProperty = new PathImpl<LocalDate>(LocalDate.class, "DATE_TEST");
        Path<DateTime> dateTimeProperty = new PathImpl<DateTime>(DateTime.class, "DATE_TEST");
        SQLInsertClause insert = insert(dateTest);
        insert.set(localDateProperty, localDate);
        insert.execute();

        Tuple result = query().from(dateTest).singleResult(
                dateTest.dateTest.year(),
                dateTest.dateTest.month(),
                dateTest.dateTest.dayOfMonth(),
                dateTimeProperty);
        assertEquals(Integer.valueOf(1978), result.get(0, Integer.class));
        assertEquals(Integer.valueOf(1), result.get(1, Integer.class));
        assertEquals(Integer.valueOf(2), result.get(2, Integer.class));

        DateTime dateTime = result.get(dateTimeProperty);
        if (target == CUBRID) {
            // XXX Cubrid adds random milliseconds for some reason
            dateTime = dateTime.withMillisOfSecond(0);
        }
        assertEquals(localDate.toDateTimeAtStartOfDay(), dateTime);
    }

    @Test
    public void Complex1() {
        // related to #584795
        QSurvey survey = new QSurvey("survey");
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLInsertClause insert = insert(survey);
        insert.columns(survey.id, survey.name);
        insert.select(new SQLSubQuery().from(survey)
          .innerJoin(emp1)
           .on(survey.id.eq(emp1.id))
          .innerJoin(emp2)
           .on(emp1.superiorId.eq(emp2.superiorId), emp1.firstname.eq(emp2.firstname))
          .list(survey.id, emp2.firstname));

        insert.execute();
    }

    @Test
    public void Insert_Alternative_Syntax() {
        // with columns
        assertEquals(1, insert(survey)
            .set(survey.id, 3)
            .set(survey.name, "Hello")
            .execute());
    }

    @Test
    public void Insert_Batch() {
        SQLInsertClause insert = insert(survey)
            .set(survey.id, 5)
            .set(survey.name, "55")
            .addBatch();

        insert.set(survey.id, 6)
            .set(survey.name, "66")
            .addBatch();

        assertEquals(2, insert.execute());

        assertEquals(1l, query().from(survey).where(survey.name.eq("55")).count());
        assertEquals(1l, query().from(survey).where(survey.name.eq("66")).count());
    }

    @Test
    public void Insert_Batch_Templates() {
        SQLInsertClause insert = insert(survey)
                .set(survey.id, 5)
                .set(survey.name, Expressions.stringTemplate("'55'"))
                .addBatch();

        insert.set(survey.id, 6)
                .set(survey.name, Expressions.stringTemplate("'66'"))
                .addBatch();

        assertEquals(2, insert.execute());

        assertEquals(1l, query().from(survey).where(survey.name.eq("55")).count());
        assertEquals(1l, query().from(survey).where(survey.name.eq("66")).count());
    }

    @Test
    public void Insert_Batch2() {
        SQLInsertClause insert = insert(survey)
                .set(survey.id, 5)
                .set(survey.name, "55")
                .addBatch();

        insert.set(survey.id, 6)
                .setNull(survey.name)
                .addBatch();

        assertEquals(2, insert.execute());
    }

    @Test
    public void Insert_Null_With_Columns() {
        assertEquals(1, insert(survey)
                .columns(survey.id, survey.name)
                .values(3, null).execute());
    }

    @Test
    @ExcludeIn({DB2, DERBY})
    public void Insert_Null_Without_Columns() {
        assertEquals(1, insert(survey)
                .values(4, null, null).execute());
    }

    @Test
    @ExcludeIn({FIREBIRD, HSQLDB, DB2, DERBY, ORACLE})
    public void Insert_Without_Values() {
        assertEquals(1, insert(survey).execute());
    }

    @Test
    @ExcludeIn(ORACLE)
    public void Insert_Nulls_In_Batch() {
//        QFoo f= QFoo.foo;
//        SQLInsertClause sic = new SQLInsertClause(c, new H2Templates(), f);
//        sic.columns(f.c1,f.c2).values(null,null).addBatch();
//        sic.columns(f.c1,f.c2).values(null,1).addBatch();
//        sic.execute();
        SQLInsertClause sic = insert(survey);
        sic.columns(survey.name, survey.name2).values(null, null).addBatch();
        sic.columns(survey.name, survey.name2).values(null, "X").addBatch();
        sic.execute();
    }

    @Test
    @Ignore
    @ExcludeIn({DERBY})
    public void Insert_Nulls_In_Batch2() {
        Mapper<Object> mapper = DefaultMapper.WITH_NULL_BINDINGS;
//        QFoo f= QFoo.foo;
//        SQLInsertClause sic = new SQLInsertClause(c, new H2Templates(), f);
//        Foo f1=new Foo();
//        sic.populate(f1).addBatch();
//        f1=new Foo();
//        f1.setC1(1);
//        sic.populate(f1).addBatch();
//        sic.execute();
        QEmployee employee = QEmployee.employee;
        SQLInsertClause sic = insert(employee);
        Employee e = new Employee();
        sic.populate(e, mapper).addBatch();
        e = new Employee();
        e.setFirstname("X");
        sic.populate(e, mapper).addBatch();
        sic.execute();

    }

    @Test
    public void Insert_With_Columns() {
        assertEquals(1, insert(survey)
                .columns(survey.id, survey.name)
                .values(3, "Hello").execute());
    }

    @Test
    @ExcludeIn({CUBRID, SQLSERVER})
    public void Insert_With_Keys() throws SQLException{
        ResultSet rs = insert(survey).set(survey.name, "Hello World").executeWithKeys();
        assertTrue(rs.next());
        assertTrue(rs.getObject(1) != null);
        rs.close();
    }

    @Test
    @ExcludeIn({CUBRID, SQLSERVER})
    public void Insert_With_Keys_Projected() throws SQLException{
        assertNotNull(insert(survey).set(survey.name, "Hello you").executeWithKey(survey.id));
    }

    @Test
    @ExcludeIn({CUBRID, SQLSERVER})
    public void Insert_With_Keys_Projected2() throws SQLException{
        Path<Object> idPath = new PathImpl<Object>(Object.class, "id");
        Object id = insert(survey).set(survey.name, "Hello you").executeWithKey(idPath);
        assertNotNull(id);
    }

 // http://sourceforge.net/tracker/index.php?func=detail&aid=3513432&group_id=280608&atid=2377440

    @Test
    public void Insert_With_Set() {
        assertEquals(1, insert(survey)
                .set(survey.id, 5)
                .set(survey.name, (String)null)
                .execute());
    }

    @Test
    @IncludeIn(MYSQL)
    @SkipForQuoted
    public void Insert_with_Special_Options() {
        SQLInsertClause clause = insert(survey)
            .columns(survey.id, survey.name)
            .values(3, "Hello");

        clause.addFlag(Position.START_OVERRIDE, "insert ignore into ");

        assertEquals("insert ignore into SURVEY (ID, NAME) values (?, ?)", clause.toString());
        clause.execute();
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void Insert_With_SubQuery() {
        int count = (int)query().from(survey).count();
        assertEquals(count, insert(survey)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(20), survey2.name))
            .execute());
    }

    @Test
    @ExcludeIn({HSQLDB, CUBRID, DERBY, FIREBIRD})
    public void Insert_With_SubQuery2() {
//        insert into modules(name)
//        select 'MyModule'
//        where not exists
//        (select 1 from modules where modules.name = 'MyModule')

        assertEquals(1, insert(survey).set(survey.name,
            sq().where(sq().from(survey2)
                           .where(survey2.name.eq("MyModule")).notExists())
                .unique(Expressions.constant("MyModule")))
            .execute());

        assertEquals(1l , query().from(survey).where(survey.name.eq("MyModule")).count());
    }

    @Test
    @ExcludeIn({HSQLDB, CUBRID, DERBY})
    public void Insert_With_SubQuery3() {
//        insert into modules(name)
//        select 'MyModule'
//        where not exists
//        (select 1 from modules where modules.name = 'MyModule')

        assertEquals(1, insert(survey).columns(survey.name).select(
            sq().where(sq().from(survey2)
                           .where(survey2.name.eq("MyModule2")).notExists())
                .unique(Expressions.constant("MyModule2")))
            .execute());

        assertEquals(1l , query().from(survey).where(survey.name.eq("MyModule2")).count());
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void Insert_With_SubQuery_Params() {
        Param<Integer> param = new Param<Integer>(Integer.class, "param");
        SQLSubQuery sq = sq().from(survey2);
        sq.set(param, 20);

        int count = (int)query().from(survey).count();
        assertEquals(count, insert(survey)
            .columns(survey.id, survey.name)
            .select(sq.list(survey2.id.add(param), survey2.name))
            .execute());
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void Insert_With_SubQuery_Via_Constructor() {
        int count = (int)query().from(survey).count();
        SQLInsertClause insert = insert(survey, sq().from(survey2));
        insert.set(survey.id, survey2.id.add(20));
        insert.set(survey.name, survey2.name);
        assertEquals(count, insert.execute());
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void Insert_With_SubQuery_Without_Columns() {
        int count = (int)query().from(survey).count();
        assertEquals(count, insert(survey)
            .select(sq().from(survey2).list(survey2.id.add(10), survey2.name, survey2.name2))
            .execute());

    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void Insert_Without_Columns() {
        assertEquals(1, insert(survey).values(4, "Hello", "World").execute());

    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void InsertBatch_with_Subquery() {
        SQLInsertClause insert = insert(survey)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(20), survey2.name))
            .addBatch();

        insert(survey)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(40), survey2.name))
            .addBatch();

        insert.execute();
//        assertEquals(1, insert.execute());
    }

    @Test
    public void Like() {
        insert(survey).values(11, "Hello World", "a\\b").execute();
        assertEquals(1l, query().from(survey).where(survey.name2.contains("a\\b")).count());
    }

    @Test
    public void Like_with_Escape() {
        SQLInsertClause insert = insert(survey);
        insert.set(survey.id, 5).set(survey.name, "aaa").addBatch();
        insert.set(survey.id, 6).set(survey.name, "a_").addBatch();
        insert.set(survey.id, 7).set(survey.name, "a%").addBatch();
        assertEquals(3, insert.execute());

        assertEquals(1l, query().from(survey).where(survey.name.like("a|%", '|')).count());
        assertEquals(1l, query().from(survey).where(survey.name.like("a|_", '|')).count());
        assertEquals(3l, query().from(survey).where(survey.name.like("a%")).count());
        assertEquals(2l, query().from(survey).where(survey.name.like("a_")).count());

        assertEquals(1l, query().from(survey).where(survey.name.startsWith("a_")).count());
        assertEquals(1l, query().from(survey).where(survey.name.startsWith("a%")).count());
    }

    @Test
    @IncludeIn(MYSQL)
    @SkipForQuoted
    public void Replace() {
        SQLInsertClause clause = mysqlReplace(survey);
        clause.columns(survey.id, survey.name)
            .values(3, "Hello");

        assertEquals("replace into SURVEY (ID, NAME) values (?, ?)", clause.toString());
        clause.execute();
    }

    @Test
    public void Insert_With_TempateExpression_In_Batch() {
        insert(survey)
                .set(survey.id, 3)
                .set(survey.name, Expressions.stringTemplate("'Hello'"))
                .addBatch();
    }

    @Test
    @IncludeIn({H2, POSTGRES})
    @SkipForQuoted
    public void Uuids() {
        delete(QUuids.uuids).execute();
        QUuids uuids = QUuids.uuids;
        UUID uuid = UUID.randomUUID();
        insert(uuids).set(uuids.field, uuid).execute();
        assertEquals(uuid, query().from(uuids).singleResult(uuids.field));
    }

    @Test
    public void XML() {
        delete(QXmlTest.xmlTest).execute();
        QXmlTest xmlTest = QXmlTest.xmlTest;
        String contents = "<html><head>a</head><body>b</body></html>";
        insert(xmlTest).set(xmlTest.col, contents).execute();
        assertEquals(contents, query().from(xmlTest).singleResult(xmlTest.col));
    }

}
