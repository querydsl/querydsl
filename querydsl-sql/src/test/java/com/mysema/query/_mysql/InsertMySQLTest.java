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
package com.mysema.query._mysql;

import static com.mysema.query.Constants.survey;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.Connections;
import com.mysema.query.InsertBaseTest;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.Target;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.mysql.MySQLReplaceClause;
import com.mysema.testutil.Label;

@Label(Target.MYSQL)
public class InsertMySQLTest extends InsertBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initMySQL();
    }

    @Before
    public void setUp() throws SQLException {
        templates = new MySQLTemplates(){{
            newLineToSingleSpace();
        }};
        super.setUp();
    }
    
    @Test
    public void Insert_with_Special_Options(){
        SQLInsertClause clause = insert(survey)
            .columns(survey.id, survey.name)
            .values(3, "Hello");
        
        clause.addFlag(Position.START_OVERRIDE, "insert ignore into ");
        
        assertEquals("insert ignore into SURVEY (ID, NAME) values (?, ?)", clause.toString());
        clause.execute();        
    }

    @Test
    public void Replace(){
        SQLInsertClause clause = new MySQLReplaceClause(Connections.getConnection(), templates, survey);
        clause.columns(survey.id, survey.name)
            .values(3, "Hello");
        
        assertEquals("replace into SURVEY (ID, NAME) values (?, ?)", clause.toString());
        clause.execute();
    }
}
