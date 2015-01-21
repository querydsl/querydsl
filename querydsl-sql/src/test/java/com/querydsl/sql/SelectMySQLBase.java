package com.querydsl.sql;

import static com.querydsl.sql.Constants.survey;
import static com.querydsl.core.Target.MYSQL;

import org.junit.Test;

import com.querydsl.sql.mysql.MySQLQuery;
import com.querydsl.core.testutil.IncludeIn;


public class SelectMySQLBase extends AbstractBaseTest {

    protected MySQLQuery mysqlQuery() {
        return new MySQLQuery(connection, configuration);
    }

    @Test
    @IncludeIn(MYSQL)
    public void MySQL_Extensions() {
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

}
