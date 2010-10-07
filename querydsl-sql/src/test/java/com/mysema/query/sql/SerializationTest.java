package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;

import org.easymock.EasyMock;
import org.junit.Test;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.sql.domain.QSurvey;

public class SerializationTest {
    
    private Connection connection = EasyMock.createMock(Connection.class);

    @Test
    public void InnerJoin(){        
        SQLQuery query = new SQLQueryImpl(connection,SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).innerJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\ninner join SURVEY s2", query.toString());
    }
    
    @Test
    public void LeftJoin(){        
        SQLQuery query = new SQLQueryImpl(connection,SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).leftJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\nleft join SURVEY s2", query.toString());
    }
    
    @Test
    public void RightJoin(){        
        SQLQuery query = new SQLQueryImpl(connection,SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).rightJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\nright join SURVEY s2", query.toString());
    }
    
    @Test
    public void FullJoin(){        
        SQLQuery query = new SQLQueryImpl(connection,SQLTemplates.DEFAULT);
        query.from(new QSurvey("s1")).fullJoin(new QSurvey("s2"));
        assertEquals("from SURVEY s1\nfull join SURVEY s2", query.toString());
    }
    
    
    @Test
    public void Update(){
        QSurvey survey = new QSurvey("survey");
        SQLUpdateClause updateClause = new SQLUpdateClause(connection,SQLTemplates.DEFAULT,survey);
        updateClause.set(survey.id, 1);
        updateClause.set(survey.name, (String)null);
        assertEquals("update SURVEY\nset ID = ?, NAME = null", updateClause.toString());
    }

    @Test
    public void Insert(){
        QSurvey survey = new QSurvey("survey");
        SQLInsertClause insertClause = new SQLInsertClause(connection,SQLTemplates.DEFAULT,survey);
        insertClause.set(survey.id, 1);
        insertClause.set(survey.name, (String)null);
        assertEquals("insert into SURVEY(ID, NAME)\nvalues (?, null)", insertClause.toString());
    }
    
    @Test
    public void Delete_with_SubQuery_exists(){
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");
        SQLDeleteClause delete = new SQLDeleteClause(connection, SQLTemplates.DEFAULT,survey1);
        delete.where(survey1.name.eq("XXX"), new SQLSubQuery().from(employee).where(survey1.id.eq(employee.id)).exists());
        assertEquals("delete from SURVEY\n" +
                     "where SURVEY.NAME = ? and exists (select 1\n" +
                     "from EMPLOYEE2 e\n" +
                     "where SURVEY.ID = e.ID)", delete.toString());
    }

}
