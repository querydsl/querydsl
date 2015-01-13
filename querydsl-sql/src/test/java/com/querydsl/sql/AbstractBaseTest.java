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

import java.sql.Connection;
import java.util.List;

import javax.annotation.Nullable;

import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Target;
import com.querydsl.core.dml.DMLClause;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLMergeClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.mysql.MySQLReplaceClause;
import com.querydsl.sql.teradata.TeradataQuery;
import com.querydsl.sql.types.XMLAsStringType;

public abstract class AbstractBaseTest {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractBaseTest.class);

    protected final class TestQuery extends AbstractSQLQuery<TestQuery> implements SQLCommonQuery<TestQuery> {

        private TestQuery(Connection conn, Configuration configuration) {
            super(conn, configuration);
        }

        private TestQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
            super(conn, configuration, metadata);
        }

        @Override
        protected SQLSerializer serialize(boolean countRow) {
            SQLSerializer serializer = super.serialize(countRow);
            String rv = serializer.toString();
            if (expectedQuery != null) {
                assertEquals(expectedQuery, rv.replace('\n', ' '));
                expectedQuery = null;
            }
            logger.debug(rv);
            return serializer;
        }

        public TestQuery clone(Connection conn) {
            TestQuery q = new TestQuery(conn, getConfiguration(), getMetadata().clone());
            q.union = union;
            q.unionAll = unionAll;
            q.firstUnionSubQuery = firstUnionSubQuery;
            return q;
        }
    }

    protected Connection connection = Connections.getConnection();

    protected Target target = Connections.getTarget();

    protected Configuration configuration = Connections.getConfiguration();

    @Nullable
    protected String expectedQuery;

    public AbstractBaseTest() {
        // TODO enable registration of (jdbc type, java type) -> usertype mappings
        if (target == Target.POSTGRES || target == Target.ORACLE) {
            configuration.register("XML_TEST", "COL", new XMLAsStringType());
        }
    }

    @Rule
    public MethodRule skipForQuotedRule = new SkipForQuotedRule(configuration);

    @Rule
    public MethodRule targetRule = new TargetRule();

    protected <T> void add(List<T> list, T arg, Target... exclusions) {
        if (exclusions.length > 0) {
            for (Target t : exclusions) {
                if (t.equals(target)) {
                    return;
                }
            }
        }
        list.add(arg);
    }

    protected SQLUpdateClause update(RelationalPath<?> e) {
        SQLUpdateClause sqlUpdateClause = new SQLUpdateClause(connection, configuration, e);
        sqlUpdateClause.addListener(new TestLoggingListener());
        return sqlUpdateClause;
    }

    protected SQLInsertClause insert(RelationalPath<?> e) {
        SQLInsertClause sqlInsertClause = new SQLInsertClause(connection, configuration, e);
        sqlInsertClause.addListener(new TestLoggingListener());
        return sqlInsertClause;
    }

    protected SQLInsertClause insert(RelationalPath<?> e, AbstractSQLSubQuery<?> sq) {
        SQLInsertClause sqlInsertClause = new SQLInsertClause(connection, configuration, e, sq);
        sqlInsertClause.addListener(new TestLoggingListener());
        return sqlInsertClause;
    }

    protected SQLDeleteClause delete(RelationalPath<?> e) {
        SQLDeleteClause sqlDeleteClause = new SQLDeleteClause(connection, configuration, e);
        sqlDeleteClause.addListener(new TestLoggingListener());
        return sqlDeleteClause;
    }

    protected SQLMergeClause merge(RelationalPath<?> e) {
        SQLMergeClause sqlMergeClause = new SQLMergeClause(connection, configuration, e);
        sqlMergeClause.addListener(new TestLoggingListener());
        return sqlMergeClause;
    }

    protected ExtendedSQLQuery extQuery() {
        ExtendedSQLQuery extendedSQLQuery = new ExtendedSQLQuery(connection, configuration);
        extendedSQLQuery.addListener(new TestLoggingListener());
        return extendedSQLQuery;
    }

    protected SQLInsertClause mysqlReplace(RelationalPath<?> path) {
        MySQLReplaceClause mySQLReplaceClause = new MySQLReplaceClause(connection, configuration, path);
        mySQLReplaceClause.addListener(new TestLoggingListener());
        return mySQLReplaceClause;
    }

    protected TestQuery query() {
        TestQuery testQuery = new TestQuery(connection, configuration);
        testQuery.addListener(new TestLoggingListener());
        return testQuery;
    }

    protected TeradataQuery teradataQuery() {
        TeradataQuery teradataQuery = new TeradataQuery(connection, configuration);
        teradataQuery.addListener(new TestLoggingListener());
        return teradataQuery;
    }

    protected TestQuery testQuery() {
        TestQuery testQuery = new TestQuery(connection, configuration,
                new DefaultQueryMetadata().noValidate());
        testQuery.addListener(new TestLoggingListener());
        return testQuery;
    }

    protected SQLSubQuery sq() {
        return new SQLSubQuery();
    }

    protected long execute(DMLClause<?>... clauses) {
        long execute = 0;
        for (DMLClause<?> clause : clauses) {
            execute += clause.execute();
        }
        return execute;
    }

}
