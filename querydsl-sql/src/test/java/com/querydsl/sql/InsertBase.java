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

import static com.querydsl.core.Target.*;
import static com.querydsl.sql.Constants.survey;
import static com.querydsl.sql.Constants.survey2;
import static com.querydsl.sql.SQLExpressions.select;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.Tuple;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Param;
import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.Mapper;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.domain.*;

public class InsertBase extends AbstractBaseTest {

    private void reset() throws SQLException {
        delete(survey).execute();
        insert(survey).values(1, "Hello World", "Hello").execute();

        delete(QDateTest.qDateTest).execute();
    }

    @Before
    public void setUp() throws SQLException {
        reset();
    }

    @After
    public void tearDown() throws SQLException {
        reset();
    }

    @Test
    @ExcludeIn(SQLITE) // https://bitbucket.org/xerial/sqlite-jdbc/issue/133/prepstmtsetdate-int-date-calendar-seems
    public void insert_dates() {
        QDateTest dateTest = QDateTest.qDateTest;
        LocalDate localDate = new LocalDate(1978, 1, 2);

        Path<LocalDate> localDateProperty = ExpressionUtils.path(LocalDate.class, "DATE_TEST");
        Path<DateTime> dateTimeProperty = ExpressionUtils.path(DateTime.class, "DATE_TEST");
        SQLInsertClause insert = insert(dateTest);
        insert.set(localDateProperty, localDate);
        insert.execute();

        Tuple result = query().from(dateTest).select(
                dateTest.dateTest.year(),
                dateTest.dateTest.month(),
                dateTest.dateTest.dayOfMonth(),
                dateTimeProperty).fetchFirst();
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
    public void complex1() {
        // related to #584795
        QSurvey survey = new QSurvey("survey");
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLInsertClause insert = insert(survey);
        insert.columns(survey.id, survey.name);
        insert.select(select(survey.id, emp2.firstname).from(survey)
          .innerJoin(emp1)
           .on(survey.id.eq(emp1.id))
          .innerJoin(emp2)
           .on(emp1.superiorId.eq(emp2.superiorId), emp1.firstname.eq(emp2.firstname)));

        assertEquals(0, insert.execute());
    }

    @Test
    public void insert_alternative_syntax() {
        // with columns
        assertEquals(1, insert(survey)
            .set(survey.id, 3)
            .set(survey.name, "Hello")
            .execute());
    }

    @Test
    public void insert_batch() {
        SQLInsertClause insert = insert(survey)
                .set(survey.id, 5)
                .set(survey.name, "55")
                .addBatch();

        assertEquals(1, insert.getBatchCount());

        insert.set(survey.id, 6)
                .set(survey.name, "66")
                .addBatch();

        assertEquals(2, insert.getBatchCount());
        assertEquals(2, insert.execute());

        assertEquals(1L, query().from(survey).where(survey.name.eq("55")).fetchCount());
        assertEquals(1L, query().from(survey).where(survey.name.eq("66")).fetchCount());
    }

    @Test
    public void insert_batch_to_bulk() {
        SQLInsertClause insert = insert(survey);
        insert.setBatchToBulk(true);

        insert.set(survey.id, 5)
                .set(survey.name, "55")
                .addBatch();

        assertEquals(1, insert.getBatchCount());

        insert.set(survey.id, 6)
                .set(survey.name, "66")
                .addBatch();

        assertEquals(2, insert.getBatchCount());
        assertEquals(2, insert.execute());

        assertEquals(1L, query().from(survey).where(survey.name.eq("55")).fetchCount());
        assertEquals(1L, query().from(survey).where(survey.name.eq("66")).fetchCount());
    }

    @Test
    public void insert_batch_Templates() {
        SQLInsertClause insert = insert(survey)
                .set(survey.id, 5)
                .set(survey.name, Expressions.stringTemplate("'55'"))
                .addBatch();

        insert.set(survey.id, 6)
                .set(survey.name, Expressions.stringTemplate("'66'"))
                .addBatch();

        assertEquals(2, insert.execute());

        assertEquals(1L, query().from(survey).where(survey.name.eq("55")).fetchCount());
        assertEquals(1L, query().from(survey).where(survey.name.eq("66")).fetchCount());
    }

    @Test
    public void insert_batch2() {
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
    public void insert_null_with_columns() {
        assertEquals(1, insert(survey)
                .columns(survey.id, survey.name)
                .values(3, null).execute());
    }

    @Test
    @ExcludeIn({DB2, DERBY})
    public void insert_null_without_columns() {
        assertEquals(1, insert(survey)
                .values(4, null, null).execute());
    }

    @Test
    @ExcludeIn({FIREBIRD, HSQLDB, DB2, DERBY, ORACLE})
    public void insert_without_values() {
        assertEquals(1, insert(survey).execute());
    }

    @Test
    @ExcludeIn(ORACLE)
    public void insert_nulls_in_batch() {
//        QFoo f= QFoo.foo;
//        SQLInsertClause sic = new SQLInsertClause(c, new H2Templates(), f);
//        sic.columns(f.c1,f.c2).values(null,null).addBatch();
//        sic.columns(f.c1,f.c2).values(null,1).addBatch();
//        sic.execute();
        SQLInsertClause sic = insert(survey);
        sic.columns(survey.name, survey.name2).values(null, null).addBatch();
        sic.columns(survey.name, survey.name2).values(null, "X").addBatch();
        assertEquals(2, sic.execute());
    }

    @Test
    @Ignore
    @ExcludeIn({DERBY})
    public void insert_nulls_in_batch2() {
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
        assertEquals(0, sic.execute());

    }

    @Test
    public void insert_with_columns() {
        assertEquals(1, insert(survey)
                .columns(survey.id, survey.name)
                .values(3, "Hello").execute());
    }

    @Test
    @ExcludeIn({CUBRID, SQLSERVER})
    public void insert_with_keys() throws SQLException {
        ResultSet rs = insert(survey).set(survey.name, "Hello World").executeWithKeys();
        assertTrue(rs.next());
        assertTrue(rs.getObject(1) != null);
        rs.close();
    }

    @Test
    @ExcludeIn({CUBRID, SQLSERVER})
    public void insert_with_keys_listener() throws SQLException {
        final AtomicBoolean result = new AtomicBoolean();
        SQLListener listener = new SQLBaseListener() {
            @Override
            public void end(SQLListenerContext context) {
                result.set(true);
            }
        };
        SQLInsertClause clause = insert(survey).set(survey.name, "Hello World");
        clause.addListener(listener);
        ResultSet rs = clause.executeWithKeys();
        assertFalse(result.get());
        assertTrue(rs.next());
        assertTrue(rs.getObject(1) != null);
        rs.close();
        assertTrue(result.get());
    }

    @Test
    @ExcludeIn({CUBRID, SQLSERVER})
    public void insert_with_keys_Projected() throws SQLException {
        assertNotNull(insert(survey).set(survey.name, "Hello you").executeWithKey(survey.id));
    }

    @Test
    @ExcludeIn({CUBRID, SQLSERVER})
    public void insert_with_keys_Projected2() throws SQLException {
        Path<Object> idPath = ExpressionUtils.path(Object.class, "id");
        Object id = insert(survey).set(survey.name, "Hello you").executeWithKey(idPath);
        assertNotNull(id);
    }

 // http://sourceforge.net/tracker/index.php?func=detail&aid=3513432&group_id=280608&atid=2377440

    @Test
    public void insert_with_set() {
        assertEquals(1, insert(survey)
                .set(survey.id, 5)
                .set(survey.name, (String) null)
                .execute());
    }

    @Test
    @IncludeIn(MYSQL)
    @SkipForQuoted
    public void insert_with_special_options() {
        SQLInsertClause clause = insert(survey)
            .columns(survey.id, survey.name)
            .values(3, "Hello");

        clause.addFlag(Position.START_OVERRIDE, "insert ignore into ");

        assertEquals("insert ignore into SURVEY (ID, NAME) values (?, ?)", clause.toString());
        assertEquals(1, clause.execute());
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void insert_with_subQuery() {
        int count = (int) query().from(survey).fetchCount();
        assertEquals(count, insert(survey)
            .columns(survey.id, survey.name)
            .select(query().from(survey2).select(survey2.id.add(20), survey2.name))
            .execute());
    }

    @Test
    @ExcludeIn({HSQLDB, CUBRID, DERBY, FIREBIRD})
    public void insert_with_subQuery2() {
//        insert into modules(name)
//        select 'MyModule'
//        where not exists
//        (select 1 from modules where modules.name = 'MyModule')

        assertEquals(1, insert(survey).set(survey.name,
            query().where(query().from(survey2)
                           .where(survey2.name.eq("MyModule")).notExists())
                .select(Expressions.constant("MyModule")).fetchFirst())
            .execute());

        assertEquals(1L , query().from(survey).where(survey.name.eq("MyModule")).fetchCount());
    }

    @Test
    @ExcludeIn({HSQLDB, CUBRID, DERBY})
    public void insert_with_subQuery3() {
//        insert into modules(name)
//        select 'MyModule'
//        where not exists
//        (select 1 from modules where modules.name = 'MyModule')

        assertEquals(1, insert(survey).columns(survey.name).select(
            query().where(query().from(survey2)
                           .where(survey2.name.eq("MyModule2")).notExists())
                .select(Expressions.constant("MyModule2")))
            .execute());

        assertEquals(1L , query().from(survey).where(survey.name.eq("MyModule2")).fetchCount());
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void insert_with_subQuery_Params() {
        Param<Integer> param = new Param<Integer>(Integer.class, "param");
        SQLQuery<?> sq = query().from(survey2);
        sq.set(param, 20);

        int count = (int) query().from(survey).fetchCount();
        assertEquals(count, insert(survey)
            .columns(survey.id, survey.name)
            .select(sq.select(survey2.id.add(param), survey2.name))
            .execute());
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void insert_with_subQuery_Via_Constructor() {
        int count = (int) query().from(survey).fetchCount();
        SQLInsertClause insert = insert(survey, query().from(survey2));
        insert.set(survey.id, survey2.id.add(20));
        insert.set(survey.name, survey2.name);
        assertEquals(count, insert.execute());
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void insert_with_subQuery_Without_Columns() {
        int count = (int) query().from(survey).fetchCount();
        assertEquals(count, insert(survey)
            .select(query().from(survey2).select(survey2.id.add(10), survey2.name, survey2.name2))
            .execute());

    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void insert_without_columns() {
        assertEquals(1, insert(survey).values(4, "Hello", "World").execute());

    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void insertBatch_with_subquery() {
        SQLInsertClause insert = insert(survey)
            .columns(survey.id, survey.name)
            .select(query().from(survey2).select(survey2.id.add(20), survey2.name))
            .addBatch();

        insert(survey)
            .columns(survey.id, survey.name)
            .select(query().from(survey2).select(survey2.id.add(40), survey2.name))
            .addBatch();

        assertEquals(1, insert.execute());
    }

    @Test
    public void like() {
        insert(survey).values(11, "Hello World", "a\\b").execute();
        assertEquals(1L, query().from(survey).where(survey.name2.contains("a\\b")).fetchCount());
    }

    @Test
    public void like_with_escape() {
        SQLInsertClause insert = insert(survey);
        insert.set(survey.id, 5).set(survey.name, "aaa").addBatch();
        insert.set(survey.id, 6).set(survey.name, "a_").addBatch();
        insert.set(survey.id, 7).set(survey.name, "a%").addBatch();
        assertEquals(3, insert.execute());

        assertEquals(1L, query().from(survey).where(survey.name.like("a|%", '|')).fetchCount());
        assertEquals(1L, query().from(survey).where(survey.name.like("a|_", '|')).fetchCount());
        assertEquals(3L, query().from(survey).where(survey.name.like("a%")).fetchCount());
        assertEquals(2L, query().from(survey).where(survey.name.like("a_")).fetchCount());

        assertEquals(1L, query().from(survey).where(survey.name.startsWith("a_")).fetchCount());
        assertEquals(1L, query().from(survey).where(survey.name.startsWith("a%")).fetchCount());
    }

    @Test
    @IncludeIn(MYSQL)
    @SkipForQuoted
    public void replace() {
        SQLInsertClause clause = mysqlReplace(survey);
        clause.columns(survey.id, survey.name)
            .values(3, "Hello");

        assertEquals("replace into SURVEY (ID, NAME) values (?, ?)", clause.toString());
        clause.execute();
    }

    @Test
    public void insert_with_tempateExpression_in_batch() {
        assertEquals(1, insert(survey)
                .set(survey.id, 3)
                .set(survey.name, Expressions.stringTemplate("'Hello'"))
                .addBatch()
                .execute());
    }

    @Test
    @IncludeIn({H2, POSTGRESQL})
    @SkipForQuoted
    public void uuids() {
        delete(QUuids.uuids).execute();
        QUuids uuids = QUuids.uuids;
        UUID uuid = UUID.randomUUID();
        insert(uuids).set(uuids.field, uuid).execute();
        assertEquals(uuid, query().from(uuids).select(uuids.field).fetchFirst());
    }

    @Test
    @ExcludeIn({ORACLE})
    public void xml() {
        delete(QXmlTest.xmlTest).execute();
        QXmlTest xmlTest = QXmlTest.xmlTest;
        String contents = "<html><head>a</head><body>b</body></html>";
        insert(xmlTest).set(xmlTest.col, contents).execute();
        assertEquals(contents, query().from(xmlTest).select(xmlTest.col).fetchFirst());
    }

}
