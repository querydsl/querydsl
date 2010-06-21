/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.annotation.Nullable;

import org.junit.AfterClass;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLMergeClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.path.PEntity;

public abstract class AbstractBaseTest {

    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
        Connections.close();
    }

    protected SQLTemplates dialect;

    @Nullable
    protected String expectedQuery;

    protected SQLUpdateClause update(PEntity<?> e){
        return new SQLUpdateClause(Connections.getConnection(), dialect, e);
    }

    protected SQLInsertClause insert(PEntity<?> e){
        return new SQLInsertClause(Connections.getConnection(), dialect, e);
    }

    protected SQLDeleteClause delete(PEntity<?> e){
        return new SQLDeleteClause(Connections.getConnection(), dialect, e);
    }

    protected SQLMergeClause merge(PEntity<?> e){
        return new SQLMergeClause(Connections.getConnection(), dialect, e);
    }

    protected SQLQuery query() {
        return new SQLQueryImpl(Connections.getConnection(), dialect) {
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
