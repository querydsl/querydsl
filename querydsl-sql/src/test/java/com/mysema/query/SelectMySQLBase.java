package com.mysema.query;

import com.mysema.query.sql.domain.QNumberTest;
import com.mysema.query.sql.mysql.MySQLQuery;
import com.mysema.testutil.IncludeIn;
import org.junit.Test;
import static com.mysema.query.Constants.survey;
import static com.mysema.query.Target.MYSQL;
import static org.junit.Assert.assertEquals;


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

    @Test
    @IncludeIn(MYSQL)
    public void Tinyint() {
        QNumberTest numberTest = QNumberTest.numberTest;
        delete(numberTest).execute();
        insert(numberTest).set(numberTest.col1Boolean, true).execute();
        insert(numberTest).set(numberTest.col1Number, 1).execute();
        assertEquals(2, query().from(numberTest).list(numberTest.col1Boolean).size());
        assertEquals(2, query().from(numberTest).list(numberTest.col1Number).size());
    }

}
