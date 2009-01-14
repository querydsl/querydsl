/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.grammar.Dialects;
import com.mysema.query.sql.domain.QSURVEY;
import com.mysema.query.sql.dto.IdName;
import com.mysema.query.sql.dto.QIdName;


/**
 * SqlQueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class SqlQueryTest {
    
    private QSURVEY survey = new QSURVEY("survey");
    
    private QSURVEY survey2 = new QSURVEY("survey2");
    
    private static Connection c;
    
    private static Statement stmt;
    
    @BeforeClass
    public static void setUp() throws Exception{
        c = getHSQLConnection();
        stmt = c.createStatement();
        stmt.executeUpdate("create table survey (id int,name varchar(30));");
        stmt.execute("insert into survey values (1, 'Hello World');");
    }
    
    @AfterClass
    public static void tearDown() throws Exception{
        stmt.close();
        c.close();
    }
    
    @Test
    public void testQuery1() throws Exception{                
        SqlQuery query = new SqlQuery(c,Dialects.HSQLDB);
        for (String s : query.from(survey).list(survey.name)){
            System.out.println(s);
        }        
    }
    
    @Test
    public void testQuery2() throws Exception{              
        SqlQuery query = new SqlQuery(c,Dialects.HSQLDB);
        for (Object[] row : query.from(survey).list(survey.id, survey.name)){
            System.out.println(row[0]+", " + row[1]);
        }        
    }
    
    @Test
    public void testQueryWithConstant() throws Exception{
        SqlQuery query = new SqlQuery(c,Dialects.HSQLDB);
        for (Object[] row : query.from(survey)
                           .where(survey.id.eq(1))
                           .list(survey.id, survey.name)){
            System.out.println(row[0]+", " + row[1]);
        }
    }
    
    @Test
    public void testJoin() throws Exception{
        SqlQuery query = new SqlQuery(c,Dialects.HSQLDB);
        for (String name : query.from(survey, survey2)
                          .where(survey.id.eq(survey2.id))
                          .list(survey.name)){
            System.out.println(name);
        }
    }
    
    @Test
    public void testConstructor() throws Exception{
        SqlQuery query = new SqlQuery(c,Dialects.HSQLDB);
        for (IdName idName : query.from(survey)
                            .list(new QIdName(survey.id, survey.name))){
            System.out.println("id and name : " + idName.getId()+ ","+idName.getName());
        }
    }
    
    private static Connection getHSQLConnection() throws Exception{
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:data/tutorial";
        return DriverManager.getConnection(url, "sa", "");
    }

}
