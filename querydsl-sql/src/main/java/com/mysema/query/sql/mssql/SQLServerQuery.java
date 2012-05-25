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
package com.mysema.query.sql.mssql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.AbstractSQLQuery;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.SQLCommonQuery;
import com.mysema.query.sql.SQLServerTemplates;
import com.mysema.query.sql.SQLTemplates;

/**
 * SQLServerQuery provides SQL Server related extensions to SQLQuery
 * 
 * @author tiwe
 *
 */
public class SQLServerQuery extends AbstractSQLQuery<SQLServerQuery> implements SQLCommonQuery<SQLServerQuery> {
    
    public SQLServerQuery(Connection conn) {
        this(conn, new SQLServerTemplates(), new DefaultQueryMetadata());
    }

    public SQLServerQuery(Connection conn, SQLTemplates templates) {
        this(conn, templates, new DefaultQueryMetadata());
    }

    protected SQLServerQuery(Connection conn, SQLTemplates templates, QueryMetadata metadata) {
        super(conn, new Configuration(templates), metadata);
    }

    public SQLServerQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public SQLServerQuery(Connection conn, Configuration configuration) {
        super(conn, configuration);
    }

    public SQLServerQuery tableHints(SQLServerTableHints... tableHints) {
        List<String> tableHintStrs = new ArrayList<String>(tableHints.length);
        for (SQLServerTableHints tableHint : tableHints) {
            tableHintStrs.add(tableHint.name());
        }
        return tableHints(tableHintStrs);
    }

    private SQLServerQuery tableHints(Collection<String> tableHints) {
        if (!tableHints.isEmpty()) {
            String with = getConfiguration().getTemplates().getWith();
            StringBuilder tableHintsStr = new StringBuilder(" ").append(with).append("(");
            Iterator<String> it = tableHints.iterator();
            tableHintsStr.append(it.next());
            while (it.hasNext()) {
                tableHintsStr.append(", ").append(it.next());
            }
            tableHintsStr.append(") ");
            addFlag(QueryFlag.Position.BEFORE_FILTERS, tableHintsStr.toString());
        }
        return this;
    }
}