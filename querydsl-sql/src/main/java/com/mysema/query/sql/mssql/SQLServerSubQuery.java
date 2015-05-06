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
package com.mysema.query.sql.mssql;

import com.mysema.query.JoinFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.AbstractSQLSubQuery;
import com.mysema.query.sql.Configuration;

/**
 * @author tiwe
 *
 */
public class SQLServerSubQuery extends AbstractSQLSubQuery<SQLServerSubQuery> {
    
    public SQLServerSubQuery() {
        super();
    }

    public SQLServerSubQuery(QueryMetadata metadata) {
        super(metadata);
    }
    
    public SQLServerSubQuery(Configuration configuration, QueryMetadata metadata){
        super(configuration, metadata);
    }

    /**
     * @param tableHints
     * @return
     */
    public SQLServerSubQuery tableHints(SQLServerTableHints... tableHints) {
        if (tableHints.length > 0) {
            String hints = SQLServerGrammar.tableHints(tableHints);
            addJoinFlag(hints, JoinFlag.Position.END);
        }
        return this;
    }

    @Override
    public SQLServerSubQuery clone() {
        SQLServerSubQuery subQuery = new SQLServerSubQuery(this.configuration, this.getMetadata().clone());
        return subQuery;
    }

}
