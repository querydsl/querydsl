package com.querydsl.r2dbc.suites;

import com.google.common.base.Throwables;
import com.querydsl.core.QueryException;
import com.querydsl.core.testutil.H2;
import com.querydsl.r2dbc.AbstractBaseTest;
import com.querydsl.r2dbc.Connections;
import com.querydsl.r2dbc.H2Templates;
import com.querydsl.sql.DefaultSQLExceptionTranslator;
import com.querydsl.sql.SQLExceptionTranslator;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import reactor.core.publisher.Mono;

import java.sql.SQLException;

import static com.querydsl.r2dbc.domain.QSurvey.survey;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Category(H2.class)
public class H2ExceptionSuiteTest extends AbstractBaseTest {

    private static final SQLExceptionTranslator exceptionTranslator = DefaultSQLExceptionTranslator.DEFAULT;

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initConfiguration(H2Templates.builder().build());
        Connections.initH2();

        Mono.just(Connections.getConnection().createStatement("ALTER TABLE SURVEY ADD CONSTRAINT UNIQUE_ID UNIQUE(ID)")
                .execute()).block();
    }

    public static void tearDown() throws Exception {
        Mono.just(Connections.getConnection().createStatement("ALTER TABLE SURVEY DROP CONSTRAINT UNIQUE_ID")
                .execute()).block();
    }

    @Test
    public void sQLExceptionCreationTranslated() {
        SQLException e1 = new SQLException("Exception #1", "42001", 181);
        SQLException e2 = new SQLException("Exception #2", "HY000", 1030);
        e1.setNextException(e2);
        SQLException sqlException = new SQLException("Batch operation failed");
        sqlException.setNextException(e1);
        RuntimeException result = exceptionTranslator.translate(sqlException);
        inspectExceptionResult(result);
    }

    @Test
    @Ignore("Multiple batches are supported")
    public void updateBatchFailed() {
        execute(insert(survey).columns(survey.name, survey.name2)
                .values("New Survey", "New Survey")).block();
        Exception result = null;
        try {
            execute(update(survey)
                    .set(survey.id, 1).addBatch()
                    .set(survey.id, 2).addBatch()).block();
        } catch (QueryException e) {
            result = e;
        }
        assertNotNull(result);
        inspectExceptionResult(result);
    }

    private void inspectExceptionResult(Exception result) {
        String stackTraceAsString = Throwables.getStackTraceAsString(result);
        assertTrue(stackTraceAsString.contains("Suppressed:"));
    }

}
