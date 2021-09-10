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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import org.jetbrains.annotations.Nullable;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.QueryException;

/**
 * {@code SQLResultIterator} is an Iterator adapter for JDBC result sets with customizable projections
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class SQLResultIterator<T> implements CloseableIterator<T> {

    @Nullable
    private Boolean next = null;

    private final Configuration configuration;

    private final ResultSet rs;

    private final Statement stmt;

    private final SQLDetailedListener listener;

    private final SQLListenerContext context;

    public SQLResultIterator(Configuration conf, Statement stmt, ResultSet rs) {
        this(conf, stmt, rs, null, null);
    }

    public SQLResultIterator(Configuration conf, Statement stmt, ResultSet rs,
                             SQLDetailedListener listener, SQLListenerContext context) {
        this.configuration = conf;
        this.stmt = stmt;
        this.rs = rs;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void close() {
        try {
            try {
                if (rs != null) {
                    rs.close();
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (SQLException e) {
            throw configuration.translate(e);
        } finally {
            if (listener != null) {
                listener.end(context);
            }
        }
    }

    @Override
    public boolean hasNext() {
        if (next == null) {
            try {
                next = rs.next();
            } catch (SQLException e) {
                close();
                throw configuration.translate(e);
            }
        }
        return next;
    }

    @Override
    public T next() {
        if (hasNext()) {
            next = null;
            try {
                return produceNext(rs);
            } catch (SQLException e) {
                close();
                throw configuration.translate(e);
            } catch (Exception e) {
                close();
                throw new QueryException(e);
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    protected abstract T produceNext(ResultSet rs) throws Exception;

    @Override
    public void remove() {
        try {
            rs.deleteRow();
        } catch (SQLException e) {
            close();
            throw configuration.translate(e);
        }
    }

}
