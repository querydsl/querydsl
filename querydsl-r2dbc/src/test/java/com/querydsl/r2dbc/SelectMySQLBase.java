package com.querydsl.r2dbc;

import com.querydsl.core.testutil.IncludeIn;
import com.querydsl.r2dbc.mysql.R2DBCMySQLQuery;
import org.junit.Test;

import static com.querydsl.core.Target.MYSQL;
import static com.querydsl.r2dbc.Constants.survey;


public class SelectMySQLBase extends AbstractBaseTest {

    protected R2DBCMySQLQuery<?> myR2DBCQuery() {
        return new R2DBCMySQLQuery<Void>(connection, configuration);
    }

    @Test
    @IncludeIn(MYSQL)
    public void mysql_extensions() {
        myR2DBCQuery().from(survey).bigResult().select(survey.id).fetch().collectList().block();
        myR2DBCQuery().from(survey).bufferResult().select(survey.id).fetch().collectList().block();
        myR2DBCQuery().from(survey).cache().select(survey.id).fetch().collectList().block();
        myR2DBCQuery().from(survey).calcFoundRows().select(survey.id).fetch().collectList().block();
        myR2DBCQuery().from(survey).noCache().select(survey.id).fetch().collectList().block();

        myR2DBCQuery().from(survey).highPriority().select(survey.id).fetch().collectList().block();
        myR2DBCQuery().from(survey).lockInShareMode().select(survey.id).fetch().collectList().block();
        myR2DBCQuery().from(survey).smallResult().select(survey.id).fetch().collectList().block();
        myR2DBCQuery().from(survey).straightJoin().select(survey.id).fetch().collectList().block();
    }

}
