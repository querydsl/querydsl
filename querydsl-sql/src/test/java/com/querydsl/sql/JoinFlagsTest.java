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
package com.querydsl.sql;

import java.sql.Connection;

import com.querydsl.core.JoinFlag;
import com.querydsl.sql.domain.QSurvey;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JoinFlagsTest {
    
    private Connection connection = EasyMock.createMock(Connection.class);

    private QSurvey s1, s2, s3, s4, s5, s6;
    
    private SQLQuery query;
    
    @Before
    public void setUp() {
        s1 = new QSurvey("s");
        s2 = new QSurvey("s2");    
        s3 = new QSurvey("s3");    
        s4 = new QSurvey("s4");    
        s5 = new QSurvey("s5");
        s6 = new QSurvey("s6");        
        query = new SQLQuery(connection,SQLTemplates.DEFAULT);
        query.from(s1);    
    }

    @Test
    public void JoinFlags_BeforeCondition() {
        query.innerJoin(s2).on(s1.eq(s2));
        query.addJoinFlag(" a ", JoinFlag.Position.BEFORE_CONDITION);
        
        assertEquals("from SURVEY s\n" +
                "inner join SURVEY s2 a \n" +
                "on s.ID = s2.ID", query.toString());
    }
    
    @Test
    public void JoinFlags_BeforeTarget() {
        query.innerJoin(s3).on(s1.eq(s3));
        query.addJoinFlag(" b ", JoinFlag.Position.BEFORE_TARGET);
        
        assertEquals("from SURVEY s\n" +
                "inner join  b SURVEY s3\n" +
                "on s.ID = s3.ID", query.toString());
    }
    
    @Test
    public void JoinFlags_End() {
        query.innerJoin(s4).on(s1.eq(s4));
        query.addJoinFlag(" c ", JoinFlag.Position.END);
        
        assertEquals("from SURVEY s\n" +
                "inner join SURVEY s4\n" +
                "on s.ID = s4.ID c", query.toString());
    }
    
    @Test
    public void JoinFlags_Override() {
        query.innerJoin(s5).on(s1.eq(s5));
        query.addJoinFlag(" d ", JoinFlag.Position.OVERRIDE);
        
        assertEquals("from SURVEY s d SURVEY s5\n" +
                "on s.ID = s5.ID", query.toString());
    }
    
    @Test
    public void JoinFlags_Start() {
        query.innerJoin(s6).on(s1.eq(s6));
        query.addJoinFlag(" e ", JoinFlag.Position.START);
        
        assertEquals("from SURVEY s e \n" +
                "inner join SURVEY s6\n" +
                "on s.ID = s6.ID", query.toString());
    }
    

}
