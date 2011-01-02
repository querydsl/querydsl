package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.JoinFlag;
import com.mysema.query.sql.domain.QSurvey;

public class JoinFlagsTest {
    
    private Connection connection = EasyMock.createMock(Connection.class);

    private QSurvey s1, s2, s3, s4, s5, s6;
    
    private SQLQueryImpl query;
    
    @Before
    public void setUp(){
        s1 = new QSurvey("s");
        s2 = new QSurvey("s2");    
        s3 = new QSurvey("s3");    
        s4 = new QSurvey("s4");    
        s5 = new QSurvey("s5");
        s6 = new QSurvey("s6");        
        query = new SQLQueryImpl(connection,SQLTemplates.DEFAULT);
        query.from(s1);    
    }

    @Test
    public void JoinFlag_BeforeCondition(){
        query.innerJoin(s2).on(s1.eq(s2));
        query.addJoinFlag(" a ", JoinFlag.Position.BEFORE_CONDITION);
        
        assertEquals("from SURVEY s\n" +
                "inner join SURVEY s2 a \n" +
                "on s = ?", query.toString());
    }
    
    @Test
    public void JoinFlags_BeforeTarget(){
        query.innerJoin(s3).on(s1.eq(s3));
        query.addJoinFlag(" b ", JoinFlag.Position.BEFORE_TARGET);
        
        assertEquals("from SURVEY s\n" +
                "inner join  b SURVEY s3\n" +
                "on s = ?", query.toString());
    }
    
    @Test
    public void JoinFlags_End(){
        query.innerJoin(s4).on(s1.eq(s4));
        query.addJoinFlag(" c ", JoinFlag.Position.END);
        
        assertEquals("from SURVEY s\n" +
                "inner join SURVEY s4\n" +
                "on s = ? c", query.toString());
    }
    
    @Test
    public void JoinFlags_Override(){
        query.innerJoin(s5).on(s1.eq(s5));
        query.addJoinFlag(" d ", JoinFlag.Position.OVERRIDE);
        
        assertEquals("from SURVEY s d SURVEY s5\n" +
                "on s = ?", query.toString());
    }
    
    @Test
    public void JoinFlags_Start(){
        query.innerJoin(s6).on(s1.eq(s6));
        query.addJoinFlag(" e ", JoinFlag.Position.START);
        
        assertEquals("from SURVEY s e \n" +
                "inner join SURVEY s6\n" +
                "on s = ?", query.toString());
    }
    

}
