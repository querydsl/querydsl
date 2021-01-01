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

import java.util.List;
import java.util.logging.Logger;

import com.querydsl.core.util.CollectionUtils;

/**
 * {@code SQLBindings} provides the SQL query string and bindings
 *
 * @author tiwe
 *
 */
public class SQLBindings {

    private static final Logger log = Logger.getLogger(SQLBindings.class.getName());

    private final String sql;

    private final List<Object> bindings;

    public SQLBindings(String sql, List<Object> bindings) {
        this.sql = sql;
        this.bindings = CollectionUtils.unmodifiableList(bindings);
    }

    public String getSQL() {
        return sql;
    }

    public List<Object> getNullFriendlyBindings() {
        return bindings;
    }

}
