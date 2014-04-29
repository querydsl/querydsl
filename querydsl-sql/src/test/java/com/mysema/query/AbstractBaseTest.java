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

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.List;

import com.mysema.query.sql.*;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLMergeClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.sql.mysql.MySQLReplaceClause;
import com.mysema.query.sql.teradata.TeradataQuery;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.assertEquals;

public abstract class AbstractBaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBaseTest.class);

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
            return q;
        }
    }

    protected Connection connection = Connections.getConnection();

    protected SQLTemplates templates = Connections.getTemplates();

    protected Target target = Connections.getTarget();

    protected Configuration configuration = new Configuration(templates);

    @Nullable
    protected String expectedQuery;

    @Rule
    public static MethodRule skipForQuotedRule = new SkipForQuotedRule();

    @Rule
    public static MethodRule targetRule = new TargetRule();

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
        return new SQLUpdateClause(connection, configuration, e);
    }

    protected SQLInsertClause insert(RelationalPath<?> e) {
        return new SQLInsertClause(connection, configuration, e);
    }

    protected SQLInsertClause insert(RelationalPath<?> e, AbstractSQLSubQuery<?> sq) {
        return new SQLInsertClause(connection, configuration, e, sq);
    }

    protected SQLDeleteClause delete(RelationalPath<?> e) {
        return new SQLDeleteClause(connection, configuration, e);
    }

    protected SQLMergeClause merge(RelationalPath<?> e) {
        return new SQLMergeClause(connection, configuration, e);
    }

    protected ExtendedSQLQuery extQuery() {
        return new ExtendedSQLQuery(connection, configuration);
    }

    protected SQLInsertClause mysqlReplace(RelationalPath<?> path) {
        return new MySQLReplaceClause(connection, configuration, path);
    }

    protected TestQuery query() {
        return new TestQuery(connection, configuration);
    }

    protected TeradataQuery teradataQuery() {
        return new TeradataQuery(connection, configuration);
    }

    protected TestQuery testQuery() {
        return new TestQuery(connection, configuration,
                new DefaultQueryMetadata().noValidate());
    }

    protected SQLSubQuery sq() {
        return new SQLSubQuery();
    }

}
