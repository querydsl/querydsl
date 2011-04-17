/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
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
import com.mysema.query.Target;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.mysql.MySQLReplaceClause;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@ResourceCheck("/mysql.run")
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
        
        assertEquals("insert ignore into SURVEY(ID, NAME) values (?, ?)", clause.toString());
        clause.execute();        
    }

    @Test
    public void Replace(){
        SQLInsertClause clause = new MySQLReplaceClause(Connections.getConnection(), templates, survey);
        clause.columns(survey.id, survey.name)
            .values(3, "Hello");
        
        assertEquals("replace into SURVEY(ID, NAME) values (?, ?)", clause.toString());
        clause.execute();
    }
}
