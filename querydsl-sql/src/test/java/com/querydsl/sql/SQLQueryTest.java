package com.querydsl.sql;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.sql.Connection;

import org.easymock.EasyMock;
import org.junit.Test;

import com.querydsl.sql.domain.QSurvey;

public class SQLQueryTest {

    @Test(expected = IllegalStateException.class)
    public void noConnection() {
        QSurvey survey = QSurvey.survey;
        SQLExpressions.select(survey.id).from(survey).fetch();
    }
    
    @Test
    public void cloneNoArgHavingConnection() {
        Connection connection = EasyMock.createMock(Connection.class);
        SQLQuery<?> query = new SQLQuery<>(connection, new Configuration(SQLTemplates.DEFAULT));
        SQLQuery<?> clone = query.clone();
        assertSame(connection, clone.connection());
    }
    
    @Test
    public void cloneConnectionArgHavingConnection() {
        Connection connection1 = EasyMock.createMock(Connection.class);
        Connection connection2 = EasyMock.createMock(Connection.class);
        SQLQuery<?> query = new SQLQuery<>(connection1, new Configuration(SQLTemplates.DEFAULT));
        SQLQuery<?> clone = query.clone(connection2);
        assertSame(connection2, clone.connection());
    }
    
    @Test
    public void cloneNoArgHavingConnectionProvider() {
        SQLQuery<?> query = new SQLQuery<>(() -> EasyMock.createMock(Connection.class), new Configuration(SQLTemplates.DEFAULT));
        SQLQuery<?> clone = query.clone();
        assertNotNull(clone.connection());
        assertNotSame(query.connection(), clone.connection());
    }
    
    @Test
    public void cloneConnectionArgHavingConnectionProvider() {
        Connection connection = EasyMock.createMock(Connection.class);
        SQLQuery<?> query = new SQLQuery<>(() -> EasyMock.createMock(Connection.class), new Configuration(SQLTemplates.DEFAULT));
        SQLQuery<?> clone = query.clone(connection);
        assertSame(connection, clone.connection());
    }

}
