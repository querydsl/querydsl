package com.querydsl.sql.suites;

import static com.querydsl.sql.domain.QSurvey.survey;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import com.google.common.base.Throwables;
import com.querydsl.sql.AbstractBaseTest;
import com.querydsl.sql.Connections;
import com.querydsl.core.JavaSpecVersion;
import com.querydsl.core.QueryException;
import com.querydsl.sql.DefaultSQLExceptionTranslator;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLExceptionTranslator;

import org.junit.BeforeClass;
import org.junit.Test;

public class H2ExceptionSuiteTest extends AbstractBaseTest {

    private static final SQLExceptionTranslator exceptionTranslator = DefaultSQLExceptionTranslator.DEFAULT;

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initH2();
        Connections.initConfiguration(H2Templates.builder().build());

        Connections.getConnection().createStatement()
                .execute("ALTER TABLE SURVEY ADD CONSTRAINT UNIQUE_ID UNIQUE(ID)");
    }

    public static void tearDown() throws Exception {
        Connections.getConnection().createStatement()
                .execute("ALTER TABLE SURVEY DROP CONSTRAINT UNIQUE_ID");
    }

    @Test
    public void SQLExceptionCreationTranslated() {
        SQLException e1 = new SQLException("Exception #1", "42001", 181);
        SQLException e2 = new SQLException("Exception #2", "HY000", 1030);
        e1.setNextException(e2);
        SQLException sqlException = new SQLException("Batch operation failed");
        sqlException.setNextException(e1);
        RuntimeException result = exceptionTranslator.translate(sqlException);
        inspectExceptionResult(result);
    }

    @Test
    public void UpdateBatchFailed() {
        execute(insert(survey).columns(survey.name, survey.name2)
                .values("New Survey", "New Survey"));
        Exception result = null;
        try {
            execute(update(survey)
                    .set(survey.id, 1).addBatch()
                    .set(survey.id, 2).addBatch());
        } catch (QueryException e) {
            result = e;
        }
        assertNotNull(result);
        inspectExceptionResult(result);
    }

    private void inspectExceptionResult(Exception result) {
        String stackTraceAsString = Throwables.getStackTraceAsString(result);
        switch (JavaSpecVersion.CURRENT) {
            case JAVA6:
                assertTrue(stackTraceAsString
                        .contains("Detailed SQLException information:"));
                break;
            default://Java™ 7 and higher
                assertTrue(stackTraceAsString
                        .contains("Suppressed:"));
        }
    }
}
