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

import static com.mysema.query.Constants.employee;
import static com.mysema.query.Constants.survey;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.Connections;
import com.mysema.query.SelectBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.mysql.MySQLQuery;
import com.mysema.testutil.Label;

@Label(Target.MYSQL)
public class SelectMySQLTest extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initMySQL();
    }

    @Before
    public void setUpForTest() {
        templates = new MySQLTemplates(){{
            newLineToSingleSpace();
        }};
    }

    @Test
    @Override
    public void Alias_Quotes() {
        expectedQuery = "select e.FIRSTNAME as `First Name` from EMPLOYEE e";
        query().from(employee).list(employee.firstname.as("First Name"));
    }
    
    @Test
    public void Extensions(){
        mysqlQuery().from(survey).bigResult().list(survey.id);
        mysqlQuery().from(survey).bufferResult().list(survey.id);
        mysqlQuery().from(survey).cache().list(survey.id);        
        mysqlQuery().from(survey).calcFoundRows().list(survey.id);
        mysqlQuery().from(survey).noCache().list(survey.id);
        
        mysqlQuery().from(survey).highPriority().list(survey.id);
        mysqlQuery().from(survey).lockInShareMode().list(survey.id);
        mysqlQuery().from(survey).smallResult().list(survey.id);
        mysqlQuery().from(survey).straightJoin().list(survey.id);        
    }
    
    private MySQLQuery mysqlQuery(){
        return new MySQLQuery(Connections.getConnection(), templates);
    }
}
