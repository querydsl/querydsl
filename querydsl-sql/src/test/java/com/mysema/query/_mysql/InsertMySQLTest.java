/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._mysql;

import static com.mysema.query.Constants.survey;
import static org.junit.Assert.*;

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
        templates = new MySQLTemplates().newLineToSingleSpace();
        super.setUp();
    }
    
    @Test
    public void insert_with_special_options(){
        SQLInsertClause clause = insert(survey)
            .columns(survey.id, survey.name)
            .values(3, "Hello");
        
        clause.addFlag(Position.START_OVERRIDE, "INSERT IGNORE INTO ");
        
        assertEquals("INSERT IGNORE INTO SURVEY(ID, NAME) values (?, ?)", clause.toString());
        clause.execute();        
    }

}
