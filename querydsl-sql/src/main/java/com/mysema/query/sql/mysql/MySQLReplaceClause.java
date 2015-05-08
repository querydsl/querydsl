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
package com.mysema.query.sql.mysql;

import java.sql.Connection;

import com.mysema.query.QueryFlag.Position;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * @author tiwe
 *
 */
public class MySQLReplaceClause extends SQLInsertClause{
    
    private static final String REPLACE_INTO = "replace into ";

    public MySQLReplaceClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        super(connection, templates, entity);
        addFlag(Position.START_OVERRIDE, REPLACE_INTO);
    }
    
    public MySQLReplaceClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(connection, configuration, entity);
        addFlag(Position.START_OVERRIDE, REPLACE_INTO);
    }

}
