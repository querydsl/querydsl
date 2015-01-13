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
package com.querydsl.sql.mysql;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.domain.QSurvey;

public class MySQLQueryTest {
    private MySQLQuery query;

    private QSurvey survey = new QSurvey("survey");

    @Before
    public void setUp() {
        query = new MySQLQuery(null, new MySQLTemplates() {{
            newLineToSingleSpace();
        }});
        query.from(survey);
        query.orderBy(survey.name.asc());
        query.getMetadata().addProjection(survey.name);
    }

    @Test
    public void Syntax() {
//        SELECT
//        [ALL | DISTINCT | DISTINCTROW ]
//          [HIGH_PRIORITY]
        query.highPriority();
//          [STRAIGHT_JOIN]
        query.straightJoin();
//          [SQL_SMALL_RESULT] [SQL_BIG_RESULT] [SQL_BUFFER_RESULT]
        query.smallResult();
        query.bigResult();
        query.bufferResult();
//          [SQL_CACHE | SQL_NO_CACHE] [SQL_CALC_FOUND_ROWS]
        query.cache();
        query.noCache();
        query.calcFoundRows();
//        select_expr [, select_expr ...]
//        [FROM table_references
        query.from(new QSurvey("survey2"));
//        [WHERE where_condition]
        query.where(survey.id.isNotNull());
//        [GROUP BY {col_name | expr | position}
        query.groupBy(survey.name);
//          [ASC | DESC], ... [WITH ROLLUP]]
        query.withRollup();
//        [HAVING where_condition]
        query.having(survey.name.isNull());
//        [ORDER BY {col_name | expr | position}
//          [ASC | DESC], ...]
        query.orderBy(survey.name.asc());
//        [LIMIT {[offset,] row_count | row_count OFFSET offset}]
        query.limit(2);
        query.offset(3);
//        [PROCEDURE procedure_name(argument_list)]
        // TODO
//        [INTO OUTFILE 'file_name' export_options
//          | INTO DUMPFILE 'file_name'
//          | INTO var_name [, var_name]]
//        [FOR UPDATE | LOCK IN SHARE MODE]]
        query.forUpdate();
        query.lockInShareMode();
    }

    @Test
    public void ForceIndex() {
        query = new MySQLQuery(null, new MySQLTemplates() {{
            newLineToSingleSpace();
        }});
        query.from(survey);
        query.forceIndex("col1_index");
        query.orderBy(survey.name.asc());
        query.getMetadata().addProjection(survey.name);

        assertEquals("select survey.NAME from SURVEY survey force index (col1_index) " +
                     "order by survey.NAME asc", toString(query));
    }

    @Test
    public void IgnoreIndex() {
        query = new MySQLQuery(null, new MySQLTemplates() {{
            newLineToSingleSpace();
        }});
        query.from(survey);
        query.ignoreIndex("col1_index");
        query.orderBy(survey.name.asc());
        query.getMetadata().addProjection(survey.name);

        assertEquals("select survey.NAME from SURVEY survey ignore index (col1_index) " +
                     "order by survey.NAME asc", toString(query));
    }

    @Test
    public void UseIndex() {
        query = new MySQLQuery(null, new MySQLTemplates() {{
            newLineToSingleSpace();
        }});
        query.from(survey);
        query.useIndex("col1_index");
        query.orderBy(survey.name.asc());
        query.getMetadata().addProjection(survey.name);

        assertEquals("select survey.NAME from SURVEY survey use index (col1_index) " +
                     "order by survey.NAME asc", toString(query));
    }

    @Test
    public void UseIndex2() {
        query = new MySQLQuery(null, new MySQLTemplates() {{
            newLineToSingleSpace();
        }});
        query.from(survey);
        query.useIndex("col1_index","col2_index");
        query.orderBy(survey.name.asc());
        query.getMetadata().addProjection(survey.name);

        assertEquals("select survey.NAME from SURVEY survey use index (col1_index, col2_index) " +
                     "order by survey.NAME asc", toString(query));
    }

    @Test
    public void HighPriority() {
        query.highPriority();
        assertEquals("select high_priority survey.NAME from SURVEY survey order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void StraightJoin() {
        query.straightJoin();
        assertEquals("select straight_join survey.NAME from SURVEY survey order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void SmallResult() {
        query.smallResult();
        assertEquals("select sql_small_result survey.NAME from SURVEY survey order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void BigResult() {
        query.bigResult();
        assertEquals("select sql_big_result survey.NAME from SURVEY survey order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void BufferResult() {
        query.bufferResult();
        assertEquals("select sql_buffer_result survey.NAME from SURVEY survey order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void Cache() {
        query.cache();
        assertEquals("select sql_cache survey.NAME from SURVEY survey order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void NoCache() {
        query.noCache();
        assertEquals("select sql_no_cache survey.NAME from SURVEY survey order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void CalcFoundRows() {
        query.calcFoundRows();
        assertEquals("select sql_calc_found_rows survey.NAME from SURVEY survey order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void WithRollup() {
        query.groupBy(survey.name);
        query.withRollup();
        assertEquals("select survey.NAME from SURVEY survey group by survey.NAME with rollup  order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void ForUpdate() {
        query.forUpdate();
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc for update",
                toString(query));
    }

    @Test
    public void ForUpdate_With_Limit() {
        query.forUpdate();
        query.limit(2);
        assertEquals("select survey.NAME from SURVEY survey order by survey.NAME asc limit ? for update",
                toString(query));
    }

    @Test
    public void IntoOutfile() {
        query.intoOutfile(new File("target/out"));
        assertEquals("select survey.NAME from SURVEY survey " +
                     "order by survey.NAME asc into outfile 'target" + File.separator + "out'", toString(query));
    }

    @Test
    public void IntoDumpfile() {
        query.intoDumpfile(new File("target/out"));
        assertEquals("select survey.NAME from SURVEY survey " +
                     "order by survey.NAME asc into dumpfile 'target" + File.separator + "out'", toString(query));
    }

    @Test
    public void IntoString() {
        query.into("var1");
        assertEquals("select survey.NAME from SURVEY survey " +
                     "order by survey.NAME asc into var1", toString(query));
    }

    @Test
    public void LockInShareMode() {
        query.lockInShareMode();
        assertEquals("select survey.NAME from SURVEY survey " +
                     "order by survey.NAME asc lock in share mode", toString(query));
    }

    private String toString(MySQLQuery query) {
        return query.toString().replace('\n', ' ');
    }

}
