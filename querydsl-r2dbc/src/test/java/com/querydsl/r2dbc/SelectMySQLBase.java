package com.querydsl.r2dbc;

import com.querydsl.core.testutil.IncludeIn;
import com.querydsl.r2dbc.mysql.R2DBCMySQLQuery;
import org.junit.Test;

import static com.querydsl.core.Target.MYSQL;
import static com.querydsl.r2dbc.Constants.survey;


public class SelectMySQLBase extends AbstractBaseTest {

    protected R2DBCMySQLQuery<?> mysqlQuery() {
        return new R2DBCMySQLQuery<Void>(connection, configuration);
    }

    @Test
    @IncludeIn(MYSQL)
    public void mysql_extensions() {
        mysqlQuery().from(survey).bigResult().select(survey.id).fetch().collectList().block();
        mysqlQuery().from(survey).bufferResult().select(survey.id).fetch().collectList().block();
        mysqlQuery().from(survey).cache().select(survey.id).fetch().collectList().block();
        mysqlQuery().from(survey).calcFoundRows().select(survey.id).fetch().collectList().block();
        mysqlQuery().from(survey).noCache().select(survey.id).fetch().collectList().block();

        mysqlQuery().from(survey).highPriority().select(survey.id).fetch().collectList().block();
        mysqlQuery().from(survey).lockInShareMode().select(survey.id).fetch().collectList().block();
        mysqlQuery().from(survey).smallResult().select(survey.id).fetch().collectList().block();
        mysqlQuery().from(survey).straightJoin().select(survey.id).fetch().collectList().block();
    }

}
