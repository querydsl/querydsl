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

import com.querydsl.core.QueryException;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.Tuple;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Param;
import com.querydsl.r2dbc.dml.DefaultMapper;
import com.querydsl.r2dbc.dml.Mapper;
import com.querydsl.r2dbc.dml.R2DBCInsertClause;
import com.querydsl.r2dbc.domain.*;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.SQLExpressions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.querydsl.core.Target.*;
import static com.querydsl.r2dbc.Constants.survey;
import static com.querydsl.r2dbc.Constants.survey2;
import static org.junit.Assert.*;

public class InsertBase extends AbstractBaseTest {

    private void reset() {
        delete(survey).execute();
        insert(survey).values(1, "Hello World", "Hello").execute();

        delete(QDateTest.qDateTest).execute();
    }

    @Before
    public void setUp() {
        reset();
    }

    @After
    public void tearDown() {
        reset();
    }

    @Test
    @ExcludeIn({CUBRID, SQLITE})
    // https://bitbucket.org/xerial/sqlite-jdbc/issue/133/prepstmtsetdate-int-date-calendar-seems
    public void insert_dates() {
        QDateTest dateTest = QDateTest.qDateTest;
        LocalDate localDate = LocalDate.of(1978, 1, 2);

        Path<LocalDate> localDateProperty = ExpressionUtils.path(LocalDate.class, "DATE_TEST");
        Path<LocalDateTime> dateTimeProperty = ExpressionUtils.path(LocalDateTime.class, "DATE_TEST");
        R2DBCInsertClause insert = insert(dateTest);
        insert.set(localDateProperty, localDate);
        insert.execute();

        Tuple result = query().from(dateTest).select(
                dateTest.dateTest.year(),
                dateTest.dateTest.month(),
                dateTest.dateTest.dayOfMonth(),
                dateTimeProperty
        ).fetchFirst().block();
        assertEquals(Integer.valueOf(1978), result.get(0, Integer.class));
        assertEquals(Integer.valueOf(1), result.get(1, Integer.class));
        assertEquals(Integer.valueOf(2), result.get(2, Integer.class));

        LocalDateTime dateTime = result.get(dateTimeProperty);
        assertEquals(localDate.atStartOfDay(), dateTime);
    }

