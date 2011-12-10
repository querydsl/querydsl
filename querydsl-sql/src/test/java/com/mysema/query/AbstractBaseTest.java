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

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.annotation.Nullable;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;

import com.mysema.query.sql.AbstractSQLSubQuery;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLMergeClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.LabelRule;
import com.mysema.testutil.SkipForAnnotationRule;

@RunWith(FilteringTestRunner.class)
public abstract class AbstractBaseTest {

    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
        Connections.close();
    }

    protected SQLTemplates templates;

    @Nullable
    protected String expectedQuery;
    
    @Rule
    public static MethodRule skipForQuoted = new SkipForAnnotationRule(SkipForQuoted.class, SkipForQuoted.class);
    
    @Rule
    public static MethodRule labelRule = new LabelRule();
    
    protected SQLUpdateClause update(RelationalPath<?> e){
        return new SQLUpdateClause(Connections.getConnection(), templates, e);
    }

    protected SQLInsertClause insert(RelationalPath<?> e){
        return new SQLInsertClause(Connections.getConnection(), templates, e);
    }

    protected SQLInsertClause insert(RelationalPath<?> e, AbstractSQLSubQuery<?> sq) {
        return new SQLInsertClause(Connections.getConnection(), templates, e, sq);
    }
    
    protected SQLDeleteClause delete(RelationalPath<?> e){
        return new SQLDeleteClause(Connections.getConnection(), templates, e);
    }

    protected SQLMergeClause merge(RelationalPath<?> e){
        return new SQLMergeClause(Connections.getConnection(), templates, e);
    }

    protected SQLQuery query() {
        return new SQLQueryImpl(Connections.getConnection(), templates) {
            @Override
            protected String buildQueryString(boolean countRow) {
                String rv = super.buildQueryString(countRow);
                if (expectedQuery != null) {
                    assertEquals(expectedQuery, rv.replace('\n', ' '));
                    expectedQuery = null;
                }
                System.out.println(rv);
                return rv;
            }
        };
    }

    protected SQLSubQuery sq(){
        return new SQLSubQuery();
    }

}
