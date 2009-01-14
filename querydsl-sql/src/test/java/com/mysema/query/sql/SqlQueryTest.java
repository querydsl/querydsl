/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.grammar.Dialects;
import com.mysema.query.grammar.Grammar;
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
        if (stmt != null) stmt.close();
        if (c != null) c.close();
    }
    
    @Test
    public void testQuery1() throws Exception{                
        for (String s : q().from(survey).list(survey.name)){
            System.out.println(s);
        }        
    }
    
    @Test
    public void testQuery2() throws Exception{              
        for (Object[] row : q().from(survey).list(survey.id, survey.name)){
            System.out.println(row[0]+", " + row[1]);
        }        
    }
    
    @Test
    public void testQueryWithConstant() throws Exception{
        for (Object[] row : q().from(survey)
                           .where(survey.id.eq(1))
                           .list(survey.id, survey.name)){
            System.out.println(row[0]+", " + row[1]);
        }
    }
    
    @Test
    public void testJoin() throws Exception{
        for (String name : q().from(survey, survey2)
                          .where(survey.id.eq(survey2.id))
                          .list(survey.name)){
            System.out.println(name);
        }
    }
    
    @Test
    public void testConstructor() throws Exception{
        for (IdName idName : q().from(survey)
                            .list(new QIdName(survey.id, survey.name))){
            System.out.println("id and name : " + idName.getId()+ ","+idName.getName());
        }
    }
    
    @Test
    public void testSyntax() throws SQLException{
        System.out.println(q().from(survey).list(survey.name.lower()));
        System.out.println(q().from(survey).list(survey.name.add("abc")));
        System.out.println(q().from(survey).list(survey.id.eq(0)));        
        System.out.println(q().from(survey).list(Grammar.sqrt(survey.id)));
        
        // TODO : support for union
        // TODO : support for aggregators
        // TODO : support for wildcard
    }
    
    private SqlQuery q(){
        return new SqlQuery(c, Dialects.HSQLDB);
    }
    
    private static Connection getHSQLConnection() throws Exception{
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:data/tutorial";
        return DriverManager.getConnection(url, "sa", "");
    }

}