    @Test
    public void complex1() {
        // related to #584795
        QSurvey survey = new QSurvey("survey");
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        R2DBCInsertClause insert = insert(survey);
        insert.columns(survey.id, survey.name);
        insert.select(SQLExpressions.select(survey.id, emp2.firstname).from(survey)
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
        R2DBCInsertClause insert = insert(survey)
                .set(survey.id, 5)
                .set(survey.name, "55")
                .addBatch();

        assertEquals(1, insert.getBatchCount());

        insert.set(survey.id, 6)
                .set(survey.name, "66")
                .addBatch();

        assertEquals(2, insert.getBatchCount());
        assertEquals(2, insert.execute());

        assertEquals(1L, (long) query().from(survey).where(survey.name.eq("55")).fetchCount().block());
        assertEquals(1L, (long) query().from(survey).where(survey.name.eq("66")).fetchCount().block());
    }

    @Test
    public void insert_batch_to_bulk() {
        R2DBCInsertClause insert = insert(survey);
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

        assertEquals(1L, (long) query().from(survey).where(survey.name.eq("55")).fetchCount().block());
        assertEquals(1L, (long) query().from(survey).where(survey.name.eq("66")).fetchCount().block());
    }

    @Test
    public void insert_batch_Templates() {
        R2DBCInsertClause insert = insert(survey)
                .set(survey.id, 5)
                .set(survey.name, Expressions.stringTemplate("'55'"))
                .addBatch();

        insert.set(survey.id, 6)
                .set(survey.name, Expressions.stringTemplate("'66'"))
                .addBatch();

        assertEquals(2, insert.execute());

        assertEquals(1L, (long) query().from(survey).where(survey.name.eq("55")).fetchCount().block());
        assertEquals(1L, (long) query().from(survey).where(survey.name.eq("66")).fetchCount().block());
    }

    @Test
    public void insert_batch2() {
        R2DBCInsertClause insert = insert(survey)
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
//        R2DBCInsertClause sic = new R2DBCInsertClause(c, new H2Templates(), f);
//        sic.columns(f.c1,f.c2).values(null,null).addBatch();
//        sic.columns(f.c1,f.c2).values(null,1).addBatch();
//        sic.execute();
        R2DBCInsertClause sic = insert(survey);
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
//        R2DBCInsertClause sic = new R2DBCInsertClause(c, new H2Templates(), f);
//        Foo f1=new Foo();
//        sic.populate(f1).addBatch();
//        f1=new Foo();
//        f1.setC1(1);
//        sic.populate(f1).addBatch();
//        sic.execute();
        QEmployee employee = QEmployee.employee;
        R2DBCInsertClause sic = insert(employee);
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
    public void insert_with_keys() {
        Integer key = insert(survey).set(survey.name, "Hello World").executeWithKeys(survey.id).blockFirst();
        assertTrue(key != null);
    }

    @Test
    @ExcludeIn({CUBRID, SQLSERVER})
    public void insert_with_keys_listener() {
        R2DBCInsertClause clause = insert(survey).set(survey.name, "Hello World");
        Integer key = clause.executeWithKeys(survey.id).blockFirst();
        assertTrue(key != null);
    }

    @Test
    @ExcludeIn({CUBRID, SQLSERVER})
    public void insert_with_keys_Projected() {
        assertNotNull(insert(survey).set(survey.name, "Hello you").executeWithKey(survey.id));
    }

    @Test
    @ExcludeIn({CUBRID, SQLSERVER})
    public void insert_with_keys_Projected2() {
        Path<Object> idPath = ExpressionUtils.path(Object.class, "id");
        Object id = insert(survey).set(survey.name, "Hello you").executeWithKey(idPath);
        assertNotNull(id);
    }

    @Test(expected = QueryException.class)
    @IncludeIn({DERBY, HSQLDB})
    public void insert_with_keys_OverriddenColumn() {
        String originalColumnName = ColumnMetadata.getName(survey.id);
        try {
            configuration.registerColumnOverride(survey.getSchemaName(), survey.getTableName(),
                    originalColumnName, "wrongColumnName");

            R2DBCInsertClause insert = new R2DBCInsertClause(connection, configuration, survey);
            Object id = insert.set(survey.name, "Hello you").executeWithKey(survey.id);
            assertNotNull(id);
        } finally {
            configuration.registerColumnOverride(survey.getSchemaName(), survey.getTableName(),
                    originalColumnName, originalColumnName);
        }
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
        R2DBCInsertClause clause = insert(survey)
                .columns(survey.id, survey.name)
                .values(3, "Hello");

        clause.addFlag(Position.START_OVERRIDE, "insert ignore into ");

        assertEquals("insert ignore into SURVEY (ID, NAME) values (?, ?)", clause.toString());
        assertEquals(1, clause.execute());
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void insert_with_subQuery() {
        Long count = query().from(survey).fetchCount().block();
        assertEquals(count, insert(survey)
                .columns(survey.id, survey.name)
                .select(query().from(survey2).select(survey2.id.add(20), survey2.name))
                .execute());
    }

    @Test
    @ExcludeIn({DB2, HSQLDB, CUBRID, DERBY, FIREBIRD})
    public void insert_with_subQuery2() {
//        insert into modules(name)
//        select 'MyModule'
//        where not exists
//        (select 1 from modules where modules.name = 'MyModule')

        R2DBCQuery<String> select = query().where(query().from(survey2)
                .where(survey2.name.eq("MyModule")).notExists())
                .select(Expressions.constant("MyModule"));
        assertEquals(1, (long) insert(survey).set(survey.name, select.fetchFirst().block()).execute().block());

        assertEquals(1L, (long) query().from(survey).where(survey.name.eq("MyModule")).fetchCount().block());
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

        assertEquals(1L, (long) query().from(survey).where(survey.name.eq("MyModule2")).fetchCount().block());
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void insert_with_subQuery_Params() {
        Param<Integer> param = new Param<Integer>(Integer.class, "param");
        R2DBCQuery<?> sq = query().from(survey2);
        sq.set(param, 20);

        long count = query().from(survey).fetchCount().block();
        assertEquals(count, insert(survey)
                .columns(survey.id, survey.name)
                .select(sq.select(survey2.id.add(param), survey2.name))
                .execute());
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void insert_with_subQuery_Via_Constructor() {
        long count = query().from(survey).fetchCount().block();
        R2DBCInsertClause insert = insert(survey, query().from(survey2));
        insert.set(survey.id, survey2.id.add(20));
        insert.set(survey.name, survey2.name);
        assertEquals(count, insert.execute());
    }

    @Test
    @ExcludeIn(FIREBIRD) // too slow
    public void insert_with_subQuery_Without_Columns() {
        long count = query().from(survey).fetchCount().block();
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
        R2DBCInsertClause insert = insert(survey)
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
        assertEquals(1L, (long) query().from(survey).where(survey.name2.contains("a\\b")).fetchCount().block());
    }

    @Test
    public void like_with_escape() {
        R2DBCInsertClause insert = insert(survey);
        insert.set(survey.id, 5).set(survey.name, "aaa").addBatch();
        insert.set(survey.id, 6).set(survey.name, "a_").addBatch();
        insert.set(survey.id, 7).set(survey.name, "a%").addBatch();
        assertEquals(3, insert.execute());

        assertEquals(1L, (long) query().from(survey).where(survey.name.like("a|%", '|')).fetchCount().block());
        assertEquals(1L, (long) query().from(survey).where(survey.name.like("a|_", '|')).fetchCount().block());
        assertEquals(3L, (long) query().from(survey).where(survey.name.like("a%")).fetchCount().block());
        assertEquals(2L, (long) query().from(survey).where(survey.name.like("a_")).fetchCount().block());

        assertEquals(1L, (long) query().from(survey).where(survey.name.startsWith("a_")).fetchCount().block());
        assertEquals(1L, (long) query().from(survey).where(survey.name.startsWith("a%")).fetchCount().block());
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
