/*
 * Copyright 2015, Mysema Ltd
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
package com.mysema.query.sql.nuodb;

import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.AbstractSQLQuery;
import com.mysema.query.sql.Configuration;

import javax.annotation.Nullable;
import java.sql.Connection;

/**
 * @author Sergey Bushik
 */
public class NuoDBQuery extends AbstractSQLQuery<NuoDBQuery> {

    public NuoDBQuery(@Nullable Connection connection, Configuration configuration) {
        super(connection, configuration);
    }

    public NuoDBQuery(@Nullable Connection connection, Configuration configuration, QueryMetadata metadata) {
        super(connection, configuration, metadata);
    }

    @Override
    public NuoDBQuery clone(Connection conn) {
        NuoDBQuery query = new NuoDBQuery(conn, getConfiguration(), getMetadata().clone());
        query.clone(this);
        return query;
    }
}