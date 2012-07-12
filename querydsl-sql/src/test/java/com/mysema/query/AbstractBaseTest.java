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

import java.sql.Connection;

import javax.annotation.Nullable;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.rules.MethodRule;

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
import com.mysema.query.sql.mysql.MySQLQuery;
import com.mysema.query.sql.mysql.MySQLReplaceClause;
import com.mysema.query.sql.oracle.OracleQuery;

public abstract class AbstractBaseTest {

    private Connection connection = Connections.getConnection();
    
    private SQLTemplates templates = Connections.getTemplates();

    @Nullable
    protected String expectedQuery;
    
    @Rule
    public static MethodRule skipForQuotedRule = new SkipForQuotedRule();
    
    @Rule
    public static MethodRule targetRule = new TargetRule();
    
    protected SQLUpdateClause update(RelationalPath<?> e){
        return new SQLUpdateClause(connection, templates, e);
    }

    protected SQLInsertClause insert(RelationalPath<?> e){
        return new SQLInsertClause(connection, templates, e);
    }

    protected SQLInsertClause insert(RelationalPath<?> e, AbstractSQLSubQuery<?> sq) {
        return new SQLInsertClause(connection, templates, e, sq);
    }
    
    protected SQLDeleteClause delete(RelationalPath<?> e){
        return new SQLDeleteClause(connection, templates, e);
    }

    protected SQLMergeClause merge(RelationalPath<?> e){
        return new SQLMergeClause(connection, templates, e);
    }
    
    protected ExtendedSQLQuery extQuery() {
        return new ExtendedSQLQuery(connection, templates);
    }

    protected MySQLQuery mysqlQuery(){
        return new MySQLQuery(connection, templates);
    }
    
    protected SQLInsertClause mysqlReplace(RelationalPath<?> path) {
        return new MySQLReplaceClause(connection, templates, path);
    }
    
    protected SQLQuery query() {
        return new SQLQueryImpl(connection, templates) {
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
    
    protected OracleQuery oracleQuery(){
        return new OracleQuery(connection, templates){
            @Override
            protected String buildQueryString(boolean forCountRow) {
                String rv = super.buildQueryString(forCountRow);
                if (expectedQuery != null){
                   Assert.assertEquals(expectedQuery, rv.replace('\n', ' '));
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
